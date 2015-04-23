package com.wizzardo.downloader;

import com.wizzardo.http.websocket.Message;
import com.wizzardo.http.websocket.WebSocketHandler;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.json.JsonTools;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wizzardo on 11.04.15.
 */
public class DownloaderWebSocketHandler extends WebSocketHandler {

    private Set<WebSocketListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private App app;

    public DownloaderWebSocketHandler(App app) {
        this.app = app;
    }

    @Override
    public void onConnect(WebSocketListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onDisconnect(WebSocketListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onMessage(WebSocketListener listener, Message message) {
//        System.out.println(message.asString());
        JsonObject json = JsonTools.parse(message.asString()).asJsonObject();
        if ("cancel".equals(json.getAsString("command"))) {
            int id = json.getAsInteger("id", 0);
            DownloadJob job = app.getDownloadJobService().getJob(id);
            if (job != null)
                job.cancel();
        }
    }

    public void broadcast(String s) {
        Message message = new Message().append(s);
        for (WebSocketListener listener : listeners) {
            listener.sendMessage(message);
        }
    }
}
