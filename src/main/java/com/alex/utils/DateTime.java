package com.alex.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateTime {

    public static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0"));
    }

    public static String ConvertTimeToString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
    }

    public static String getCurrentDate(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
