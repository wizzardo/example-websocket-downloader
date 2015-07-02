package com.wizzardo.downloader;

import com.wizzardo.downloader.handlers.CreateHandler;
import com.wizzardo.downloader.handlers.FormHandler;
import com.wizzardo.downloader.handlers.ListHandler;
import com.wizzardo.downloader.handlers.SaveHandler;
import com.wizzardo.http.FileTreeHandler;
import com.wizzardo.http.framework.ControllerHandler;
import com.wizzardo.http.framework.WebApplication;
import com.wizzardo.http.mapping.UrlMapping;

/**
 * Created by wizzardo on 04.04.15.
 */
public class App {
    final WebApplication server;
    final DownloadJobService downloadJobService = new DownloadJobService(this);
    final DownloaderWebSocketHandler webSocketHandler;

    public App() {
        server = new WebApplication("localhost", 8084, 4);

        server.getUrlMapping()
                .append("/download/*", "download", new FileTreeHandler("/tmp/downloader", "/download"))
                .append("/list", "list", new ListHandler(this))
                .append("/", (request, response) -> {
                    response.setRedirectPermanently("list");
                    return response;
                })
                .append("/form", "form", new FormHandler(this))
                .append("/save", "save", new SaveHandler(this))
                .append("/create", "create", new CreateHandler(this))
                .append("/ws", webSocketHandler = new DownloaderWebSocketHandler(this))
                .append("/j/", new ControllerHandler(DownloadJobsController.class, "index"))
                .append("/j/list", new ControllerHandler(DownloadJobsController.class, "list"))
                .append("/j/create", new ControllerHandler(DownloadJobsController.class, "create"))
                .append("/j/$id", new ControllerHandler(DownloadJobsController.class, "result"))
                .append("/j/form", new ControllerHandler(DownloadJobsController.class, "form"))
                .append("/j/save", new ControllerHandler(DownloadJobsController.class, "save"))
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
