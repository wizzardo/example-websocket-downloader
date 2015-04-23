package com.wizzardo.downloader;

import com.wizzardo.http.html.Tag;
import com.wizzardo.tools.io.ZipTools;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.misc.Unchecked;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import static com.wizzardo.http.html.HtmlBuilder.*;

/**
 * Created by wizzardo on 04.04.15.
 */
public abstract class DownloadJob {

    public String type;
    public String name;
    public Map<String, String> params;
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

    public void cancel() {
        setStatus(DownloadStatus.CANCELLED);
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
        downloadJobService.broadcast(new JsonObject()
                        .append("command", "updateLog")
                        .append("id", id)
                        .append("log", s)
        );
    }

    public void setProgress(int progress) {
        this.progress = progress;
        downloadJobService.broadcast(new JsonObject()
                        .append("command", "updateProgress")
                        .append("id", id)
                        .append("progress", progress)
        );
    }

    public void setProgress(int page, int total) {
        setProgress((int) (page * 100f / (total)));
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
        downloadJobService.broadcast(new JsonObject()
                        .append("command", "updateStatus")
                        .append("id", id)
                        .append("status", status)
                        .append("cssClass", status.name().toLowerCase())
        );
    }

    public int getProgress() {
        return progress;
    }

    public String log() {
        return webLog.toString();
    }

    public void generateResult(List<File> files) {
        ZipTools.ZipBuilder builder = new ZipTools.ZipBuilder();
        builder.method(ZipEntry.STORED);
        for (File file : files) {
            builder.append(file);
        }

        Unchecked.run(() ->
                        builder.zip(new FileOutputStream(new File(getWorkDir(), name + ".zip")))
        );
    }
}
