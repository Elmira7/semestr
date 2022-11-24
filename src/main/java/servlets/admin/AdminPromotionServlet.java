package servlets.admin;

import entities.Product;
import entities.Promotion;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.EventService;
import services.ProductService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "AdminPromotionServlet", value = "/admin/promotion")
public class AdminPromotionServlet extends HttpServlet {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private EventService eventService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        eventService = (EventService) getServletContext().getAttribute("eventService");
        productService = (ProductService) getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Promotion, List<Product>> promotionProduct = new HashMap<>();
        HttpSession session = request.getSession();

        for (Promotion promotion: eventService.findAllPromotion()){
            promotionProduct.put(promotion, eventService.listPromotionProduct(promotion.getId()));
        }

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.setAttribute("productSet", productService.listProduct());
        request.setAttribute("promotionSet", eventService.findAllPromotion());
        request.setAttribute("promotionProduct", promotionProduct);
        request.getRequestDispatcher("/WEB-INF/jsp/admin-promotion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (request.getParameter("add") != null && request.getParameter("add").equals("promotion")){
            Promotion promotion = Promotion.builder().build();

            if(request.getParameter("name") != null){
                promotion.setName(request.getParameter("name"));
            } else {
                session.setAttribute("errorMessage", "Ошибка ввода имени");
                response.sendRedirect("/admin/promotion");
                return;
            }
            if(request.getParameter("description") != null) {
                promotion.setDescription(request.getParameter("description"));
            }

            if(request.getParameter("discount") != null) {
                try {
                    promotion.setDiscount(Integer.parseInt(request.getParameter("discount")));
                } catch (NumberFormatException e) {
                    session.setAttribute("errorMessage", "Неверный формат данных");
                    response.sendRedirect("/admin/promotion");
                    return;
                }
            } else {
                promotion.setDiscount(0);
            }

            if(request.getParameter("date_begin") != null && request.getParameter("date_end") != null){
                try {
                    promotion.setDateBegin(dateFormat.parse(request.getParameter("date_begin")));
                    promotion.setDateEnd(dateFormat.parse(request.getParameter("date_end")));
                } catch (ParseException e) {
                    session.setAttribute("errorMessage", "Неверный формат данных");
                    response.sendRedirect("/admin/promotion");
                    return;
                }
            }

            eventService.addPromotion(promotion);

        }

        if (request.getParameter("add") != null && request.getParameter("add").equals("product")) {
            Product product = null;
            Promotion promotion = null;

            if (request.getParameter("product") != null
                    && !request.getParameter("product").equals("")
                    && request.getParameter("promotion") != null
                    && !request.getParameter("promotion").equals("")){
                try {
                    product = productService.findProduct(Long.parseLong(request.getParameter("product")));
                    promotion = eventService.findPromotion(Long.parseLong(request.getParameter("promotion")));


                    if (promotion != null && product != null) {
                        eventService.addProduct(promotion.getId(), product.getId());
                    } else {
                        session.setAttribute("errorMessage", "Акции или товара не сущесвует");
                        response.sendRedirect("/admin/promotion");
                        return;
                    }

                } catch (NumberFormatException e){
                    session.setAttribute("errorMessage", "Неверный формат данных");
                    response.sendRedirect("/admin/promotion");
                    return;
                }
            } else {
                session.setAttribute("errorMessage", "Ошибка ввода данных");
                response.sendRedirect("/admin/promotion");
                return;
            }
        }

        if (request.getParameter("promotion") != null && request.getParameter("promotion").equals("remove")){
            try{
                Promotion promotion = eventService.findPromotion(Long.parseLong(request.getParameter("id_promotion")));
                if(promotion != null){
                    eventService.deletePromotion(promotion);
                } else {
                    session.setAttribute("errorMessage", "Акции не существует");
                    response.sendRedirect("/admin/promotion");
                    return;
                }
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Ошибка ввода данных");
                response.sendRedirect("/admin/promotion");
                return;
            }
        }

        response.sendRedirect("/admin/promotion");
    }
}
