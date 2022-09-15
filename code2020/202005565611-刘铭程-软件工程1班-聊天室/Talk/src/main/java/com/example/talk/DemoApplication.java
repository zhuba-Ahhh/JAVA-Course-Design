package com.example.talk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DemoApplication extends Application {
    public static String userName = "";
    public static String serviceIP = "192.168.43.168";

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(serviceIP);
        FXMLLoader fxmlLoader = new FXMLLoader(DemoApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("登录");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
