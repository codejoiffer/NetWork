package com.nju.HttpServer.Common;

import com.nju.HttpServer.Http.Components.Body;
import com.nju.HttpServer.Http.Components.Headers;
import com.nju.HttpServer.Http.Components.StatusLine;
import com.nju.HttpServer.Http.HttpResponse;
import com.nju.HttpServer.Common.StatusCode;

/**
 * 一些对应状态码的response模板
 **/
public class Template {

    public static HttpResponse generateStatusCode_200(String hint) {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.OK.getCode(), "200 OK");
        Headers headers = new Headers();
        String html_200 = "<html>\n" +
                "<head><title>200 OK</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>200 OK</h1><h2>" + hint + "</h2><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_200.length()));
        Body body = new Body(html_200);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_404() {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.NOT_FOUND.getCode(), "404 Method Not Allowed");
        Headers headers = new Headers();
        String html404 = "<html>\n" +
                "<head><title>404 Not Found</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>404 Not Found</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html404.length()));
        Body body = new Body(html404);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_405() {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.METHOD_NOT_ALLOWED.getCode(), "405 Method Not Allowed");
        Headers headers = new Headers();
        String html405 = "<html>\n" +
                "<head><title>405 Not Allowed</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>405 Not Allowed</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html405.length()));
        Body body = new Body(html405);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_500() {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.INTERNAL_SERVER_ERROR.getCode(), "500 Internal Server Error");
        Headers headers = new Headers();
        String html_500 = "<html>\n" +
                "<head><title>500 Internal Server Error</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>500 Internal Server Error</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_500.length()));
        Body body = new Body(html_500);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_400() {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.INTERNAL_SERVER_ERROR.getCode(), "400 Bad Request");
        Headers headers = new Headers();
        String html_400 = "<html>\n" +
                "<head><title>400 Bad Request</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>400 Bad Request</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_400.length()));
        Body body = new Body(html_400);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_304() {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.NOT_MODIFIED.getCode(), "304 Not Modified");
        Headers headers = new Headers();
        Body body = new Body();
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_301(String url) {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.MOVED_PERMANENTLY.getCode(), "301 Moved Permanrntly");
        Headers headers = new Headers();
        String html_301 = "<html>\n" +
                "<head><title>301 Moved Permanrntly</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>301 Moved Permanrntly</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_301.length()));
        headers.addHeader("Location", url);
        Body body = new Body(html_301);
        return new HttpResponse(statusLine, headers, body);
    }

    public static HttpResponse generateStatusCode_302(String url) {
        StatusLine statusLine = new StatusLine(1.1, StatusCode.FOUND.getCode(), "302 Found");
        Headers headers = new Headers();
        String html_302 = "<html>\n" +
                "<head><title>302 Found</title></head>\n" +
                "<body bgcolor=\"white\">\n" +
                "<center><h1>302 Found</h1><h6>Simple AIO Http-Server<h6></center>\n" +
                "</body>\n" +
                "</html>";
        headers.addHeader("Content-Type", "text/html");
        headers.addHeader("Content-Length", Long.toString(html_302.length()));
        headers.addHeader("Location", url);
        Body body = new Body(html_302);
        return new HttpResponse(statusLine, headers, body);
    }
}
