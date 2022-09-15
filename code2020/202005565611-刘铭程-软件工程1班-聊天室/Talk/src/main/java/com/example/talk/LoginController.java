package com.example.talk;

/*
 * 判断账号密码是否正确
 * 返回值涵义
 * 0---登录成功
 * 1---用户名或密码错误
 * 2---登录异常
 */

import Services.LoginService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import thread.LivingThread;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class LoginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label txtLogin;

    @FXML
    public void loginButtonClicked() {
        //获取输入框内容
        String s1 = txtUsername.getText();
        String s2 = txtPassword.getText();

        //根据返回的登录结果进行处理
        int result = LoginService.isLoginEnabled(s1, s2);
        if (result == 0) {
            DemoApplication.userName = txtUsername.getText();

            //开启LivingThread线程
            Thread thread = new Thread(new LivingThread());
            //设置为守护线程
            thread.setDaemon(true);
            thread.start();

            openMainWindow();
        } else if (result == 2) {
            alertLoginException();
        } else {
            alertLoginFail();
        }
    }

    public void registerButtonClicked() {
        openRegisterWindow();
    }

    private void openRegisterWindow() {
        //注册页面
        Stage registerStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("register.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
            registerStage.setResizable(false);
            registerStage.setScene(scene);
            registerStage.setTitle("注册账号");
            registerStage.show();
        } catch (IOException e) {
            System.out.println("注册出现异常");
            e.printStackTrace();
        }
    }

    private void openMainWindow() {
        //等待LivingThread线程启动
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("openMainWindow()---sleep");
        }

        //关闭登录窗口
        Window window = txtUsername.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }

        //用户主界面
        Stage mainStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("mainWindow.fxml"));
        Scene scene;
        try {
            System.out.println("1");
            scene = new Scene(fxmlLoader.load());
            System.out.println("2");
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.setTitle("用户：" + txtUsername.getText());
            mainStage.show();
        } catch (IOException e) {
            System.out.println("登录出现异常");
            e.printStackTrace();
        }
    }

    private void alertLoginFail() {
        txtLogin.setText("账号或密码错误");
    }

    private void alertLoginException() {
        txtLogin.setText("登录异常");
    }

}
