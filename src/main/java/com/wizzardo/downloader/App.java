package com.wizzardo.downloader;

import com.wizzardo.downloader.handlers.CreateHandler;
import com.wizzardo.downloader.handlers.FormHandler;
import com.wizzardo.downloader.handlers.ListHandler;
import com.wizzardo.downloader.handlers.SaveHandler;
import com.wizzardo.http.FileTreeHandler;
import com.wizzardo.http.HttpConnection;
import com.wizzardo.http.HttpServer;

/**
 * Created by wizzardo on 04.04.15.
 */
public class App {
    final HttpServer<HttpConnection> server;
    final DownloadJobService downloadJobService = new DownloadJobService(this);
    final DownloaderWebSocketHandler webSocketHandler;

    public App() {
        server = new HttpServer<>(8084);

        server.getUrlMapping()
                .append("/static/*", new FileTreeHandler("src/main/resources", "/static"))
                .append("/list", new ListHandler(this))
                .append("/", (request, response) -> {
                    response.setRedirectPermanently("list");
                    return response;
                })
                .append("/form", new FormHandler(this))
                .append("/save", new SaveHandler(this))
                .append("/create", new CreateHandler(this))
                .append("/ws", webSocketHandler = new DownloaderWebSocketHandler())
        ;

        server.start();
    }

    public DownloaderWebSocketHandler getWebSocketHandler() {
        return webSocketHandler;
    }

    public DownloadJobService getDownloadJobService() {
        return downloadJobService;
    }

    public static void main(String[] args) {
        new App();
    }
}
