package storage;

import entities.Order;
import entities.Product;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
import services.ProductService;
import services.ProductServiceImpl;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStorageDataBase implements OrderStorage {

    private ProductService productService = new ProductServiceImpl(new ProductStorageDataBase());

    @Override
    public Integer addOrder(Order order) {
        try (Connection connection = DataSource.getConnection()){
            int number = (int)(Math.random() * 10000);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into orders (id_user, date, status, number) values (?, now(), ?, ?)");
            preparedStatement.setLong(1, order.getIdUser());
            preparedStatement.setString(2, order.getStatus());
            preparedStatement.setInt(3, number);
            preparedStatement.execute();
            return number;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeOrder(Order order) {
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("delete from orders where id = ?");
            preparedStatement.setLong(1, order.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateOrder(Order order) {
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("update orders set id_user = ?, date = ?, status = ? where id = ?");
            preparedStatement.setLong(1, order.getIdUser());
            preparedStatement.setDate(2, new Date(order.getDate().getTime()));
            preparedStatement.setString(3, order.getStatus());
            preparedStatement.setLong(4, order.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findOrder(Long id) {
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("select * from orders where id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Order.builder()
                        .id(resultSet.getLong("id"))
                        .date(resultSet.getDate("date"))
                        .idUser(resultSet.getLong("id_user"))
                        .status(resultSet.getString("status"))
                        .number(resultSet.getInt("number"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAllOrders() {
        List<Order> orderList = new ArrayList<>();
        List<Order.Entity> orderEntityList = new ArrayList<>();
        ResultSet resultProduct;
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("select * from orders");

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                preparedStatement = connection.prepareStatement("select * from orders_product where id_order = ?");
                preparedStatement.setLong(1, resultSet.getLong("id"));
                resultProduct = preparedStatement.executeQuery();
                while (resultProduct.next()){
                    orderEntityList.add(Order.Entity.builder()
                            .price(resultProduct.getInt("price"))
                            .idProduct(resultProduct.getLong("id_product"))
                            .count(resultProduct.getInt("count"))
                            .build());
                }
                orderList.add(Order.builder()
                        .id(resultSet.getLong("id"))
                        .date(resultSet.getDate("date"))
                        .idUser(resultSet.getLong("id_user"))
                        .status(resultSet.getString("status"))
                        .number(resultSet.getInt("number"))
                        .orderEntity(new ArrayList<>(orderEntityList))
                        .build());

                orderEntityList.clear();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderList;
    }

    @Override
    public List<Order> findOrderByUser(Long id) {
        List<Order> orderList = new ArrayList<>();
        List<Order.Entity> orderEntityList = new ArrayList<>();
        ResultSet resultProduct;
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("select * from orders where id_user = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                preparedStatement = connection.prepareStatement("select * from orders_product where id_order = ?");
                preparedStatement.setLong(1, resultSet.getLong("id"));
                resultProduct = preparedStatement.executeQuery();
                while (resultProduct.next()){
                    orderEntityList.add(Order.Entity.builder()
                            .price(resultProduct.getInt("price"))
                            .idProduct(resultProduct.getLong("id_product"))
                            .count(resultProduct.getInt("count"))
                            .build());

                }
                orderList.add(Order.builder()
                        .id(resultSet.getLong("id"))
                        .date(resultSet.getDate("date"))
                        .idUser(resultSet.getLong("id_user"))
                        .status(resultSet.getString("status"))
                        .number(resultSet.getInt("number"))
                        .orderEntity(new ArrayList<>(orderEntityList))
                        .build());

                orderEntityList.clear();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderList;
    }

    public Order findOrderBuNumber(Integer orderNumber){
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("select * from orders where number = ?");
            preparedStatement.setInt(1, orderNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Order.builder()
                        .id(resultSet.getLong("id"))
                        .date(resultSet.getDate("date"))
                        .idUser(resultSet.getLong("id_user"))
                        .status(resultSet.getString("status"))
                        .number(resultSet.getInt("number"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProductToOrder(Integer orderNumber, Long productId, Integer count, Integer price) {
        try (Connection connection = DataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("insert into orders_product (id_order, id_product, count, price) values (?, ?, ?, ?)");
            preparedStatement.setLong(1,  findOrderBuNumber(orderNumber).getId());
            preparedStatement.setLong(2, productId);
            preparedStatement.setInt(3, count);
            preparedStatement.setInt(4, price);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findProductByOrder(Order order) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select * from orders_product join products on products.id = orders_product.id_product where id_order = ?");
            statement.setLong(1, order.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                products.add(Product.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .feature(Product.featureToMap(resultSet.getString("feature")))
                        .pathImage(resultSet.getString("path_image"))
                        .price(resultSet.getInt("price"))
                        .category(productService.findCategory(resultSet.getString("category")))
                        .count(resultSet.getInt("count"))
                        .build());

            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findTopProduct() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select id_product, count(*) from orders_product group by id_product order by count(*) desc limit 4;");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                products.add(productService.findProduct(resultSet.getLong("id_product")));
            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
