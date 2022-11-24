package listeners;

import entities.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.*;
import storage.*;
import utils.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {

    private static ProductService productService;
    private static UserService userService;
    private static EventService eventService;
    private static OrderService orderService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        productService = new ProductServiceImpl(new ProductStorageDataBase());
        userService = new UserServiceImpl(new UserStorageDataBase());
        eventService = new EventServiceImpl(new EventStorageDataBase());
        orderService = new OrderServiceImpl(new OrderStorageDataBase());
        sce.getServletContext().setAttribute("productService", productService);
        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("eventService", eventService);
        sce.getServletContext().setAttribute("orderService", orderService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
