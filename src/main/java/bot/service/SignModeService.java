package bot.service;

import bot.entity.ArithmeticSign;

public interface SignModeService {

    static SignModeService getInstance() {
        return new HashMapSignModeService();
    }

    ArithmeticSign getOriginalSign(long chatId);

    void setOriginalSign(long chatId, ArithmeticSign currency);

}
