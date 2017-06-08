package splash.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import de.vogella.rss.model.Feed;
import de.vogella.rss.model.FeedMessage;
import de.vogella.rss.read.RSSFeedParser;
import splash.lib.CassandraHosts;

public class ArticleFinder{
    private static final long MINUTE_IN_MS = 60000;
    private static final long MS_BETWEEN_CACHE_UPDATE = MINUTE_IN_MS * 1;
    
    private static final Pattern REPLACE_ANCHOR_PATTERN = Pattern.compile(
        "<a[^>]*?>(.*?)</a>",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern REPLACE_BR_PATTERN = Pattern.compile(
        "(<br ?/?>([\\r\\n])*(\\s)*)+",
        Pattern.CASE_INSENSITIVE |
        Pattern.MULTILINE
    );
    
    private static final Pattern REPLACE_TAG_PATTERN = Pattern.compile(
        "(<div[^>]*?></div>)|" +
        "(<script[^>]*?></script>)|" +
        "(<video[^>]*?></video>)|" +
        "(<iframe[^>]*?></iframe>)|" +
        "(<center[^>]*?></center>)|" +
        "(<img[^>]*?></img>)|" +
        "(<hh[^>]*>.*?</hh[^>]*?>)",
        Pattern.DOTALL |
        Pattern.CASE_INSENSITIVE |
        Pattern.MULTILINE
    );
    
    private Matcher matcher;
    
    private final Thread cache_update_thread;
    private final Cluster cluster;
    
    private RSSFeedParser parser;
    private Feed feed;
    
    public ArticleFinder(){
        this.cluster = CassandraHosts.getCluster();
        this.cache_update_thread = new Thread(
            new Runnable(){
                @Override
                public void run(){
                    updateCache();
                }
            }
        );
    }
    
    private boolean timeToUpdateCache(){
        long last_cache_timestamp;
        try(Session session = cluster.connect("Splash")){
            SimpleStatement query = new SimpleStatement("SELECT cache_timestamp FROM cache_info;");
            ResultSet result_set = session.execute(query);
            Row row = result_set.one();
            if(row == null){
                return true;
            }
            last_cache_timestamp = row.getDate("cache_timestamp").getTime();
        }
        long current_timestamp = new Date().getTime();
        return current_timestamp - last_cache_timestamp > MS_BETWEEN_CACHE_UPDATE;
    }
    
    private String purge(String description){
        matcher = REPLACE_ANCHOR_PATTERN.matcher(description);
        description = matcher.replaceAll("$1");
        matcher = REPLACE_BR_PATTERN.matcher(description);
        description = matcher.replaceAll("<br>");
        matcher = REPLACE_TAG_PATTERN.matcher(description);
        description = matcher.replaceAll("");
        return description;
    }
    
    private void updateCache(){
        System.out.println("\n===== Begin caching =====");
        ResultSet feed_result_set = getAllRssFeeds();
        UUID feed_id;
        String feed_url;
        String feed_name;
        String category;
        String publish_date;
        
        String article_title;
        String article_description;
        String article_url;
        UUID article_id;
        try(Session session = cluster.connect("Splash")){
            // Delete all cache_info rows
            SimpleStatement query = new SimpleStatement("TRUNCATE cache_info;");
            session.execute(query);
            
            // Create timestamp
            Date timestamp = new Date();
            
            // Insert timestamp into cache_info table
            PreparedStatement ps = session.prepare("INSERT INTO cache_info (cache_timestamp) Values(?)");
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(timestamp));
        }catch(Exception e){
            System.out.println("Error connecting to database.");
            System.out.println(e.toString());
        }
        
        // Pull articles from RSS feeds and fill list of Article objects
        List<Article> articles = new ArrayList<>();
        for(Row row : feed_result_set){
            feed_id = row.getUUID("feed_id");
            feed_url = row.getString("rss");
            feed_name = row.getString("name");
            category = row.getString("category");
            
            parser = new RSSFeedParser(feed_url);
            feed = parser.readFeed();
            publish_date = feed.getPubDate();
            
            for(FeedMessage article : feed.getMessages()){
                article_title = purge(article.getTitle());
                article_description = purge(article.getDescription());
                article_url = article.getLink();
                article_id = UUID.nameUUIDFromBytes((feed_id.toString() + article_url).getBytes());
                articles.add(new Article(
                    article_id,
                    article_title,
                    article_description,
                    article_url,
                    feed_id,
                    feed_name,
                    category,
                    publish_date
                ));
            }
        }
        
        System.out.println(articles.size() + " articles found");
        
        try(Session session = cluster.connect("Splash")){
            // Delete all cached_article table rows
            SimpleStatement query = new SimpleStatement("TRUNCATE cached_article;");
            session.execute(query);

            // Fill cached_article table using the list of Article objects
            PreparedStatement ps = session.prepare("INSERT INTO cached_article (cached_article_id, title, description, url, feed_id, feed_name, category, publish_date) Values(?,?,?,?,?,?,?,?)");
            BoundStatement boundStatement = new BoundStatement(ps);
            for(Article article : articles){
                session.execute(boundStatement.bind(
                    article.get_article_id(),
                    article.get_title(),
                    article.get_description(),
                    article.get_url(),
                    article.get_feed_id(),
                    article.get_feed_name(),
                    article.get_category(),
                    article.get_publish_date()
                ));
            }
            
            // Delete all cache_info rows
            query = new SimpleStatement("TRUNCATE cache_info;");
            session.execute(query);
            
            // Create timestamp
            Date timestamp = new Date();
            
            // Insert timestamp into cache_info table
            ps = session.prepare("INSERT INTO cache_info (cache_timestamp) Values(?)");
            boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(timestamp));
        }catch(Exception e){
            System.out.println("Could not connect to database:");
            System.out.println(e.toString());
        }
        System.out.println("===== End caching =====\n");
    }
    
    public ResultSet getAllRssFeeds(){
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM feed");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            return result_set;
        }
    }
    private List<UUID> getSavedIDs(String username){
        List<UUID> saved_articles = new ArrayList<>();
        Set<UUID> saved_articles_set;
        Iterator<UUID> iterator;
        
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT saved_article from user where username=?");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind(username));
            Row row = result_set.one();
            saved_articles_set = row.getSet("saved_article", UUID.class);
            iterator = saved_articles_set.iterator();
            
            while(iterator.hasNext()){
                saved_articles.add(iterator.next());
            }
        }
        return saved_articles;
    }
    
    private List<UUID> getUserRssFeedIDs(String username){
        List<UUID> rss_feeds = new ArrayList<>();
        Set<UUID> userRss;
        Iterator<UUID> iterator;
        
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT liked_source from user where username=?");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind(username));
            Row row = result_set.one();
            userRss = row.getSet("liked_source", UUID.class);
            iterator = userRss.iterator();
            
            while(iterator.hasNext()){
                rss_feeds.add(iterator.next());
            }
        }
        return rss_feeds;
    }
    
    public List<Article> getAllArticlesForUser(String username){
        List<UUID> rss_feeds = getUserRssFeedIDs(username);
        List<Article> articles = new ArrayList<>();
        
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM cached_article");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            for(Row row : result_set){
                if(rss_feeds.contains(row.getUUID("feed_id"))){
                    articles.add(articleFromRow(row));
                }
            }
        }
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return articles;
    }
    
    public List<Article> getAllArticles(){
        List<Article> articles = new ArrayList<>();
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM cached_article");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            for(Row row : result_set){
                articles.add(articleFromRow(row));
            }
        }
        
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return articles;
    }
    
    public List<Article> getSavedArticles(String username){
        List<UUID> saved_articles = getSavedIDs(username);
        List<Article> articles = new ArrayList<>();
        
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM saved_article");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            for(Row row : result_set){
                if(saved_articles.contains(row.getUUID("saved_article_id"))){
                    articles.add(savedArticleFromRow(row));
                }
            }
        }
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return articles;
    }
    
    public List<Article> getAllArticlesByCategory(String category){
        List<Article> articles = new ArrayList<>();
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM cached_article");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            for(Row row : result_set){
                if(row.getString("category") == null ? category == null : row.getString("category").equals(category)){
                    articles.add(articleFromRow(row));
                }
            }
        }
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return articles;
    }
    
    public Article getArticleById(UUID article_id){
        Article article;
        String title;
        String description;
        String url;
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM cached_article where cached_article_id=?");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind(article_id));
            Row row = result_set.one();
            title = row.getString("title");
            description = row.getString("description");
            url = row.getString("url");
            article = new Article(article_id, title, description, url);
        }
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return article;
    }
    
    public List<Article> getArticlesFromFeed(String feed_id){
        List<Article> articles = new ArrayList<>();
        UUID feed_uuid = java.util.UUID.fromString(feed_id);
        try(Session session = cluster.connect("Splash")){
            PreparedStatement prepared_statement = session.prepare("SELECT * FROM cached_article");
            BoundStatement bound_statement = new BoundStatement(prepared_statement);
            ResultSet result_set = session.execute(bound_statement.bind());
            for(Row row : result_set){
                if(feed_uuid == row.getUUID("feed_id")){
                    articles.add(articleFromRow(row));
                }
            }
        }
        if(timeToUpdateCache()){
            this.cache_update_thread.start();
        }
        return articles;
    }
    
    private Article articleFromRow(Row row){
        return new Article(
            row.getUUID("cached_article_id"),
            row.getString("title"),
            row.getString("description"),
            row.getString("url"),
            row.getUUID("feed_id"),
            row.getString("feed_name"),
            row.getString("category"),
            row.getString("publish_date")
        );
    }
    
    private Article savedArticleFromRow(Row row){
        return new Article(
            row.getUUID("saved_article_id"),
            row.getString("title"),
            row.getString("description"),
            row.getString("url")
        );
    }
}