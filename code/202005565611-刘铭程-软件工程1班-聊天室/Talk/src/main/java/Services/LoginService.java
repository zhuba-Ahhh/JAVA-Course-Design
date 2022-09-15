package Services;

/*
 * 判断账号密码是否正确---0
 * 返回值涵义
 * 0---登录成功
 * 1---用户名或密码错误
 * 2---登录异常
 */

import com.example.talk.DemoApplication;

import java.io.*;
import java.net.Socket;

public class LoginService {
    public static int isLoginEnabled(String username, String password) {
        BufferedReader brClient = null;
        BufferedWriter bwClient = null;

        try {

            Socket s = new Socket(DemoApplication.serviceIP, 12321);
            bwClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //指令0---代表登录
            bwClient.write(0);
            bwClient.newLine();
            bwClient.flush();

            //用户名
            bwClient.write(username);
            bwClient.newLine();

            //密码
            bwClient.write(password);
            bwClient.newLine();

            bwClient.flush();

            //接收返回的结果
            brClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            return brClient.read();
        } catch (IOException e) {
            System.out.println("服务器连接失败");
        } finally {
            try {
                if (brClient != null) {
                    brClient.close();
                }
            } catch (IOException e) {
                System.out.println("brClient=null");
            }
            try {
                if (bwClient != null) {
                    bwClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 2;
    }
}
