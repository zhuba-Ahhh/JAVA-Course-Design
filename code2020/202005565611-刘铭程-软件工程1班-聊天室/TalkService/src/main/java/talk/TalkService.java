package talk;

/*
返回值
0---成功
1---失败
2---好友不在线
 */

import main.MainController;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TalkService {
    public static int sendResult(String toUser, char[] msg, int len, String fromUser) {
        if (!MainController.userMap.containsKey(toUser)) {
            return 2;
        }
        try {
            Socket s = new Socket(MainController.userMap.get(toUser), 34543);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(fromUser);
            bw.newLine();
            bw.flush();

            //发送消息
            bw.write(msg, 0, len);
            bw.flush();

            return 0;
        } catch (IOException e) {
            System.out.println("发送消息失败");
            return 1;
        }
    }
}
