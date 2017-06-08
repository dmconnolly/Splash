package splash.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import splash.models.User;
import splash.stores.loggedIn;

public class login_ extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String username=request.getParameter("username");
        String password=request.getParameter("password");        
        HttpSession session=request.getSession();
        User user = new User();
        if(user.checkExisting(username)){
            if(user.checkPassword(password, username)){
                loggedIn lg = new loggedIn();
                lg.setUsername(username);
                lg.setName(user.getName(username));
                lg.logIn();
                session.setAttribute("loggedIn", lg);
                response.sendRedirect("/splash/");
            }
            else{
                request.setAttribute("registered", "Password is Wrong");
                request.getRequestDispatcher("/login").forward(request, response);
            }
        }
        else{
            request.setAttribute("registered", "User Does Not Exist");
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }
}