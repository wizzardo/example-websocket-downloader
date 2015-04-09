package com.wizzardo.downloader.jobs;

import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.downloader.DownloadStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Created by wizzardo on 04.04.15.
 */
public class FakeJob extends DownloadJob {
    public FakeJob(DownloadStatus status) {
        this.status = status;
        this.name = "FakeJob";
        this.type = "fake";
    }

    @Override
    public void execute() {
    }

    @Override
    public Map<String, String> requiredParams() {
        return Collections.emptyMap();
    }
}
