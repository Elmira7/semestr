package servlets;

import entities.Basket;
import entities.Product;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/basket")
public class BasketServlet extends HttpServlet {

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
        Map<String, List<String>> categoryMap = productService.mapFromCategoryToString(productService.findAllCategories());
        User user = null;
        List<Basket> basketList = new ArrayList<>();
        Map<Product, Basket> basketMap = new HashMap<>();
        Map<Product, Integer> promotionMap = new HashMap<>();
        Integer price = 0;

        if(session.getAttribute("uuid") != null){
            user = userService.findUserByUUID(session.getAttribute("uuid").toString());
            if (user != null){
                basketList = productService.findAllBasketByUser(user.getId());
            }
        }

        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        for(Basket basketEntity: basketList){
            basketMap.put(productService.findProduct(basketEntity.getProductId()), basketEntity);
        }

        Integer discount = 0;
        for(Map.Entry<Product, Basket> product: basketMap.entrySet()){
            discount = (int)(product.getKey().getPrice() - (product.getKey().getPrice() * 0.01 * (eventService.findPromotionByProduct(product.getKey().getId()).stream().mapToInt(x -> x.getDiscount()).sum())));
            promotionMap.put(product.getKey(), discount);
            price += discount * product.getValue().getCount();

        }

        request.setAttribute("categoryMap", categoryMap);
        request.setAttribute("user", user);
        request.setAttribute("basketSet", basketList);
        request.setAttribute("basketMap", basketMap);
        request.setAttribute("promotionMap", promotionMap);
        request.setAttribute("price", price);
        request.getRequestDispatcher("/WEB-INF/jsp/basket.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (req.getParameter("change") != null && req.getParameter("change").equals("count")){
            try{
                Long productId = Long.parseLong(req.getParameter("product"));
                Long userId = Long.parseLong(req.getParameter("user"));
                Integer countProduct = Integer.parseInt(req.getParameter("count"));
                Product product = productService.findProduct(productId);

                Basket basket = productService.findBasket(userId, productId);

                if (basket != null && product != null && countProduct <= product.getCount()) {
                    basket.setCount(countProduct);
                    productService.updateBasket(basket);
                }
                resp.sendRedirect("/basket");
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Неверный формат данных");
                resp.sendRedirect("/admin/promotion");
                return;
            }
        }
        if (req.getParameter("remove") != null && req.getParameter("remove").equals("basket")){
            try{
                Basket basket = productService.findBasket(Long.parseLong(req.getParameter("user")), Long.parseLong(req.getParameter("product")));
                if (basket != null) {
                    productService.removeBasket(basket);
                    resp.sendRedirect("/basket");
                } else {
                    session.setAttribute("errorMessage", "Позиция в корзине не найдена");
                    resp.sendRedirect("/admin/promotion");
                    return;
                }
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Неверный формат данных");
                resp.sendRedirect("/admin/promotion");
                return;
            }
        }
    }
}
