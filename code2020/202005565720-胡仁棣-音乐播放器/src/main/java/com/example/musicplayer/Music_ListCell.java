package com.example.musicplayer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.io.File;

public class Music_ListCell extends ListCell<File> {
    private Label label;
    BorderPane pane;
    public Music_ListCell(){
        pane = new BorderPane();
        label = new Label();
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        Button button = new Button();
        button.setGraphic(new Region());
        button.getStyleClass().add("remove-btn");
        button.setOnAction(event ->{//点击按钮时删除当前歌曲列表行
            getListView().getItems().remove(getItem());
        } );
        pane.setCenter(label);
        pane.setRight(button);

    }
    @Override
    protected void updateItem(File item, boolean empty){
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText("");
        }else{
            String name = item.getName();
            label.setText(name.substring(0,name.length()-4));
            setGraphic(pane);
        }
    }
}
