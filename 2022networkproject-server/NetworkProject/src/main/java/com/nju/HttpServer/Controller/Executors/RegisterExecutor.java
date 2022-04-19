package com.nju.HttpServer.Controller.Executors;

import com.nju.HttpServer.Common.Template;
import com.nju.HttpServer.Http.Components.Body;
import com.nju.HttpServer.Http.Components.Headers;
import com.nju.HttpServer.Http.HttpRequest;
import com.nju.HttpServer.Http.HttpResponse;

import java.util.HashMap;

public class RegisterExecutor implements Executor {
    public static HashMap<String, String> db = new HashMap<>();

    public HttpResponse handle(HttpRequest request) throws Exception {
        HashMap<String, String> db = RegisterExecutor.db;
        HttpResponse response = null;
        Headers headers = request.getHeaders();
        String contentType = headers.getValue("Content-Type").split(";")[0].trim();
        Body body = request.getBody();

        if (!contentType.equals("application/x-www-form-urlencoded")) {
            response = Template.generateStatusCode_405();
            return response;
        }

        String[] key_val = body.ToString().split("&");
        assert (key_val.length == 2);
        String username = null;
        String password = null;
        for (int i = 0; i < key_val.length; i++) {
            String[] tmp = key_val[i].split("=");
            assert (tmp.length == 2);
            if (tmp[0].equals("username")) {
                username = tmp[1].trim();
            } else if (tmp[0].equals("password")) {
                password = tmp[1].trim();
            }
        }
        if (username == null || password == null) {
            response = Template.generateStatusCode_405();
        } else {
            if (!db.containsKey(username)) {
                db.put(username, password);
                String hint = "You have successfully register!";
                response = Template.generateStatusCode_200(hint);
            } else {
                String hint = "You have successfully register!";
                response = Template.generateStatusCode_200(hint);
            }
        }

        return response;
    }
}
