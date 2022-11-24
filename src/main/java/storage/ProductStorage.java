package storage;

import entities.Basket;
import entities.Category;
import entities.Product;

import java.util.List;
import java.util.Map;

public interface ProductStorage {

    void addProduct(Product product);
    void removeProduct(Product product);
    void updateProduct(Product product);
    Product findProduct(Long id);
    List<Product> listProduct();

    void addCategory(Category category);
    void removeCategory(Category category);

    Map<Category, List<Category>> findAllCategories();
    Category findCategory(String category);

    void addBasket(Basket basket);
    void removeBasket(Basket basket);
    void updateBasket(Basket basket);

    Basket findBasket(Long idUser, Long idProduct);

    List<Basket> findAllBasket();



}
