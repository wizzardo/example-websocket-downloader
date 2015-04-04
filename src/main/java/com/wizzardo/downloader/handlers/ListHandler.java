package com.wizzardo.downloader.handlers;

import com.wizzardo.http.Handler;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;

import java.io.IOException;

/**
 * Created by wizzardo on 04.04.15.
 */
public class ListHandler implements Handler{
    @Override
    public Response handle(Request request, Response response) throws IOException {
        return response;
    }
}
