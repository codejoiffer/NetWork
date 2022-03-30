package com.networkcourse.httpclient.client;

import com.networkcourse.httpclient.exception.InvalidHttpRequestException;
import com.networkcourse.httpclient.exception.MissingHostException;
import com.networkcourse.httpclient.exception.UnsupportedHostException;
import com.networkcourse.httpclient.handler.RequestHandler;
import com.networkcourse.httpclient.handler.ResponseHandler;
import com.networkcourse.httpclient.history.History;
import com.networkcourse.httpclient.message.HttpRequest;
import com.networkcourse.httpclient.message.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * @author fguohao
 * @date 2021/05/31
 */
public class Client {
//    这个用于记录日志
    History history ;
//    重定向缓存(处理301状态码)
    ClientRedirectCache clientRedirectCache ;
//    这个是用来处理304状态码
    ClientModifiedCache clientModifiedCache ;
//    连接池(这个是管理所有与服务端连接的客户端的连接池)
    ClientPool clientPool;
//    请求处理
    RequestHandler requestHandler;
//    响应处理
    ResponseHandler responseHandler ;

    public Client(){
        clientRedirectCache = new ClientRedirectCache();
        clientModifiedCache = new ClientModifiedCache();
        history = new History();
        clientPool = new ClientPool(history);
        requestHandler = new RequestHandler(clientRedirectCache,clientModifiedCache,history);
        responseHandler = new ResponseHandler(clientRedirectCache, clientModifiedCache,this,history);
    }


//  发送Http请求 返回http响应
    public HttpResponse sendHttpRequest(HttpRequest httpRequest) throws URISyntaxException, MissingHostException, UnsupportedHostException, ParseException {
        HttpResponse httpResponse = null;
        try {
//            由最原始的请求重构一下请求 详见handle方法
            HttpRequest refactedHttpRequest = requestHandler.handle(httpRequest);
//            创建一个clientServer 这个时候客户端和服务端已经连接了
            ClientServer clientServer = this.clientPool.sendHttpRequest(refactedHttpRequest);
//            得到http响应 具体见handle方法
            httpResponse = this.responseHandler.handle(httpRequest,clientServer.getRecvStream());
//            如果不是长连接 直接关掉
            if(!httpRequest.isKeepAlive()){
                clientPool.removeConnection(httpRequest.getHost());
            }
            return httpResponse;
        } catch (IOException | InvalidHttpRequestException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

//  这个是什么模式
    public void stdMode(){
        while (true){
            try {
                HttpRequest httpRequest = new HttpRequest(System.in);
                HttpResponse httpResponse = this.sendHttpRequest(httpRequest);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (UnsupportedHostException e) {
                e.printStackTrace();
            } catch (MissingHostException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLogLevel(int logLevel){
        history.setLogLevel(logLevel);
    }



}
