package com.alex.services;

import com.alex.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class Scheduling {

    @Value("${send.photo.message.enable}")
    private boolean enable;

    @Autowired
    private DataHolder dataHolder;

//    @Autowired
//    private TelegramBot telegramBot;


    @Scheduled(cron = "0 0/1 * ? * *")
    public void test() {
        try (Stream<Path> walk = Files.walk(Paths.get("/home/alexml/.wine/drive_c/Program Files (x86)/ForexClub MT4/MQL4/Files"))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            result.forEach((file) -> {
                log.info(new File(file).getName());
            });

        } catch (IOException e) {
            log.error("Cannot read files from folder - " + e.getMessage(), e);
        }

    }

}
