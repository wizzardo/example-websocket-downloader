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
        return renderString("form");
    }
}
