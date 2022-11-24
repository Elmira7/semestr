package services;

import entities.Order;
import entities.Product;

import java.util.List;

public interface OrderService {
    Integer addOrder(Order order);
    void removeOrder(Order order);
    void updateOrder(Order order);
    Order findOrder(Long id);
    List<Order> findAllOrders();
    List<Order> findOrderByUser(Long id);

    void addProductToOrder(Integer orderNumber, Long productId, Integer count, Integer price);

    List<Product> findProductByOrder(Order order);
    List<Product> findTopProduct();
}
