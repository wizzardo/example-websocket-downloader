package com.wizzardo.downloader;

import com.wizzardo.http.framework.Controller;
import com.wizzardo.http.framework.template.Renderer;

/**
 * Created by wizzardo on 11.07.15.
 */
public class DownloadJobsRiotController extends Controller {
    DownloadJobService downloadJobService;

    public Renderer list() {
        model().append("jobTypes", downloadJobService.getTypes());

        return renderView("list");
    }
}
