package storage;

import entities.Basket;
import entities.Category;
import entities.Product;
import lombok.Data;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStorageDataBase implements ProductStorage {

    @Override
    public void addProduct(Product product) {
        try (Connection connection = DataSource.getConnection()){

            System.out.println(product);
            PreparedStatement statement = connection.prepareStatement("insert into products (name, path_image, price, feature, description, category, count) values (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, product.getName());
            statement.setString(2, product.getPathImage());
            statement.setInt(3, product.getPrice());
            statement.setString(4, Product.featureToString(product.getFeature()));
            statement.setString(5, product.getDescription());
            statement.setString(6, product.getCategory() == null ? null : product.getCategory().getName());
            statement.setInt(7, product.getCount());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeProduct(Product product) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("delete from products where id = ?");
            statement.setLong(1, product.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProduct(Product product) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("update products set name = ?, path_image = ?, price = ?, feature = ?, description = ?, category = ?, count = ? where id = ?");
            statement.setString(1, product.getName());
            statement.setString(2, product.getPathImage());
            statement.setInt(3, product.getPrice());
            statement.setString(4, Product.featureToString(product.getFeature()));
            statement.setString(5, product.getDescription());
            statement.setString(6, product.getCategory() == null ? null : product.getCategory().getName());
            statement.setInt(7, product.getCount());
            statement.setLong(8, product.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findProduct(Long id) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select * from products where id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return Product.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .feature(Product.featureToMap(resultSet.getString("feature")))
                        .pathImage(resultSet.getString("path_image"))
                        .price(resultSet.getInt("price"))
                        .category(findCategory(resultSet.getString("category")))
                        .count(resultSet.getInt("count"))
                        .build();

            } else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> listProduct() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select * from products");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                products.add(Product.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .feature(Product.featureToMap(resultSet.getString("feature")))
                        .pathImage(resultSet.getString("path_image"))
                        .price(resultSet.getInt("price"))
                        .category(findCategory(resultSet.getString("category")))
                        .count(resultSet.getInt("count"))
                        .build());

            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCategory(Category category) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("insert into category (name, parent_name) values (?, ?)");
            statement.setString(1, category.getName());
            statement.setString(2, category.getParentName());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCategory(Category category) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("delete from category where name = ? and parent_name = ?");
            statement.setString(1, category.getName());
            statement.setString(2, category.getParentName());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Category, List<Category>> findAllCategories() {
        List<Category> categories = new ArrayList<>();
        List<Category> childrenCategory = new ArrayList<>();
        Map<Category, List<Category>> categoryListHashMap = new HashMap<>();
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement("select * from category where parent_name is null");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                categories.add(Category.builder()
                        .name(resultSet.getString("name"))
                        .build());
            }

            for (Category category: categories){

                preparedStatement = connection.prepareStatement("select * from category where parent_name = ? and parent_name is not null");
                preparedStatement.setString(1, category.getName());

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    childrenCategory.add(Category.builder()
                        .name(resultSet.getString("name"))
                        .parentName(resultSet.getString("parent_name"))
                        .build());
                }
                categoryListHashMap.put(category, new ArrayList<>(childrenCategory));
                childrenCategory.clear();
            }

            return categoryListHashMap;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category findCategory(String category){
        Category categoryPost = null;
        for(Map.Entry<Category, List<Category>> categoryEntry: findAllCategories().entrySet()){
            if(categoryEntry.getKey().getName().equals(category)){
                return Category.builder()
                        .name(category)
                        .build();
            } else {
                categoryPost = Category.builder()
                        .name(category)
                        .parentName(categoryEntry.getKey().getName())
                        .build();
                if(categoryEntry.getValue().contains(categoryPost)){
                    return categoryPost;
                }
            }
        }
        return null;
    }

    @Override
    public void addBasket(Basket basket) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("insert into basket (id_user, id_product, count) values (?, ?, ?)");
            statement.setLong(1, basket.getUserId());
            statement.setLong(2, basket.getProductId());
            statement.setInt(3, basket.getCount());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeBasket(Basket basket) {
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("delete from basket where id_user = ? and id_product = ?");
            statement.setLong(1, basket.getUserId());
            statement.setLong(2, basket.getProductId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Basket findBasket(Long idUser, Long idProduct) {
        Basket basket = null;
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select * from basket where id_user = ? and id_product = ?");
            statement.setLong(1, idUser);
            statement.setLong(2, idProduct);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return Basket.builder()
                        .productId(resultSet.getLong("id_product"))
                        .userId(resultSet.getLong("id_user"))
                        .count(resultSet.getInt("count"))
                        .build();

            } else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Basket> findAllBasket() {
        List<Basket> baskets = new ArrayList<>();
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("select * from basket");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                baskets.add(Basket.builder()
                        .productId(resultSet.getLong("id_product"))
                        .userId(resultSet.getLong("id_user"))
                        .count(resultSet.getInt("count"))
                        .build());

            }

            return baskets;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBasket(Basket basket){
        try (Connection connection = DataSource.getConnection()){

            PreparedStatement statement = connection.prepareStatement("update basket set count = ? where id_product = ? and id_user = ?");
            statement.setInt(1, basket.getCount());
            statement.setLong(2, basket.getProductId());
            statement.setLong(3, basket.getUserId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
