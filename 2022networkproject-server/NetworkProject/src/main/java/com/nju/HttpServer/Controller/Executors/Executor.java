package com.nju.HttpServer.Controller.Executors;

import com.nju.HttpServer.Http.HttpRequest;
import com.nju.HttpServer.Http.HttpResponse;

/**
 * 执行器接口，处理非静态资源请求，如登陆、注册等
 **/
public interface Executor {
    /**
     * @param request:输入的request
     * @return 输出的response
     */
    public HttpResponse handle(HttpRequest request) throws Exception;
}
