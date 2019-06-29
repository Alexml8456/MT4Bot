package com.alex.services;

import com.alex.interfaces.SessionStorage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class KrakenSessionStorage implements SessionStorage<WebSocketSession> {
    private WebSocketSession session;

    @Getter
    @Setter
    private boolean connecting = false;

    @Override
    public void storeSession(WebSocketSession session) {
        this.session = session;
        this.connecting = false;
    }

    @Override
    public WebSocketSession getSession() {
        return session;
    }

}
