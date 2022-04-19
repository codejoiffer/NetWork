package com.nju.HttpServer.Http;

import com.nju.HttpServer.Http.Components.*;

public class Util {
    public static String lineBreak = System.getProperty("line.separator");

    public Util() {
    }

    public static HttpRequest String2Request(String s) {
        String[] tmp = s.split(lineBreak);
        StartLine startLine = StartLine.String2StartLine(tmp[0]);
        Headers headers = new Headers();

        int i;
        for (i = 1; i < tmp.length && !tmp[i].equals("") && !tmp[i].equals(lineBreak); ++i) {
            headers.addHeader(tmp[i]);
        }

        Body body;
        for (body = new Body(); i < tmp.length; ++i) {
            body.append(tmp[i]);
        }

        return new HttpRequest(startLine, headers, body);
    }

    public static HttpResponse String2Response(String s) {
        String[] tmp = s.split(lineBreak);
        StatusLine statusLine = StatusLine.String2StatusLine(tmp[0]);
        Headers headers = new Headers();

        int i;
        for (i = 1; i < tmp.length && !tmp[i].equals(""); ++i) {
            headers.addHeader(tmp[i]);
        }

        Body body;
        for (body = new Body(); i < tmp.length; ++i) {
            body.append(tmp[i]);
        }

        return new HttpResponse(statusLine, headers, body);
    }
}