package com.wizzardo.downloader;

import com.wizzardo.http.framework.di.Injectable;
import com.wizzardo.http.websocket.Message;
import com.wizzardo.http.websocket.WebSocketHandler;
import com.wizzardo.tools.json.JsonArray;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.json.JsonTools;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by wizzardo on 11.04.15.
 */
@Injectable
public class DownloaderWebSocketHandler extends WebSocketHandler {

    private Set<WebSocketListener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    protected Map<String, CommandHandler> handlers = new ConcurrentHashMap<>();
    DownloadJobService downloadJobService;

    protected interface CommandHandler {
        void handle(JsonObject data);
    }

    public DownloaderWebSocketHandler() {
        handlers.put("cancel", json -> {
            int id = json.getAsInteger("id", 0);
            downloadJobService.getJobOptional(id).ifPresent(DownloadJob::cancel);
        });

        handlers.put("jobs", json -> broadcast(new JsonObject()
                        .append("command", "jobs")
                        .append("jobs", new JsonArray().appendAll(downloadJobService.getRecentJobs().stream()
                                .map(job -> new JsonObject()
                                        .append("type", job.type)
                                        .append("name", job.name)
                                        .append("params", job.params != null ? job.params.toString() : null)
                                        .append("id", job.id)
                                        .append("log", job.log())
                                        .append("status", job.status)).collect(Collectors.toList())))
        ));
    }

    public DownloaderWebSocketHandler(App app) {
        this();
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
        CommandHandler handler = handlers.get(json.getAsString("command"));
        if (handler != null)
            handler.handle(json);
        else
            System.out.println("unknown command: " + message.asString());
    }

    public void broadcast(String s) {
        Message message = new Message().append(s);
        for (WebSocketListener listener : listeners) {
            listener.sendMessage(message);
        }
    }

    public void broadcast(JsonObject json) {
        broadcast(json.toString());
    }
}
