package bigHomeWork;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.io.File;

public class MusicListCell extends ListCell<File> {
    private Label label;
    private BorderPane pane;
    public MusicListCell(){
        pane=new BorderPane();
        label=new Label();
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        Button button=new Button();
        button.setGraphic(new Region());
        button.getStyleClass().add("remove-btn");
        button.setOnAction(e->{
            getListView().getItems().remove(getItem());
        });
        pane.setCenter(label);
        pane.setRight(button);
    }

    @Override
    protected void updateItem(File file, boolean b) {
        super.updateItem(file, b);
        if(file==null||b){      //如果是空行
            setGraphic(null);
        }
        else {
            String name=file.getName();
            //去掉mp3字段
            String mp3name=name.substring(0,name.length()-4);
            label.setText(mp3name);
            setGraphic(pane);
        }
    }
}
