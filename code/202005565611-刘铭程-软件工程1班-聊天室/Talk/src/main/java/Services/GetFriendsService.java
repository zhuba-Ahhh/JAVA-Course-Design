package Services;

/*
 * 获取好友列表
 */

import com.example.talk.DemoApplication;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GetFriendsService {

    public static ArrayList<String> getFriendsName() {
        ArrayList<String> friendsList = new ArrayList<>();

        BufferedReader brClient = null;
        BufferedWriter bwClient = null;

        try {
            Socket s = new Socket(DemoApplication.serviceIP,12321);

            bwClient = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //指令2---代表获取好友列表
            bwClient.write(2);
            bwClient.newLine();
            bwClient.flush();

            bwClient.write(DemoApplication.userName);
            bwClient.newLine();
            bwClient.flush();

            //接收返回的结果
            brClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String data = brClient.readLine();
            while (data != null) {
                friendsList.add(data);
                data = brClient.readLine();
                System.out.println(data);
            }

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


        return friendsList;
    }
}
