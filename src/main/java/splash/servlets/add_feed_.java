package splash.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import splash.models.FeedControl;

@WebServlet(name = "add_feed_", urlPatterns = {"/add_feed_"})
public class add_feed_ extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String url = request.getParameter("url");
        FeedControl feed_control = new FeedControl();
        feed_control.add_feed(name, category, url);
        response.sendRedirect("/splash/add_feed");
    }
}