package splash.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import splash.models.FeedControl;
import splash.stores.loggedIn;

public class add_feeds extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        loggedIn lg = (loggedIn)session.getAttribute("loggedIn");
        
        if(lg == null){
            response.sendRedirect("/splash/");
            return;
        }
        
        String username = lg.getUsername();
        
        if((!username.equals("lewis")) && (!lg.getUsername().equals("danny"))){
            response.sendRedirect("/splash/");
        }
        
        FeedControl feed_control = new FeedControl();
        
        String[] name = {
            // News
            "BBC",
            "BBC",
            "The Independent",
            "Sky",
            "Huffington Post",
            "NPR",
            // Entertainment and Arts
            "BBC",
            "The Independent",
            "Sky",
            "Huffington Post",
            "NPR",
            // Sports
            "BBC",
            "The Independent",
            "ESPN",
            "UEFA",
            "NPR",
            // Gaming
            "Gamespot",
            "Games Radar",
            // Science and Technology
            "BBC",
            "Huffington Post",
            "NASA",
            // Business and Finance
            "BBC",
            "NPR",
            // Music
            "Rolling Stone",
            "NME",
            "Pitchfork",
            "NPR",
        };
        String[] category = {
            // News
            "News",
            "News",
            "News",
            "News",
            "News",
            "News",
            // Entertainment and Arts
            "Entertainment and Arts",
            "Entertainment and Arts",
            "Entertainment and Arts",
            "Entertainment and Arts",
            "Entertainment and Arts",
            // Sports
            "Sports",
            "Sports",
            "Sports",
            "Sports",
            "Sports",
            // Gaming
            "Gaming",
            "Gaming",
            // Science and Technology
            "Science and Technology",
            "Science and Technology",
            "Science and Technology",
            // Business and Finance
            "Business and Finance",
            "Business and Finance",
            // Music
            "Music",
            "Music",
            "Music",
            "Music",
        };
        String[] url = {
            // News
            "http://feeds.bbci.co.uk/news/uk/rss.xml",
            "http://feeds.bbci.co.uk/news/rss.xml",
            "http://www.independent.co.uk/news/rss",
            "http://feeds.skynews.com/feeds/rss/home.xml",
            "http://www.huffingtonpost.co.uk/feeds/verticals/uk/index.xml",
            "http://www.npr.org/rss/rss.php?id=1001",
            // Entertainment and Arts
            "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml",
            "http://www.independent.co.uk/arts-entertainment/rss",
            "http://feeds.skynews.com/feeds/rss/entertainment.xml",
            "http://www.huffingtonpost.co.uk/feeds/verticals/uk-entertainment/index.xml",
            "http://www.npr.org/rss/rss.php?id=1008",
            // Sports
            "http://feeds.bbci.co.uk/sport/0/rss.xml",
            "http://www.independent.co.uk/sport/rss",
            "http://sports.espn.go.com/espn/rss/news",
            "http://www.uefa.com/rssfeed/news/rss.xml",
            "http://www.npr.org/rss/rss.php?id=1055",
            // Gaming
            "http://www.gamespot.com/feeds/mashup/",
            "http://www.gamesradar.com/all-platforms/news/rss/",
            // Science and Technology
            "http://feeds.bbci.co.uk/news/technology/rss.xml",
            "http://www.huffingtonpost.co.uk/feeds/verticals/uk-tech/index.xml",
            "https://www.nasa.gov/rss/dyn/breaking_news.rss",
            // Business and Finance
            "http://feeds.bbci.co.uk/news/business/rss.xml",
            "http://www.npr.org/rss/rss.php?id=1017",
            // Music
            "http://www.rollingstone.com/music.rss",
            "http://www.nme.com/rss/news",
            "http://pitchfork.com/rss/news/",
            "http://www.npr.org/rss/rss.php?id=1039",
        };
        
        for(int i=0; i<name.length; i++){
            feed_control.add_feed(name[i], category[i], url[i]);
        }
        
        response.sendRedirect("/splash/");
    }
}