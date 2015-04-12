package com.wizzardo.downloader;

import com.wizzardo.http.websocket.Message;
import com.wizzardo.http.websocket.WebSocketHandler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wizzardo on 11.04.15.
 */
public class DownloaderWebSocketHandler extends WebSocketHandler {

    private Set<WebSocketListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<WebSocketListener, Boolean>());

    @Override
    public void onConnect(WebSocketListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onDisconnect(WebSocketListener listener) {
        listeners.remove(listener);
    }

    public void broadcast(String s) {
        Message message = new Message().append(s);
        for (WebSocketListener listener : listeners) {
            listener.sendMessage(message);
        }
    }
}
