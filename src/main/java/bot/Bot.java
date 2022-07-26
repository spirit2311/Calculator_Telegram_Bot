package bot;

import bot.entity.ArithmeticSign;
import bot.entity.Numbers;
import bot.exception.ZeroDivideException;
import bot.service.OperationService;
import bot.service.SignModeService;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class Bot extends TelegramLongPollingBot {


    private final SignModeService signModeService = SignModeService.getInstance();

    private final OperationService operationService = OperationService.getInstance();

    private final Numbers numbers = new Numbers();

    private Long chatId = 0L;



    @Override

    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            try {
                handleCallback(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        ArithmeticSign newArithmetic = ArithmeticSign.valueOf(param[1]);
        if (newArithmetic.isSignValid(newArithmetic) &&
                Objects.isNull(numbers.getFirstNumber()) && Objects.isNull(numbers.getSecondNumber())) {
            execute(
                    SendMessage
                            .builder()
                            .text("You have chosen a sign " + newArithmetic.name())
                            .chatId(message.getChatId().toString())
                            .build()
            );
            execute(
                    SendMessage
                            .builder()
                            .text("Enter first number:")
                            .chatId(message.getChatId().toString())
                            .build()
            );
        }
        switch (action) {
            case "ORIGINAL":
                signModeService.setOriginalSign(message.getChatId(), newArithmetic);
                break;
        }
    }

    //TODO Метод который создает и вызывает кнопки при нажатии на команду /arithmetic_operation
    // Eshe raz uvizhu russkij text - otkluchu gaz i skazhu vsem chto ti pidor. Nikakogo rus texta v code
    @SneakyThrows
    private void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities()
                            .stream()
                            .filter(e -> "bot_command".equals(e.getType()))
                            .findFirst();
            if (commandEntity.isPresent()) {
                String command =
                        message.getText().substring(
                                commandEntity.get().getOffset(), commandEntity.get().getLength()
                        );

                if ("/start".equals(command)) {
                    execute(SendMessage.builder()
                            .text("Hello this bot will solve your example, click on the command /arithmetic_operation")
                            .chatId(message.getChatId().toString())
                            .build());
                    return;
                }

                if ("/arithmetic_operation".equals(command)) {
                    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                    for (ArithmeticSign arithmeticSign : ArithmeticSign.values()) {
                        buttons.add(
                                List.of(
                                        InlineKeyboardButton.builder()
                                                .text(String.valueOf(arithmeticSign))
                                                .callbackData("ORIGINAL:" + arithmeticSign)
                                                .build()));

                    }
                    chatId = message.getChatId();
                    numbers.setFirstNumber(null);
                    numbers.setSecondNumber(null);
                    signModeService.setOriginalSign(message.getChatId(), null);
                    execute(SendMessage.builder()
                            .text("Select arithmetic sign: ")
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
                    return;
                }
            }
        }

        if (message.hasText() &&
                numbers.isNumeric(message.getText()) &&
                Objects.isNull(numbers.getFirstNumber()) &&
                Objects.isNull(numbers.getSecondNumber()) &&
                message.getChat().getId().equals(chatId)
        ) {
            numbers.setFirstNumber(Double.parseDouble(message.getText()));
            execute(
                    SendMessage
                            .builder()
                            .text("Enter second number:")
                            .chatId(message.getChatId().toString())
                            .build()
            );
        } else if (message.hasText() &&
                numbers.isNumeric(message.getText()) &&
                Objects.nonNull(numbers.getFirstNumber()) &&
                Objects.isNull(numbers.getSecondNumber()) &&
                message.getChat().getId().equals(chatId)
        ) {
            try {


                numbers.setSecondNumber(Double.parseDouble(message.getText()));
                double calculated = operationService.calculated(signModeService.getOriginalSign(
                        message.getChat().getId()), numbers.getFirstNumber(), numbers.getSecondNumber()
                );
                execute(
                        SendMessage
                                .builder()
                                .text("Result: " + calculated)
                                .chatId(message.getChatId().toString())
                                .build()
                );
            }catch (ZeroDivideException e){
                execute(SendMessage.builder()
                        .text("Oops, you can’t divide by zero, let’s do it again")
                        .chatId(message.getChatId().toString())
                        .build());
            }
            execute(SendMessage.builder()
                    .text("If you want to solve another equation, click on the command /arithmetic_operation")
                    .chatId(message.getChatId().toString())
                    .build());
            return;
        }
    }



    @Override
    public String getBotUsername() {
        return "@Arithmetic_Basic_Bot";
    }

    @Override
    public String getBotToken() {
        return "5574421631:AAE5cMaotvq_ig6z4_dhiAUCLe2erGmNi_8";
    }
}
