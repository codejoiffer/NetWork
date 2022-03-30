package Server;

import RequestExecutor.*;

import java.util.Timer;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleServer {
//    存放各种请求的executor
    public static ArrayList<BasicExecutor> Executors = new ArrayList<>();
//    timer 这个是后面配合timerTask 判断(关闭)长连接用的
    public static Timer timer = new Timer();
//    服务端程序入口
    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.go();
    }
    private void go(){
        Executors.add(new LoginExecutor());
        Executors.add(new RegisterExecutor());
        Executors.add(new ErrorExecutor());
//        这里静态资源的处理后面单独拎出来了 这里没有加进去
       // Executors.add(new StaticResourceHandler());
        try {
//            建立一个ServerSocket 监听本地的5000端口
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("http://localhost:5000");
//             死循环 长连接 保证serverSocket一直在等待客户端的连接
            while(true){
//                 accept方法返回的是一个与client端通信的socket
//                 服务端通过这个socket可以向client端写数据/接收服务端数据
                Socket socket = serverSocket.accept();
//                这个是不是连接上客户端的socket
//                每连接了一个客户端就开一个线程来跑 这样就可以并发了
//                可以用线程池 更加高效
                Thread readThread = new Thread(new ClientHandler(socket));
                readThread.start();
                System.out.println("Got a connection from " + socket.getInetAddress().getHostAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
