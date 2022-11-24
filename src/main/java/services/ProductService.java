package services;

import entities.Basket;
import entities.Category;
import entities.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    void addProduct(Product product);
    void removeProduct(Product product);
    void updateProduct(Product product);
    Product findProduct(Long id);
    Product findProduct(String name, Category category);
    List<Product> listProduct();
    List<Product> listProductByCategory(String category);

    void addCategory(Category category);
    void removeCategory(Category category);

    Map<Category, List<Category>> findAllCategories();
    Category findCategory(String category);

    Map<String, List<String>> mapFromCategoryToString(Map<Category, List<Category>> categoryListMap);

    void addBasket(Basket basket);
    void removeBasket(Basket basket);
    void updateBasket(Basket basket);

    Basket findBasket(Long idUser, Long idProduct);

    List<Basket> findAllBasket();

    List<Basket> findAllBasketByUser(Long idUser);

}
