package bot.service;

import bot.entity.ArithmeticSign;
import bot.exception.ZeroDivideException;


public class ArithmeticOperationService implements OperationService {

    @Override
    public double calculated(ArithmeticSign sign, double firstNum, double secondNum) throws ZeroDivideException {
        double result = 0;
        switch (sign) {
            case PLUS:
                return firstNum + secondNum;

            case MINUS:
                return firstNum - secondNum;

            case MULTIPLY:
                return firstNum * secondNum;

            case DIVIDE:
                if (secondNum == 0) {
                   throw new ZeroDivideException("");

                }

                return firstNum / secondNum;
            default:
                return result;
        }
    }
}
