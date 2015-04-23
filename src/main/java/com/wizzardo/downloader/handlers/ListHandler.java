package com.wizzardo.downloader.handlers;

import com.wizzardo.downloader.App;
import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.downloader.DownloadStatus;
import com.wizzardo.http.Handler;
import com.wizzardo.http.html.HtmlBuilder;
import com.wizzardo.http.html.Tag;
import com.wizzardo.http.request.Header;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;

import java.io.IOException;
import java.util.List;

import static com.wizzardo.http.html.HtmlBuilder.*;

/**
 * Created by wizzardo on 04.04.15.
 */
public class ListHandler implements Handler {

    App app;

    public ListHandler(App app) {
        this.app = app;
    }

    @Override
    public Response handle(Request request, Response response) throws IOException {
        response.appendHeader(Header.KV_CONTENT_TYPE_HTML_UTF8);
        response.setBody(render(app.getDownloadJobService().getRecentJobs()));
        return response;
    }

    private Tag render(List<DownloadJob> jobs) {
        Tag html = new HtmlBuilder()
                .add(head()
                                .add(title("List of recent downloads"))
                                .add(script().src("https://code.jquery.com/jquery-1.11.2.min.js"))
                                .add(script().src(app.getUrlMapping().getUrlTemplate("static").getRelativeUrl("/js/web_socket.js")))
                                .add(link().href(app.getUrlMapping().getUrlTemplate("static").getRelativeUrl("/css/app.css")))
                )
                .add(body()
                                .add(div().id("container")
                                                .add(a().href(app.getUrlMapping().getUrlTemplate("create").getRelativeUrl()).text("create new Job"))
                                                .add(br())
                                                .each(jobs, this::renderJob)
                                )
                                .add(script().src(app.getUrlMapping().getUrlTemplate("static").getRelativeUrl("/js/app.js")))
                );

        return html;
    }

    private Tag renderJob(DownloadJob job) {
        Tag tag = div().clazz("job " + job.status.name().toLowerCase()).id("job_" + job.id)
                .add(strong().text("status: ")).add(span().clazz("status").text(job.status.toString())).add(br())
                .add(strong().text("type: ")).add(new Tag.Text(job.type)).add(br())
                .add(strong().text("name: ")).add(new Tag.Text(job.name)).add(br())
                .add(strong().text("params: ")).add(new Tag.Text(String.valueOf(job.params))).add(br())
                .add(br())
                .add(div()
                                .add(a()
                                        .href(app.getUrlMapping().getUrlTemplate("download").getRelativeUrl("/" + job.id + "/" + job.name + ".zip"))
                                        .style(job.status != DownloadStatus.DONE ? "display: none" : "")
                                        .text("Download result")
                                        .clazz("download"))
                                .add(span()
                                        .clazz("toggleLog")
                                        .attr("onclick", "toggleLog(" + job.id + ")")
                                        .text("toggle log"))
                                .addIf(job.status == DownloadStatus.IN_PROGRESS, () -> {
                                    return span().clazz("cancel").attr("onclick", "cancel(" + job.id + ")").text("cancel");
                                })
                );

        if (job.status != DownloadStatus.DONE)
            tag.add(div().clazz("progress").add(div().clazz("value").style("width: " + job.getProgress() + "%").text("")));

        tag.add(new Tag("textarea").clazz("log").attr("readonly", "true").style("display: none").text(job.log()));

        if (job.status == DownloadStatus.IN_PROGRESS)
            tag.add(span().clazz("cancel").attr("onclick", "cancel(" + job.id + ")").text("cancel"));

        return tag;
    }
}
