package splash.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import splash.models.Article;
import splash.models.ArticleFinder;
import splash.models.FeedControl;
import splash.stores.loggedIn;

@WebServlet(name = "articles", urlPatterns = {"/articles"})
public class articles extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ArticleFinder finder = new ArticleFinder();
        String username = null;
        String category = (String)request.getParameter("category").toLowerCase();
        List<Article> articles = new ArrayList<>();
        HttpSession session = request.getSession();
        loggedIn lg = (loggedIn) session.getAttribute("loggedIn");
        Set<UUID> blocked_feeds = new HashSet<>();
        
        if(lg != null){
            username = lg.getUsername();
            FeedControl control = new FeedControl();
            blocked_feeds = control.get_blocked_feeds(username);
        }
        
        switch(category){
            case "all":
                articles = finder.getAllArticles();
                break;
            case "news":
                articles = finder.getAllArticlesByCategory("News");
                break;
            case "entertainment and arts":
                articles = finder.getAllArticlesByCategory("Entertainment and Arts");
                break;
            case "sports":
                articles = finder.getAllArticlesByCategory("Sports");
                break;
            case "gaming":
                articles = finder.getAllArticlesByCategory("Gaming");
                break;
            case "science and technology":
                articles = finder.getAllArticlesByCategory("Science and Technology");
                break;
            case "business and finance":
                articles = finder.getAllArticlesByCategory("Business and Finance");
                break;
            case "music":
                articles = finder.getAllArticlesByCategory("Music");
                break;
            case "userfeed":
                articles = finder.getAllArticlesForUser(username);
                break;
            case "readinglist":
                articles = finder.getSavedArticles(username);
                break;
        }
        
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        if(!articles.isEmpty()){
            Set<UUID> liked_feeds = new HashSet();
            Set<UUID> saved_articles = new HashSet();
            
            if(lg != null){
                FeedControl fc = new FeedControl();
                liked_feeds = fc.get_liked_feeds(username);
                saved_articles = fc.get_read_later(username);
            }
            
            for(Article article : articles){
                String title = escapeHTML(article.get_title());
                String description = article.get_description();
                String url = article.get_url();
                UUID article_id = article.get_article_id();
                
                String feed_category;
                UUID feed_id;
                String feed_name;
                
                String subscribeGlyphicon = "glyphicons-heart-empty";
                String subscribedText = "Subscribe";
                String readLaterGlyphicon = "glyphicons-bookmark";
                String readLaterText = "Read Later";
                
                if(saved_articles.contains(article_id)){
                    readLaterGlyphicon = "glyphicons-bookmark readLaterActive";
                    readLaterText = "Remove from Reading List";
                }

                if(!category.equals("readinglist")){
                    feed_category = article.get_category();
                    feed_id = article.get_feed_id();
                    feed_name = article.get_feed_name();
                    
                    if(liked_feeds.contains(feed_id)){
                        subscribeGlyphicon = "glyphicons-heart";
                        subscribedText = "Unsubscribe";
                    }
                    
                    if(!blocked_feeds.contains(feed_id)){
                        sb.append("<div class='row row-centered'>");
                        sb.append("    <div class='col-sm-12 col-md-12 col-lg-12 news-tile-container'>");
                        sb.append("        <div class='news-tile'>");
                        sb.append("                <a target='_blank' id='articleLink' href='").append(url).append("'>");
                        sb.append("                    <div class='news-tile-title'>");
                        sb.append(                         title);
                        sb.append("                    </div>");
                        sb.append("                </a>");
                        sb.append("                <div id='collapseTrigger'>");
                        sb.append("                    <a role='button' data-toggle='collapse' href='#collapse").append(count).append("' onclick='toggle_expand(").append(count).append(");'>");
                        sb.append("                        <div class='news-tile-name'>");
                        sb.append("                             Source: ").append(feed_name).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Category: ").append(feed_category);
                        sb.append("                        </div>");
                        sb.append("                        <div class='news-tile-description'>");
                        sb.append(                             description);
                        sb.append("                        </div>");
                        sb.append("                        <div class='expand-icon-container'>");
                        sb.append("                            <span class='glyphicons glyphicons-chevron-down expand-icon' id='toggle-expand-glyphicon").append(count).append("'></span>");
                        sb.append("                        </div>");
                        sb.append("                    </a>");
                        sb.append("                </div>");
                        sb.append("            <div class='collapse no-select' id='collapse").append(count).append("'>");
                        sb.append("                <div class='well options-expanded'>");
                        sb.append("                    <ul class='nav nav-pills collapseLinks'>");
                        sb.append("                        <li role='presentation'><a class='collapseLinks' onclick='subscribe(\"").append(feed_id).append("\", ").append(count).append(");'><span class='glyphicons ").append(subscribeGlyphicon).append("' id='subscribe-glyphicon").append(count).append("'></span>").append(subscribedText).append("</a></li>");
                        sb.append("                        <li role='presentation'><a class='collapseLinks' onclick='block(\"").append(feed_id).append("\", ").append(count).append(");'><span class='glyphicons glyphicons-ban' id='block-glyphicon").append(count).append("'></span>Block</a></li>");
                        sb.append("                        <li role='presentation'><a class='collapseLinks' onclick='readLater(\"").append(article_id).append("\", ").append(count).append(");'><span class='glyphicons ").append(readLaterGlyphicon).append("' id='readLater-glyphicon").append(count).append("'></span>").append(readLaterText).append("</a></li>");
                        sb.append("                    </ul>");
                        sb.append("                </div>");
                        sb.append("            </div>");
                        sb.append("        </div>");
                        sb.append("    </div>");
                        sb.append("</div>");
                        count++;
                    }
                }
                else{
                    sb.append("<div class='row row-centered'>");
                    sb.append("    <div class='col-sm-12 col-md-12 col-lg-12 news-tile-container'>");
                    sb.append("        <div class='news-tile'>");
                    sb.append("            <a target='_blank' id='articleLink' href='").append(url).append("'>");
                    sb.append("                <div class='news-tile-title'>");
                    sb.append(                     title);
                    sb.append("                </div>");
                    sb.append("            </a>");
                    sb.append("            <div class='news-tile-description'>");
                    sb.append(                 description);
                    sb.append("            </div>");
                    sb.append("            <li><a class='collapseLinks' onclick='readLater(\"").append(article_id).append("\", ").append(count).append(");'><span class='glyphicons ").append(readLaterGlyphicon).append("' id='readLater-glyphicon").append(count).append("'></span>").append(readLaterText).append("</a></li>");
                    sb.append("        </div>");
                    sb.append("    </div>");
                    sb.append("</div>");
                    count++;
                }
            }
        }
        else{
            if(category.equals("readingList")){
                sb.append("<div class='row row-centered'>");
                sb.append("    <div class='col-sm-12 col-md-12 col-lg-12 news-tile-container'>");
                sb.append("        <p id='emptySource'>You are not subscribed to any sources. Explore categories and subscribe to Sources in order to customise your own news feed</p>");
                sb.append("    </div>");
                sb.append("</div>");
            }
            else{
                sb.append("<div class='row row-centered'>");
                sb.append("    <div class='col-sm-12 col-md-12 col-lg-12 news-tile-container'>");
                sb.append("        <p id='emptySource'>You have not got any articles in your reading list. When browsing, click on the bookmark for future reading.</p>");
                sb.append("    </div>");
                sb.append("</div>");
            }
        }
        sb.append("</div>");
        out.println(sb);
    }
    
    // Method modified from http://stackoverflow.com/a/25228492/1791872
    public static String escapeHTML(String s){
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for(int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if(c>127 || c=='"' || c=='<' || c=='>' || c=='&' || c=='\''){
                out.append("&#");
                out.append((int) c);
                out.append(';');
            }else{
                out.append(c);
            }
        }
        return out.toString();
    }
}
