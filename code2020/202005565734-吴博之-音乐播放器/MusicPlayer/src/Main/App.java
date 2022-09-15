package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Duration;

public class App extends Application {
    // 设置全局舞台对象（以实现后面按钮功能）
    public static Stage staticStage;
    // 记录坐标值
    private double resetX;
    private double resetY;
    // 记录窗体宽度和高度
    private double resetWidth;
    private double resetHeight;
    // 窗体移动前相对于Scene的xy坐标
    private double mouseX;
    private double mouseY;
    // 拖拽改变窗体前的xy坐标
    private double xOffset;
    private double yOffset;
    // 歌单名称标签
    private Label labGroupName;
    // 播放列表的TableView
    private TableView<PlayBean> tableView;
    // 当前播放歌曲的索引
    private int currentIndex;
    // 当前播放的时间的前一秒
    private int prevScond;
    // 当前播放的PlayBean；
    private PlayBean currentPlayBean;
    // 下侧面板总时间
    private Label labTotalTime;
    // 播放按钮图片
    private ImageView butplay;
    // 播放按钮Label
    private Label labplay;
    // 存储当前播放模式
    private int playMode = 1;// 1:列表循环；2：单曲循环
    // 播放时间滚动条
    private Slider sliderSong;
    // 当前已播放的时间
    private Label labPlayTime;
    // 音量滚动条
    private Slider sldVolume;
    // 音量进度条
    private ProgressBar volumeProgress;
    // 显示歌词的Vbox
    private VBox lrcVBox;
    // 存储歌词时间的ArrayList
    private ArrayList<BigDecimal> lrcList = new ArrayList<>();
    // 当前歌词索引
    private int currentLrcIndex;
    //是否存在歌词文件
    private boolean iflrc;

    @Override
    public void start(Stage primaryStage) throws Exception {
        staticStage = primaryStage;
        // 舞台设置
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getTopPane());
        borderPane.setLeft(getLeftPane());
        borderPane.setBottom(getBottomPane());
        borderPane.setCenter(getCenterPane());
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));// 设置颜色
        Scene scene = new Scene(borderPane, 1200, 800); // 幕布大小
        primaryStage.setScene(scene); // 将场景设置到舞台
        primaryStage.initStyle(StageStyle.UNDECORATED); // 去掉舞台的标题栏
        primaryStage.show(); // 显示舞台
    }

    // 中心面板
    private Node getCenterPane() {
        // 读取上一次关闭时播放的歌单和歌曲
        String[] playInfo = XMLUtils.readPrevPlayInfo();

        // 歌单标签
        Label lab1 = new Label("歌单：");
        lab1.setTextFill(Color.rgb(180, 0, 0));
        BorderStroke bs = new BorderStroke(Color.rgb(180, 0, 0), // 四个边的颜色
                Color.rgb(180, 0, 0), Color.rgb(180, 0, 0), Color.rgb(180, 0, 0), BorderStrokeStyle.SOLID, // 四个边的线型：实现
                BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, new CornerRadii(1),
                new BorderWidths(1), new Insets(1, 2, 1, 2));
        lab1.setBorder(new Border(bs));
        lab1.setLayoutX(30); // 设置绝对位置
        lab1.setLayoutY(10);
        lab1.setPrefSize(50, 25);
        lab1.setAlignment(Pos.CENTER);

        // 歌单名称标签
        labGroupName = new Label(playInfo == null ? "（无记录）" : playInfo[0]);
        labGroupName.setLayoutX(90);
        labGroupName.setLayoutY(9);
        labGroupName.setTextFill(Color.WHITE);
        labGroupName.setFont(new Font("黑体", 18));
        labGroupName.setPrefWidth(200);
        labGroupName.setPrefHeight(25);
        labGroupName.setAlignment(Pos.CENTER_LEFT);

        // 碟片的图片
        ImageView panImageView = new ImageView("img/center/1.jpg");
        panImageView.setFitHeight(200);
        panImageView.setFitWidth(200);
        Label lab3 = new Label("", panImageView);
        lab3.setLayoutX(30);
        lab3.setLayoutY(40);

        // 定义一个圆
        Circle circle = new Circle();
        circle.setCenterX(100); // 设置圆心位置
        circle.setCenterY(100);
        circle.setRadius(100); // 设置半径

        panImageView.setClip(circle); // 将图片添加到圆中

        

        // 歌词的VBox容器
        lrcVBox = new VBox(15);
        lrcVBox.setPadding(new Insets(20, 20, 20, 20));
        lrcVBox.setLayoutX(250);
        lrcVBox.setLayoutY(0);

        // 歌单列表标签
        Label lab2 = new Label("歌单列表");
        lab2.setPrefWidth(80);
        lab2.setPrefHeight(25);
        lab2.setTextFill(Color.WHITE);
        lab2.setAlignment(Pos.CENTER);
        lab2.setBackground(new Background(new BackgroundFill(Color.rgb(180, 0, 0), null, null)));
        lab2.setLayoutX(30);
        lab2.setLayoutY(275);

        // 模糊背景
        Image image = new Image("img/center/1.jpg");

        // 获取像素读取器
        PixelReader pr = image.getPixelReader();

        // 创建一个WritableImage
        WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());

        // 创建一个像素写入器
        PixelWriter pixelWriter = wImage.getPixelWriter();
        // 循环读取image中的每个像素
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = pr.getColor(i, j);
                for (int k = 0; k < 4; k++) {
                    color = color.darker(); // 颜色淡化（4次）
                }
                pixelWriter.setColor(i, j, color); // 写入图片
            }
        }

        ImageView backImageView = new ImageView(wImage);
        backImageView.setLayoutX(0);
        backImageView.setLayoutY(0);
        backImageView.setFitHeight(300);
        backImageView.setFitWidth(300);

        // 高斯模糊
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(63);
        backImageView.setEffect(gaussianBlur); // 将效果加到背景上

        // 分割红线
        Label labLine = new Label();
        labLine.setMinHeight(0);
        labLine.setPrefHeight(2);
        labLine.setBackground(new Background(new BackgroundFill(Color.rgb(180, 0, 0), null, null)));
        labLine.setLayoutX(0);
        labLine.setLayoutY(lab2.getLayoutY() + lab2.getPrefHeight());

        // AnchorPane
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        anchorPane.getChildren().addAll(backImageView, lab1, labGroupName, lrcVBox, lab2, lab3, labLine);

        // 上侧ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // 隐藏滚动条
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0, 0, 0, 0));
        scrollPane.setContent(anchorPane);
        scrollPane.setPrefHeight(304);
        scrollPane.setMouseTransparent(true);                       //不接受鼠标事件

        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        backImageView.fitWidthProperty().bind(scrollPane.widthProperty());
        backImageView.fitHeightProperty().bind(scrollPane.heightProperty());
        labLine.prefWidthProperty().bind(scrollPane.widthProperty());
        // ***********************************上侧完毕************************************
        // */
        // 下侧歌单列表
        tableView = new TableView();
        tableView.setPrefWidth(960);
        tableView.getStylesheets().add("playTable.css"); // 导入css文件中设定的样式

        TableColumn c1 = new TableColumn("序号");
        c1.setPrefWidth(80);
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn c2 = new TableColumn("标题");
        c2.setPrefWidth(300);
        c2.setCellValueFactory(new PropertyValueFactory<>("soundName"));

        TableColumn c3 = new TableColumn("歌手");
        c3.setPrefWidth(150);
        c3.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn c4 = new TableColumn("专辑");
        c4.setPrefWidth(150);
        c4.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn c5 = new TableColumn("时间");
        c5.setPrefWidth(150);
        c5.setCellValueFactory(new PropertyValueFactory<>("time"));

        tableView.getColumns().addAll(c1, c2, c4, c3, c5);

        tableView.setRowFactory(tv -> {
            TableRow<PlayBean> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // 验证双击
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // 获取选中行的索引
                    this.currentIndex = row.getIndex();
                    // 将前一秒置为0
                    this.prevScond = 0;
                    // 判断当前是否在播放，如果是则停止
                    if (this.currentPlayBean != null) {
                        this.currentPlayBean.getMediaPlayer().stop();
                    }
                    // 获取当前的PlayBean
                    this.currentPlayBean = row.getItem();
                    // 播放（调用方法）
                    play();
                }
            });
            return row;
        });

        // 总的BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(scrollPane);
        borderPane.setCenter(tableView);
        c5.prefWidthProperty().bind(borderPane.widthProperty());// 将时间列随着窗口大小改变宽度
        return borderPane;
    }

    // 顶端面板
    private BorderPane getTopPane() {
        BorderPane topPane = new BorderPane();

        // 顶端左侧logo
        // 图片部分
        ImageView imgView = new ImageView("img/logo.png");
        imgView.setFitHeight(40); // 设置图片高度
        imgView.setPreserveRatio(true); // 设置宽度为自适应
        // 封装部分
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT); // 居中左对齐
        hBox.setPrefWidth(600); // 默认宽度
        hBox.setPrefHeight(50); // 默认高度
        hBox.setMaxHeight(50); // 最大高度，以定死高度
        hBox.setPadding(new Insets(10, 0, 10, 20)); // 设置内部元素与四边间距
        hBox.getChildren().add(imgView); // 添加到Hbox

        // 顶端右侧按钮
        HBox hBox2 = new HBox(15);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPrefHeight(50);
        hBox2.setPrefWidth(150);
        hBox2.setPadding(new Insets(10, 0, 0, 30));

        // 最小化按钮
        ImageView v1 = new ImageView("img/top imgs/MinmizeDark.png");
        v1.setFitHeight(15);
        v1.setFitWidth(15);
        Label lab1 = new Label("", v1);
        lab1.setMinSize(0, 0);
        lab1.setPrefSize(15, 15);
        // 鼠标移动事件
        lab1.setOnMouseEntered(e -> v1.setImage(new Image("img/top imgs/Minmize.png")));
        lab1.setOnMouseExited(e -> v1.setImage(new Image("img/top imgs/MinmizeDark.png")));
        // 鼠标点击事件
        lab1.setOnMouseClicked(e -> staticStage.setIconified(true));
        // 添加到hBox
        hBox2.getChildren().add(lab1);

        // 最大化按钮
        ImageView v2 = new ImageView("img/top imgs/MaximizeDark.png");
        v2.setFitWidth(15);
        v2.setFitHeight(15);
        Label lab2 = new Label("", v2);
        lab2.setMinSize(0, 0);
        lab2.setPrefSize(15, 15);

        lab2.setOnMouseEntered(e -> v2.setImage(new Image("img/top imgs/Maximize.png")));
        lab2.setOnMouseExited(e -> v2.setImage(new Image("img/top imgs/MaximizeDark.png")));
        lab2.setOnMouseClicked(e -> {
            // 如果窗体为正常状态则最大化
            if (!staticStage.isMaximized()) {
                // 记录操作之前的窗体形状和位置信息
                resetX = staticStage.getX();
                resetY = staticStage.getY();
                resetWidth = staticStage.getWidth();
                resetHeight = staticStage.getHeight();
                staticStage.setMaximized(true); // 最大化
                // 重新设置图片
                v2.setImage(new Image("img/top imgs/resetDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("img/top imgs/reset.png")));
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("img/top imgs/resetDark.png")));
            }
            // 如果窗体为最大化状态则还原
            else {
                staticStage.setX(resetX);
                staticStage.setY(resetY);
                staticStage.setWidth(resetWidth);
                staticStage.setHeight(resetHeight);
                // 操作之后将窗体设置为正常状态
                staticStage.setMaximized(false);
                v2.setImage(new Image("img/top imgs/MaximizeDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("img/top imgs/Maximize.png")));
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("img/top imgs/MaximizeDark.png")));
            }
        });

        hBox2.getChildren().add(lab2);

        // 关闭按钮
        ImageView v3 = new ImageView("img/top imgs/CloseDark.png");
        v3.setFitHeight(15);
        v3.setFitWidth(15);
        Label lab3 = new Label("", v3);
        lab3.setMinSize(0, 0);
        lab3.setPrefSize(15, 15);
        lab3.setOnMouseEntered(e -> v3.setImage(new Image("img/top imgs/Close.png")));
        lab3.setOnMouseExited(e -> v3.setImage(new Image("img/top imgs/CloseDark.png")));
        lab3.setOnMouseClicked(e -> {
            // 记录正在播放的歌曲
            // 结束程序
            System.exit(0); // 结束虚拟机
        });
        hBox2.getChildren().add(lab3);

        // 连接处的线
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(100);
        rectangle.setHeight(2);
        // 设置背景色并使其渐变
        Stop[] stops = new Stop[] { new Stop(0, Color.rgb(120, 5, 14)), new Stop(0.5, Color.RED),
                new Stop(1, Color.rgb(120, 5, 14)) };
        rectangle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops));

        // 设置窗体拖动
        // 鼠标按下时
        topPane.setOnMousePressed(e -> {
            // 记录鼠标相对于窗体的xy坐标
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        // 鼠标拖拽时
        topPane.setOnMouseDragged(e -> {
            // 设置新的xy
            staticStage.setX(e.getScreenX() - mouseX);
            staticStage.setY(e.getScreenY() - mouseY);
        });
        // 整体封装
        topPane.setLeft(hBox);
        topPane.setRight(hBox2);
        topPane.setBottom(rectangle);
        // 将rectangle的宽度绑定至stage
        rectangle.widthProperty().bind(staticStage.widthProperty());
        return topPane;
    }

    // 左侧面板
    private BorderPane getLeftPane() {

        // 图片背景
        ImageView v1 = new ImageView("img/Furry.jpg");
        v1.setFitHeight(255);
        v1.setPreserveRatio(true);

        // 署名
        Label labAuthor = new Label("作者：陌離|OwQ");
        labAuthor.setPrefWidth(255);
        labAuthor.setTextFill(Color.WHITE);
        labAuthor.setFont(new Font("黑体", 18));
        labAuthor.setAlignment(Pos.CENTER);

        // 日期
        Label labDateLabel = new Label("日期：2022-1-12");
        labDateLabel.setPrefWidth(255);
        labDateLabel.setTextFill(Color.WHITE);
        labDateLabel.setFont(new Font("黑体", 18));
        labDateLabel.setAlignment(Pos.CENTER);

        // 已创建歌单
        Label labMusicList = new Label("已创建歌单");
        labMusicList.setPrefWidth(220);
        labMusicList.setPrefHeight(20);
        labMusicList.setTextFill(Color.rgb(160, 160, 160));

        // 符号“+”
        ImageView v2 = new ImageView("img/left/create_2_Dark.png");
        v2.setFitHeight(15);
        v2.setFitWidth(15);

        Label lab = new Label("", v2);
        lab.setPrefSize(15, 15);
        lab.setOnMouseEntered(e -> v2.setImage(new Image("img/left/create_2.png")));
        lab.setOnMouseExited(e -> v2.setImage(new Image("img/left/create_2_Dark.png")));

        // 封装歌单和符号的控件的HBox(水平布局)
        HBox hBox = new HBox(5); // 元素间距
        hBox.getChildren().addAll(labMusicList, lab);

        // 图片署名日期
        VBox vBox = new VBox(15); // 元素间距
        vBox.setPrefSize(255, 300);
        vBox.setPadding(new Insets(5, 5, 5, 10));
        vBox.getChildren().addAll(v1, labAuthor, labDateLabel, hBox);

        // ****************读取所有创建歌单*******************/
        List<String> groupList = XMLUtils.readAllGroup();
        // 将每个歌单名字封装为一个Hbox对象
        List<HBox> hBoxList = new ArrayList<>();
        for (String groupName : groupList) {
            // 收藏图标（心形）ImageView
            ImageView iv1 = new ImageView("img/left/xinyuanDark.png");
            iv1.setFitWidth(15);
            iv1.setPreserveRatio(true);
            Label lab1 = new Label("", iv1);
            lab1.setMinSize(0, 0);
            lab1.setPrefSize(15, 15);
            lab1.setOnMouseEntered(e -> iv1.setImage(new Image("img/left/xinyuan.png")));
            lab1.setOnMouseExited(e -> iv1.setImage(new Image("img/left/xinyuanDark.png")));

            // 歌单名称，Label
            Label labGroupName = new Label(groupName);
            labGroupName.setMinHeight(0);
            labGroupName.setPrefSize(150, 15);
            labGroupName.setTextFill(Color.rgb(210, 210, 210));

            // 播放图片：ImageView
            ImageView iv2 = new ImageView("img/top imgs/PlayDark.png");
            iv2.setFitHeight(15);
            iv2.setFitWidth(15);
            Label lab2 = new Label("", iv2);
            lab2.setMinSize(0, 0);
            lab2.setPrefSize(15, 15);
            lab2.setOnMouseEntered(e -> iv2.setImage(new Image("img/top imgs/Play.png")));
            lab2.setOnMouseExited(e -> iv2.setImage(new Image("img/top imgs/PlayDark.png")));
            lab2.setOnMouseClicked(e -> {
                // 设置歌单名称
                this.labGroupName.setText(labGroupName.getText().trim()); // this调用成员变量
                // 读取歌单下所有的歌曲(调用封装好的方法)
                readAllSoundByGroup();

            });

            // +符号
            ImageView iv3 = new ImageView("img/left/addDark.png");
            iv3.setFitHeight(15);
            iv3.setFitWidth(15);
            Label lab3 = new Label("", iv3);
            lab3.setMinSize(0, 0);
            lab3.setPrefSize(15, 15);
            lab3.setOnMouseEntered(e -> iv3.setImage(new Image("img/left/add.png")));
            lab3.setOnMouseExited(e -> iv3.setImage(new Image("img/left/addDark.png")));

            // 删除符号
            ImageView iv4 = new ImageView("img/left/laji_1_Dark.png");
            iv4.setFitHeight(15);
            iv4.setFitWidth(15);
            Label lab4 = new Label("", iv4);
            lab4.setMinSize(0, 0);
            lab4.setPrefSize(15, 15);
            lab4.setOnMouseEntered(e -> iv4.setImage(new Image("img/left/laji_1.png")));
            lab4.setOnMouseExited(e -> iv4.setImage(new Image("img/left/laji_1_Dark.png")));

            HBox hBox1 = new HBox(10);
            hBox1.getChildren().addAll(lab1, labGroupName, lab2, lab3, lab4);
            hBox1.setPadding(new Insets(5, 5, 5, 10));

            hBoxList.add(hBox1);
        }

        // 封装
        VBox vBox1 = new VBox(10);
        vBox1.setPrefWidth(255);
        vBox1.setPadding(new Insets(10, 0, 0, 15));
        for (HBox hb : hBoxList) {
            vBox1.getChildren().add(hb);
        }
        // 总面板
        BorderPane leftPane = new BorderPane();
        leftPane.setTop(vBox);
        leftPane.setCenter(vBox1);
        return leftPane;

    }

    // 下侧面板
    private BorderPane getBottomPane() {
        // 左侧三个按钮
        // 上一首
        ImageView v1 = new ImageView("img/top imgs/LastDark.png");
        v1.setFitHeight(40);
        v1.setFitWidth(40);
        Label lab1 = new Label("", v1);
        lab1.setOnMouseEntered(e -> v1.setImage(new Image("img/top imgs/Last.png")));
        lab1.setOnMouseExited(e -> v1.setImage(new Image("img/top imgs/LastDark.png")));
        lab1.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            //让当前的索引-1
            this.currentIndex--;
            if (currentIndex < 0) {
                    this.currentIndex = this.tableView.getItems().size() - 1;//定位到最后一首歌
            }
            //设置Table的选中
            this.tableView.getSelectionModel().select(currentIndex);
            //设置播放PlayBean对象
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //开始播放
            play();
        });

        // 播放
        butplay = new ImageView("img/top imgs/PlayDark.png");
        butplay.setFitHeight(40);
        butplay.setFitWidth(40);
        labplay = new Label("", butplay);
        labplay.setOnMouseEntered(e -> butplay.setImage(new Image("img/top imgs/Play.png")));
        labplay.setOnMouseExited(e -> butplay.setImage(new Image("img/top imgs/PlayDark.png")));
        labplay.setOnMouseClicked(e -> {
            // 如果当前正在播放，则暂停
            if (this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                this.currentPlayBean.getMediaPlayer().pause();
                // 设置播放按钮图标为播放状态
                butplay.setImage(new Image("img/top imgs/PlayDark.png"));
                labplay.setOnMouseEntered(ee -> butplay.setImage(new Image("img/top imgs/Play.png")));
                labplay.setOnMouseExited(ee -> butplay.setImage(new Image("img/top imgs/PlayDark.png")));
            } else if (this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PAUSED) {// 如果当前是暂停，则播放
                this.currentPlayBean.getMediaPlayer().play();
                butplay.setImage(new Image("img/top imgs/PauseDark.png"));
                labplay.setOnMouseEntered(ee -> butplay.setImage(new Image("img/top imgs/Pause.png")));
                labplay.setOnMouseExited(ee -> butplay.setImage(new Image("img/top imgs/PauseDark.png")));
            }
        });

        // 下一首
        ImageView v3 = new ImageView("img/top imgs/NextDark.png");
        v3.setFitHeight(40);
        v3.setFitWidth(40);
        Label lab3 = new Label("", v3);
        lab3.setOnMouseEntered(e -> v3.setImage(new Image("img/top imgs/Next.png")));
        lab3.setOnMouseExited(e -> v3.setImage(new Image("img/top imgs/NextDark.png")));
        lab3.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            //让当前的索引+1
            this.currentIndex++;
            if (currentIndex > this.tableView.getItems().size()) {
                    this.currentIndex = 0;//定位到第一首歌
            }
            //设置Table的选中
            this.tableView.getSelectionModel().select(currentIndex);
            //设置播放PlayBean对象
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //开始播放
            play();
        });

        // 封装
        HBox hBox1 = new HBox(30);
        hBox1.setPrefWidth(255);
        hBox1.setPadding(new Insets(5, 10, 5, 30));
        hBox1.getChildren().addAll(lab1, labplay, lab3);
        // 中间的滚动条
        // 已播放的时间
        labPlayTime = new Label("00:00");
        labPlayTime.setPrefSize(40, 50);
        labPlayTime.setTextFill(Color.WHITE);
        // 滚动条
        sliderSong = new Slider();
        sliderSong.setMinSize(0, 0);
        sliderSong.setPrefWidth(200);
        sliderSong.setPrefHeight(12);

        // 进度条
        ProgressBar pb1 = new ProgressBar();
        pb1.setProgress(0);
        pb1.setMinSize(0, 0);
        pb1.setMaxWidth(5000);
        pb1.setPrefWidth(200);
        // 值发生变化时
        sliderSong.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // 设置进度条
                if (currentPlayBean != null) {
                    pb1.setProgress((newValue.doubleValue() + 1) / currentPlayBean.getTotalSeconds());

                }
            }
        });
        // slider的鼠标抬起事件中
        sliderSong.setOnMouseReleased(e -> {
            if (currentPlayBean != null) {
                Duration duration = new Duration(sliderSong.getValue() * 1000);
                currentPlayBean.getMediaPlayer().seek(duration);// 设置新的播放时间

                // 同时设置Label
                Date date = new Date();
                date.setTime((long) currentPlayBean.getMediaPlayer().getCurrentTime().toMillis());
                labPlayTime.setText(new SimpleDateFormat("mm:ss").format(date));
                // 设置前一秒
                prevScond = (int) duration.toSeconds() - 1;
            }
        });

        // 存储进度条和滚动条
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pb1, sliderSong);

        // 总时间标签
        labTotalTime = new Label("00:00");
        labTotalTime.setPrefSize(40, 50);
        labTotalTime.setTextFill(Color.WHITE);
        labTotalTime.setAlignment(Pos.CENTER_RIGHT);
        // 封装
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(labPlayTime);
        borderPane.setCenter(stackPane);
        borderPane.setRight(labTotalTime);

        sliderSong.prefHeightProperty().bind(borderPane.prefHeightProperty());

        // 右侧组件
        // 音量图片
        ImageView v5 = new ImageView("img/top imgs/VolumnDark.png");
        v5.setFitWidth(17);
        v5.setFitHeight(17);
        Label lab5 = new Label("", v5);

        // 音量滚动条
        sldVolume = new Slider();
        sldVolume.setMax(100);
        sldVolume.setValue(50);
        sldVolume.setMajorTickUnit(1); // 每前进一格滚轮，增加一点音量值

        sldVolume.setMinHeight(0);
        sldVolume.setPrefHeight(10);
        sldVolume.setPrefWidth(100);
        // 音量进度条
        volumeProgress = new ProgressBar(0);
        volumeProgress.setMinHeight(0);
        volumeProgress.setPrefHeight(10);
        volumeProgress.setProgress(0.5); // 初始中间位置
        volumeProgress.setPrefHeight(10);
        volumeProgress.setPrefWidth(100);
        volumeProgress.prefWidthProperty().bind(sldVolume.prefWidthProperty());// 绑定宽度确保宽度相同
        // 监听音量进度条的鼠标抬起事件
        sldVolume.setOnMouseReleased(e -> {
            // 设置进度条
            volumeProgress.setProgress(sldVolume.getValue() / 100);
            // 设置音量
            if (this.currentPlayBean != null) {
                currentPlayBean.getMediaPlayer().setVolume(volumeProgress.getProgress());
            }
        });
        // 封装
        StackPane sp2 = new StackPane();
        sp2.getChildren().addAll(volumeProgress, sldVolume);

        // 播放模式图片
        ImageView v6 = new ImageView("img/top imgs/RepeatDark.png");
        v6.setFitWidth(25);
        v6.setFitHeight(25);
        Label lab6 = new Label("", v6);
        lab6.setOnMouseClicked(e -> {
            // 此处只处理playMode实现，放在播放的事件中
            this.playMode++;
            if (playMode > 2) {
                this.playMode = 1;
            }
            switch (this.playMode) {
                case 1:
                    v6.setImage(new Image("img/top imgs/RepeatDark.png"));
                    lab6.setOnMouseEntered(ee -> v6.setImage(new Image("img/top imgs/Repeat.png")));
                    lab6.setOnMouseExited(ee -> v6.setImage(new Image("img/top imgs/RepeatDark.png")));
                    break;
                case 2:
                    v6.setImage(new Image("img/top imgs/RepeatInOneDark.png"));
                    lab6.setOnMouseEntered(ee -> v6.setImage(new Image("img/top imgs/RepeatInOne.png")));
                    lab6.setOnMouseExited(ee -> v6.setImage(new Image("img/top imgs/RepeatInOneDark.png")));
                    break;
            }
        });

        // 歌词图片
        ImageView v7 = new ImageView("img/top imgs/ciDark.png");
        v7.setFitHeight(25);
        v7.setFitWidth(25);
        Label lab7 = new Label("", v7);

        // 拖拽图片
        ImageView v8 = new ImageView("img/top imgs/right_drag.png");
        v8.setFitWidth(30);
        v8.setFitHeight(50);
        Label lab8 = new Label("", v8);
        // 当鼠标按下时
        lab8.setOnMousePressed(e -> {
            // 记录当前鼠标所在位置的屏幕的xy坐标
            xOffset = e.getScreenX();
            yOffset = e.getScreenY();
        });
        // 鼠标移动时
        lab8.setOnMouseMoved(e -> {

        });
        // 当鼠标拖拽时
        lab8.setOnMouseDragged(e -> {
            if (staticStage.getWidth() + (e.getScreenX() - xOffset) >= 1200) {
                staticStage.setWidth(staticStage.getWidth() + (e.getScreenX() - xOffset));
                xOffset = e.getScreenX();
            }
            if (staticStage.getHeight() + (e.getScreenY() - yOffset) >= 800) {
                staticStage.setHeight(staticStage.getHeight() + (e.getScreenY() - yOffset));
                yOffset = e.getScreenY();
            }
        });
        // 封装
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(0, 0, 0, 10));
        hBox.setPrefWidth(270);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(lab5, sp2, lab6, lab7, lab8);

        // 整体封装
        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(hBox1);
        bottomPane.setCenter(borderPane);
        bottomPane.setRight(hBox);
        return bottomPane;
    }

    // 读取某个歌单的所有歌曲
    private void readAllSoundByGroup() {
        // 从标签中读取分组名
        List<SoundBean> soundList = XMLUtils.findSoundByGroupName(labGroupName.getText().trim());
        // 解析每个歌曲文件，封装PlayBean
        List<PlayBean> playBeanList = new ArrayList<>();
        for (int i = 0; i < soundList.size(); i++) {
            SoundBean soundBean = soundList.get(i);
            PlayBean playBean = new PlayBean();
            playBean.setId(i + 1);

            // 读取音频文件
            File file = new File(soundBean.getFilePath());
            // 解析文件
            MP3File mp3File = null;

            try {
                mp3File = new MP3File(file);
            } catch (TagException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }

            // 获取MP3文件的头信息
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            // 获取一个字符串形式的总时长
            String strLength = audioHeader.getTrackLengthAsString();
            // 获取int时长
            int intLength = audioHeader.getTrackLength();

            Set<String> keySet = mp3File.getID3v2Tag().frameMap.keySet();
            String songName = file.getName(); // 歌名
            String artist = null; // 歌手
            String album = null; // 专辑名称

            
            if (keySet.contains("TPE1")) { // 是否包含歌手
                artist = mp3File.getID3v2Tag().frameMap.get("TPE1").toString();
            }
            if (keySet.contains("TALB")) { // 是否包含专辑
                album = mp3File.getID3v2Tag().frameMap.get("TALB").toString();
            }
            System.out.println("歌名：" + songName + "演唱者：" + artist + "专辑名称：" + album);
            if (artist != null && !artist.equals("null")) {
                songName = songName.substring(songName.indexOf("\"") + 1, songName.indexOf("."));
                artist = artist.substring(artist.indexOf("\"") + 1, artist.lastIndexOf("\""));
            }
            if (album != null && !album.equals("null")) {
                album = album.substring(album.indexOf("\"") + 1, album.lastIndexOf("\""));
            }

            // 为PlayBean赋值
            playBean.setSoundName(songName);
            playBean.setArtist(artist);
            playBean.setAlbum(album);
            playBean.setFilePath(soundBean.getFilePath());

            URI uri = file.toURI();
            Media media = new Media(uri.toString());
            MediaPlayer mp = new MediaPlayer(media);
            // 监听播放完毕时
            mp.setOnEndOfMedia(() -> {
                // 停止当前播放器播放
                this.currentPlayBean.getMediaPlayer().stop();
                // 根据当前模式选择下一首歌
                switch (this.playMode) {
                    case 1:// 循环播放
                        this.currentIndex++;
                        if (this.currentIndex >= this.tableView.getItems().size()) {
                            currentIndex = 0;
                        }
                        this.currentPlayBean = tableView.getItems().get(this.currentIndex);
                        break;
                    case 2:// 单曲循环
                        this.currentPlayBean.getMediaPlayer().seek(new Duration(0));// 设置播放时间为0
                        break;
                }
                this.tableView.getSelectionModel().select(currentIndex);
                play();
            });

            // 监听播放时的时间
            mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable,
                                    Duration oldValue, Duration newValue) {
                    // 非设置情况，每秒调用10次
                    // 每秒使滚动条前进一次，获取newValue中的秒
                    int currentSecond = (int) newValue.toSeconds();
                    // 设置滚动条，一秒一次
                    if (currentSecond == prevScond + 1) {
                        // 设置滚动条
                        sliderSong.setValue(sliderSong.getValue() + 1);
                        // 设置前一秒
                        prevScond++;
                        // 设置新的播放时间
                        Date date = new Date();
                        date.setTime((int) sliderSong.getValue() * 1000);
                        labPlayTime.setText(new SimpleDateFormat("mm:ss").format(date));
                    }
                    //设置歌词
                    if(iflrc==true){
                    //获取当前播放时间
                    double millis = newValue.toMillis();

                    //判断此次是否在正常的播放取鉴
                    double min = 0;
                    double max = 0;
                    if(currentLrcIndex==0){
                     min = 0;
                    }else{
                        min = lrcList.get(currentLrcIndex).doubleValue();
                    }
                    if(currentLrcIndex!=lrcList.size()-1){
                        max = lrcList.get(currentLrcIndex+1).doubleValue();
                    }else{
                        max = lrcList.get(currentLrcIndex).doubleValue();
                    }
                    if(millis>=min&&millis<max){
                        return;
                    }
                    if(currentIndex<lrcList.size()-1&&millis>=lrcList.get(currentIndex+1).doubleValue()){
                        currentLrcIndex++;//当前歌词指示器
                        //上移
                        //时间轴动画
                        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(15),//隔15ms执行一次 
                        new EventHandler<ActionEvent>(){
                           @Override
                            public void handle(ActionEvent event){                  //每次执行时移动
                                lrcVBox.setLayoutY(lrcVBox.getLayoutY()-1);
                            }
                        }
                        ));
                        t1.setCycleCount(50);//执行50次
                        t1.play();
                        //当前歌词变黄，字号：30
                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.YELLOW);
                        //字号：30(动画)
                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("黑体",startSize++));
                                    }
                                }
                        ));
                        t2.setCycleCount(12);
                        t2.play();

                        //前一行变小，变为：浅灰色
                        Label lab_Pre_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                        if (lab_Pre_1 != null) {

                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 30;
                                        @Override
                                        public void handle(ActionEvent event) {
                                            lab_Pre_1.setFont(new Font("黑体",startSize--));
                                        }
                                    }
                            ));
                            t3.setCycleCount(12);
                            t3.play();
                            t3.setOnFinished(e -> lab_Pre_1.setTextFill(Color.rgb(114,114,114)));

                        }

                        //前二行
                        if (currentLrcIndex - 2 >= 0) {
                            Label lab_Pre_2 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 2);
                            lab_Pre_2.setTextFill(Color.rgb(53,53,53));
                        }

                        //当前行的后一行，白色
                        if (currentLrcIndex + 1 < lrcList.size()) {
                            Label lab_next_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab_next_1.setTextFill(Color.WHITE);
                        }
                    } else if (currentLrcIndex > 0 && millis < lrcList.get(currentLrcIndex).doubleValue()) {
                        //拖动播放条，回退了
                        currentLrcIndex--;
                        //歌词VBox的下移
                        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(15),//每隔15毫秒执行一次
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {//每次执行时，会执行此方法
                                        lrcVBox.setLayoutY(lrcVBox.getLayoutY() + 1);
                                    }
                                }
                        ));
                        t1.setCycleCount(50);//执行50次
                        t1.play();

                        //当前歌词变黄，字号：30
                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.YELLOW);

                        //字号：30(动画)
                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("黑体",startSize++));
                                    }
                                }
                        ));
                        t2.setCycleCount(12);
                        t2.play();

                        //前一行变为：浅灰
                        if (currentLrcIndex - 1 >= 0) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                            lab.setTextFill(Color.rgb(114,114,114));
                        }
                        //后一行变为百色：字号：18
                        if (currentLrcIndex + 1 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab.setTextFill(Color.WHITE);
                            //动画
                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 30;
                                        @Override
                                        public void handle(ActionEvent event) {
                                            lab.setFont(new Font("黑体",startSize--));
                                        }
                                    }
                            ));
                            t3.setCycleCount(12);
                            t3.play();
                        }
                        //后二行，变为浅灰
                        if (currentLrcIndex + 2 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 2);
                            lab.setTextFill(Color.rgb(114,114,114));
                        }
                        //后三行，变为深灰
                        if (currentLrcIndex + 3 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 3);
                            lab.setTextFill(Color.rgb(53,53,53));
                        }
                    }
                    



                    }
                }
            });

            
            


            playBean.setMediaPlayer(mp);
            playBean.setTime(strLength); // 字符串时间
            playBean.setTotalSeconds(intLength); // 总秒数
            playBeanList.add(playBean);

        }

        // 将PlayBeanList中的数据显示到表格中
        ObservableList<PlayBean> data = FXCollections.observableList(playBeanList);
        this.tableView.getItems().clear(); // 清空表格
        this.tableView.setItems(data);
    }

    // 播放
    private void play() {
        // 读取歌词
        loadLrc();
        // 设置总时间
        this.labTotalTime.setText(this.currentPlayBean.getTime());

        // 设置滚动条总的值
        this.sliderSong.setMax(this.currentPlayBean.getTotalSeconds());
        this.sliderSong.setMajorTickUnit(1);
        this.sliderSong.setValue(0);
        prevScond = 0;

        // 设置总音量
        this.currentPlayBean.getMediaPlayer().setVolume(this.volumeProgress.getProgress());
        // 开始播放
        new Thread() {
            @Override
            public void run() {
                currentPlayBean.getMediaPlayer().play();
            }
        }.start();

        // 设置播放按钮为暂停
        this.butplay.setImage(new Image("img/top imgs/PauseDark.png"));
        this.labplay.setOnMouseEntered(e -> butplay.setImage(new Image("img/top imgs/Pause.png")));
        this.labplay.setOnMouseExited(e -> butplay.setImage(new Image("img/top imgs/PauseDark.png")));
    }

    // 加载正在播放的歌曲的LRC文件（歌词文件）
    private void loadLrc(){
        if (this.currentPlayBean == null) {

            return;
        }
        // 初始化lrcvbox
        this.lrcVBox.getChildren().clear();
        this.lrcVBox.setLayoutY(50 * 2 - 10);
        this.currentIndex = 0;

        // 读取MP3文件
        iflrc = true;
        File mp3File = new File(this.currentPlayBean.getFilePath());
        // 查找歌词文件
        File lrcFile = new File(mp3File.getParent(),
                mp3File.getName().substring(0, mp3File.getName().indexOf(".")) + ".lrc");
        if (!lrcFile.exists()) {
            iflrc=false;
            return;
        }
        // 读取每一行，封装歌词Label
        try {
            BufferedReader bufIn = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), "GBK"));
            String row = null;
            while ((row = bufIn.readLine()) != null) {
                if(row.indexOf("[")==-1||row.indexOf("]")==-1){
                    continue;
                }
                if(row.charAt(1)<'0'||row.charAt(1)>'9'){
                    continue;
                }
                String strTime = row.substring(1,row.indexOf("]"));//取时间
                String strMinute = strTime.substring(0,strTime.indexOf(":"));//取分钟
                String strSecond = strTime.substring(strTime.indexOf(":")+1);//取秒和毫秒
                //转化为int时间
                int intMinute = Integer.parseInt(strMinute);
                //换算为总的毫秒
                BigDecimal totalMilli = new BigDecimal(intMinute*60).add(new BigDecimal(strSecond)).multiply(new BigDecimal(1000));
                this.lrcList.add(totalMilli);
                //创建歌词Label
                Label lab = new Label(row.trim().substring(row.indexOf("]")+1));
                lab.setMinWidth(550);
                lab.setMinHeight(35);
                lab.setMaxHeight(35);
                lab.setPrefWidth(550);
                lab.setPrefHeight(35);
                lab.setTextFill(Color.rgb(53, 53, 53));
                lab.setFont(new Font("黑体",18));
                lab.setAlignment(Pos.CENTER);

                //判断是否是第一个歌词，如果是改为30号黄色
                if(this.lrcVBox.getChildren().size()==0){
                    lab.setTextFill(Color.YELLOW);
                    lab.setFont(new Font("黑体", 30));
                }
                //判断是否是第二个歌词
                if(this.lrcVBox.getChildren().size()==1){
                    lab.setTextFill(Color.WHITE);
                }
                //将歌词label添加到lrcVbox中
                this.lrcVBox.getChildren().add(lab);
            }

        } catch (Exception e) {
            return;
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
