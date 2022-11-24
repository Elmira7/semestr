package servlets;

import entities.Basket;
import entities.Order;
import entities.Product;
import entities.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.EventService;
import services.OrderService;
import services.ProductService;
import services.UserService;

import java.io.IOException;

@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {

    private ProductService productService;
    private OrderService orderService;
    private UserService userService;
    private EventService eventService;

    @Override
    public void init() throws ServletException {
        productService = (ProductService) getServletContext().getAttribute("productService");
        orderService = (OrderService) getServletContext().getAttribute("orderService");
        userService = (UserService) getServletContext().getAttribute("userService");
        eventService = (EventService) getServletContext().getAttribute("eventService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (request.getParameter("create") != null && request.getParameter("create").equals("order")){
            User user = null;
            Product product = null;
            Integer orderNumber = null;

            if(session.getAttribute("login") != null){
                user = userService.findUserByLogin(session.getAttribute("login").toString());
            }

            if (user != null){

                if (!productService.findAllBasketByUser(user.getId()).isEmpty()) {

                    orderNumber = orderService.addOrder(Order.builder()
                            .status("Ожидает оплаты")
                            .idUser(user.getId())
                            .build());
                }
                for(Basket basketUser: productService.findAllBasketByUser(user.getId())){

                    product = productService.findProduct(basketUser.getProductId());

                    if(product != null) {
                        if (product.getCount() <= 0){
                            session.setAttribute("errorMessage", "Товара нет в наличии");
                            response.sendRedirect("/basket");
                            return;
                        } else {
                            if (product.getCount() <= basketUser.getCount()) basketUser.setCount(product.getCount());
                            orderService.addProductToOrder(orderNumber, product.getId(), basketUser.getCount(), (int) (product.getPrice() - (product.getPrice() * 0.01 * (eventService.findPromotionByProduct(product.getId()).stream().mapToInt(x -> x.getDiscount()).sum()))));
                            product.setCount(product.getCount() - basketUser.getCount());
                            productService.updateProduct(product);
                        }
                    }

                    productService.removeBasket(basketUser);
                }

            } else {
                session.setAttribute("errorMessage", "Необходима авторизация");
                response.sendRedirect("/login");
                return;
            }
        }

        if (request.getParameter("order") != null && request.getParameter("order").equals("post")){
            Long idOrder = null;
            Order order = null;
            try {
                idOrder = Long.parseLong(request.getParameter("id_order"));
                order = orderService.findOrder(idOrder);
                if (order != null){
                    if (order.getStatus().equals("Ожидает оплаты")) {
                        order.setStatus("Ожидает сборки");
                        orderService.updateOrder(order);
                    }
                }
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Ошибка ввода данных");
                response.sendRedirect("/profile");
                return;
            }
        }
        if (request.getParameter("order") != null && request.getParameter("order").equals("collect")){
            Long idOrder = null;
            Order order = null;
            try {
                idOrder = Long.parseLong(request.getParameter("id_order"));
                order = orderService.findOrder(idOrder);
                if (order != null){
                    if (order.getStatus().equals("Ожидает сборки")) {
                        order.setStatus("Готов к выдаче");
                        orderService.updateOrder(order);
                    }
                } else {
                    session.setAttribute("errorMessage", "Заказа не существует");
                    response.sendRedirect("/profile");
                    return;
                }
            } catch (NumberFormatException e){
                session.setAttribute("errorMessage", "Ошибка ввода данных");
                response.sendRedirect("/profile");
                return;
            }
            response.sendRedirect("/admin/order");
            return;
        }
        response.sendRedirect("/profile");
    }
}
