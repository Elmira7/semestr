package storage;

import entities.Product;
import entities.Promotion;

import java.util.List;

public interface EventStorage {
    void addPromotion(Promotion promotion);
    void deletePromotion(Promotion promotion);
    Promotion findPromotion(Long id);

    List<Promotion> findAllPromotion();

    void addProduct(Long promotionId, Long productId);
    void removeProduct(Long promotionId, Long productId);

    List<Product> listPromotionProduct(Long id);

    List<Promotion> findPromotionByProduct(Long idProduct);
}
