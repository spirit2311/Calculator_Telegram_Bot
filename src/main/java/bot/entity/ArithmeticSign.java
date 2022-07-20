package bot.entity;

import lombok.Getter;

@Getter
public enum ArithmeticSign {
    PLUS(), MINUS(),MULTIPLY(), DIVIDE();

    ArithmeticSign() {

    }

    public boolean isSignValid (ArithmeticSign sign) {
        return sign == PLUS || sign == MINUS || sign == MULTIPLY || sign == DIVIDE;
    }
}
