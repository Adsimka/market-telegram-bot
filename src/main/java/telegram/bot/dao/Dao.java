package telegram.bot.dao;

import telegram.bot.entity.Purchase;

import java.util.List;

public interface Dao {
    void save(Purchase purchase);

    Purchase findById(Long id);

    List<Purchase> findAll();

    void update(Purchase purchase);

    void remove(Purchase purchase);
}
