package bot.service;

import bot.entity.ArithmeticSign;
import bot.exception.ZeroDivideException;

public interface OperationService {

    static ArithmeticOperationService getInstance() {
        return new ArithmeticOperationService();
    }

    double calculated(ArithmeticSign arithmeticSign , double firstNum, double secondNum) throws ZeroDivideException;
}
