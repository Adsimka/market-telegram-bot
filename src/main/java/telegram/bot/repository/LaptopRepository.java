package telegram.bot.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import telegram.bot.config.ObjectMapperConfig;
import telegram.bot.model.Laptop;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LaptopRepository {

    private static final LaptopRepository INSTANCE = new LaptopRepository(
            ObjectMapperConfig.getInstance()
    );

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<Laptop> readLaptops() {
        List<Laptop> laptops = new ArrayList<>();

        try (InputStream phoneStream = getClass().getClassLoader()
                .getResourceAsStream("laptops.json")) {
            JsonNode rootNode = objectMapper.readTree(phoneStream);
            if (rootNode.hasNonNull("laptops")) {
                JsonNode laptopsNode = rootNode.get("laptops");
                laptops.addAll(List.of(objectMapper.convertValue(laptopsNode, Laptop[].class)));
            }
        }
        return laptops;
    }

    public static LaptopRepository getInstance() {
        return INSTANCE;
    }

}
