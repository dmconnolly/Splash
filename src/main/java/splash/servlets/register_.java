package splash.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import splash.models.User;

public class register_ extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        User user = new User();
        if(!user.checkExisting(username)){
            user.registerUser(username, password, name);
            request.setAttribute("registered", "Sucessfully Registered");
            request.getRequestDispatcher("/login").forward(request, response);
        }
        else{
            request.setAttribute("registered", "This username is already taken");
            request.getRequestDispatcher("/register").forward(request, response);
        }
    }
}