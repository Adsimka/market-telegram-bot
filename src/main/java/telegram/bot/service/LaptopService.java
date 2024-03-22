package telegram.bot.service;

import lombok.RequiredArgsConstructor;
import telegram.bot.entity.Laptop;
import telegram.bot.repository.LaptopRepository;

import java.util.List;

@RequiredArgsConstructor
public class LaptopService {

    private static final LaptopService INSTANCE = new LaptopService(
            LaptopRepository.getInstance()
    );

    private final LaptopRepository laptopRepository;

    public List<Laptop> getLaptops() {
        return laptopRepository.readLaptops();
    }

    public static LaptopService getInstance() {
        return INSTANCE;
    }

}
