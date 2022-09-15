package com.example.talk;

/*
 * 判断注册是否成功
 * 返回值涵义
 * 0---注册成功
 * 1---用户名已存在
 * 2---注册异常
 */

import Services.RegisterService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegisterController {
    @FXML
    private Label registerAlarm;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtPassword1;
    @FXML
    private PasswordField txtPassword2;

    public void registerButtonClicked() {
        //获取输入框数据
        String username = txtUserName.getText();
        String password1 = txtPassword1.getText();
        String password2 = txtPassword2.getText();

        if (username.isEmpty() || password1.isEmpty()) {
            registerAlarm.setText("用户名或密码不能为空");
        } else if (password1.equals(password2)) {
            //判断注册是否成功
            int result = RegisterService.isRegisterEnabled(username, password1);
            if (result == 0) {
                registerAlarm.setText("注册成功");
            } else if (result == 1) {
                registerAlarm.setText("用户名已存在");
            } else {
                registerAlarm.setText("注册异常");
            }
        } else {
            registerAlarm.setText("两次输入密码不同");
        }
    }


    private void close() {
        //关闭注册窗口
        Window window = txtUserName.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

}
