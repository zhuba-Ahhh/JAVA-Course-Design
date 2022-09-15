package cn.hykDemo.media;

import cn.hykDemo.utils.XMLUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;


public class AddGroup {
    private Stage parentStage;//父窗体
    private VBox groupVBox;//父窗体中显示个单列表的VBox对象
    private MainApp mainApp;

    //移动前的X,Y坐标
    private double mouseX;
    private double mouseY;

    //本窗体的舞台对象
    private Stage stage;
    public AddGroup(Stage parentStage,VBox groupVBox,MainApp mainApp){
        this.parentStage = parentStage;
        this.groupVBox = groupVBox;
        this.mainApp = mainApp;

        //新建歌单label；
        Label lab1 = new Label("新建歌单");
        lab1.setTextFill(Color.WHITE);
        lab1.setPrefWidth(150);
        lab1.setPrefHeight(50);
        lab1.setLayoutX(20);
        lab1.setLayoutY(0);

        //设置一个关闭按钮
        ImageView v1 = new ImageView(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\topandbottom\\CloseDark.png"));
        v1.setFitWidth(13);
        v1.setFitHeight(13);
        Label lab2 = new Label("",v1);
        lab2.setMinWidth(13);
        lab2.setMinHeight(13);
        lab2.setPrefWidth(13);
        lab2.setPrefHeight(13);
        lab2.setLayoutX(270);
        lab2.setLayoutY(20);
        lab2.setOnMouseClicked(e->{ //隐藏窗口
            stage.hide();
        });

        //文本框:
        TextField textGroupName = new TextField();
        textGroupName.setPromptText("请输入歌单名称");
        textGroupName.setPrefWidth(220);
        textGroupName.setPrefHeight(15);
        textGroupName.setLayoutX(20);
        textGroupName.setLayoutY(70);

        //4.提示标签
        Label labMsg = new Label();
        labMsg.setPrefWidth(200);
        labMsg.setLayoutX(20);
        labMsg.setLayoutY(100);
        labMsg.setTextFill(Color.RED);

        //确定按钮
        Button butOk = new Button("确定");
        butOk.setPrefWidth(80);
        butOk.setPrefHeight(30);
        butOk.setLayoutX(50);
        butOk.setLayoutY(190);
        butOk.setTextFill(Color.WHITE);
        butOk.setBackground(new Background(new BackgroundFill(Color.rgb(50,45,128),null,null)));
        butOk.setOnMouseClicked(e->{
            //获取文件筐数据
            String txt = textGroupName.getText().trim();//去掉前后空格
            if (txt == null || txt.length() == 0){
                labMsg.setText("请输入歌单名称!");
                return;
            }
            //验证是否重复
            List<String> groupNameList = XMLUtils.readAllGroup();
            for (String s :groupNameList){
                if (txt.equals(s)){
                    labMsg.setText("歌单名称"+txt+"已经存在！");
                    return;
                }
            }
            //3.写入到文件里面
            XMLUtils.addGroup(txt);

            //4.更新主窗体上面的VBox
            //心形图标：imaganation
            ImageView iv1 = new ImageView(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\xinyuanDark.png"));
            iv1.setFitWidth(15);
            iv1.setPreserveRatio(true);
            Label lab11 = new Label("",iv1);
            lab11.setMinWidth(0);
            lab11.setMinHeight(0);
            lab11.setPrefWidth(15);
            lab11.setPrefHeight(15);
            lab11.setOnMouseEntered(ee->iv1.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\xinyuan.png")));
            lab11.setOnMouseExited(ee-> iv1.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\xinyuanDark.png")));


            //歌单名称：
            Label labGroupName = new Label(txt);
            labGroupName.setMinHeight(10);
            labGroupName.setPrefHeight(20);
            labGroupName.setPrefWidth(150);
            labGroupName.setTextFill(Color.rgb(210,210,210));
            labGroupName.setOnMouseEntered(ee->labGroupName.setTextFill(Color.WHITE));
            labGroupName.setOnMouseExited(ee->labGroupName.setTextFill(Color.rgb(210,210,210)));

            //3.播放图片:
            ImageView iv2 = new ImageView(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\volumn_1_Dark.png"));
            iv2.setFitWidth(15);
            iv2.setFitHeight(15);
            Label lab22 = new Label("",iv2);
            lab22.setMinWidth(0);
            lab22.setMinHeight(0);
            lab22.setPrefWidth(15);
            lab22.setPrefHeight(15);
            lab22.setOnMouseEntered(ee->iv2.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\volumn_1.png")));
            lab22.setOnMouseExited(ee->iv2.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\volumn_1_Dark.png")));


            //3.+符号：
            ImageView iv3 = new ImageView(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\addDark.png"));
            iv3.setFitWidth(15);
            iv3.setFitHeight(15);
            Label lab3 = new Label("",iv3);
            lab3.setMinWidth(0);
            lab3.setMinHeight(0);
            lab3.setPrefWidth(15);
            lab3.setPrefHeight(15);
            lab3.setOnMouseEntered(ee->iv3.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\add.png")));
            lab3.setOnMouseExited(ee->iv3.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\addDark.png")));

            //4.垃圾桶符号：
            ImageView iv4 = new ImageView(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\laji_1_Dark.png"));
            iv4.setFitWidth(15);
            iv4.setFitHeight(15);
            Label lab4 = new Label("",iv4);
            lab4.setMinWidth(0);
            lab4.setMinHeight(0);
            lab4.setPrefWidth(15);
            lab4.setPrefHeight(15);
            lab4.setOnMouseEntered(ee->iv4.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\laji_1.png")));
            lab4.setOnMouseExited(ee->iv4.setImage(new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\left\\laji_1_Dark.png")));

            HBox hBox1 = new HBox(10);
            hBox1.getChildren().addAll(lab11,labGroupName,lab22,lab3,lab4);//注意顺序!!
            hBox1.setPadding(new Insets(5,5,5,10));

            this.groupVBox.getChildren().add(hBox1);

            //关闭舞台
            this.stage.hide();
        });

        //取消按钮：Button
        Button butCancel = new Button("取消");
        butCancel.setPrefWidth(80);
        butCancel.setPrefHeight(30);
        butCancel.setLayoutX(150);
        butCancel.setLayoutY(190);
        butCancel.setTextFill(Color.WHITE);
        butCancel.setBackground(new Background(new BackgroundFill(Color.rgb(100,100,100),null,null)));
        butCancel.setOnMouseClicked(e->stage.hide());

        //创建一个新舞台对象
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        //创建一个场景
        Group group = new Group();
        group.getChildren().addAll(lab1,lab2,textGroupName,labMsg,butOk,butCancel);
        Scene scene = new Scene(group,300,240);
        scene.setFill(Color.rgb(45,47,51));
        scene.setOnMousePressed(e->{
            //记录原位置
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();

        });
        //设置新位置
        scene.setOnMouseDragged(e->{
            //设置新位置
            stage.setX(e.getScreenX()-mouseX);
            stage.setY(e.getScreenY()-mouseY);
        });
        //设置场景
        stage.setScene(scene);
        //显示舞台
        stage.show();
    }
}
