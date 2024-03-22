package telegram.bot.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import telegram.bot.entity.Purchase;
import telegram.bot.exception.DaoException;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static telegram.bot.util.MessageConstant.NOT_TRANSACTIONAL;

public class PurchaseDao implements Dao {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void save(Purchase purchase) {
        performWithinPersistenceContext(entityManager -> entityManager.persist(purchase));
    }

    @Override
    public Purchase findById(Long id) {
        return performReturningWithinPersistenceContext(entityManager -> entityManager.find(Purchase.class, id));
    }

    @Override
    public List<Purchase> findAll() {
        return performReturningWithinPersistenceContext(entityManager ->
                entityManager.createQuery("select a from Account a", Purchase.class).getResultList());
    }

    @Override
    public void update(Purchase purchase) {
        performWithinPersistenceContext(em -> em.merge(purchase));
    }

    @Override
    public void remove(Purchase purchase) {
        performWithinPersistenceContext(entityManager -> {
            Purchase mergedAccount = entityManager.merge(purchase);
            entityManager.remove(mergedAccount);
        });
    }

    private void performWithinPersistenceContext(Consumer<EntityManager> entityManagerConsumer) {
        performReturningWithinPersistenceContext(entityManager -> {
            entityManagerConsumer.accept(entityManager);
            return null;
        });
    }

    private <T> T performReturningWithinPersistenceContext(Function<EntityManager, T> entityManagerFunction) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        T result;
        try {
            result = entityManagerFunction.apply(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new DaoException(NOT_TRANSACTIONAL, e);
        } finally {
            entityManager.close();
        }
        return result;
    }
}
