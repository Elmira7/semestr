package servlets;

import entities.Basket;
import entities.Product;
import entities.Promotion;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.EventService;
import services.ProductService;
import services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private UserService userService;
    private EventService eventService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
        userService = (UserService) getServletContext().getAttribute("userService");
        eventService = (EventService) getServletContext().getAttribute("eventService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long productId = null;
        Product product = null;
        User user = null;
        Map<String, List<String>> categoryMap = productService.mapFromCategoryToString(productService.findAllCategories());
        Integer price = null;

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        try{
            productId = Long.parseLong(request.getParameter("product_id"));
            product = productService.findProduct(productId);
            price = (int)(product.getPrice() - (product.getPrice() * 0.01 * (eventService.findPromotionByProduct(product.getId()).stream().mapToInt(x -> x.getDiscount()).sum())));

            if (session.getAttribute("uuid") != null) {
                user = userService.findUserByUUID(session.getAttribute("uuid").toString());
            }

        } catch (NumberFormatException e){
            session.setAttribute("errorMessage", "Неверный формат данных");
            response.sendRedirect("/basket");
            return;
        }

        if (product != null){

            request.setAttribute("product", product);
            request.setAttribute("categoryMap", categoryMap);
            request.setAttribute("user", user);
            request.setAttribute("price", price);
            request.setAttribute("promotionSet", eventService.findPromotionByProduct(product.getId()));
            request.getRequestDispatcher("/WEB-INF/jsp/product.jsp").forward(request, response);

        } else {
            response.sendRedirect("/catalog");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = null;
        User user = null;
        HttpSession session = request.getSession();
        if (request.getParameter("product") != null
                && request.getParameter("user") != null){
            try {
                product = productService.findProduct(Long.parseLong(request.getParameter("product")));
                user = userService.findUser(Long.parseLong(request.getParameter("user")));
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Неверный формат данных");
                response.sendRedirect("/basket");
                return;
            }

        }


        if (product != null && user != null) {

            if (product.getCount() <= 0){
                session.setAttribute("errorMessage", "Товара нет в наличии");
                response.sendRedirect("/basket");
                return;
            }

            if (productService.findBasket(user.getId(), product.getId()) != null){
                session.setAttribute("errorMessage", "Товар уже в корзине");
                response.sendRedirect("/basket");
                return;
            } else {
                productService.addBasket(Basket.builder()
                        .userId(user.getId())
                        .productId(product.getId())
                        .count(1)
                        .build());
            }
        } else {
            session.setAttribute("errorMessage", "Продукт не найден");
            response.sendRedirect("/basket");
            return;
        }

        response.sendRedirect("/basket");
    }
}
