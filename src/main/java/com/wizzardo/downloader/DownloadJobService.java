package com.wizzardo.downloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzardo on 04.04.15.
 */
public class DownloadJobService {
    private List<DownloadJob> recentJobs = new ArrayList<>();

    public DownloadJobService() {
        recentJobs.add(new FakeJob(DownloadStatus.DONE));
        recentJobs.add(new FakeJob(DownloadStatus.FAILED));
    }

    public List<DownloadJob> getRecentJobs() {
        return recentJobs;
    }

}
