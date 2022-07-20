package bot.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class Numbers {
    @Getter @Setter
    private Double firstNumber = null;

    @Getter @Setter
    private Double secondNumber = null;

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
