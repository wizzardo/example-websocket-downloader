package com.wizzardo.downloader;

import com.wizzardo.http.framework.di.Injectable;
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
@Injectable
public class DownloaderWebSocketHandler extends WebSocketHandler {

    private Set<WebSocketListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    DownloadJobService downloadJobService;

    public DownloaderWebSocketHandler() {
    }

    public DownloaderWebSocketHandler(App app) {
        downloadJobService = app.getDownloadJobService();
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
            downloadJobService.getJobOptional(id).ifPresent(DownloadJob::cancel);
        }
    }

    public void broadcast(String s) {
        Message message = new Message().append(s);
        for (WebSocketListener listener : listeners) {
            listener.sendMessage(message);
        }
    }
}
