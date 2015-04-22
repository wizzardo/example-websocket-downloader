package com.wizzardo.downloader.handlers;

import com.wizzardo.downloader.App;
import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.http.Handler;
import com.wizzardo.http.MultiValue;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzardo on 09.04.15.
 */
public class SaveHandler implements Handler {
    private App app;

    public SaveHandler(App app) {
        this.app = app;
    }

    @Override
    public Response handle(Request request, Response response) throws IOException {
        DownloadJob job = app.getDownloadJobService().createByType(request.param("type"));
        if (job == null) {
            response.setRedirectTemporarily(app.getUrlMapping().getUrlTemplate("create").getRelativeUrl());
            return response;
        }


        job.name = request.param("name");
        job.type = request.param("type");

        job.params = new HashMap<>();
        Map<String, MultiValue> params = request.params();
        for (Map.Entry<String, MultiValue> entry : params.entrySet()) {
            if (!entry.getKey().equals("name") && !entry.getKey().equals("type"))
                job.params.put(entry.getKey(), entry.getValue().getValue());
        }

        job.id = app.getDownloadJobService().generateId();
        app.getDownloadJobService().addJob(job);

        response.setRedirectTemporarily(app.getUrlMapping().getUrlTemplate("list").getRelativeUrl());
        return response;
    }
}
