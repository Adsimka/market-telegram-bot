package com.producer.service;

import com.producer.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product save(Product product);

    Product findById(UUID id);

    Product update(UUID id, Product product);

    void delete(UUID id);
}
