package splash.servlets;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import splash.models.ArticleFinder;
import splash.models.FeedControl;

public class fetch_blocked extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String username = (String)request.getParameter("username").toLowerCase();
        FeedControl fc = new FeedControl();
        ArticleFinder ac = new ArticleFinder();
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        Set<UUID> blocked_sources = fc.get_blocked_feeds(username);
        ResultSet rs = ac.getAllRssFeeds();
        int count = 0;
        sb.append("<h2>Manage Blocked Feeds</h2>");
        sb.append("<table class='table'>");
        sb.append("    <thead>");
        sb.append("    <tr>");
        sb.append("        <th>Feed Name</td>");
        sb.append("        <th>Feed Category</td>");
        sb.append("        <td></td>");
        sb.append("    </tr>");
        sb.append("    </thead>");
        sb.append("    <tbody>");
        for(Row row: rs){
            if(blocked_sources.contains(row.getUUID("feed_id")))
            {
                sb.append("    <tr>");
                sb.append("        <td>").append(row.getString("name")).append("</td>");
                sb.append("        <td>").append(row.getString("category")).append("</td>");
                sb.append("        <td><li role='presentation'><a class='collapseLinks unblock-anchor' onclick='block(\"").append(row.getUUID("feed_id")).append("\", ").append(count).append(");'><span class='glyphicons glyphicons-ban blockActive' id='block-glyphicon").append(count).append("'></span>Unblock</a></li></td>");
                sb.append("    </tr>");
                count++;
            }
        }
        sb.append("    </tbody>");
        sb.append("</table>");
        out.println(sb);
    }
}