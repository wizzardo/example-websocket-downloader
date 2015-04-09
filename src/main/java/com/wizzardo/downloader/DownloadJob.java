package com.wizzardo.downloader;

import com.wizzardo.http.html.Tag;

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


    public abstract void execute();

    public abstract Map<String, String> requiredParams();

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
}
