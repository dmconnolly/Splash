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

public class subscribe extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String feed_id_string = (String)request.getParameter("feedId");
        UUID feed_id = UUID.fromString(feed_id_string);
        HttpSession session = request.getSession();
        loggedIn lg = (loggedIn)session.getAttribute("loggedIn");
        String username = lg.getUsername();
        FeedControl feed_control = new FeedControl();
        feed_control.subscribe(username, feed_id);
    }
}