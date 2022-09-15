package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    //在线用户的 进程 和 IP
    public static Map<String, MainControllerThread> userThreadMap = new HashMap<>();
    public static Map<String, String> userMap = new HashMap<>();
    //用户服务线程生存的信号
    public static Map<MainControllerThread, Boolean> threadMap = new HashMap<>();

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(12321);
            ServerSocket ss2 = new ServerSocket(23432);

            while (true) {
                Socket s = ss.accept();

                InetAddress localAddress = s.getInetAddress();
                String clientIP = localAddress.getHostAddress();

                Thread serviceThread = new Thread(new MainControllerThread(s, clientIP,ss2));
                serviceThread.setDaemon(true);
                serviceThread.start();
            }

        } catch (IOException e) {
            System.out.println("服务启动异常");
            e.printStackTrace();
        }
    }
}
