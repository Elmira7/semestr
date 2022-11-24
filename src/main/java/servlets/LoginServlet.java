package servlets;

import entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.UserService;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "RegistrationServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private static UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userLogin = null;
        String userPassword = null;
        String userMail = null;
        User user = null;
        String uuid = null;

        if(session.getAttribute("login") == null){

            if (request.getParameter("login") != null
                    && !request.getParameter("login").equals("")
                    && request.getParameter("login").length() < 32){
                userLogin = request.getParameter("login");
                user = userService.findUserByLogin(userLogin);

            } else {

                session.setAttribute("errorMessage", "Неверный логин");
                response.sendRedirect("/login");
                return;

            }

            if (request.getParameter("password") != null
                    && !request.getParameter("password").equals("")
                    && request.getParameter("password").length() < 32){
                userPassword = request.getParameter("password");
            } else {

                session.setAttribute("errorMessage", "Неверный формат пароля");
                response.sendRedirect("/login");
                return;

            }

            for(Cookie cookie: request.getCookies()){
                if (cookie.getName().equals("uuid")
                        && !cookie.getValue().equals("")){
                    uuid = cookie.getValue();
                }
            }

            if (request.getParameter("action").equals("login")){

                if(user != null){

                    if (userPassword.equals(user.getPassword())){

                        session.setAttribute("login", user.getLogin());
                        session.setAttribute("uuid", user.getUuid());
                        session.setAttribute("role", user.getRole());
                        Cookie cookie = new Cookie("uuid", user.getUuid());
                        cookie.setMaxAge(60*60*24*7);
                        response.addCookie(cookie);
                        response.sendRedirect("/");

                    } else {
                        session.setAttribute("errorMessage", "Неверный пароль");
                        response.sendRedirect("/login");
                        return;
                    }

                } else {
                    session.setAttribute("errorMessage", "Пользователь не найден");
                    response.sendRedirect("/login");
                    return;
                }

            } else if (request.getParameter("action").equals("registration")){

                if (request.getParameter("mail") != null
                        && !request.getParameter("mail").equals("")
                        && request.getParameter("mail").length() < 32){
                    userMail = request.getParameter("mail");
                } else {

                    session.setAttribute("errorMessage", "Неверная почта");
                    response.sendRedirect("/login");
                    return;

                }

                if (request.getParameter("password_repeat") != null
                        && !request.getParameter("password_repeat").equals("")
                        && request.getParameter("password_repeat").length() < 32){
                    if(!request.getParameter("password").equals(request.getParameter("password_repeat"))){
                        session.setAttribute("errorMessage", "Пароли не совпадают");
                        response.sendRedirect("/login");
                        return;
                    }
                } else {
                    session.setAttribute("errorMessage", "Неверный формат пароля");
                    response.sendRedirect("/login");
                    return;
                }

                if (user == null) {
                    if (uuid == null) {
                        uuid = UUID.randomUUID().toString();
                        Cookie cookie = new Cookie("uuid", uuid);
                        cookie.setMaxAge(60*60*24*7);
                        response.addCookie(cookie);

                        user = User.builder()
                                .login(userLogin)
                                .password(userPassword)
                                .email(userMail)
                                .role("user")
                                .uuid(uuid)
                                .build();

                        userService.addUser(user);
                    } else {

                        user = userService.findUserByUUID(uuid);

                        if (user != null){
                            user.setLogin(userLogin);
                            user.setPassword(userPassword);
                            user.setEmail(userMail);
                            userService.updateUser(user);
                        } else {
                            user = User.builder()
                                    .login(userLogin)
                                    .password(userPassword)
                                    .email(userMail)
                                    .role("user")
                                    .uuid(uuid)
                                    .build();

                            userService.addUser(user);
                        }

                    }
                    session.setAttribute("login", userLogin);
                    session.setAttribute("role", user.getRole());
                    response.sendRedirect("/");

                } else {

                    session.setAttribute("errorMessage", "Пользователь с таким ником уже существует");
                    response.sendRedirect("/login");
                    return;

                }

            } else {

                session.setAttribute("errorMessage", "Ошибка доступа");
                response.sendRedirect("/login");
                return;

            }

        } else {

            session.setAttribute("errorMessage", "Ошибка доступа");
            response.sendRedirect("/login");
            return;

        }
    }
}
