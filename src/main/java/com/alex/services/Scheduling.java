package com.alex.services;

import com.alex.telegram.TelegramBot;
import com.alex.utils.DateTime;
import com.alex.utils.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
public class Scheduling {

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    @Autowired
    private KrakenProcessingService processingService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KrakenConnectionService connectionService;

    @Autowired
    private KrakenSessionStorage sessionStorage;

    @Autowired
    private CsvMetrics timeMetrics;

    @Autowired
    private FileOperations fileOperations;

    @Autowired
    private ImageOperations imageOperations;

    @Autowired
    private CSVOperations csvOperations;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TradeCondition tradeCondition;

    //@Autowired
    private TelegramBot telegramBot;

    //@Scheduled(cron = "0 52 23 ? * *")
    public void deleteRows() {
        //csvOperations.deleteRowsForFile();
        dataHolder.clearFileList();
    }

    @Scheduled(fixedDelay = 60000)
    public void test() throws InvalidKeyException, NoSuchAlgorithmException {
        final String API_KEY = "iXeMbH8J0VdoMFHdAy";
        final String API_SECRET = "ffS6bcky0TjYlWT8HlkmUWOajEgCHumP7fRw";
        final String url = "https://api.bybit.com/futures/private/order/list?";
        final String TIMESTAMP = Long.toString(ZonedDateTime.now().toInstant().toEpochMilli());
        Map<String, Object> map = new TreeMap<>();
        map.put("api_key", API_KEY);
        map.put("timestamp", TIMESTAMP);
        map.put("symbol", "BTCUSDU22");
        map.put("recv_window", "5000");
        //map.put("order_status", "Created,New,Filled,Cancelled");
        String st1 = "api_key=iXeMbH8J0VdoMFHdAy&recv_window=5000"+"&symbol=BTCUSDU22"+"&timestamp="+TIMESTAMP;
        String signature = HashUtils.hmacSha256(st1,API_SECRET);
        map.put("sign", signature);
        String st = st1+"&sign="+signature;
        log.info(url+st);
        ResponseEntity<String> response;
        response = restTemplate.getForEntity(url+st, String.class);
        log.info(response.getBody());
    }


    //@Scheduled(fixedDelay = 60000)
    public void verifyStalePrice() {
        try {
            LocalDateTime fiveMinsBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(5);
            if (processingService.getLastTradeTime() != null &&
                    processingService.getLastTradeTime().isBefore(fiveMinsBefore)) {
                log.error("Stale price was found!");
                log.info("Closing session");
                sessionStorage.getSession().close();
                //scheduledReconnect();
            }
        } catch (Exception e) {
            log.error("Verify Stale Price failed", e);
        }
    }


    //@Scheduled(cron = "05 0/5 * ? * *")
    //@Scheduled(cron = "10 0/1 * ? * *")
    public void saveFiles() {
        fileOperations.saveFilesToList();

        imageOperations.mergeImages();

        dataHolder.clearFileList();

        //csvOperations.saveValuesToMap();

        //tradeCondition.checkTradeCondition();

        //timeMetrics.getCsvMetrics().clear();

        //fileOperations.cleanDirectory();
    }


    //@Scheduled(cron = "30 0 2/4 ? * *")
    public void pushMessage() {
        log.info("Preparing to send messages...");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("Crypto.png"));
        log.info("Message with crypto was sent!");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("GBP.png"));
        log.info("Message with GBP was sent!");
        //telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("EUR.png"));
        //log.info("Message with EUR was sent!");
    }

    //@Scheduled(fixedDelay = 1000)
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

    private static String genSign(Map<String, Object> params) throws NoSuchAlgorithmException, InvalidKeyException {
        final String API_SECRET = "ffS6bcky0TjYlWT8HlkmUWOajEgCHumP7fRw";
        Set<String> keySet = params.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(API_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return bytesToHex(sha256_HMAC.doFinal(sb.toString().getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}