package com.wizzardo.downloader;

import com.wizzardo.http.html.Tag;
import com.wizzardo.tools.json.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.wizzardo.http.html.HtmlBuilder.*;

/**
 * Created by wizzardo on 04.04.15.
 */
public abstract class DownloadJob {

    public String type;
    public String name;
    public Map params;
    public int id;
    public volatile DownloadStatus status = DownloadStatus.WAITING;

    private DownloadJobService downloadJobService;
    private int progress;
    private StringBuilder webLog = new StringBuilder();

    public abstract void execute();

    public abstract Map<String, String> requiredParams();

    public void setDownloadJobService(DownloadJobService downloadJobService) {
        this.downloadJobService = downloadJobService;
    }

    public Tag renderInputFields() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "just name of the job");
        params.putAll(requiredParams());

        return div()
                .add(input().name("type").type("hidden").value(getClass().getSimpleName()))
                .each(params.entrySet(), it -> {
                    return div()
                            .add(span().clazz("key").text(it.getKey()))
                            .add(input().type("text").name(it.getKey()).placeholder(it.getValue().replace("\"", "")));
                });
    }

    public void prepareWorkDir() {
        deleteAllFiles();
        getWorkDir().mkdirs();
    }

    public void deleteAllFiles() {
        deleteAll(getWorkDir());
    }

    public File getWorkDir() {
        return new File("/tmp/downloader/" + id);
    }

    private void deleteAll(File dir) {
        if (dir.isDirectory())
            for (File file : dir.listFiles()) {
                if (file.isDirectory())
                    deleteAll(file);
                else
                    file.delete();
            }
        dir.delete();
    }

    public boolean isCanceled() {
        return status == DownloadStatus.CANCELLED;
    }

    public void log(Object ob) {
        log(String.valueOf(ob));
    }

    public void log(String s) {
        s = s.trim() + '\n';
        webLog.append(s);
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }
}
