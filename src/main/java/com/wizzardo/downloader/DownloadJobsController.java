package com.wizzardo.downloader;

import com.wizzardo.http.framework.Controller;
import com.wizzardo.http.framework.template.Renderer;

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
        return renderString("save");
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
