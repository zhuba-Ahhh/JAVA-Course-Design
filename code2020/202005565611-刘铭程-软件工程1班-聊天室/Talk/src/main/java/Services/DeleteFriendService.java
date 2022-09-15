package Services;

/*
返回结果
0 --- 成功
1 --- 失败
 */

import com.example.talk.DemoApplication;

import java.io.*;
import java.net.Socket;

public class DeleteFriendService {
    public static boolean deleteFriend(String username,String friendName){
        BufferedReader brClient = null;
        BufferedWriter bwClient = null;

        try {

            Socket s = new Socket(DemoApplication.serviceIP,12321);
            bwClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //指令4---代表登录
            bwClient.write(4);
            bwClient.newLine();
            bwClient.flush();

            //当前用户名
            bwClient.write(username);
            bwClient.newLine();

            //想要删除的好友的用户名
            bwClient.write(friendName);
            bwClient.newLine();

            bwClient.flush();

            //接收返回的结果
            brClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if(brClient.read() == 0){
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            System.out.println("服务器连接失败");
        } finally {
            try {
                if(brClient!=null){
                    brClient.close();
                }
            } catch (IOException e) {
                System.out.println("brClient=null");
            }
            try {
                if(bwClient!=null){
                    bwClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
