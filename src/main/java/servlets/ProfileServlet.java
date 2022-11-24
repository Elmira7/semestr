package servlets;

import entities.Order;
import entities.Product;
import entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.OrderService;
import services.ProductService;
import services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {
    private UserService userService;
    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        productService = (ProductService) getServletContext().getAttribute("productService");
        orderService = (OrderService) getServletContext().getAttribute("orderService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = null;
        Map<String, List<String>> categoryMap = productService.mapFromCategoryToString(productService.findAllCategories());
        Map<Order, List<Product>> orderListMap = new HashMap<>();
        List<Product> products = new ArrayList<>();
        Map<Order, Integer> priceMap = new HashMap<>();

        if (session.getAttribute("login") != null) {
            user = userService.findUserByLogin(session.getAttribute("login").toString());
        }

        if(user != null){

            for (Order order:  orderService.findOrderByUser(user.getId())){
                Product product;
                Integer price = 0;
                for(Order.Entity productEntity: order.getOrderEntity()){
                    product = productService.findProduct(productEntity.getIdProduct());
                    products.add(product);
                    price += productEntity.getPrice() * productEntity.getCount();
                }
                orderListMap.put(order, new ArrayList<>(products));
                priceMap.put(order, price);
                products.clear();
            }

        }

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.setAttribute("categoryMap", categoryMap);
        request.setAttribute("orderMap", orderListMap);
        request.setAttribute("priceMap", priceMap);
        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if(request.getParameter("action") != null && request.getParameter("action").equals("exit")){
            if (session.getAttribute("login") != null){
                User user = userService.findUserByLogin(session.getAttribute("login").toString());
                if(user != null){
                    user.setUuid(session.getAttribute("uuid").toString());
                    userService.updateUser(user);
                    session.removeAttribute("login");
                    session.removeAttribute("uuid");
                    session.setAttribute("role", "user");
                    session.setAttribute("errorMessage", "Вы вышли из системы");
                    Cookie cookie = new Cookie("uuid", "exit");
                    cookie.setMaxAge(60*60*24*7);
                    response.addCookie(cookie);
                    response.sendRedirect("/");
                }
            } else {
                session.setAttribute("errorMessage", "Вы не авторизованы");
                response.sendRedirect("/");
                return;
            }
        }
    }
}
