package com.wizzardo.downloader;

import com.wizzardo.http.MultiValue;
import com.wizzardo.http.framework.Controller;
import com.wizzardo.http.framework.di.DependencyFactory;
import com.wizzardo.http.framework.template.Renderer;
import com.wizzardo.http.mapping.UrlMapping;
import com.wizzardo.http.request.Header;
import com.wizzardo.http.response.Status;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wizzardo on 25.06.15.
 */
public class DownloadJobsController extends Controller {

    DownloadJobService downloadJobService;

    public Renderer index() {
        return list();
    }

    public Renderer list() {
        model().append("jobs", downloadJobService.getRecentJobs());

        return renderView("list");
    }

    public Renderer create() {
        model().append("jobTypes", downloadJobService.getTypes());

        return renderView("create");
    }

    public Renderer result() {
        return renderString("result");
    }

    public Renderer save() {
        DownloadJob job = downloadJobService.createByType(request.param("type"));
        if (job == null)
            return redirect(DependencyFactory.getDependency(UrlMapping.class).getUrlTemplate(name() + ".create").getRelativeUrl(), Status._303);

        job.name = request.param("name");
        job.type = request.param("type");

        job.params = new HashMap<>();
        Map<String, MultiValue> params = request.params();
        for (Map.Entry<String, MultiValue> entry : params.entrySet()) {
            if (!entry.getKey().equals("name") && !entry.getKey().equals("type"))
                job.params.put(entry.getKey(), entry.getValue().getValue());
        }

        job.id = downloadJobService.generateId();
        downloadJobService.addJob(job);

        return redirect(DependencyFactory.getDependency(UrlMapping.class).getUrlTemplate(name() + ".list").getRelativeUrl(), Status._303);
    }

    public Renderer form() {
        DownloadJob job = downloadJobService.createByType(request.param("type"));
        if (job == null)
            return renderString("");

        Map<String, String> params = new HashMap<>();
        params.put("name", "just name of the job");
        params.putAll(job.requiredParams());

        model().append("job", job);
        model().append("requiredParams", params);

        return renderView("form");
    }
}
