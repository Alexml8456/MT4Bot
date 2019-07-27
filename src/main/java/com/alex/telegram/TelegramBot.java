package com.alex.telegram;

import com.alex.services.DataHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private String botToken;

    @Autowired
    private DataHolder dataHolder;

    @Override
    public void onUpdateReceived(Update update) {
        handle(update);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::handle);
    }

    private void handle(Update update) {
        String text = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        log.info("User message - " + text + "; " + "User ID - " + chatId);
        if (text.contains("start")) {
            dataHolder.addSubscriber(chatId);
            pushMessage(Collections.singletonList(chatId), "You verified RestNotifier subscription");
        } else if (text.contains("stop")) {
            dataHolder.deleteSubscriber(chatId);
        }
    }


    public void pushMessage(List<String> chatIds, String text) {
        for (String chatId : chatIds) {
            SendMessage sendMessageRequest = new SendMessage();
            sendMessageRequest.setChatId(chatId);
            sendMessageRequest.setText(text);
            try {
                execute(sendMessageRequest);
            } catch (TelegramApiException e) {
                log.error("Can't send telegram message", e);
            }
        }
    }

    public void pushPhotoMessage(List<String> chatIds, String filePath) {
        for (String chatId : chatIds) {
            SendPhoto sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(chatId);
            sendPhotoRequest.setPhoto(new File(filePath));
            try {
                execute(sendPhotoRequest);
            } catch (TelegramApiException e) {
                log.error("Can't send telegram photo message", e);
            }
        }
    }

    public void pushFile(List<String> chatIds, String filePath) {
        for (String chatId : chatIds) {
            SendDocument sendFileRequest = new SendDocument();
            sendFileRequest.setChatId(chatId);
            sendFileRequest.setDocument(new File(filePath));
            try {
                execute(sendFileRequest);
            } catch (TelegramApiException e) {
                log.error("Can't send telegram file", e);
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "RestNotifierBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public void onClosing() {
        log.warn("Exit telegram bot");
    }

    @PostConstruct
    public void init() {
        log.info("Telegram bot is up and running with token: " + botToken);
    }

}
