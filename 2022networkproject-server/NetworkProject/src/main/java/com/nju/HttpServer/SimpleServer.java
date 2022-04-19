package com.nju.HttpServer;

import com.nju.HttpServer.Handler.ServerHandler;
import com.nju.HttpServer.Router.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

public class SimpleServer {
    private static Logger logger = LogManager.getLogger(SimpleServer.class);
    //uri匹配器，负责匹配uri和对应的请求方法
    public static Router router = new Router(System.getProperty("java.class.path"));
    //timer 这个是后面配合timerTask 判断(关闭)长连接用的
    public static Timer timer = new Timer();

    public SimpleServer() {
    }

    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        //把RequestMapper里的注解匹配方法注册到router上
        router.addRouter("com.nju.HttpServer.Controller.RequestMapper");
        server.go();
    }

    private void go() {
        //开一个ServerHandler线程，用来侦听连接
        Thread serverThread = new Thread(new ServerHandler(5000));
        serverThread.start();
    }
}