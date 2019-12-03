package com.alex.controllers;

import com.alex.services.DataHolder;
import com.alex.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(method = RequestMethod.GET)
public class MT4Controller {

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private DataHolder dataHolder;

    @GetMapping(value = "/get/image/crypto")
    public ResponseEntity pushCyptoImage() {
        log.info("Manual image get request for crypto was sent...");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("Crypto.png"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/get/image/gbp")
    public ResponseEntity pushGbpImage() {
        log.info("Manual image get request for GBPUSD was sent...");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("GBP.png"));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/get/image/eur")
    public ResponseEntity pushEurImage() {
        log.info("Manual image get request for EURUSD was sent...");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("EUR.png"));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}