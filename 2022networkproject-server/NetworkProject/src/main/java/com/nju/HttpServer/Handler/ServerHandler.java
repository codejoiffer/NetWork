package com.nju.HttpServer.Handler;

import com.nju.HttpServer.SimpleServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class ServerHandler implements Runnable {
    private static Logger logger = LogManager.getLogger(ServerHandler.class);
    private static final CountDownLatch EXIT_LATCH = new CountDownLatch(1);
    public AsynchronousServerSocketChannel channel = null;

    /**
     * @param port:侦听的端口
     **/
    public ServerHandler(int port) {
        try {
            channel = AsynchronousServerSocketChannel.open();
            //把本机地址绑定到Socket上
            channel.bind(new InetSocketAddress(port));
            logger.info("服务端已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        /*
         *是异步操作，与客户端建立连接后，系统会自动回调AcceptHandler()处理。
         *第一个参数this是传给AcceptHandler()的，以便继续侦听新连接。
         */
        channel.accept(this, new AcceptHandler());
        try {
            EXIT_LATCH.await(); //保证主线程一直运行。八股。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
