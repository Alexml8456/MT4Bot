package com.alex.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

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

    public static LocalDateTime GMTTimeConverter(String time) {
        time = time.substring(0, 14).replace(".", "");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time)), TimeZone.getTimeZone("UTC").toZoneId());
    }

    public static LocalDateTime GMTLastPeriod(Integer minutes) {
        return LocalDateTime.ofInstant(Instant.now().truncatedTo(ChronoUnit.MINUTES).minus(minutes, ChronoUnit.MINUTES),
                TimeZone.getTimeZone("UTC").toZoneId());
    }
}
