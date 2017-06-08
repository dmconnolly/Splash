package splash.lib;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public final class Keyspaces{
    public Keyspaces(){
        // Empty constructor
    }

    public static void SetUpKeySpaces(Cluster c){
        try{
            String createkeyspace = "CREATE KEYSPACE IF NOT EXISTS splash  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String createUserTable = "CREATE TABLE IF NOT EXISTS splash.user (\n"
                    + "username text,\n"
                    + "password text,\n"
                    + "name text,\n"
                    + "liked_source set<uuid>,\n"
                    + "blocked_source set<uuid>,\n"
                    + "saved_article set<uuid>,\n"
                    + "PRIMARY KEY (username)\n"
                    + ");";
            String createFeedTable = "CREATE TABLE IF NOT EXISTS splash.feed (\n"
                    + "feed_id uuid,\n"
                    + "name text,\n"
                    + "rss text,\n"
                    + "category text,\n"
                    + "PRIMARY KEY (feed_id)\n"
                    + ");";
            String createSavedArticleTable = "CREATE TABLE IF NOT EXISTS splash.saved_article (\n"
                    + "saved_article_id uuid,\n"
                    + "title text,\n"
                    + "description text,\n"
                    + "url text,\n"
                    + "PRIMARY KEY (saved_article_id)\n"
                    + ");";
            String createCachedArticleTable = "CREATE TABLE IF NOT EXISTS splash.cached_article (\n"
                    + "cached_article_id uuid,\n"
                    + "title text,\n"
                    + "description text,\n"
                    + "url text,\n"
                    + "feed_id uuid,\n"
                    + "feed_name text,\n"
                    + "category text,\n"
                    + "publish_date text,\n"
                    + "PRIMARY KEY (cached_article_id)\n"
                    + ");";
            String createCacheInfoTable = "CREATE TABLE IF NOT EXISTS splash.cache_info (\n"
                    + "cache_timestamp timestamp,\n"
                    + "PRIMARY KEY (cache_timestamp)\n"
                    + ");";
            
            Session session = c.connect();
            
            try{
                PreparedStatement statement = session.prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(statement);
                session.execute(boundStatement);
            }catch(Exception et){
                System.out.println("Can't create splash " + et);
            }
            
            try{
                SimpleStatement cqlQuery = new SimpleStatement(createUserTable);
                session.execute(cqlQuery);
            }catch(Exception et){
                System.out.println("Can't create user table " + et);
            }
            
            try{
                SimpleStatement cqlQuery = new SimpleStatement(createFeedTable);
                session.execute(cqlQuery);
            }catch(Exception et){
                System.out.println("Can't create feed table " + et);
            }
            
            try{
                SimpleStatement cqlQuery = new SimpleStatement(createSavedArticleTable);
                session.execute(cqlQuery);
            }catch(Exception et){
                System.out.println("Can't create saved article table " + et);
            }
            
            try{
                SimpleStatement cqlQuery = new SimpleStatement(createCachedArticleTable);
                session.execute(cqlQuery);
            }catch(Exception et){
                System.out.println("Can't create cached article table " + et);
            }
            
            try{
                SimpleStatement cqlQuery = new SimpleStatement(createCacheInfoTable);
                session.execute(cqlQuery);
            }catch(Exception et){
                System.out.println("Can't create cache info table " + et);
            }
            
            session.close();
            
        }catch(Exception et){
            System.out.println("Other keyspace or column definition error" + et);
        }
    }
}