package com.wizzardo.downloader;

import com.wizzardo.downloader.handlers.CreateHandler;
import com.wizzardo.downloader.handlers.FormHandler;
import com.wizzardo.downloader.handlers.ListHandler;
import com.wizzardo.downloader.handlers.SaveHandler;
import com.wizzardo.http.FileTreeHandler;
import com.wizzardo.http.HttpConnection;
import com.wizzardo.http.HttpServer;
import com.wizzardo.http.mapping.UrlMapping;

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
                .append("/static/*", "static", new FileTreeHandler("src/main/resources", "/static"))
                .append("/download/*", "download", new FileTreeHandler("/tmp/downloader", "/download"))
                .append("/list", "list", new ListHandler(this))
                .append("/", (request, response) -> {
                    response.setRedirectPermanently("list");
                    return response;
                })
                .append("/form", "form", new FormHandler(this))
                .append("/save", "save", new SaveHandler(this))
                .append("/create", "create", new CreateHandler(this))
                .append("/ws", webSocketHandler = new DownloaderWebSocketHandler())
        ;

        server.start();
    }

    public UrlMapping getUrlMapping() {
        return server.getUrlMapping();
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
