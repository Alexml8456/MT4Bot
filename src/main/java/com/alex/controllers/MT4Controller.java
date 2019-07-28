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

    @GetMapping(value = "/get/image")
    public ResponseEntity pushImage() {
        log.info("Manual image get request was sent...");
        telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}