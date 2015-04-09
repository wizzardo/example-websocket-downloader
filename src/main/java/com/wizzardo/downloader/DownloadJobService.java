package com.wizzardo.downloader;

import com.wizzardo.downloader.jobs.FakeJob;

import java.io.File;
import java.util.*;

/**
 * Created by wizzardo on 04.04.15.
 */
public class DownloadJobService {
    private List<DownloadJob> recentJobs = new ArrayList<>();
    private Map<String, Class<? extends DownloadJob>> types;

    public DownloadJobService() {
        recentJobs.add(new FakeJob(DownloadStatus.DONE));
        recentJobs.add(new FakeJob(DownloadStatus.FAILED));

        initType();
    }

    private void initType() {
        types = new LinkedHashMap<>();
        try {
            for (File it : new File(DownloadJob.class.getResource("jobs").getFile()).listFiles()) {
                String name = it.getName().substring(0, it.getName().length() - 6);
                Class clazz = DownloadJob.class.getClassLoader().loadClass("com.wizzardo.downloader.jobs." + name);
                if (DownloadJob.class.isAssignableFrom(clazz)) {
                    types.put(name, (Class<? extends DownloadJob>) clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<DownloadJob> getRecentJobs() {
        return recentJobs;
    }

    public Set<String> getTypes() {
        return types.keySet();
    }
}
