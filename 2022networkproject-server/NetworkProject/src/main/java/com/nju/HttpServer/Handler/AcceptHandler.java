package com.nju.HttpServer.Handler;

import com.nju.HttpServer.SimpleServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.TimerTask;

/**
 * AsynchronousSocketChannel:与客户端建立的连接
 * ServerHandler：为了继续异步与客户端建立连接，而传入的ServerHandler(八股，可以不用管)
 **/
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, ServerHandler> {
    private static Logger logger = LogManager.getLogger(AcceptHandler.class);

    /**
     * 系统与客户端成功建立连接后，会自动调用completed()
     *
     * @param channel:与客户端成功建立的连接,系统给你的
     * @param serverHandler:为了继续异步与客户端建立连接，而传入的ServerHandler(八股，可以不用管)
     **/
    @Override
    public void completed(AsynchronousSocketChannel channel, ServerHandler serverHandler) {
        /*继续接收其它客户端的请求。八股。
         *第一个参数依然需要传serverHandler
         * */
        serverHandler.channel.accept(serverHandler, this);

        try {
            logger.info("建立连接:" + channel.getRemoteAddress().toString() + ",hashcode:" + channel.hashCode());
        } catch (Exception e) {
            logger.error(e);
        }
        readChannel(channel);
    }

    /**
     * 让系统把channel中读到的数据放到buffer中，并注册处理函数RequestHandler()
     *
     * @param channel:需要读取的channel
     **/
    public static void readChannel(AsynchronousSocketChannel channel) {
        if (channel.isOpen()) {
            /*分配一个1024*1024字节的ByteBuffer供系统将收到的数据写入
             *这个大小应该是接受的request报文的最大长度，如果请求太大，可能会出错
             */
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            //异步读，第三个参数为接收消息回调的业务handle
            channel.read(buffer,//读事件需要将数据写入这个buffer
                    buffer,//给回调函数传入的参数，
                    new RequestHandler(channel));
        }
    }

    /**
     * 调用此方法关闭channel!
     * @exception: java.net.SocketException: Connection reset by peer: shutdown 超并发，系统主动关闭连接引起，不影响正常运行
     *
     * @param channel:要关闭的channel
     * @param logMsg:关闭channel时的日志
     **/
    public static void closeChannel(AsynchronousSocketChannel channel, String logMsg) {
        if (channel.isOpen()) {
            try {
//                logger.debug(logMsg + "关闭channel输入输出流");
                channel.shutdownInput();
                channel.shutdownOutput();
                logger.debug(logMsg + "关闭channel:" + channel.hashCode());
                channel.close();
            } catch (IOException e) {
                logger.warn(logMsg + "关闭channel时异常:" + channel.hashCode());
                logger.error(e);
            }
        }
    }

    /**
     * 系统与客户端建立连接失败，会自动调用failed()
     **/
    @Override
    public void failed(Throwable exc, ServerHandler serverHandler) {
        logger.warn("建立连接失败！");
        exc.printStackTrace();
    }
}
