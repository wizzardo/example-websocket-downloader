package com.wizzardo.downloader;

import com.wizzardo.downloader.jobs.FakeJob;
import com.wizzardo.http.framework.di.Service;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.misc.Unchecked;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wizzardo on 04.04.15.
 */
public class DownloadJobService implements Service {
    private BlockingQueue<DownloadJob> waiting = new LinkedBlockingQueue<>();
    private Map<Integer, DownloadJob> allJobs = new LinkedHashMap<>();
    private LinkedList<DownloadJob> recentJobs = new LinkedList<>();
    private Map<String, Class<? extends DownloadJob>> types;
    private Set<Integer> ids = new HashSet<>();
    private Random random = new Random();

    DownloaderWebSocketHandler webSocketHandler;

    public DownloadJobService() {
        recentJobs.add(new FakeJob(DownloadStatus.FAILED));

        initType();
        new Downloader(this, waiting).start();
    }

    public DownloadJobService(App app) {
        this();
        webSocketHandler = app.getWebSocketHandler();
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

        return Unchecked.call(() -> clazz.newInstance());
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
        webSocketHandler.broadcast(json.toString());
    }

    public DownloadJob getJob(int id) {
        return allJobs.get(id);
    }

    public Optional<DownloadJob> getJobOptional(int id) {
        return Optional.ofNullable(getJob(id));
    }
}
