package telegram.bot.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import telegram.bot.exception.DaoException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.function.Consumer;
import java.util.function.Function;

import static telegram.bot.util.MessageConstant.NOT_TRANSACTIONAL;

@UtilityClass
@RequiredArgsConstructor
public class EntityManagerUtil {

    private EntityManagerFactory entityManagerFactory;

    public void performWithinPersistenceContext(Consumer<EntityManager> entityManagerConsumer) {
        performReturningWithinPersistenceContext(entityManager -> {
            entityManagerConsumer.accept(entityManager);
            return null;
        });
    }

    public <T> T performReturningWithinPersistenceContext(Function<EntityManager, T> entityManagerFunction) {
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
