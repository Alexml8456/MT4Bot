package com.alex.services;

import com.alex.services.handlers.KrakenHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.websocket.DeploymentException;
import java.io.IOException;

@Service
@Slf4j
public class KrakenConnectionService {
    @Value("${address.kraken}")
    private String krakenAdress;
    @Value("${wss.instrument}")
    private String instrument;

    @Autowired
    private KrakenProcessingService processingService;
    @Autowired
    private KrakenSessionStorage sessionStorage;

    @Getter
    private WebSocketConnectionManager connectionManager;

    public void connect() throws IOException, DeploymentException {
        try {

            if (sessionStorage.isConnecting()) {
                return;
            }

            sessionStorage.setConnecting(true);

            StandardWebSocketClient client = new StandardWebSocketClient();
            KrakenHandler handler = new KrakenHandler(sessionStorage, instrument, processingService);
            connectionManager = new WebSocketConnectionManager(client, handler, krakenAdress);
            connectionManager.start();
        } catch (Exception e) {
            sessionStorage.setConnecting(false);
        }
    }
}
