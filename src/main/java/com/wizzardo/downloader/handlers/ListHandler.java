package com.wizzardo.downloader.handlers;

import com.wizzardo.downloader.App;
import com.wizzardo.downloader.DownloadJob;
import com.wizzardo.http.Handler;
import com.wizzardo.http.html.HtmlBuilder;
import com.wizzardo.http.html.Renderer;
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
        response.setBody(render("", app.getDownloadJobService().getRecentJobs()));
        return response;
    }

    private String render(String websocketUrl, List<DownloadJob> jobs) {
        Tag html = new HtmlBuilder()
                .add(head()
                                .add(title("List of recent downloads"))
                                .add(script().src("https://code.jquery.com/jquery-1.11.2.min.js"))
                                .add(script().src("/static/js/web_socket.js"))
                                .add(link().href("/static/css/app.css"))
                )
                .add(body()
                                .add(div().id("container")
                                                .add(a().href("").text("create new Job"))
                                                .add(br())
                                                .each(jobs, this::renderJob)
                                )
                                .add(script().text("var webSocketsUrl = '" + websocketUrl + "';"))
                                .add(script().src("/static/js/app.js"))
                );

        return render(html);
    }

    private Tag renderJob(DownloadJob job) {
        return div().clazz("job " + job.status.name().toLowerCase()).id("job_" + job.id)
                .add(strong().text("status: ")).add(span().clazz("status").text(job.status.toString())).add(br())
                .add(strong().text("type: ")).add(new Tag.Text(job.status.toString())).add(br())
                .add(strong().text("name: ")).add(new Tag.Text(job.name)).add(br())
                .add(strong().text("params: ")).add(new Tag.Text(String.valueOf(job.params))).add(br())
                .add(br())
                ;
    }

    private String render(Tag tag) {
        StringBuilder sb = new StringBuilder();
        tag.render(Renderer.create(sb));
        return sb.toString();
    }
}
