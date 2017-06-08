package splash.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import splash.lib.CassandraHosts;

public class FeedControl{
    private final Cluster cluster;
    
    public FeedControl(){
        cluster = CassandraHosts.getCluster();
    }
    
    public void add_feed(String name, String category, String url){
        java.util.UUID uuid = java.util.UUID.fromString(new com.eaio.uuid.UUID().toString());
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("insert into feed (feed_id, name, rss, category) Values(?,?,?,?)");
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(uuid, name, url, category));
        }
    }
    
    public void subscribe(String username, UUID feed_id){
        PreparedStatement ps = null;
        try(Session session = cluster.connect("Splash")){
            Set<java.util.UUID> feed_id_set = new HashSet();
            feed_id_set.add(feed_id);
            if(!check_subscribe(username, feed_id)){
                ps = session.prepare("UPDATE user SET liked_source = liked_source+? where username=?");
            }
            else{
                ps = session.prepare("UPDATE user SET liked_source = liked_source-? where username=?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(feed_id_set, username));
        }
    }
    
    public boolean check_subscribe(String username, UUID feed_id){
        boolean subscribed=false;
        Set<UUID> liked_source = get_liked_feeds(username);
        if(liked_source.contains(feed_id)){
            subscribed = true;
        }
        return subscribed;
    }
    
    public Set<UUID> get_liked_feeds(String username){
        Set<UUID> liked_source;
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("SELECT liked_source from user where username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            ResultSet rs = session.execute(boundStatement.bind(username));
            Row row = rs.one();
            liked_source = row.getSet("liked_source", UUID.class);
        }
        return liked_source;
    }
    
    public void block(String username, UUID feed_id){
        PreparedStatement ps;
        try(Session session = cluster.connect("Splash")){
            Set<java.util.UUID> block_id_set = new HashSet();
            block_id_set.add(feed_id);
            if(!check_blocked(username, feed_id)){
                ps = session.prepare("UPDATE user SET blocked_source = blocked_source+? where username=?");
            }
            else{
                ps = session.prepare("UPDATE user SET blocked_source = blocked_source-? where username=?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(block_id_set, username));
        }
    }
    
    public boolean check_blocked(String username, UUID feed_id){
        boolean blocked=false;
        Set<UUID> blocked_source = get_blocked_feeds(username);
        if(blocked_source.contains(feed_id)){
            blocked = true;
        }
        return blocked;
    }
    
    public Set<UUID> get_blocked_feeds(String username){
        Set<UUID> blocked_source;
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("SELECT blocked_source from user where username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            ResultSet rs = session.execute(boundStatement.bind(username));
            Row row = rs.one();
            blocked_source = row.getSet("blocked_source", UUID.class);
        }
        return blocked_source;
    }
    
    public void readLater(String username, UUID article_id){
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try(Session session = cluster.connect("Splash")){
            Set<java.util.UUID> readLater_id_set = new HashSet();
            readLater_id_set.add(article_id);
            if(!check_readLater(username, article_id)){
                ps = session.prepare("UPDATE user SET saved_article = saved_article+? where username=?");
                addSavedArticle(article_id);
            }
            else{
                ps = session.prepare("UPDATE user SET saved_article = saved_article-? where username=?");
                ps2 = session.prepare("DELETE FROM saved_article where saved_article_id=?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(readLater_id_set, username));
            if(ps2 != null){
                BoundStatement boundStatement2 = new BoundStatement(ps2);            
                session.execute(boundStatement2.bind(article_id));
            }
        }
    }
    
    public void addSavedArticle(UUID article_id){
        ArticleFinder af = new ArticleFinder();
        Article article = af.getArticleById(article_id);
        String title = article.get_title();
        String description = article.get_description();
        String url = article.get_url();
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("INSERT INTO saved_article(saved_article_id, title, description, url) VALUES(?,?,?,?)");
            BoundStatement bs = new BoundStatement(ps);
            session.execute(bs.bind(article_id, title, description, url));
        }
    }
    
    public boolean check_readLater(String username, UUID article_id){
        boolean readLater=false;
        Set<UUID> read_later = get_read_later(username);
        if(read_later.contains(article_id)){
            readLater = true;
        }
        return readLater;
    }
    
    public Set<UUID> get_read_later(String username){
        Set<UUID> read_later;
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("SELECT saved_article from user where username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            ResultSet rs = session.execute(boundStatement.bind(username));
            Row row = rs.one();
            read_later = row.getSet("saved_article", UUID.class);
        }
        return read_later;
    }
}
