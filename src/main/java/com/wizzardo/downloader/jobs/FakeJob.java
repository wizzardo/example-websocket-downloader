package com.wizzardo.downloader.jobs;

import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.downloader.DownloadStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Created by wizzardo on 04.04.15.
 */
public class FakeJob extends DownloadJob {

    public FakeJob() {
    }

    public FakeJob(DownloadStatus status) {
        this.status = status;
        this.name = "FakeJob";
        this.type = "fake";
    }

    @Override
    public void execute() {
        for (int i = 0; i < 10; i++) {
            if (isCanceled())
                break;

            log("step " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            setProgress(i + 1, 10);
        }
        log("done");
    }

    @Override
    public Map<String, String> requiredParams() {
        return Collections.emptyMap();
    }
}
