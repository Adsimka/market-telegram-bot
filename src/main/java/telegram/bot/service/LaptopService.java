package telegram.bot.service;

import lombok.RequiredArgsConstructor;
import telegram.bot.model.Laptop;
import telegram.bot.repository.LaptopRepository;

import java.util.List;

@RequiredArgsConstructor
public class LaptopService {

    private final LaptopRepository laptopRepository;

    private static final LaptopService INSTANCE = new LaptopService(
            LaptopRepository.getInstance()
    );

    public List<Laptop> getPhones() {
        return laptopRepository.readPhones();
    }

    public static LaptopService getInstance() {
        return INSTANCE;
    }

}
