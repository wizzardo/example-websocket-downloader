package com.wizzardo.downloader;

import com.wizzardo.epoll.ByteBufferProvider;
import com.wizzardo.epoll.ByteBufferWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wizzardo on 11.04.15.
 */
public class Downloader extends Thread implements ByteBufferProvider {
    private BlockingQueue<DownloadJob> waiting = new LinkedBlockingQueue<>();
    private DownloadJobService downloadJobService;
    private ByteBufferWrapper byteBufferWrapper = new ByteBufferWrapper(1024 * 50);

    Downloader(DownloadJobService downloadJobService, BlockingQueue<DownloadJob> waiting) {
        this.downloadJobService = downloadJobService;
        this.waiting = waiting;
        setDaemon(true);
        setName("Downloader");
    }

    @Override
    public void run() {
        while (true) {
            try {
                DownloadJob job = waiting.take();

                job.setStatus(DownloadStatus.IN_PROGRESS);
                try {
                    job.prepareWorkDir();
                    job.execute();
                    if (!job.isCanceled())
                        job.setStatus(DownloadStatus.DONE);
                } catch (Exception e) {
                    job.log(printStackTrace(e));
                    job.setStatus(DownloadStatus.FAILED);
                }
                if (job.status == DownloadStatus.FAILED || job.status == DownloadStatus.CANCELLED)
                    job.deleteAllFiles();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private String printStackTrace(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        t.printStackTrace(writer);
        writer.append('\n');
        writer.close();
        return out.toString();
    }

    @Override
    public ByteBufferWrapper getBuffer() {
        return byteBufferWrapper;
    }
}
