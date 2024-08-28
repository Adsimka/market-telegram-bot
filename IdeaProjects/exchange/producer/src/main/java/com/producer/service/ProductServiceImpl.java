package com.producer.service;

import com.producer.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final RestClient restClient;

    @Override
    public List<Product> findAll() {
        var products = restClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(List.class);

        log.info("List of products: {}", products);

        return products;
    }

    @Override
    public Product save(Product product) {
        var entity = restClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(product)
                .retrieve()
                .toEntity(Product.class);

        log.info("Created product: {}", entity.getBody());

        if (entity.getStatusCode().is2xxSuccessful()) {
            log.info("Created: {}", entity.getStatusCode());
        }

        return entity.getBody();
    }

    @Override
    public Product update(UUID id, Product product) {
        var entity = restClient.put()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .body(product)
                .retrieve()
                .toEntity(Product.class);

        log.info("Updated product: {}", entity.getBody());

        if (entity.getStatusCode().is2xxSuccessful()) {
            log.info("Updated: {}", entity.getStatusCode());
        }

        return entity.getBody();
    }

    @Override
    public Product findById(UUID id) {
        var product = restClient.get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> {
                            log.error("Failed to get Product: {}", res.getStatusText());
                            log.error("Failed to get Product: {}", res.getStatusCode());
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> {
                            log.error("Server failed: {}", res.getStatusText());
                            log.error("Server failed: {}", res.getStatusCode());
                        })
                .body(Product.class);

        log.info("Product: {}", product);

        return product;
    }

    @Override
    public void delete(UUID id) {
        ResponseEntity<Void> response = restClient.delete()
                .uri("/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> {
                            log.error("Client couldn't delete: {}", res.getStatusText());
                            log.error("Client couldn't delete: {}", res.getStatusCode());
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> {
                            log.error("Server couldn't delete: {}", res.getStatusText());
                            log.error("Server couldn't delete: {}",res.getStatusCode());
                        }
                )
                .toBodilessEntity();

        if (response.getStatusCode().is2xxSuccessful())
            log.info("Deleted with status " +
                    response.getStatusCode());
    }
}
