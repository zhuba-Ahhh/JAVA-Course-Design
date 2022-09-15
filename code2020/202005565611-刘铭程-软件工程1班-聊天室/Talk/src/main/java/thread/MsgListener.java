package thread;

import com.example.talk.MainWindowController;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import music.PlayMusic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MsgListener implements Runnable {
    private MainWindowController main;

    public MsgListener(MainWindowController main) {
        this.main = main;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(34543);

            while (true) {
                Socket s = ss.accept();
                BufferedReader brClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String name = brClient.readLine();
                char[] data = new char[1024];
                int len = brClient.read(data);
                String msg = new String(data, 0, len);
                System.out.println(msg);

                if(name.equals("上号提示音")){
                    //好友上线消息
                    PlayMusic.playComeMusic();
                    sleep(1500);
                    continue;
                }

                PlayMusic.playMsgMusic();
                main.showMsg(name,msg);
                if(!main.getFriendName_2().equals(name)){
                    tranState(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tranState(String name){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                TreeView<String> unRead = main.getUnRead();
                ArrayList<String> unfris = main.getUnfris();

                //更新消息列表
                unfris.add(name);

                TreeItem<String> rootItem = new TreeItem<>("消息列表");
                unRead.setRoot(rootItem);
                rootItem.setExpanded(true);

                //加载更新后的消息列表
                TreeItem<String> friName;
                for (String s : unfris) {
                    friName = new TreeItem<>(s);
                    rootItem.getChildren().add(friName);
                }
            }
        });
    }
}
