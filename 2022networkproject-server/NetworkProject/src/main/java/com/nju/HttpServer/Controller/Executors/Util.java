package com.nju.HttpServer.Controller.Executors;

import com.nju.HttpServer.Http.Components.Headers;


public class Util {
    public Util() {
    }
    //Todo:用HashMap等方式替代MIME类型分支
    public static void targetToMIME(String target, Headers headers) {
        if (target.endsWith(".html")) {
            headers.addHeader("Content-Type", "text/html");
        } else if (target.endsWith(".png")) {
            headers.addHeader("Content-Type", "image/png");
        } else if (target.endsWith(".jpg")) {
            headers.addHeader("Content-Type", "image/jpeg");
        } else if (target.endsWith(".js")) {
            headers.addHeader("Content-Type", "text/javascript");
        } else if (target.endsWith(".css")) {
            headers.addHeader("Content-Type", "text/css");
        }
    }
}
