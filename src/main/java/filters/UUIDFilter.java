package filters;

import entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import services.UserService;

import java.io.IOException;
import java.util.UUID;

@WebFilter(urlPatterns = {"/admin/*", "/basket", "/catalog", "/", "", "/login", "/product", "/profile", "/save/image"})
public class UUIDFilter extends HttpFilter {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();

        if(session.getAttribute("uuid") == null && req.getCookies() != null){
            for (Cookie cookie: req.getCookies()){
                if (cookie.getName().equals("uuid")){
                    if (!cookie.getValue().equals("exit")){

                        User user = userService.findUserByUUID(cookie.getValue());
                        session.setAttribute("uuid", cookie.getValue());
                        if (user != null && user.getLogin() != null) {
                            session.setAttribute("login", user.getLogin());
                            session.setAttribute("role", user.getRole());
                        }
                    }
                    chain.doFilter(req, res);
                    return;
                }
            }
            String uuid = UUID.randomUUID().toString();
            userService.addUser(User.builder()
                    .uuid(uuid)
                    .role("user")
                    .build());
            Cookie cookie = new Cookie("uuid", uuid);
            cookie.setMaxAge(60*60*24*7);
            res.addCookie(cookie);
            session.setAttribute("uuid", uuid);
            session.setAttribute("role", "user");
        }
        chain.doFilter(req, res);
    }
}
