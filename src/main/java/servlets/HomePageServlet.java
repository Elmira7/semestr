package servlets;

import entities.Category;
import entities.Product;
import entities.Promotion;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.EventService;
import services.OrderService;
import services.ProductService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "HomePageServlet", value = "")
public class HomePageServlet extends HttpServlet {

    private ProductService productService;
    private EventService eventService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
        eventService = (EventService) getServletContext().getAttribute("eventService");
        orderService = (OrderService) getServletContext().getAttribute("orderService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, List<String>> categoryMap = productService.mapFromCategoryToString(productService.findAllCategories());
        Map<Promotion, List<Product>> promotionProduct = new HashMap<>();
        List<Product> topProduct = orderService.findTopProduct();

        for (Promotion promotion: eventService.findAllPromotion()){
            promotionProduct.put(promotion, eventService.listPromotionProduct(promotion.getId()));
        }

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.setAttribute("categoryMap", categoryMap);
        request.setAttribute("promotionSet", promotionProduct);
        request.setAttribute("topProduct", topProduct);
        request.getRequestDispatcher("/WEB-INF/jsp/homepage.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}
