package com.wizzardo.downloader;

import com.wizzardo.downloader.handlers.ListHandler;
import com.wizzardo.http.HttpConnection;
import com.wizzardo.http.HttpServer;

/**
 * Created by wizzardo on 04.04.15.
 */
public class App {
    HttpServer<HttpConnection> server;

    public App() {
        server = new HttpServer<>(8080);

        server.getUrlMapping().append("/", new ListHandler());
    }


    public static void main(String[] args) {
        new App();
    }
}
