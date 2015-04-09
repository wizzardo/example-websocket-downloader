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
        response.setBody(render(new ArrayList<>()));
        return response;
    }

    private Tag render(List<String> types) {
        List<String> withDefaultType = new ArrayList<>(types.size() + 1);
        withDefaultType.add(" -- ");
        withDefaultType.addAll(types);

        Tag html = new HtmlBuilder()
                .add(head()
                                .add(title("Create download job"))
                                .add(script().src("https://code.jquery.com/jquery-1.11.2.min.js"))
                                .add(link().href("/static/css/app.css"))
                )
                .add(body()
                                .add(div().id("container")
                                                .add(new Tag.Select(withDefaultType).id("types").attr("onchange", "onSelectJobType()"))
                                                .add(new Tag("form").attr("method", "post").attr("action", "save")
                                                                .add(div().id("inputs"))
                                                                .add(new Tag("input").attr("type", "submit").attr("value", "create"))
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
