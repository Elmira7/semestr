package storage;

import entities.Product;
import entities.Promotion;
import services.ProductService;
import services.ProductServiceImpl;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventStorageDataBase implements EventStorage {

    private ProductService productService = new ProductServiceImpl(new ProductStorageDataBase());

    @Override
    public void addPromotion(Promotion promotion) {
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("insert into promotion (date_begin, date_end, name, discount, description) values (?, ?, ?, ?, ?)");
            statement.setDate(1, new Date(promotion.getDateBegin().getTime()));
            statement.setDate(2,  new Date(promotion.getDateEnd().getTime()));
            statement.setString(3, promotion.getName());
            statement.setInt(4, promotion.getDiscount());
            statement.setString(5, promotion.getDescription());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePromotion(Promotion promotion) {
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("delete from promotion where id = ?");
            statement.setLong(1, promotion.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Promotion findPromotion(Long id) {
        Promotion promotion = null;
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from promotion where id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                promotion =  Promotion.builder()
                        .id(resultSet.getLong("id"))
                        .dateBegin(resultSet.getDate("date_begin"))
                        .dateEnd(resultSet.getDate("date_end"))
                        .discount(resultSet.getInt("discount"))
                        .description(resultSet.getString("description"))
                        .name(resultSet.getString("name"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return promotion;
    }

    @Override
    public List<Promotion> findAllPromotion() {
        List<Promotion> listPromotion = new ArrayList<>();
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from promotion");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                listPromotion.add(Promotion.builder()
                        .id(resultSet.getLong("id"))
                        .dateBegin(resultSet.getDate("date_begin"))
                        .dateEnd(resultSet.getDate("date_end"))
                        .discount(resultSet.getInt("discount"))
                        .description(resultSet.getString("description"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPromotion;
    }

    @Override
    public void addProduct(Long promotionId, Long productId) {
        try(Connection connection = DataSource.getConnection()){
            if (!this.listPromotionProduct(promotionId).stream().filter(x -> x.getId().equals(productId)).collect(Collectors.toList()).isEmpty()) return;

            PreparedStatement statement = connection.prepareStatement("insert into promotion_product (id_promotion, id_product) values (?, ?)");
            statement.setLong(1, promotionId);
            statement.setLong(2,  productId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeProduct(Long promotionId, Long productId) {
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("delete from promotion_product where id_promotion = ? and id_product = ?");
            statement.setLong(1, promotionId);
            statement.setLong(2,  productId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> listPromotionProduct(Long id) {
        List<Product> products = new ArrayList<>();
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from promotion_product  join products on promotion_product.id_product = products.id where id_promotion = ?");
            statement.setLong(1, id);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public List<Promotion> findPromotionByProduct(Long idProduct){
        List<Promotion> listPromotion = new ArrayList<>();
        try(Connection connection = DataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("select * from promotion join promotion_product on promotion.id = promotion_product.id_promotion where id_product = ?");
            statement.setLong(1, idProduct);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                listPromotion.add(Promotion.builder()
                        .id(resultSet.getLong("id"))
                        .dateBegin(resultSet.getDate("date_begin"))
                        .dateEnd(resultSet.getDate("date_end"))
                        .discount(resultSet.getInt("discount"))
                        .description(resultSet.getString("description"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPromotion;
    }

}
