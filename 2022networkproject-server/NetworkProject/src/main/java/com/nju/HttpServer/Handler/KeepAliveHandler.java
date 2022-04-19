package com.nju.HttpServer.Handler;

import com.nju.HttpServer.SimpleServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * 对一个channel处理keepAlive
 * 体现为定时关闭channel
 **/
public class KeepAliveHandler {
    private static Logger logger = LogManager.getLogger(KeepAliveHandler.class);
    private TimerTask timerTask = null;//定时器
    private static final long aliveTime = 10L; //响应给客户端的keep-alive的时间，单位:秒
    private static HashMap<AsynchronousSocketChannel, TimerTask> timerTaskHashMap = new HashMap<>();

    /**
     * 如果标记成同步方法synchronized,能避免异步造成的timerTask相关的exception(虽然这些exception复现率低，并且不影响运行)，但也许会降低并发性能
     *
     * @param channel:需要定时关闭的channel
     **/
    public void setKeepAlive(AsynchronousSocketChannel channel) {
        //如果改channel已经被设置定时关闭，先关掉，并从timer队列里清除引用
        if (timerTaskHashMap.get(channel) != null) {
            logger.debug("取消了一个已有的定时器");
            timerTaskHashMap.get(channel).cancel();
            timerTaskHashMap.remove(channel);
            SimpleServer.timer.purge();
        }
        //然后设置新定时任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                AcceptHandler.closeChannel(channel, "因长连接到期");
            }
        };
        //把定时任务加入timer队列,并在hashmap中标记
        timerTaskHashMap.put(channel, timerTask);
        SimpleServer.timer.schedule(timerTask, aliveTime * 1000L);
    }

    public static long getAliveTime() {
        return aliveTime;
    }
}
