package com.alex;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        //LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(test)), TimeZone.getDefault().toZoneId());
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now().truncatedTo(ChronoUnit.MINUTES).minus(5,ChronoUnit.MINUTES), TimeZone.getTimeZone("UTC").toZoneId());
        System.out.println(now);
    }

}