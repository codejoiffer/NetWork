package Server;

import Http.HttpRequest;
import Http.HttpResponse;
import Http.Util;
import RequestExecutor.BasicExecutor;
import Common.Template;
import RequestExecutor.StaticResourceHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
    // new thread
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket socket;
    boolean ServerSwitch;
    boolean isTimeout = false;
    public static TimerTask timerTask = null;
    public ClientHandler(Socket clientSock) {
        try {
            socket = clientSock;
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());
            ServerSwitch = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    这里是真正的处理逻辑
    @Override
    public void run() {
        try {
            while (true) {
//                连接超时 关闭socket 断开连接 (这里并没有关闭serverSocket 服务端还开着 只是与某个客户端的连接断了)
                if (isTimeout) {
                    socket.close();
                    return;
                }
                // 读取客户端发过来的数据并且打印出来
                String line;
//                用一个stringBuilder存储客户端的数据
                StringBuilder sb = new StringBuilder();
                while ((line = inFromClient.readLine()) != null) {
                    sb.append(line).append('\n');
                    if (line.isEmpty())
                        break;
                }
//                客户端没有输入
                if (sb.toString().equals("")) return;
                System.out.println(sb.toString());
//                使用 轮子Util.String2Request 转成 HttpRequest
                HttpRequest request = Util.String2Request(sb.toString());
//                keep-alive表示的是长连接 但是长连接并不是一直都让客户端和服务端连着的 它有一个timeout 超过了这个时间就断开连接(什么虚假的长连接)
                if (request.getHeaders().getValue("Keep-Alive") != null) {
                    String timeout = request.getHeaders().getValue("Keep-Alive");
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
//                    timerTask就是定时任务 时间一道就去做
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            isTimeout = true;
                        }
                    };
//                    使用schedule方法调用timerTask 这里就是在过了timeout时间之后把isTimeout设置成false 断掉长连接
                    SimpleServer.timer.schedule(timerTask, Integer.parseInt(timeout.substring(8)) * 1000L);
                }
//                这里是获取请求体(数据部分)的长度
                String contentLength = request.getHeaders().getValue("Content-Length");
                if (contentLength != null) {
                    int length = Integer.parseInt(contentLength);
//                    把数据写到cbuf中
                    char[] cbuf = new char[length];
                    inFromClient.read(cbuf, 0, length);
                    request.getBody().setData(String.valueOf(cbuf));
                }
//                得到请求地址和请求方法
                String target = request.getStartLine().getTarget();
                String method = request.getStartLine().getMethod();
//                开始构造 http响应
                HttpResponse response = null;
//                这个是抽象父类 要根据请求地址和方法具体化
                BasicExecutor executor = null;
                // 如果请求一个静态资源，调用StaticResourceHandler
                if (StaticResourceHandler.isStaticTarget(target) && method.toLowerCase().equals("get")) {
                    executor = new StaticResourceHandler();
                } else {
                    // 否则，在持有的executor中找到合适的，用这个executor处理请求
                    for (BasicExecutor e : SimpleServer.Executors) {
                        if (target.endsWith(e.getUrl()) && method.toLowerCase().equals(e.getMethod().toLowerCase())) {
                            executor = e;
                            break;
                        }
                    }
                }
                // 找不到合适的executor
                // 404: 没有对应的url
                // 405: 有对应的url但是没有对应的method
                if (executor == null) {
//                    先当作是404找不到url 但是有可能是405 后面还要再判断一下得到最终的response是什么
                    response = Template.generateStatusCode_404();
//                    特殊情况处理
                    //post静态资源会出现bug，不一定是404
                    for (BasicExecutor e : SimpleServer.Executors) {
                        if (target.endsWith(e.getUrl())) {
                            response = Template.generateStatusCode_405();
                            break;
                        }
                    }

                } else {
//                    正常情况下的response
                    response = executor.handle(request);
                }
                outToClient.write(response.ToBytes());
                //timer 如果再次收到请求，重置timer，否则就关闭
            }
//            outToClient.close();
        }catch (SocketException e){
        } catch (Exception e) {
            HttpResponse response = Template.generateStatusCode_500();
            try {
                outToClient.write(response.ToBytes());
            }catch (Exception ee){}
            e.printStackTrace();
        }
    }
}
