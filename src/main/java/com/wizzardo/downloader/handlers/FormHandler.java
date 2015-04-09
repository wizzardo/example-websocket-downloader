package com.wizzardo.downloader.handlers;

import com.wizzardo.downloader.App;
import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.http.Handler;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;

import java.io.IOException;

/**
 * Created by wizzardo on 09.04.15.
 */
public class FormHandler implements Handler {
    private App app;

    public FormHandler(App app) {
        this.app = app;
    }

    @Override
    public Response handle(Request request, Response response) throws IOException {
        DownloadJob job = app.getDownloadJobService().createByType(request.param("type"));
        if (job != null)
            response.setBody(job.renderInputFields());

        return response;
    }
}
