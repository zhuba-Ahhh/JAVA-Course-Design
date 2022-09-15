package main;

/*
 * 接受客户端的请求
 *
 * 各指令涵义
 * 0---登录
 * 1---注册
 * 2---查询好友列表
 * 3---添加好友
 * 4---删除好友
 *
 * 5---聊天
 */

import FriendsService.FriendsService;
import FriendsService.DeleteFriendService;
import FriendsService.AddFriendService;
import LoginSerivce.LoginService;
import RegisterService.RegisterService;
import comeMsg.SendComeMsg;
import talk.TalkService;
import thread.LivingThread;
import user.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainControllerThread implements Runnable {
    private User user;
    private Socket s;
    private ServerSocket ss;
    private String clientIP;
    private BufferedReader brServer;
    private int result;
    private boolean signal = true;

    public MainControllerThread(Socket s, String clientIP,ServerSocket ss) {
        this.clientIP = clientIP;
        this.s = s;
        this.ss = ss;
    }


    @Override
    public void run() {
        int order;
        try {

            while (signal) {
                brServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
                order = brServer.read();

                if (order == 0) {
                    login();
                    if (result == 0) {
                        //向在线的好友广播上线了
                        SendComeMsg.sendComeMsg(user.getUsername());

                        Thread.currentThread().setName(user.getUsername());

                        MainController.userThreadMap.put(user.getUsername(), this);
                        MainController.userMap.put(user.getUsername(), clientIP);
                        MainController.threadMap.put(this, true);

                        System.out.println(MainController.userThreadMap);
                        System.out.println(MainController.userMap);

                        //开启LivingThread线程
                        Thread thread = new Thread(new LivingThread(user.getUsername(),ss.accept()));
                        thread.setDaemon(true);
                        thread.start();

                    }
                } else if (order == 1) {
                    register();
                } else if (order == 2) {
                    friendsList();
                } else if (order == 3) {
                    addFriend();
                } else if (order == 4) {
                    deleteFriend();
                } else if (order == 5) {
                    talk();
                }

                //查看当前线程的信号
                signal = MainController.threadMap.containsKey(this);
            }
        } catch (IOException e) {
            System.out.println(" ");
        }

    }

    private void friendsList() {
        BufferedWriter bwServer = null;
        try {
            brServer.readLine();
            String userName = brServer.readLine();

            ArrayList<String> friends = FriendsService.getFriendsList(userName);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            for (String s : friends) {
                bwServer.write(s);
                bwServer.newLine();
                bwServer.flush();
            }

        } catch (IOException e) {
            System.out.println("friendsList(String userName)---s.getOutputStream()");
            e.printStackTrace();
        } finally {
            try {
                bwServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void register() {
        BufferedWriter bwServer = null;
        String username;
        String password;

        try {
            brServer.readLine();
            username = brServer.readLine();
            password = brServer.readLine();


            user = new User();
            user.setUsername(username);
            user.setPassword(password);


            result = RegisterService.registerResult(user);
            System.out.println(result);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bwServer.write(result);
            bwServer.newLine();
            bwServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void login() {
        BufferedWriter bwServer = null;
        String username;
        String password;

        try {
            brServer.readLine();
            username = brServer.readLine();
            password = brServer.readLine();

            user = new User();
            user.setUsername(username);
            user.setPassword(password);

            result = LoginService.loginResult(user);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bwServer.write(result);
            bwServer.newLine();
            bwServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFriend() {
        BufferedWriter bwServer = null;
        String username;
        String friendName;

        try {
            brServer.readLine();
            username = brServer.readLine();
            friendName = brServer.readLine();

            result = DeleteFriendService.deleteFriend(username, friendName);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bwServer.write(result);
            bwServer.newLine();
            bwServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bwServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFriend() {
        BufferedWriter bwServer = null;
        String username;
        String friendName;

        try {
            brServer.readLine();
            username = brServer.readLine();
            friendName = brServer.readLine();

            result = AddFriendService.addFriend(username, friendName);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bwServer.write(result);
            bwServer.newLine();
            bwServer.flush();
        } catch (IOException e) {
            System.out.println("Main---addFriend()");
        } finally {
            try {
                if (bwServer != null) {
                    bwServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void talk() {
        BufferedWriter bwServer = null;
        char[] msg = new char[1024];
        String friendName;
        String username;

        try {
            brServer.readLine();
            friendName = brServer.readLine();
            username = brServer.readLine();
            int len = brServer.read(msg);

            result = TalkService.sendResult(friendName,msg,len,username);

            bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bwServer.write(result);
            bwServer.newLine();
            bwServer.flush();
        } catch (IOException e) {
            System.out.println("Main---talk()");
        } finally {
            try {
                if (bwServer != null) {
                    bwServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
