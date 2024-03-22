package telegram.bot.service;

import lombok.RequiredArgsConstructor;
import telegram.bot.entity.Product;
import telegram.bot.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private static final ProductService INSTANCE = new ProductService(
            ProductRepository.getInstance()
    );

    private final ProductRepository productRepository;

    public List<Product> getLaptops() {
        return productRepository.readLaptops();
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }
}
