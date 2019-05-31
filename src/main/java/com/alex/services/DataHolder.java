package com.alex.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataHolder {

    @Getter
    private List<String> subscriptions = new ArrayList<String>() {{
        add("311771410");
        //add("393063353");
    }};

    public void addSubscriber(String subscription) {
        if (!subscriptions.contains(subscription)) {
            subscriptions.add(subscription);
        }
    }

    public void deleteSubscriber(String subscription) {
        subscriptions.remove(subscription);
    }
}
