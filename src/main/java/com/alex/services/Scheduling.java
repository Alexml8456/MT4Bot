package com.alex.services;

import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class Scheduling {

    @Autowired
    private KrakenProcessingService processingService;

    @Autowired
    private KrakenConnectionService connectionService;

    @Autowired
    private KrakenSessionStorage sessionStorage;

    @Autowired
    private TimeMetrics timeMetrics;

    @Autowired
    private FileOperations fileOperations;

    @Autowired
    private CSVOperations csvOperations;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TradeCondition tradeCondition;

    @Scheduled(cron = "0 52 23 ? * *")
    public void deleteRows() {
        csvOperations.deleteRowsForFile();
        dataHolder.clearFileList();
    }

    @Scheduled(fixedDelay = 60000)
    public void verifyStalePrice() {
        try {
            LocalDateTime fiveMinsBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(5);
            if (processingService.getLastTradeTime() != null &&
                    processingService.getLastTradeTime().isBefore(fiveMinsBefore)) {
                log.error("Stale price is found. Reconnect will be initiated");
                log.info("Closing session");
                sessionStorage.getSession().close();
                //scheduledReconnect();
            }
        } catch (Exception e) {
            log.error("Verify Stale Price failed", e);
        }
    }


    @Scheduled(cron = "05 0/5 * ? * *")
    //@Scheduled(cron = "10 0/1 * ? * *")
    public void saveFiles() {
        fileOperations.saveFilesToList();

        csvOperations.saveValuesToMap();

        tradeCondition.checkTradeCondition();

        timeMetrics.getCsvMetrics().clear();
    }

    @Scheduled(fixedDelay = 1000)
    public void reconnect() throws InterruptedException, IOException, DeploymentException {
        if (!isConnected()) {
            log.info("Connection status is {}", isConnected());
            Optional<WebSocketConnectionManager> connectionManager = ofNullable(connectionService.getConnectionManager());
            log.info("Connection manager status is {}", connectionManager.isPresent());
            if (connectionManager.isPresent()) {
                log.warn("Reconnecting");
                try {
                    if (sessionStorage.getSession() != null && sessionStorage.getSession().isOpen()) {
                        log.info("Closing session");
                        sessionStorage.getSession().close();
                    }
                    connectionManager.get().stop();
                    SECONDS.sleep(2);
                    connect();
                } catch (Exception e) {
                    log.error("Can't reconnect. " + e.getMessage(), e);
                }
            } else {
                connect();
            }
        }
    }

    public void scheduledReconnect() throws InterruptedException, IOException, DeploymentException {
        Optional<WebSocketConnectionManager> connectionManager = ofNullable(connectionService.getConnectionManager());
        if (connectionManager.isPresent()) {
            log.warn("Reconnecting");
            try {
                log.info("Closing session");
                sessionStorage.getSession().close();
                log.info("Stop connection manager");
                connectionManager.get().stop();
                SECONDS.sleep(2);
                reconnect();
            } catch (Exception e) {
                log.error("Can't reconnect. " + e.getMessage(), e);
            }
        } else {
            connect();
        }
    }

    private void connect() throws IOException, DeploymentException, InterruptedException {
        connectionService.connect();
        SECONDS.sleep(2);
        if (isConnected()) {
            //log.info("Connected");
        } else {
            log.error("Can't connect");
        }
    }

    private boolean isConnected() {
        WebSocketSession session = sessionStorage.getSession();
        return session != null && (session.isOpen() || sessionStorage.isConnecting());
    }
}