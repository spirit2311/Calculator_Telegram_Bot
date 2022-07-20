package bot.service;

import bot.entity.ArithmeticSign;

import java.util.HashMap;
import java.util.Map;

public class HashMapSignModeService implements SignModeService{

    private final Map<Long, ArithmeticSign> originalSign = new HashMap<>();

    public HashMapSignModeService() {

    }

    @Override
    public ArithmeticSign getOriginalSign(long chatId) {
        return originalSign.getOrDefault(chatId, ArithmeticSign.PLUS);
    }

    @Override
    public void setOriginalSign(long chatId, ArithmeticSign currency) {
        originalSign.put(chatId, currency);
    }

}
