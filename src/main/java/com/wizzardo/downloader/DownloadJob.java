package com.wizzardo.downloader;

import java.util.Map;

/**
 * Created by wizzardo on 04.04.15.
 */
public abstract class DownloadJob {

    public String type;
    public String name;
    public Map params;
    public int id;
    public volatile DownloadStatus status = DownloadStatus.WAITING;


    public abstract void execute();

    public abstract Map<String, String> requiredParams();
}
