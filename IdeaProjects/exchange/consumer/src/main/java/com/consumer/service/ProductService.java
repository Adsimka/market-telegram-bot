package com.consumer.service;

import com.consumer.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> update(UUID id, Product product);

    boolean delete(UUID id);
}
