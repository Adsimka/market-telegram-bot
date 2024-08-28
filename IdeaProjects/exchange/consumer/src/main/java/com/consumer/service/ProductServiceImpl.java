package com.consumer.service;

import com.consumer.model.Product;
import com.consumer.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();

        log.info("Products: {}", products);

        return products;
    }

    @Override
    public Product save(Product product) {
        var createProduct = productRepository.save(product);

        log.info("Created: {}", createProduct);

        return createProduct;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        var product = productRepository.findById(id);

        log.info("Product search by id: {} - {}", id, product);

        return product;
    }

    @Override
    public Optional<Product> update(UUID id, Product product) {
        var optionalProduct = productRepository.findById(id);

        return optionalProduct.map(entity -> {
            entity.setTitle(product.getTitle());
            entity.setDescription(product.getDescription());

            log.info("Product update: {}", entity);

            return entity;
        }).map(productRepository::saveAndFlush);
    }

    @Override
    public boolean delete(UUID id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    productRepository.flush();

                    log.info("Product remove: {}", product);

                    return true;
                }).orElse(false);
    }
}
