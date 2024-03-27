package telegram.bot.dao;

import telegram.bot.entity.Purchase;
import telegram.bot.util.EntityManagerUtil;

import java.util.List;

public class PurchaseDao implements Dao {

    private final static PurchaseDao INSTANCE = new PurchaseDao();

    @Override
    public void save(Purchase purchase) {
        EntityManagerUtil.performWithinPersistenceContext(entityManager -> entityManager.persist(purchase));
    }

    @Override
    public Purchase findById(Long id) {
        return EntityManagerUtil.performReturningWithinPersistenceContext(entityManager -> entityManager.find(Purchase.class, id));
    }

    @Override
    public List<Purchase> findAll() {
        return EntityManagerUtil.performReturningWithinPersistenceContext(entityManager ->
                entityManager.createQuery("select a from Account a", Purchase.class).getResultList());
    }

    @Override
    public void update(Purchase purchase) {
        EntityManagerUtil.performWithinPersistenceContext(em -> em.merge(purchase));
    }

    @Override
    public void remove(Purchase purchase) {
        EntityManagerUtil.performWithinPersistenceContext(entityManager -> {
            Purchase mergedAccount = entityManager.merge(purchase);
            entityManager.remove(mergedAccount);
        });
    }

    public static PurchaseDao getInstance() {
        return INSTANCE;
    }
}
