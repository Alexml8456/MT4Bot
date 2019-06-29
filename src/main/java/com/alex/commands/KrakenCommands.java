package com.alex.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KrakenCommands {
    public static final String TRADE_SUBSCRIBE_COMMAND = "{\"event\": \"subscribe\",\"pair\": [\"{ticker}\"],\"subscription\": {\"name\": \"trade\"}}";
}
