package bigHomeWork;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.StageStyle;

public class MusicPlayer extends Application{
    private double offsetX,offsetY;
    private Scene scene;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/fxml/player.fxml"));
        scene=new Scene(root,null);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreenExitHint("");//全屏时不显示按esc退出全屏提示框
        stage.show();
        scene.setOnMousePressed(e->{
//            注意是scene不是screen
            offsetX=e.getSceneX();
            offsetY=e.getSceneY();
        });
        scene.setOnMouseDragged(e->{
            stage.setX(e.getScreenX()-offsetX);
            stage.setY(e.getScreenY()-offsetY);
        });
    }
    public static void main(String args[]){
        launch(args);
    }
}
