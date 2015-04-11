package com.wizzardo.downloader;

import com.wizzardo.downloader.jobs.FakeJob;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.misc.UncheckedThrow;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wizzardo on 04.04.15.
 */
public class DownloadJobService {
    private BlockingQueue<DownloadJob> waiting = new LinkedBlockingQueue<>();
    private Map<Integer, DownloadJob> allJobs = new LinkedHashMap<>();
    private LinkedList<DownloadJob> recentJobs = new LinkedList<>();
    private Map<String, Class<? extends DownloadJob>> types;
    private Set<Integer> ids = new HashSet<>();
    private Random random = new Random();

    public DownloadJobService() {
        recentJobs.add(new FakeJob(DownloadStatus.DONE));
        recentJobs.add(new FakeJob(DownloadStatus.FAILED));

        initType();
        new Downloader(this, waiting).start();
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

    public DownloadJob createByType(String type) {
        Class<? extends DownloadJob> clazz = types.get(type);
        if (clazz == null)
            return null;

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw UncheckedThrow.rethrow(e);
        }
    }

    public int generateId() {
        while (true) {
            int i = random.nextInt(10000);
            if (ids.add(i))
                return i;
        }
    }

    public void addJob(DownloadJob downloadJob) {
        downloadJob.setDownloadJobService(this);
        waiting.add(downloadJob);
        allJobs.put(downloadJob.id, downloadJob);
        recentJobs.addFirst(downloadJob);

        if (recentJobs.size() > 30)
            allJobs.remove(recentJobs.removeLast().id);
    }

    public void broadcast(JsonObject json) {
        System.out.println(json.toString());
    }
}
