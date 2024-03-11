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

    private final ObjectMapper objectMapper;

    private static final LaptopRepository INSTANCE = new LaptopRepository(
            ObjectMapperConfig.getInstance()
    );

    @SneakyThrows
    public List<Laptop> readPhones() {
        List<Laptop> laptops = new ArrayList<>();

        try (InputStream phoneStream = getClass().getClassLoader()
                .getResourceAsStream("laptops.json")) {
            JsonNode rootNode = objectMapper.readTree(phoneStream);
            if (rootNode.hasNonNull("laptops")) {
                JsonNode phonesNode = rootNode.get("laptops");
                laptops.addAll(List.of(objectMapper.convertValue(phonesNode, Laptop[].class)));
            }
        }
        return laptops;
    }

    public static LaptopRepository getInstance() {
        return INSTANCE;
    }

}
