package com.nju.HttpServer.Controller;

import com.nju.HttpServer.Controller.Executors.ErrorExecutor;
import com.nju.HttpServer.Controller.Executors.LoginExecutor;
import com.nju.HttpServer.Controller.Executors.RegisterExecutor;
import com.nju.HttpServer.Http.HttpRequest;
import com.nju.HttpServer.Http.HttpResponse;
import com.nju.HttpServer.Router.RouteMapping;

/**
 * 手搓类Springboot风格的注解匹配器
 **/
public class RequestMapper {

    @RouteMapping(uri = "/login", method = "post")
    public HttpResponse Login(HttpRequest request) throws Exception {
        return new LoginExecutor().handle(request);
    }

    @RouteMapping(uri = "/register", method = "post")
    public HttpResponse Register(HttpRequest request) throws Exception {
        return new RegisterExecutor().handle(request);
    }

    @RouteMapping(uri = "/error", method = "get")
    public HttpResponse Error(HttpRequest request) throws Exception {
        return new ErrorExecutor().handle(request);
    }
}
