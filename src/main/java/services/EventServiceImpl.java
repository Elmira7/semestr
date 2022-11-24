package services;

import entities.Product;
import entities.Promotion;
import storage.EventStorage;

import java.util.List;

public class EventServiceImpl implements EventService {

    private EventStorage eventStorage;

    public EventServiceImpl(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Override
    public void addPromotion(Promotion promotion) {
        eventStorage.addPromotion(promotion);
    }

    @Override
    public void deletePromotion(Promotion promotion) {
        eventStorage.deletePromotion(promotion);
    }

    @Override
    public Promotion findPromotion(Long id) {
        return eventStorage.findPromotion(id);
    }

    @Override
    public List<Promotion> findAllPromotion() {
        return eventStorage.findAllPromotion();
    }

    @Override
    public void addProduct(Long promotionId, Long productId){
        eventStorage.addProduct(promotionId, productId);
    }

    @Override
    public void removeProduct(Long promotionId, Long productId){
        eventStorage.removeProduct(promotionId, productId);
    }

    @Override
    public List<Product> listPromotionProduct(Long id){
        return eventStorage.listPromotionProduct(id);
    }

    @Override
    public List<Promotion> findPromotionByProduct(Long idProduct){
        return eventStorage.findPromotionByProduct(idProduct);
    }

}
