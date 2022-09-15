package Services;

/*
返回值
0---成功
1---失败
2---好友不在线
 */

import com.example.talk.DemoApplication;

import java.io.*;
import java.net.Socket;

public class TalkService {
    public static int sendMsg(String friendName, String msg) {
        BufferedReader brClient = null;
        BufferedWriter bwClient = null;

        try {

            Socket s = new Socket(DemoApplication.serviceIP, 12321);
            bwClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //指令5---代表发送消息
            bwClient.write(5);
            bwClient.newLine();
            bwClient.flush();

            //to用户名
            bwClient.write(friendName);
            bwClient.newLine();
            bwClient.flush();

            //from用户名
            bwClient.write(DemoApplication.userName);
            bwClient.newLine();
            bwClient.flush();

            //消息
            char[] msgData = msg.toCharArray();
            bwClient.write(msgData,0,msgData.length);
//            bwClient.newLine();

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
        return 1;
    }
}
