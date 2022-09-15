package com.example.musicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class HelloApplication extends Application {
    //start方法执行时将窗口给他，以便controller里面获取窗口信息
    public static Stage static_stage;
    //最大化最小化操作要记录窗口的X,Y和窗口大小
    public static double resetX;
    public static double resetY;
    public static double resetHeight;
    public static double resetWidth;
    //拖拽前的窗口的位置信息
    public static double dragBeforeX;
    public static double dragBeforeY;
    //改变窗口大小时的鼠标位置
    public static double mouseX;
    public static double mouseY;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1015, 600);
        stage.setTitle("简音乐");
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(1015);
        stage.getIcons().add(new Image("file:src/main/resources/img/dio.jpg"));
        stage.initStyle(StageStyle.TRANSPARENT);
        static_stage = stage;
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}