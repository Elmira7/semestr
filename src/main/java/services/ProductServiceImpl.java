package services;

import entities.Basket;
import entities.Category;
import entities.Product;
import storage.ProductStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final ProductStorage productStorage;

    public ProductServiceImpl(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }

    @Override
    public void addProduct(Product product) {
        productStorage.addProduct(product);
    }

    @Override
    public void removeProduct(Product product) {
        productStorage.removeProduct(product);
    }

    @Override
    public void updateProduct(Product product) {
        productStorage.updateProduct(product);
    }

    @Override
    public Product findProduct(Long id) {
        return productStorage.findProduct(id);
    }

    @Override
    public Product findProduct(String name, Category category) {
        Product productEntity = null;
        for(Product product: listProduct()){
            if (product.getName().equals(name) && product.getCategory().equals(category)){
                productEntity = product;
            }
        }
        return productEntity;
    }

    @Override
    public List<Product> listProduct() {
        return productStorage.listProduct();
    }

    @Override
    public List<Product> listProductByCategory(String category) {
        Category categoryEntity = productStorage.findCategory(category);
        List<Product> products = new ArrayList<>();
        for(Product product: this.listProduct()){
            if(product.getCategory().equals(categoryEntity)){
                products.add(product);
            }
        }
        return products;
    }

    @Override
    public void addCategory(Category category) {
        productStorage.addCategory(category);
    }

    @Override
    public void removeCategory(Category category) {
        productStorage.removeCategory(category);
    }

    @Override
    public Map<Category, List<Category>> findAllCategories(){
        return productStorage.findAllCategories();
    }

    @Override
    public Category findCategory(String category) {
        return productStorage.findCategory(category);
    }

    @Override
    public Map<String, List<String>> mapFromCategoryToString(Map<Category, List<Category>> categoryListMap){
        Map<String, List<String>> categoryString = new HashMap<>();
        for(Map.Entry<Category, List<Category>> category: categoryListMap.entrySet()){

            categoryString.put(category.getKey().getName(), category.getValue().stream().map(x -> x.getName()).collect(Collectors.toList()));

        }
        return categoryString;
    }

    @Override
    public void addBasket(Basket basket) {
        productStorage.addBasket(basket);
    }

    @Override
    public void removeBasket(Basket basket) {
        productStorage.removeBasket(basket);
    }

    public void updateBasket(Basket basket){
        productStorage.updateBasket(basket);
    }

    @Override
    public Basket findBasket(Long idUser, Long idProduct) {
        return productStorage.findBasket(idUser, idProduct);
    }

    @Override
    public List<Basket> findAllBasket() {
        return productStorage.findAllBasket();
    }

    @Override
    public List<Basket> findAllBasketByUser(Long idUser) {
        return this.findAllBasket().stream().filter(x -> x.getUserId().equals(idUser)).collect(Collectors.toList());
    }



}
