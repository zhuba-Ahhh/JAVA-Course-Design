package thread;

import main.MainController;
import main.MainControllerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class LivingThread implements Runnable {
    private String username;
    private Socket s;

    public LivingThread(String username,Socket s) {
        this.username = username;
        this.s = s;
    }

    @Override
    public void run() {

        try {
//            ss = new ServerSocket(23432);
//            s = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            br.read();
        } catch (IOException e) {
            System.out.println(username + "下线");
            //用户下线

            //从在线名单中去除 && 杀死为该用户服务的线程
            MainControllerThread thread = MainController.userThreadMap.remove(username);
            MainController.userMap.remove(username);
            MainController.threadMap.remove(thread);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
