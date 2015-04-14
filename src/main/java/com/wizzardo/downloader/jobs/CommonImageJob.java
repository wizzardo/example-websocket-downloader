package com.wizzardo.downloader.jobs;

import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.tools.collections.MapTools;
import com.wizzardo.tools.http.HttpClient;
import com.wizzardo.tools.io.FileTools;
import com.wizzardo.tools.misc.Unchecked;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzardo on 12.04.15.
 */
public class CommonImageJob extends DownloadJob {
    @Override
    public void execute() {
        List<File> l = new ArrayList<>();

        String url = params.get("url");

        int k = MapTools.getInteger(params, "start", 1);
        int from = MapTools.getInteger(params, "from", 1);
        int to = MapTools.getInteger(params, "to", 1);
        int count = to - from;

        for (int i = from; i <= to; i++) {
            if (isCanceled())
                return;
            log("page " + i);
            File f = new File(getWorkDir(), String.format("%04d.jpg", k++));
            final int ii = i;
            Unchecked.run(() -> FileTools.bytes(f, HttpClient.createRequest(String.format(url, ii)).get().asBytes()));
            l.add(f);
            setProgress(i - from, count);
        }

        generateResult(l);
        setProgress(100);
    }


    @Override
    public Map<String, String> requiredParams() {
        return new HashMap<String, String>() {{
            put("url", "url of image file, replace page number with \"%03d\"");
            put("from", "from page, default - 1");
            put("to", "to page");
            put("start", "start page number result, default - 1");
        }};
    }
}
