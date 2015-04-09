package com.wizzardo.downloader;

import com.wizzardo.downloader.handlers.CreateHandler;
import com.wizzardo.downloader.handlers.ListHandler;
import com.wizzardo.http.FileTreeHandler;
import com.wizzardo.http.HttpConnection;
import com.wizzardo.http.HttpServer;

/**
 * Created by wizzardo on 04.04.15.
 */
public class App {
    HttpServer<HttpConnection> server;
    DownloadJobService downloadJobService = new DownloadJobService();

    public App() {
        server = new HttpServer<>(8084);

        server.getUrlMapping()
                .append("/static/*", new FileTreeHandler("src/main/resources", "/static"))
                .append("/", new ListHandler(this))
                .append("/create", new CreateHandler(this));

        server.start();
    }

    public DownloadJobService getDownloadJobService() {
        return downloadJobService;
    }

    public static void main(String[] args) {
        new App();
    }
}
