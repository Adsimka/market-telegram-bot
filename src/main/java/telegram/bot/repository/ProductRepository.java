package telegram.bot.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import telegram.bot.config.ObjectMapperConfig;
import telegram.bot.entity.Product;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepository {

    private static final ProductRepository INSTANCE = new ProductRepository(
            ObjectMapperConfig.getInstance()
    );

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<Product> readLaptops() {
        List<Product> products = new ArrayList<>();

        try (InputStream phoneStream = getClass().getClassLoader()
                .getResourceAsStream("products.json")) {
            JsonNode rootNode = objectMapper.readTree(phoneStream);
            if (rootNode.hasNonNull("laptops")) {
                JsonNode laptopsNode = rootNode.get("laptops");
                products.addAll(List.of(objectMapper.convertValue(laptopsNode, Product[].class)));
            }
        }
        return products;
    }

    public static ProductRepository getInstance() {
        return INSTANCE;
    }

}
