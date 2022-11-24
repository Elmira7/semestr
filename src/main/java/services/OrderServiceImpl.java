package services;

import entities.Order;
import entities.Product;
import storage.OrderStorage;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderStorage orderStorage;

    public OrderServiceImpl(OrderStorage orderStorage) {
        this.orderStorage = orderStorage;
    }


    @Override
    public Integer addOrder(Order order) {
        return orderStorage.addOrder(order);
    }

    @Override
    public void removeOrder(Order order) {
        orderStorage.removeOrder(order);
    }

    @Override
    public void updateOrder(Order order) {
        orderStorage.updateOrder(order);
    }

    @Override
    public Order findOrder(Long id) {
        return orderStorage.findOrder(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderStorage.findAllOrders();
    }

    @Override
    public List<Order> findOrderByUser(Long idUser) {
        return orderStorage.findOrderByUser(idUser);
    }

    @Override
    public void addProductToOrder(Integer numberOrder, Long productId, Integer count, Integer price) {
        orderStorage.addProductToOrder(numberOrder, productId, count, price);
    }

    @Override
    public List<Product> findProductByOrder(Order order) {
        return orderStorage.findProductByOrder(order);
    }

    @Override
    public List<Product> findTopProduct() {
        return orderStorage.findTopProduct();
    }
}
