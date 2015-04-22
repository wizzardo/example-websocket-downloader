package com.wizzardo.downloader.handlers;

import com.wizzardo.downloader.App;
import com.wizzardo.http.Handler;
import com.wizzardo.http.html.HtmlBuilder;
import com.wizzardo.http.html.Tag;
import com.wizzardo.http.request.Header;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.wizzardo.http.html.HtmlBuilder.*;

/**
 * Created by wizzardo on 09.04.15.
 */
public class CreateHandler implements Handler {
    private App app;

    public CreateHandler(App app) {
        this.app = app;
    }

    @Override
    public Response handle(Request request, Response response) throws IOException {
        response.appendHeader(Header.KV_CONTENT_TYPE_HTML_UTF8);
        response.setBody(render(app.getDownloadJobService().getTypes()));
        return response;
    }

    private Tag render(Set<String> types) {
        List<String> withDefaultType = new ArrayList<>(types.size() + 1);
        withDefaultType.add(" -- ");
        withDefaultType.addAll(types);

        Tag html = new HtmlBuilder()
                .add(head()
                                .add(title("Create download job"))
                                .add(script().src("https://code.jquery.com/jquery-1.11.2.min.js"))
                                .add(link().href(app.getUrlMapping().getUrlTemplate("static").getRelativeUrl("/css/app.css")))
                )
                .add(body()
                                .add(div().id("container")
                                                .add(select(withDefaultType).id("types").attr("onchange", "onSelectJobType()"))
                                                .add(form().method("post").action(app.getUrlMapping().getUrlTemplate("save").getRelativeUrl())
                                                                .add(div().id("inputs").text(""))
                                                                .add(input().type("submit").value("create"))
                                                )
                                )
                                .add(script().text("function onSelectJobType(){\n" +
                                        "    var params = {type: $('#types').val()};\n" +
                                        "    jQuery.ajax({type:'POST',data:params, url:'form',success:function(data,textStatus){jQuery('#inputs').html(data);}});\n" +
                                        "}"))
                );

        return html;
    }

}
