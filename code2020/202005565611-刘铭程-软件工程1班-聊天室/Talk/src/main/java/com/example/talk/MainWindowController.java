package com.example.talk;

import Services.AddFriendService;
import Services.DeleteFriendService;
import Services.GetFriendsService;
import Services.TalkService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import thread.MsgListener;
import javafx.application.Platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private TreeView<String> friendsList;
    @FXML
    private TreeView<String> unRead;
    @FXML
    private Button btnUserName;
    @FXML
    private Label addResult;
    @FXML
    private Label talkFriendName;
    @FXML
    private VBox msgVBox;
    @FXML
    private TextArea textMsg;
    @FXML
    private Label sendResult;
    private VBox vBox = null;
    private Map<String, VBox> talkMap = new HashMap<>();    //存自己的消息
    private ArrayList<String> fris;
    private ArrayList<String> unfris = new ArrayList<>();
    private String friendName_1;    //点击一下获取的
    private String friendName_2 = "好友";    //点击两下获取的
    private String friendName_3 = null;    //上一次双击获取的

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化

        talkMap.put("@main", msgVBox);

        //开启消息监听线程
        Thread msgListener = new Thread(new MsgListener(this));
        //设置为守护线程
        msgListener.setDaemon(true);
        msgListener.start();

        //从服务器获取好友列表
        fris = GetFriendsService.getFriendsName();

        //加载好友列表
        TreeItem<String> friName;
        TreeItem<String> rootItem = new TreeItem<>("好友");
        friendsList.setRoot(rootItem);
        rootItem.setExpanded(true);

        for (String s : fris) {
            friName = new TreeItem<>(s);
            rootItem.getChildren().add(friName);
        }

        //消息列表
        TreeItem<String> rootItem_2 = new TreeItem<>("消息列表");
        unRead.setRoot(rootItem_2);
        rootItem.setExpanded(true);

        //用户名称-----点击可查看用户信息
        btnUserName.setText(DemoApplication.userName);

        //设置好友列表双击事件
        friendsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    if (event.getClickCount() == 2 && !friendsList.getSelectionModel().getSelectedItem().getValue().equals("好友")) {
                        //打开与该好友聊天框
                        friendName_2 = friendsList.getSelectionModel().getSelectedItem().getValue();
                        //加载与该好友的聊天界面
                        if (talkMap.containsKey(friendName_2)) {
                            if (!friendName_2.equals(friendName_3)) {
                                //将聊天记录组件放回存放位置
                                while (msgVBox.getChildren().size() > 0) {
                                    vBox.getChildren().add(msgVBox.getChildren().get(0));
                                }

                                vBox = talkMap.get(friendName_2);
                            }
                        } else {
                            if (friendName_3 != null) {
                                //将聊天记录组件放回存放位置
                                while (msgVBox.getChildren().size() > 0) {
                                    vBox.getChildren().add(msgVBox.getChildren().get(0));
                                }
                            }

                            vBox = new VBox();
                            vBox.setPrefWidth(675);
                        }

                        //导入聊天记录
                        while (vBox.getChildren().size() > 0) {
                            //get()会把vBox中的节点取出来，而不仅仅是值取出来
                            msgVBox.getChildren().add(vBox.getChildren().get(0));
                        }

                        //聊天框对象名称
                        talkFriendName.setText(friendName_2);
                        //将目标用户的聊天记录以VBox的形式存起来
                        talkMap.put(friendName_2, vBox);

                        friendName_3 = friendName_2;
                        textMsg.setText("");
                        sendResult.setText("");
                    } else if (event.getClickCount() == 1 && !friendsList.getSelectionModel().getSelectedItem().getValue().equals("好友")) {
                        //选中点击好友，以对其进行删除操作
                        friendName_1 = friendsList.getSelectionModel().getSelectedItem().getValue();
                    }
                } catch (Exception e) {
                    System.out.println("啥也没点");
                }
            }
        });

        //设置消息列表点击事件
        unRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        if (event.getClickCount() == 2 && !unRead.getSelectionModel().getSelectedItem().getValue().equals("消息列表")) {
                            //打开与该好友聊天框
                            friendName_2 = unRead.getSelectionModel().getSelectedItem().getValue();
                            //加载与该好友的聊天界面
                            if (talkMap.containsKey(friendName_2)) {
                                if (!friendName_2.equals(friendName_3)) {
                                    //将聊天记录组件放回存放位置
                                    while (msgVBox.getChildren().size() > 0) {
                                        vBox.getChildren().add(msgVBox.getChildren().get(0));
                                    }

                                    vBox = talkMap.get(friendName_2);
                                }
                            } else {
                                if (friendName_3 != null) {
                                    //将聊天记录组件放回存放位置
                                    while (msgVBox.getChildren().size() > 0) {
                                        vBox.getChildren().add(msgVBox.getChildren().get(0));
                                    }
                                }

                                vBox = new VBox();
                                vBox.setPrefWidth(675);
                            }

                            //导入聊天记录
                            while (vBox.getChildren().size() > 0) {
                                //get()会把vBox中的节点取出来，而不仅仅是值取出来
                                msgVBox.getChildren().add(vBox.getChildren().get(0));
                            }

                            //聊天框对象名称
                            talkFriendName.setText(friendName_2);
                            //将目标用户的聊天记录以VBox的形式存起来
                            talkMap.put(friendName_2, vBox);

                            friendName_3 = friendName_2;
                            textMsg.setText("");
                            sendResult.setText("");

                            //去除消息列表中的该用户名
                            unfris.remove(friendName_2);

                            TreeItem<String> rootItem = new TreeItem<>("消息列表");
                            unRead.setRoot(rootItem);
                            rootItem.setExpanded(true);

                            //加载更新后的好友列表
                            TreeItem<String> friName;
                            for (String s : unfris) {
                                friName = new TreeItem<>(s);
                                rootItem.getChildren().add(friName);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("啥也没点");
                    }
                }
            });
    }

    public void userNameClicked() {

    }

    public void addFriendClicked() {
        //添加好友
        TextInputDialog textInputDialog = new TextInputDialog("输入对方用户名");
        textInputDialog.setTitle("添加好友");
        textInputDialog.setHeaderText("请输入对方用户名");
        textInputDialog.setContentText(" ");
        textInputDialog.showAndWait();

        //输入的用户名
        String result = textInputDialog.getResult();

        //向服务器发送请求
        int b = AddFriendService.addFriend(DemoApplication.userName, result);
        System.out.println(b);
        if (b == 1) {
            //添加失败
            addResult.setText("添加失败");
            return;
        } else if (b == 2) {
            //用户不存在
            addResult.setText("该用户不存在");
            return;
        }

        //更新好友列表
        fris.add(result);

        TreeItem<String> rootItem = new TreeItem<>("好友");
        friendsList.setRoot(rootItem);
        rootItem.setExpanded(true);

        //加载更新后的好友列表
        TreeItem<String> friName;
        for (String s : fris) {
            friName = new TreeItem<>(s);
            rootItem.getChildren().add(friName);
        }

        addResult.setText("添加成功");
    }

    public void deleteFriendClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "确认删除好友？");
        alert.setTitle("确认删除好友");
        alert.showAndWait();
        String result = alert.getResult().getButtonData().toString();

        //点击取消
        if (result.equals("CANCEL_CLOSE")) {
            return;
        }

        //向服务器发送请求
        boolean b = DeleteFriendService.deleteFriend(DemoApplication.userName, friendName_1);
        if (!b) {
            //删除失败
            return;
        }

        //更新好友列表
        fris.remove(friendName_1);

        TreeItem<String> rootItem = new TreeItem<>("好友");
        friendsList.setRoot(rootItem);
        rootItem.setExpanded(true);

        //加载更新后的好友列表
        TreeItem<String> friName;
        for (String s : fris) {
            friName = new TreeItem<>(s);
            rootItem.getChildren().add(friName);
        }
    }

    public void sendClicked() {
        //获取输入框内容
        String msg = textMsg.getText();

        if (msg.equals("")) {
            sendResult.setText("消息不允许为空!");
            return;
        }

        //把消息发送到服务器由服务器发送给目标
        int result = TalkService.sendMsg(friendName_2, msg);
        if (result == 1) {
            //发送失败
            sendResult.setText("发送失败!");
            return;
        } else if (result == 2) {
            //好友不在线
            sendResult.setText("好友不在线");
            return;
        }

        //导入聊天记录
        //显示在聊天框中
        HBox hBox = new HBox();
        hBox.setPrefWidth(680);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(0, 0, 20, 0));

        Label myname = new Label();
        myname.setFont(new Font(14));
        myname.setText("    <--  " + DemoApplication.userName);

        Label msgLabel = new Label();
        msgLabel.setFont(new Font(18));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(500);
        msgLabel.setText(msg);

        hBox.getChildren().add(msgLabel);
        hBox.getChildren().add(myname);
        vBox.getChildren().add(hBox);

        //get()会把vBox中的节点取出来，而不仅仅是值取出来
        msgVBox.getChildren().add(vBox.getChildren().get(0));

//        showMsg(textMsg.getText(), "zyz");

        textMsg.setText("");
        sendResult.setText("发送成功!");


//        showMsg();
    }

    //展示收到的消息
    public void showMsg(String fromUsername, String recMsg) {
        VBox v = null;

        //导入聊天记录
        if (!talkMap.containsKey(fromUsername)) {
            v = new VBox();
            talkMap.put(fromUsername, v);
        } else if (fromUsername.equals(friendName_2)) {
            v = talkMap.get("@main");
        } else {
            v = talkMap.get(fromUsername);
        }

        HBox hBox = new HBox();
        hBox.setPrefWidth(680);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(0, 0, 20, 20));

        Label myname = new Label();
        myname.setFont(new Font(14));
        myname.setText(fromUsername + "  -->    ");

        Label msgLabel = new Label();
        msgLabel.setFont(new Font(18));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(500);
        msgLabel.setText(recMsg);

        hBox.getChildren().add(myname);
        hBox.getChildren().add(msgLabel);

        VBox finalV = v;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                finalV.getChildren().add(hBox);
            }
        });
        System.out.println("消息更新完成");
    }

    public TreeView<String> getUnRead() {
        return unRead;
    }

    public ArrayList<String> getUnfris() {
        return unfris;
    }

    public String getFriendName_2() {
        return friendName_2;
    }
}
