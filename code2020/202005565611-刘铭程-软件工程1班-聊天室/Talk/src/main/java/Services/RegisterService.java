package Services;

/*
 * 判断注册是否成功---1
 * 返回值涵义
 * 0---注册成功
 * 1---用户名已存在
 * 2---注册异常
 */

import com.example.talk.DemoApplication;

import java.io.*;
import java.net.Socket;

public class RegisterService {
    public static int isRegisterEnabled(String username, String password) {
        BufferedReader brClient = null;
        BufferedWriter bwClient = null;

        try {
            Socket s = new Socket(DemoApplication.serviceIP,12321);

            bwClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //指令1---代表注册
            bwClient.write(1);
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
            e.printStackTrace();
        } finally {
            try {
                assert brClient != null;
                brClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bwClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 2;
    }
}
