package splash.models;

public class Article{
    private final java.util.UUID article_id;
    private final String title;
    private final String description;
    private final String url;
    
    private java.util.UUID feed_id = null;
    private String feed_name = null;
    private String category = null;
    private String publish_date = null;
    
    public Article(java.util.UUID article_id, String title,
                   String description,String url, java.util.UUID feed_id,
                   String feed_name, String category, String publish_date){
        this.article_id = article_id;
        this.title = title;
        this.description = description;
        this.url = url;
        
        this.feed_id = feed_id;
        this.feed_name = feed_name;
        this.category = category;
        this.publish_date = publish_date;
    }
    
    public Article(java.util.UUID article_id, String title,String description,String url){
        this.article_id = article_id;
        this.title = title;
        this.description = description;
        this.url = url;
    }
    
    public java.util.UUID get_article_id(){
        return this.article_id;
    }
    
    public java.util.UUID get_feed_id(){
        return this.feed_id;
    }
    
    public String get_title(){
        return this.title;
    }
    
    public String get_description(){
        return this.description;
    }
    
    public String get_url(){
        return this.url;
    }
    
    public String get_feed_name(){
        return this.feed_name;
    }
    
    public String get_category(){
        return this.category;
    }
    
    public String get_publish_date(){
        return this.publish_date;
    }
}
