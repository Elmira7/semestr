package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.UserService;

import java.io.IOException;

@WebFilter(urlPatterns = "/login")
public class UserLoginFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if(session.getAttribute("login") == null){
            chain.doFilter(req, res);
        } else {
            session.setAttribute("errorMessage", "Вы уже авторизованы");
            res.sendRedirect("/");
        }
    }
}
