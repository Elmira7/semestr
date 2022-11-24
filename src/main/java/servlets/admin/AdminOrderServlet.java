package servlets.admin;

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
import java.util.stream.Collectors;

@WebServlet(name = "AdminOrderServlet", value = "/admin/order")
public class AdminOrderServlet extends HttpServlet {
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
        Map<Order, List<Product>> orderListMap = new HashMap<>();
        List<Product> products = new ArrayList<>();
        Map<Order, Integer> priceMap = new HashMap<>();
        Map<Long, User> userMap = new HashMap<>();


        for (Order order:  orderService.findAllOrders()){
            Product product;
            Integer price = 0;
            for(Order.Entity productEntity: order.getOrderEntity()){
                product = productService.findProduct(productEntity.getIdProduct());
                products.add(product);
                price += productEntity.getPrice() * productEntity.getCount();
            }
            orderListMap.put(order, new ArrayList<>(products));
            priceMap.put(order, price);
            userMap.put(order.getId(), userService.findUser(order.getIdUser()));
            products.clear();
        }

        if (request.getParameter("order") != null && request.getParameter("order").equals("filter")){
            if(request.getParameter("status") != null){
                switch (request.getParameter("status")){
                    case "pay": {
                        orderListMap = orderListMap.entrySet().stream().filter(x -> x.getKey().getStatus().equals("Ожидает оплаты")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    }
                    case "collect": {
                        orderListMap = orderListMap.entrySet().stream().filter(x -> x.getKey().getStatus().equals("Ожидает сборки")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    }
                    case "ready": {
                        orderListMap = orderListMap.entrySet().stream().filter(x -> x.getKey().getStatus().equals("Готов к выдаче")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    }
                    case "all": break;
                }
            }
        }




        if (session.getAttribute("errorMessage") != null){
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.setAttribute("orderMap", orderListMap);
        request.setAttribute("priceMap", priceMap);
        request.setAttribute("userMap", userMap);
        request.getRequestDispatcher("/WEB-INF/jsp/admin-order.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
