package com.alex.services.handlers;

import com.alex.commands.KrakenCommands;
import com.alex.interfaces.SessionStorage;
import com.alex.services.KrakenProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

@Slf4j
public class KrakenHandler implements WebSocketHandler {
    private SessionStorage sessionStorage;
    private String ticker;
    private KrakenProcessingService processingService;

    public KrakenHandler(SessionStorage sessionStorage, String ticker, KrakenProcessingService processingService) {
        this.sessionStorage = sessionStorage;
        this.ticker = ticker;
        this.processingService = processingService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Session started");
        sessionStorage.storeSession(session);
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
        log.info(KrakenCommands.TRADE_SUBSCRIBE_COMMAND);
        session.sendMessage(new TextMessage(KrakenCommands.TRADE_SUBSCRIBE_COMMAND.replace("{ticker}", ticker)));
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String message = webSocketMessage.getPayload().toString();
        if (message.contains("\"trade\",\"{ticker}\"".replace("{ticker}", ticker))) {
            this.processingService.process(message, ticker);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.error("Connection closed with status " + closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}