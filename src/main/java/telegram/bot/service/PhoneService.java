package telegram.bot.service;

import lombok.RequiredArgsConstructor;
import telegram.bot.model.Phone;
import telegram.bot.repository.PhoneRepository;

import java.util.List;

@RequiredArgsConstructor
public class PhoneService {

    private static final PhoneService INSTANCE = new PhoneService(
            PhoneRepository.getInstance()
    );

    private final PhoneRepository phoneRepository;

    public List<Phone> getPhones() {
        return phoneRepository.readPhones();
    }

    public static PhoneService getInstance() {
        return INSTANCE;
    }

}
