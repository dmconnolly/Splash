package splash.servlets;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import splash.models.FeedControl;
import splash.stores.loggedIn;

public class readLater extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String article_id_string = (String)request.getParameter("articleId");
        UUID article_id = UUID.fromString(article_id_string);
        String title = (String)request.getParameter("title");
        String description = (String)request.getParameter("description");
        String url = (String)request.getParameter("url");
        HttpSession session = request.getSession();
        loggedIn lg = (loggedIn)session.getAttribute("loggedIn");
        String username = lg.getUsername();
        FeedControl feed_control = new FeedControl();
        feed_control.readLater(username, article_id);
    }
}