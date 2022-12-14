package cn.hykDemo.media;

import cn.hykDemo.bean.PlayBean;
import cn.hykDemo.bean.SoundBean;
import cn.hykDemo.daoimp.SoundDaoImp;
import cn.hykDemo.utils.ImageUtils;
import cn.hykDemo.utils.XMLUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


public class MainApp extends Application {
    //1.?????????"??????"??????
    public static Stage staticStage;
    //2.??????????????????x,y??????
    private double resetX;
    private double resetY;
    //3.?????????????????????????????????
    private double resetWidth;
    private double resetHeight;
    //4.???????????????????????????Scene???X,Y??????
    private double mouseX;
    private double mouseY;

    //5.?????????????????????VBox??????
    private VBox groupVBox;

    //6.??????????????????X,Y??????
    private double xOffset;
    private double yOffset;

    //7.??????????????????
    private Label labGroupName;

    //8.???????????????TableView
    private TableView<PlayBean> tableView;

    //9.???????????????????????????
    private int currentIndex;
    //10.?????????????????????????????????--???????????????
    private int prevSecond;
    //11.???????????????PlayBean
    private PlayBean currentPlayBean;
    //12.???????????????????????????
    private Label labTotalTime;
    //13.?????????ImageView??????
    private ImageView panImageView;
    //14.????????????????????????
    private Timeline timeline;
    //15.??????
    private ImageView backImageView;

    //16.???????????????ImageView??????
    private ImageView butPlayImage;

    //17.???????????????Label
    private Label labPlay;

    //18.?????????????????????
    private int playMode = 1;//1 : ???????????????2. ????????????  3.????????????

    //19.???????????????????????????
    private Slider sliderSong;

    //20.??????????????????Lable
    private Label labPlayTime;

    //21.???????????????
    private Slider sldVolume;

    //22.??????????????????
    private ProgressBar volumeProgress;

    //23.????????????????????????
    private double prevVolumn;

    //24.???????????????VBox??????
    private VBox lrcVBox;
    //25.?????????????????????ArrayList
    private ArrayList<BigDecimal> lrcList = new ArrayList<>();
    //26.?????????????????????
    private int currentLrcIndex;

    //27.??????????????????
    private SoundDaoImp sdi = new SoundDaoImp();





    @Override
    public void start(Stage primaryStage) throws Exception {
        staticStage = primaryStage;
        //????????????
        //1.????????????BorderPane??????
//        BorderPane borderPane = new BorderPane();
//        Image image = new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\topandbottom\\beijing1.jpg", 1210, 800, false, false, false);
//        double height = image.getHeight();
//        double width = image.getWidth();
//        WritableImage writableImage = new ImageUtil().imgOpacity(image, 0.5);
//        ImageView imageView = new ImageView();
//        imageView.setImage(writableImage);
//        borderPane.getChildren().add(imageView);
//        borderPane.setTop(getTopPane());
//        borderPane.setLeft(getLeftPane());
//        borderPane.setBottom(getBottomPane());
//        borderPane.setCenter(getCenterPane());
//        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(2),null)));

        //????????????BorderPane??????
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(getTopPane());
        borderPane.setLeft(getLeftPane());
        borderPane.setBottom(getBottomPane());
        borderPane.setCenter(getCenterPane());
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));//???????????????


        //2.??????????????????
        Scene scene = new Scene(borderPane, 1210, 800);//???????????????1300????????????????????????800??????
        //3.????????????????????????
        primaryStage.setScene(scene);
        //4.???????????????????????????
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //????????????
        primaryStage.show();
    }
    //???????????????????????????
    private BorderPane getCenterPane() {
        //1.????????????????????????????????????????????????
        String[] playInfo = XMLUtils.readPrevPlayInfo();

        //2.???????????????
        Label lab1 = new Label("?????????");
        lab1.setTextFill(Color.WHITE);//??????????????????
        BorderStroke bs = new BorderStroke(
                Color.rgb(0,150,180),//??????????????????
                Color.rgb(0,150,180),
                Color.rgb(0,150,180),
                Color.rgb(0,150,180),
                BorderStrokeStyle.SOLID,//??????????????????--??????
                BorderStrokeStyle.SOLID,
                BorderStrokeStyle.SOLID,
                BorderStrokeStyle.SOLID,
                new CornerRadii(1),
                new BorderWidths(1),
                new Insets(1,2,1,2)

        );
        lab1.setBorder(new Border(bs));
        lab1.setLayoutX(30);
        lab1.setLayoutY(10);
        lab1.setPrefWidth(50);
        lab1.setPrefHeight(25);
        lab1.setAlignment(Pos.CENTER);

        //3.?????????????????????
        labGroupName = new Label(playInfo == null ? "(?????????)" : playInfo[0]);
        labGroupName.setLayoutX(90);
        labGroupName.setLayoutY(9);
        labGroupName.setTextFill(Color.BLUE);
        labGroupName.setFont(new Font("??????",18));
        labGroupName.setPrefWidth(200);
        labGroupName.setPrefHeight(25);
        labGroupName.setAlignment(Pos.CENTER_LEFT);

        //4.???????????????
        panImageView = new ImageView("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\center\\pan_default.jpg");
        panImageView.setFitHeight(200);
        panImageView.setFitWidth(200);
        Label lab2 = new Label("", panImageView);
        lab2.setLayoutX(30);
        lab2.setLayoutY(60);

        //???????????????
        Circle circle = new Circle();
        circle.setCenterX(100);
        circle.setCenterY(100);
        circle.setRadius(100);//????????????

        panImageView.setClip(circle);

        //????????????"?????????"??????
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(new Duration(0),new KeyValue(panImageView.rotateProperty(),0)),
                new KeyFrame(new Duration(8 * 1000), new KeyValue(panImageView.rotateProperty(),360))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);//????????????
//        timeline.play();

        //5.?????????VBox??????
        lrcVBox = new VBox(15);
        lrcVBox.setPadding(new Insets(20,20,20,20));
        lrcVBox.setLayoutX(250);
        lrcVBox.setLayoutY(0);

        //6.??????????????????
        Label lab3 = new Label("????????????");
        lab3.setPrefWidth(80);
        lab3.setPrefHeight(25);
        lab3.setTextFill(Color.WHITE);
        lab3.setAlignment(Pos.CENTER);
        lab3.setBackground(new Background(new BackgroundFill(Color.rgb(180,0,0),null,null)));
        lab3.setLayoutX(30);
        lab3.setLayoutY(275);


        //7.????????????
        Image image = new Image("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\center\\pan_default.jpg");
        //??????"???????????????"
        PixelReader pr = image.getPixelReader();
        //????????????WritableImage
        WritableImage wImage = new WritableImage(
                800,
//                (int)image.getWidth(),
//                (int)image.getHeight()
                500
        );
        //????????????"???????????????"
        PixelWriter pixelWriter = wImage.getPixelWriter();
        //????????????image??????????????????
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = pr.getColor(i, j);
                for (int k = 0; k < 4; k++) {//????????????
                    color = color.darker();//????????????????????????
                }
                pixelWriter.setColor(i,j,color);
            }
        }

        backImageView = new ImageView(wImage);
        backImageView.setLayoutX(0);
        backImageView.setLayoutY(0);
        backImageView.setFitWidth(300);
        backImageView.setFitHeight(300);

        //????????????
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(63);

        backImageView.setEffect(gaussianBlur);

        //???????????????Label
        Label labLine = new Label();
        labLine.setMinHeight(0);
        labLine.setPrefHeight(2);
        labLine.setBackground(new Background(new BackgroundFill(Color.rgb(180,0,0),null,null)));
        labLine.setLayoutX(0);
        labLine.setLayoutY(lab3.getLayoutY() + lab3.getPrefHeight());

        //AnchorPane
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        anchorPane.getChildren().addAll(backImageView,lab1,labGroupName,lab2,lrcVBox,lab3,labLine);
        //?????????ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0,0,0,0));
        scrollPane.setContent(anchorPane);
        scrollPane.setPrefHeight(304);
        scrollPane.setMouseTransparent(true);//???ScrollPane?????????????????????

        anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());
        anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
        backImageView.fitWidthProperty().bind(scrollPane.widthProperty());
        backImageView.fitHeightProperty().bind(scrollPane.heightProperty());
        labLine.prefWidthProperty().bind(scrollPane.widthProperty());

        //*******************************????????????***********************************************//
        //*******************************?????????????????????******************************************//
        tableView = new TableView<>();
        tableView.setPrefWidth(960);
        tableView.getStylesheets().add("/css/playTable.css");

        TableColumn c1 = new TableColumn("??????");
        c1.setPrefWidth(80);
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn c2 = new TableColumn("????????????");
        c2.setPrefWidth(300);
        c2.setCellValueFactory(new PropertyValueFactory<>("soundName"));

        TableColumn c3 = new TableColumn("??????");
        c3.setPrefWidth(150);
        c3.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn c4 = new TableColumn("??????");
        c4.setPrefWidth(150);
        c4.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn c5 = new TableColumn("??????");
        c5.setPrefWidth(100);
        c5.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn c6 = new TableColumn("??????");
        c6.setPrefWidth(100);
        c6.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn c7 = new TableColumn("??????");
        c7.setPrefWidth(80);
        c7.setCellValueFactory(new PropertyValueFactory<>("labDelete"));



        tableView.getColumns().addAll(c1,c2,c3,c4,c5,c6,c7);


        tableView.setRowFactory(tv -> {
            TableRow<PlayBean> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                //????????????
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    //1.????????????????????????
                    this.currentIndex = row.getIndex();
                    //2.?????????????????????0
                    this.prevSecond = 0;
                    //3.?????????????????????????????????????????????????????????
                    if (this.currentPlayBean != null) {
                        this.currentPlayBean.getMediaPlayer().stop();
                    }
                    //4.???????????????PlayBean
                    this.currentPlayBean = row.getItem();
                    //5.??????
                    play();
                }
            });
            return row;
        });

        //**************************************??????BorderPane********************************//
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(scrollPane);
        borderPane.setCenter(tableView);
        //???"?????????"????????????????????????????????????,?????????????????????
        c7.prefWidthProperty().bind(borderPane.widthProperty());
        return borderPane;
    }

    //??????
    private void play() {
        //????????????
        loadLrc();
        //1.???????????????
        this.labTotalTime.setText(this.currentPlayBean.getTime());

        //???????????????????????????
        this.sliderSong.setMax(this.currentPlayBean.getTotalSeconds());
        this.sliderSong.setMajorTickUnit(1);//????????????1???
        this.sliderSong.setValue(0);
        prevSecond = 0;

        //??????????????????
        this.currentPlayBean.getMediaPlayer().setVolume(this.volumeProgress.getProgress());
        //2.????????????
        new Thread(){
            @Override
            public void run() {
                currentPlayBean.getMediaPlayer().play();
            }
        }.start();

        //3.????????????
        if (this.currentPlayBean.getImage() != null) {
            this.panImageView.setImage(this.currentPlayBean.getImage());
        }else{
            this.panImageView.setImage(new Image("File:src/main/java/img/center/pan_default.jpg"));
        }
        //4.????????????
        this.timeline.stop();
        this.timeline.play();

        //5.????????????
        WritableImage wImage = this.currentPlayBean.getImage();
        if (wImage != null) {
            //??????
            WritableImage newWritableImage = new WritableImage(
                    (int) wImage.getWidth(),
                    (int) wImage.getHeight()
            );
            PixelReader pr = wImage.getPixelReader();
            PixelWriter pw = newWritableImage.getPixelWriter();
            for (int i = 0; i < wImage.getHeight(); i++) {
                for (int j = 0; j < wImage.getWidth(); j++) {
                    Color color = pr.getColor(i, j);
                    //????????????
                    for (int k = 0; k < 4; k++) {
                        color = color.darker();
                    }
                    //??????
                    pw.setColor(i,j,color);

                }

            }
            this.backImageView.setImage(newWritableImage);
        }else{
            Image img = new Image("File:src/main/java/img/center/pan_default.jpg");
            PixelReader pr = img.getPixelReader();
            WritableImage writableImage = new WritableImage(
                    (int) img.getWidth(),
                    (int) img.getHeight()
            );
            PixelWriter pw = writableImage.getPixelWriter();
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    Color color = pr.getColor(i, j);
                    //????????????
                    for (int k = 0; k < 4; k++) {
                        color = color.darker();
                    }
                    //??????
                    pw.setColor(i,j,color);

                }

            }
            this.backImageView.setImage(writableImage);
        }

        //?????????????????????????????????
        this.butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PauseDark.png"));
        this.labPlay.setOnMouseEntered(e-> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/Pause.png")));
        this.labPlay.setOnMouseExited(e-> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PauseDark.png")));



    }

    //??????????????????????????????lrc??????(????????????)
    private void loadLrc() {
        if (this.currentPlayBean == null) {
            return;
        }

        //?????????lrcVBox
        this.lrcVBox.getChildren().clear();
        this.lrcVBox.setLayoutY(50 * 2 - 10);
        this.lrcList.clear();
        this.currentLrcIndex = 0;

        //??????MP3??????
        File mp3File = new File(this.currentPlayBean.getFilePath());
        //??????????????????
        // File lrcFile = new File(mp3File.getParent(),mp3File.getName().substring(0,mp3File.getName().indexOf(".")) + ".lrc");
        String path = mp3File.getPath();

        // ?????????????????????????????????
        List<SoundBean> playPath = sdi.findPlayPath(path);
        String strlrcPath = null;
        if(playPath.size() != 0){
            strlrcPath = playPath.get(0).getLrcPath();
        }

        // ??????????????? File??????
        File lrcFile = new File(strlrcPath);

//        File lrcFile = new File(path.substring(0,path.lastIndexOf("."))+ ".lrc");
        if (!lrcFile.exists()) {
            return;
        }

        //??????????????????????????????Label
        try {
            BufferedReader bufIn = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile),"utf-8"));
            String row = null;
            while((row = bufIn.readLine()) != null){
                if (row.indexOf("[") == -1 || row.indexOf("]") == -1) {
                    continue;
                }
                if (row.charAt(1) < '0' || row.charAt(1) > '9') {
                    continue;
                }

                String strTime = row.substring(1,row.indexOf("]"));//00:03.29
                String strMinute = strTime.substring(0, strTime.indexOf(":"));//???????????????
                String strSecond = strTime.substring(strTime.indexOf(":") + 1);//?????????????????????
                //?????????int??????
                int intMinute = Integer.parseInt(strMinute);
                //?????????????????????
                BigDecimal totalMilli = new BigDecimal(intMinute * 60).add(new BigDecimal(strSecond)).multiply(new BigDecimal("1000"));
                this.lrcList.add(totalMilli);

                //????????????Label
                Label lab = new Label(row.trim().substring(row.indexOf("]") + 1));
                lab.setMinWidth(550);
                lab.setMinHeight(35);
                lab.setMaxHeight(35);

                lab.setPrefWidth(550);
                lab.setPrefHeight(35);
                lab.setTextFill(Color.rgb(53,53,53));
                lab.setFont(new Font("??????",18));
                lab.setAlignment(Pos.CENTER);

                //???????????????????????????????????????????????????30????????????
                if (this.lrcVBox.getChildren().size() == 0) {
                    lab.setTextFill(Color.YELLOW);
                    lab.setFont(new Font("????????????",30));
                }
                //????????????????????????
                if (this.lrcVBox.getChildren().size() == 1) {
                    lab.setTextFill(Color.WHITE);
                }
                //?????????Label?????????lrcVBox???
                this.lrcVBox.getChildren().add(lab);
            }
            //??????????????????"?????????????????????"
            if(this.currentPlayBean.getMediaPlayer().getTotalDuration().toMillis() -
                    this.lrcList.get(this.lrcList.size() - 1).doubleValue() > 0){
                Label lab = new Label("?????????????????????");
                lab.setMinWidth(550);
                lab.setMinHeight(35);
                lab.setMaxHeight(35);

                lab.setPrefWidth(550);
                lab.setPrefHeight(35);
                lab.setTextFill(Color.rgb(255,0,0));
                lab.setFont(new Font("??????",26));
                lab.setAlignment(Pos.CENTER);
                this.lrcVBox.getChildren().add(lab);
                this.lrcList.add(new BigDecimal(this.currentPlayBean.getMediaPlayer().getTotalDuration().toMillis()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //??????????????????
    private BorderPane getBottomPane() {
        //*************?????????????????????***********************************//
        //1.?????????
        ImageView v1 = new ImageView("File:src/main/java/img/topandbottom/LastDark.png");
        v1.setFitHeight(40);
        v1.setFitWidth(40);
        Label lab1 = new Label("", v1);
        lab1.setOnMouseEntered(e -> v1.setImage(new Image("File:src/main/java/img/topandbottom/Last.png")));
        lab1.setOnMouseExited(e -> v1.setImage(new Image("File:src/main/java/img/topandbottom/LastDark.png")));
        lab1.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            //?????????????????????
            this.timeline.stop();
            //??????????????????-1
            this.currentIndex--;
            if (currentIndex < 0) {
                if (this.playMode == 1) {//????????????
                    this.currentIndex = this.tableView.getItems().size() - 1;//????????????????????????
                }else{
                    this.currentIndex = 0;
                }
            }
            //??????Table?????????
            this.tableView.getSelectionModel().select(currentIndex);
            //????????????PlayBean??????
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //????????????
            play();
        });

        //2.????????????
        butPlayImage = new ImageView("File:src/main/java/img/topandbottom/PlayDark.png");
        butPlayImage.setFitWidth(40);
        butPlayImage.setFitHeight(40);
        labPlay = new Label("", butPlayImage);
        labPlay.setOnMouseEntered(e -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/Play.png")));
        labPlay.setOnMouseExited(e -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PlayDark.png")));
        labPlay.setOnMouseClicked(e -> {
            //???????????????????????????????????????
            if (this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                //?????????????????????
                this.currentPlayBean.getMediaPlayer().pause();
                //????????????????????????????????????
                butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PlayDark.png"));
                labPlay.setOnMouseEntered(ee -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/Play.png")));
                labPlay.setOnMouseExited(ee -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PlayDark.png")));
                //?????????????????????
                this.timeline.pause();
            }else if(this.currentPlayBean.getMediaPlayer().getStatus() == MediaPlayer.Status.PAUSED){
                this.currentPlayBean.getMediaPlayer().play();
                this.timeline.play();
                butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PauseDark.png"));
                labPlay.setOnMouseEntered(ee -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/Pause.png")));
                labPlay.setOnMouseExited(ee -> butPlayImage.setImage(new Image("File:src/main/java/img/topandbottom/PauseDark.png")));
            }
        });

        //3.?????????
        ImageView v3 = new ImageView("File:src/main/java/img/topandbottom/NextDark.png");
        v3.setFitWidth(40);
        v3.setFitHeight(40);
        Label lab3 = new Label("", v3);
        lab3.setOnMouseEntered(e -> v3.setImage(new Image("File:src/main/java/img/topandbottom/Next.png")));
        lab3.setOnMouseExited(e -> v3.setImage(new Image("File:src/main/java/img/topandbottom/NextDark.png")));
        lab3.setOnMouseClicked(e -> {
            if (this.currentPlayBean != null) {
                this.currentPlayBean.getMediaPlayer().stop();
            }
            //?????????????????????
            this.timeline.stop();
            //1 : ???????????????2. ????????????  3.????????????

            if (this.playMode==1 || this.playMode ==2){  //??????????????? ???????????? ?????? ???????????? ????????????
                this.currentIndex+=1;
            }
            if (this.playMode == 3){ // ??????????????? ???????????? ???????????????
                this.currentIndex +=0;
            }


            if (currentIndex >= this.tableView.getItems().size()) {
                if (this.playMode == 1) {//????????????
                    this.currentIndex = 0;//?????????????????????
                }else{
                    this.currentIndex = this.tableView.getItems().size() - 1;
                }
            }
            //??????Table?????????
            this.tableView.getSelectionModel().select(currentIndex);
            //????????????PlayBean??????
            this.currentPlayBean = this.tableView.getItems().get(currentIndex);
            //????????????
            play();
        });

        HBox hBox1 = new HBox(30);
        hBox1.setPrefWidth(255);
        hBox1.setPadding(new Insets(5, 10, 5, 30));
        hBox1.getChildren().addAll(lab1, labPlay, lab3);

        //*************************?????????????????????**********************************//
        //1.?????????????????????
        labPlayTime = new Label("00:00");
        labPlayTime.setPrefHeight(50);
        labPlayTime.setPrefWidth(40);
        labPlayTime.setTextFill(Color.WHITE);
        //2.?????????
        sliderSong = new Slider();
        sliderSong.setMinWidth(0);
        sliderSong.setMinHeight(0);
        sliderSong.setPrefWidth(300);
        sliderSong.setPrefHeight(12);
        sliderSong.getStylesheets().add("css/TopAndBottomPage.css");


        //?????????
        ProgressBar pb1 = new ProgressBar(0);
        pb1.setProgress(0);
        pb1.setMinWidth(0);
        pb1.setMinHeight(0);

        pb1.setMaxWidth(5000);
        pb1.setPrefWidth(300);
        pb1.getStylesheets().add("css/TopAndBottomPage.css");

        //Slider??????????????????..
        sliderSong.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //???????????????
                if (currentPlayBean != null) {
                    pb1.setProgress((newValue.doubleValue() + 1) / currentPlayBean.getTotalSeconds());
                }
            }
        });

        //Slider????????????????????????
        sliderSong.setOnMouseReleased(e -> {
            if (currentPlayBean != null) {
                Duration duration = new Duration(sliderSong.getValue() * 1000);
                currentPlayBean.getMediaPlayer().seek(duration);//????????????????????????

                //????????????Label
                Date date = new Date();
                date.setTime((long) currentPlayBean.getMediaPlayer().getCurrentTime().toMillis());
                labPlayTime.setText(new SimpleDateFormat("mm:ss").format(date));
                //???????????????
                prevSecond = (int)duration.toSeconds() - 1;
            }
        });


        //??????StackPane??????????????????????????????
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pb1,sliderSong );

        //3.???????????????
        labTotalTime = new Label("00:00");
        labTotalTime.setPrefWidth(40);
        labTotalTime.setPrefHeight(50);
        labTotalTime.setTextFill(Color.WHITE);
        labTotalTime.setAlignment(Pos.CENTER_RIGHT);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(labPlayTime);
        borderPane.setCenter(stackPane);
        borderPane.setRight(labTotalTime);
        borderPane.setPrefHeight(50);

        labPlayTime.prefHeightProperty().bind(borderPane.prefHeightProperty());
        sliderSong.prefHeightProperty().bind(borderPane.prefHeightProperty());
        labTotalTime.prefHeightProperty().bind(borderPane.prefHeightProperty());


        //************************?????????????????????************************************//
        //1.????????????
        ImageView v5 = new ImageView("File:src/main/java/img/topandbottom/VolumnDark.png");
        v5.setFitWidth(17);
        v5.setFitHeight(17);
        Label lab5 = new Label("",v5);
        lab5.setOnMouseEntered(e -> v5.setImage(new Image("File:src/main/java/img/topandbottom/Volumn.png")));
        lab5.setOnMouseExited(e -> v5.setImage(new Image("File:src/main/java/img/topandbottom/VolumnDark.png")));
        lab5.setOnMouseClicked(e ->{
            if (this.currentPlayBean != null) {
                //?????????????????????
                if (this.currentPlayBean.getMediaPlayer().getVolume() != 0) {//???????????????
                    //??????????????????????????????
                    this.prevVolumn = this.currentPlayBean.getMediaPlayer().getVolume();
                    //??????????????????
                    this.currentPlayBean.getMediaPlayer().setVolume(0);
                    //????????????
                    v5.setImage(new Image("File:src/main/java/img/left/volumnZero_1_Dark.png"));
                    lab5.setOnMouseEntered(ee-> v5.setImage(new Image("File:src/main/java/img/left/volumnZero_1.png")));
                    lab5.setOnMouseExited(ee-> v5.setImage(new Image("File:src/main/java/img/left/volumnZero_1_Dark.png")));

                    //?????????????????????
                    this.sldVolume.setValue(0);
                }else{//?????????????????????
                    //???????????????
                    this.currentPlayBean.getMediaPlayer().setVolume(this.prevVolumn);

                    //????????????
                    v5.setImage(new Image("File:src/main/java/img/topandbottom/VolumnDark.png"));
                    lab5.setOnMouseEntered(ee-> v5.setImage(new Image("File:src/main/java/img/topandbottom/Volumn.png")));
                    lab5.setOnMouseExited(ee-> v5.setImage(new Image("File:src/main/java/img/topandbottom/VolumnDark.png")));

                    //?????????????????????
                    this.sldVolume.setValue(this.prevVolumn * 100);
                }
            }
        });

        //2.???????????????
        sldVolume = new Slider();
        sldVolume.setMax(100);
        sldVolume.setValue(50);
        sldVolume.setMajorTickUnit(1);//????????????????????????????????????

        sldVolume.setMinHeight(0);
//        sldVolume.setPrefHeight(10);
        sldVolume.setPrefWidth(100);
        sldVolume.getStylesheets().add("css/TopAndBottomPage.css");

        //?????????
        volumeProgress = new ProgressBar(0);
        volumeProgress.setMinHeight(0);
        volumeProgress.setProgress(0.5);//?????????????????????
        volumeProgress.setPrefWidth(100);
        volumeProgress.setPrefHeight(10);
        volumeProgress.prefWidthProperty().bind(sldVolume.prefWidthProperty());
        volumeProgress.getStylesheets().add("css/TopAndBottomPage.css");



        //????????????????????????????????????
        sldVolume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //1.?????????????????????
                volumeProgress.setProgress(sldVolume.getValue() / 100);
                //2.????????????
                if (currentPlayBean != null) {
                    currentPlayBean.getMediaPlayer().setVolume(volumeProgress.getProgress());
                }
            }
        });
        StackPane sp2 = new StackPane();
        sp2.getChildren().addAll(volumeProgress, sldVolume);

        //3.??????????????????
        ImageView v6 = new ImageView("File:src/main/java/img/topandbottom/RepeatDark.png");
        v6.setFitWidth(25);
        v6.setFitHeight(25);
        Label lab6 = new Label("", v6);
        lab6.setOnMouseClicked(e -> {
            //???????????????playMode????????????????????????????????????
            this.playMode++;
            if (this.playMode > 3) {
                this.playMode = 1;
            }
            switch (this.playMode) {
                case 1:
                    v6.setImage(new Image("File:src/main/java/img/topandbottom/RepeatDark.png"));
                    lab6.setOnMouseEntered(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/Repeat.png")));
                    lab6.setOnMouseExited(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/RepeatDark.png")));
                    break;
                case 2:
                    v6.setImage(new Image("File:src/main/java/img/topandbottom/OrderPlayDark.png"));
                    lab6.setOnMouseEntered(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/OrderPlay.png")));
                    lab6.setOnMouseExited(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/OrderPlayDark.png")));
                    break;
                case 3:
                    v6.setImage(new Image("File:src/main/java/img/topandbottom/RepeatInOneDark.png"));
                    lab6.setOnMouseEntered(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/RepeatInOne.png")));
                    lab6.setOnMouseExited(ee -> v6.setImage(new Image("File:src/main/java/img/topandbottom/RepeatInOneDark.png")));
                    break;
            }

        });



        //4.????????????
        ImageView v7 = new ImageView("File:src/main/java/img/topandbottom/ciDark.png");
        v7.setFitWidth(25);
        v7.setFitHeight(25);
        Label lab7 = new Label("", v7);

        //5.????????????
        ImageView v8 = new ImageView("File:src/main/java/img/topandbottom/right_drag.png");
        v8.setFitWidth(30);
        v8.setFitHeight(50);
        Label lab8 = new Label("", v8);
        //??????????????????
        lab8.setOnMousePressed(e -> {
            //??????????????????????????????X,Y??????
            xOffset = e.getScreenX();
            yOffset = e.getScreenY();
        });
        //??????????????????
        lab8.setOnMouseMoved(e -> {
            if(e.getY() > 34 && e.getY() < 50 &&
                    e.getX() > 0 && e.getX() < 30){
                //?????????????????????
                lab8.setCursor(Cursor.NW_RESIZE);
            }else{
                lab8.setCursor(Cursor.DEFAULT);
            }
        });
        //??????????????????
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
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(0, 0, 0, 10));
        hBox.setPrefWidth(270);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(lab5,sp2,lab6,lab7,lab8);

        //**********************??????BorderPane***********************************//
        BorderPane bottomPane = new BorderPane();
        bottomPane.setLeft(hBox1);
        bottomPane.setCenter(borderPane);
        bottomPane.setRight(hBox);
        return bottomPane;
    }

    //????????????????????????
    private BorderPane getLeftPane() {
        //1.????????????
        ImageView v1 = new ImageView("File:src\\main\\java\\img\\topandbottom\\keqing.jpeg");
        v1.setFitWidth(255);
        v1.setPreserveRatio(true);//???????????????????????????????????????

        //2.?????????Label
        Label labAuthor = new Label("?????????");
        labAuthor.setPrefWidth(255);
        labAuthor.setTextFill(Color.YELLOW);//????????????
        labAuthor.setFont(new Font("??????", 18));//????????????
        labAuthor.setAlignment(Pos.CENTER);

        //3.?????????Label
        Label labDate = new Label("???????????????????????????");
        labDate.setPrefWidth(255);
        labDate.setTextFill(Color.YELLOW);
        labDate.setFont(new Font("??????", 18));
        labDate.setAlignment(Pos.CENTER);

        //4.??????????????????Lable
        Label labGd = new Label("???????????????");
        labGd.setPrefWidth(220);
        labGd.setPrefHeight(20);
        labGd.setTextFill(Color.rgb(160,160,160));

        //5.+?????????ImageView
        ImageView v2 = new ImageView("File:src/main/java/img/left/create_2_Dark.png");
        v2.setFitWidth(15);
        v2.setPreserveRatio(true);

        Label lab = new Label("", v2);
        lab.setPrefWidth(15);
        lab.setPrefHeight(15);
        lab.setOnMouseEntered(e -> v2.setImage(new Image("File:src/main/java/img/left/create_2.png")));
        lab.setOnMouseExited(e -> v2.setImage(new Image("File:src/main/java/img/left/create_2_Dark.png")));
        lab.setOnMouseClicked(e -> {
            //????????????????????????
            new AddGroup(staticStage, groupVBox, this);
        });

        //??????4???5????????????HBox(????????????)
        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(labGd, lab);

        //???1,2,3???(4,5)HBox???????????????VBox???
        VBox vBox = new VBox(15);//15???????????????????????????
        vBox.setPrefWidth(255);
        vBox.setPrefHeight(300);
        vBox.setPadding(new Insets(5, 5, 5, 10));
        vBox.getChildren().addAll(v1, labAuthor, labDate, hBox);


        //*********************??????????????????????????????*******************************************//
        List<String> groupList = XMLUtils.readAllGroup();
        //?????????"????????????"???????????????"HBox"??????
        List<HBox> hBoxList = new ArrayList<>();
        for (String groupName : groupList) {
            //1.???????????????ImageView
            ImageView iv1 = new ImageView("File:src/main/java/img/left/xinyuanDark.png");
            iv1.setFitWidth(15);
            iv1.setPreserveRatio(true);
            Label lab1 = new Label("", iv1);
            lab1.setMinWidth(0);
            lab1.setMinHeight(0);
            lab1.setPrefWidth(15);
            lab1.setPrefHeight(15);
            lab1.setOnMouseEntered(e -> iv1.setImage(new Image("File:src/main/java/img/left/xinyuan.png")));
            lab1.setOnMouseExited(e -> iv1.setImage(new Image("File:src/main/java/img/left/xinyuanDark.png")));


            //2.???????????????Label
            Label labGroupName = new Label(groupName);
            labGroupName.setMinHeight(0);
            labGroupName.setPrefHeight(15);
            labGroupName.setPrefWidth(150);
            labGroupName.setTextFill(Color.rgb(210,210,210));
            labGroupName.setOnMouseEntered(e -> labGroupName.setTextFill(Color.WHITE));
            labGroupName.setOnMouseExited(e -> labGroupName.setTextFill(Color.rgb(210,210,210)));


            //3.???????????????ImageView
            ImageView iv2 = new ImageView("File:src/main/java/img/left/volumn_1_Dark.png");
            iv2.setFitWidth(15);
            iv2.setFitHeight(15);
            Label lab2 = new Label("", iv2);
            lab2.setMinWidth(0);
            lab2.setMinHeight(0);
            lab2.setPrefWidth(15);
            lab2.setPrefHeight(15);
            lab2.setOnMouseEntered(e -> iv2.setImage(new Image("File:src/main/java/img/left/volumn_1.png")));
            lab2.setOnMouseExited(e -> iv2.setImage(new Image("File:src/main/java/img/left/volumn_1_Dark.png")));
            lab2.setOnMouseClicked(e -> {
                //1.??????"????????????"
                this.labGroupName.setText(labGroupName.getText().trim());
                readAllSoundByGroup();

            });

            //4.+?????????ImageView
            ImageView iv3 = new ImageView("File:src/main/java/img/left/addDark.png");
            iv3.setFitWidth(15);
            iv3.setFitHeight(15);
            Label lab3 = new Label("", iv3);
            lab3.setMinWidth(0);
            lab3.setMinHeight(0);
            lab3.setPrefWidth(15);
            lab3.setPrefHeight(15);
            lab3.setOnMouseEntered(e -> iv3.setImage(new Image("File:src/main/java/img/left/add.png")));
            lab3.setOnMouseExited(e -> iv3.setImage(new Image("File:src/main/java/img/left/addDark.png")));
            lab3.setOnMouseClicked(e -> {
                //??????"?????????????????????"
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("??????????????????");
                //????????????
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                        new FileChooser.ExtensionFilter("flac","*.flac"),
                        new FileChooser.ExtensionFilter("????????????","*.*")
                );
                List<File> files = fileChooser.showOpenMultipleDialog(staticStage);
                if (files != null && files.size() > 0) {
                    //?????????????????????????????????????????????xml?????????
                    System.out.println(labGroupName.getText().trim());
                    XMLUtils.insertSounds(labGroupName.getText().trim(),files);
                }

            });

            //5.??????????????????ImageView
            ImageView iv4 = new ImageView("File:src/main/java/img/left/laji_1_Dark.png");
            iv4.setFitWidth(15);
            iv4.setFitHeight(15);
            Label lab4 = new Label("", iv4);
            lab4.setMinWidth(0);
            lab4.setMinHeight(0);
            lab4.setPrefWidth(15);
            lab4.setPrefHeight(15);
            lab4.setOnMouseEntered(e -> iv4.setImage(new Image("File:src/main/java/img/left/laji_1.png")));
            lab4.setOnMouseExited(e -> iv4.setImage(new Image("File:src/main/java/img/left/laji_1_Dark.png")));


            HBox hBox1 = new HBox(10);
            hBox1.getChildren().addAll(lab1, labGroupName, lab2, lab3, lab4);
            hBox1.setPadding(new Insets(5,5,5,10));

            hBoxList.add(hBox1);

            lab4.setOnMouseClicked(e -> {
                //1.????????????
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("????????????");
                alert.setHeaderText("???????????????????????????" + labGroupName.getText().trim() + "?????????");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    //??????XMLUtils????????????
                    XMLUtils.deleteGroup(labGroupName.getText().trim());

                    //???VBox?????????
                    this.groupVBox.getChildren().remove(hBox1);
                }
            });

        }

        groupVBox = new VBox(10);
        groupVBox.setPrefWidth(255);
        groupVBox.setPadding(new Insets(10,0,0,15));
        for (HBox hb : hBoxList) {
            groupVBox.getChildren().add(hb);
        }

        //?????????
        BorderPane leftPane = new BorderPane();
        leftPane.setTop(vBox);
        leftPane.setCenter(groupVBox);
        return leftPane;
    }
    //?????????????????????????????????v
    private void readAllSoundByGroup() {
        //1.???????????????????????????????????????
        List<SoundBean> soundList = XMLUtils.findSoundByGroupName(this.labGroupName.getText().trim());
        // 1??????????????????????????????????????????
//        List<SoundBean> soundList = sdi.findAll();


        //2.?????????????????????????????????PlayBean
        List<PlayBean> playBeanList = new ArrayList<>();
        for (int i = 0; i < soundList.size(); i++) {
            SoundBean soundBean = soundList.get(i);
            PlayBean playBean = new PlayBean();
            playBean.setId(i + 1);

            //??????????????????
            File file = new File(soundBean.getFilePath());
            //????????????
            MP3File mp3File = null;
            try {
                mp3File = new MP3File(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }
            //??????MP3??????????????????
            MP3AudioHeader audioHeader = (MP3AudioHeader)mp3File.getAudioHeader();
            //?????????????????????????????????
            String strLength = audioHeader.getTrackLengthAsString();
            //?????????int???????????????
            int intLength = audioHeader.getTrackLength();

            Set<String> keySet = mp3File.getID3v2Tag().frameMap.keySet();
            String songName = null;//??????
            String artist = null;//?????????
            String album = null;//????????????

            if(keySet.contains("TIT2")){
                songName = mp3File.getID3v2Tag().frameMap.get("TIT2").toString();
            }
            if(keySet.contains("TPE1")){
                artist = mp3File.getID3v2Tag().frameMap.get("TPE1").toString();
            }
            if(keySet.contains("TALB")){
                album = mp3File.getID3v2Tag().frameMap.get("TALB").toString();
            }
            System.out.println("?????????" + songName + " ????????????" + artist + " ???????????????" + album);
            if (songName != null && !songName.equals("null")) {
                songName = songName.substring(songName.indexOf("\"") + 1,songName.lastIndexOf("\""));
            }
            if (artist != null && !artist.equals("null")) {
                artist = artist.substring(artist.indexOf("\"") + 1, artist.lastIndexOf("\""));

            }
            if (album != null && !album.equals("null")) {
                album = album.substring(album.indexOf("\"") + 1,album.lastIndexOf("\""));
            }

            //???PlayBean??????
            playBean.setSoundName(songName);
            playBean.setArtist(artist);
            playBean.setAlbum(album);
            playBean.setFilePath(soundBean.getFilePath());

            URI uri = file.toURI();
            Media media = new Media(uri.toString()) ;
            MediaPlayer mp = new MediaPlayer(media);

            //?????????????????????????????????
            mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable,
                                    Duration oldValue, Duration newValue) {
                    //???????????????????????????????????????????????????????????????100??????????????????

                    //1.?????????????????????????????????????????????newValue??????"???"
                    int currentSecond = (int)newValue.toSeconds();

                    //2.??????????????????????????????
                    if (currentSecond == prevSecond + 1) {
                        //???????????????
                        sliderSong.setValue(sliderSong.getValue() + 1);
                        //???????????????
                        prevSecond++;
                        //????????????????????????
                        Date date = new Date();
                        date.setTime((int)sliderSong.getValue()*1000);
                        labPlayTime.setText(new SimpleDateFormat("mm:ss").format(date));

                    }

                    //????????????
                    //1.???????????????????????????
                    double millis = newValue.toMillis();

                    //2.??????????????????????????????????????????
                    double min = 0;
                    double max = 0;
//                    if (currentLrcIndex == 0) {
//                        min = 0;
//                    }else{
//                        min = lrcList.get(currentLrcIndex).doubleValue();
//                    }
//                    if (currentLrcIndex != lrcList.size() - 1) {
//                        max = lrcList.get(currentLrcIndex + 1).doubleValue();
//                    }else{
//                        max = lrcList.get(currentLrcIndex).doubleValue();
//                    }
                    //??????????????????????????????
                    if (millis >= min && millis < max) {
                        return;
                    }

                    if (currentLrcIndex < lrcList.size() - 1 &&
                            millis >= lrcList.get(currentLrcIndex + 1).doubleValue()) {
                        currentLrcIndex++;//??????????????????????????????
                        //??????
                        //???????????????
                        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(15),//??????15??????????????????
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {//????????????????????????????????????
                                        lrcVBox.setLayoutY(lrcVBox.getLayoutY() - 1);
                                    }
                                }
                        ));
                        t1.setCycleCount(50);//??????50???
                        t1.play();

                        //??????????????????????????????30
                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.YELLOW);
                        //?????????30(??????)
                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("??????",startSize++));
                                    }
                                }
                        ));
                        t2.setCycleCount(12);
                        t2.play();

                        //????????????????????????????????????
                        Label lab_Pre_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                        if (lab_Pre_1 != null) {

                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 30;
                                        @Override
                                        public void handle(ActionEvent event) {
                                            lab_Pre_1.setFont(new Font("??????",startSize--));
                                        }
                                    }
                            ));
                            t3.setCycleCount(12);
                            t3.play();
                            t3.setOnFinished(e -> lab_Pre_1.setTextFill(Color.rgb(114,114,114)));

                        }

                        //?????????
                        if (currentLrcIndex - 2 >= 0) {
                            Label lab_Pre_2 = (Label) lrcVBox.getChildren().get(currentLrcIndex - 2);
                            lab_Pre_2.setTextFill(Color.rgb(53,53,53));
                        }

                        //??????????????????????????????
                        if (currentLrcIndex + 1 < lrcList.size()) {
                            Label lab_next_1 = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab_next_1.setTextFill(Color.WHITE);
                        }
                    } else if (currentLrcIndex > 0 && millis < lrcList.get(currentLrcIndex).doubleValue()) {
                        //???????????????????????????
                        currentLrcIndex--;
                        //??????VBox?????????
                        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(15),//??????15??????????????????
                                new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {//????????????????????????????????????
                                        lrcVBox.setLayoutY(lrcVBox.getLayoutY() + 1);
                                    }
                                }
                        ));
                        t1.setCycleCount(50);//??????50???
                        t1.play();

                        //??????????????????????????????30
                        Label lab_current = (Label) lrcVBox.getChildren().get(currentLrcIndex);
                        lab_current.setTextFill(Color.YELLOW);

                        //?????????30(??????)
                        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(30),
                                new EventHandler<ActionEvent>() {
                                    int startSize = 18;
                                    @Override
                                    public void handle(ActionEvent event) {
                                        lab_current.setFont(new Font("??????",startSize++));
                                    }
                                }
                        ));
                        t2.setCycleCount(12);
                        t2.play();

                        //????????????????????????
                        if (currentLrcIndex - 1 >= 0) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex - 1);
                            lab.setTextFill(Color.rgb(114,114,114));
                        }
                        //?????????????????????????????????18
                        if (currentLrcIndex + 1 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 1);
                            lab.setTextFill(Color.WHITE);
                            //??????
                            Timeline t3 = new Timeline(new KeyFrame(Duration.millis(30),
                                    new EventHandler<ActionEvent>() {
                                        int startSize = 30;
                                        @Override
                                        public void handle(ActionEvent event) {
                                            lab.setFont(new Font("??????",startSize--));
                                        }
                                    }
                            ));
                            t3.setCycleCount(12);
                            t3.play();
                        }
                        //????????????????????????
                        if (currentLrcIndex + 2 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 2);
                            lab.setTextFill(Color.rgb(114,114,114));
                        }
                        //????????????????????????
                        if (currentLrcIndex + 3 < lrcVBox.getChildren().size()) {
                            Label lab = (Label) lrcVBox.getChildren().get(currentLrcIndex + 3);
                            lab.setTextFill(Color.rgb(53,53,53));
                        }
                    }


                }
            });
            //?????????????????????
            mp.setOnEndOfMedia(()->{
                //1.??????????????????????????????
                this.currentPlayBean.getMediaPlayer().stop();
                //2.?????????????????????
                this.timeline.stop();
                //?????????????????????
                this.lrcVBox.getChildren().clear();
                this.lrcVBox.setLayoutY(50 * 2 - 10);
                this.lrcList.clear();
                this.currentLrcIndex = 0;

                //?????????????????????????????????????????????
                switch (this.playMode) {
                    case 1://????????????
                        this.currentIndex++;
                        if (this.currentIndex >= this.tableView.getItems().size()) {
                            currentIndex = 0;
                        }
                        this.currentPlayBean = tableView.getItems().get(this.currentIndex);

                        break;
                    case 2://??????????????????
                        this.currentIndex++;
                        if (currentIndex >= this.tableView.getItems().size()) {
                            return;
                        }
                        this.currentPlayBean = tableView.getItems().get(this.currentIndex);

                        break;
                    case 3://????????????
                        this.currentPlayBean.getMediaPlayer().seek(new Duration(0));
                        break;
                }
                this.tableView.getSelectionModel().select(currentIndex);
                play();
            });
            playBean.setMediaPlayer(mp);

            //??????????????????
            BigDecimal bigDecimal = new BigDecimal(file.length());//??????????????????????????????
            BigDecimal result = bigDecimal.divide(new BigDecimal(1024 * 1024),2, RoundingMode.HALF_UP);
            playBean.setLength(result.toString() + " M");//????????????????????????

            playBean.setTime(strLength);//???????????????
            playBean.setTotalSeconds(intLength);//?????????

            //??????????????????
            ImageView iv = new ImageView("File:src/main/java/img/left/laji_2_Dark.png");
            iv.setFitWidth(15);
            iv.setFitHeight(15);

            Label labDelete = new Label("",iv);
            labDelete.setOnMouseEntered(e -> iv.setImage(new Image("File:src/main/java/img/left/laji_2.png")));
            labDelete.setOnMouseExited(e -> iv.setImage(new Image("File:src/main/java/img/left/laji_2_Dark.png")));

            labDelete.setAlignment(Pos.CENTER);
            playBean.setLabDelete(labDelete);

            //????????????
            AbstractID3v2Tag tag = mp3File.getID3v2Tag();
            AbstractID3v2Frame frame = (AbstractID3v2Frame)tag.getFrame("APIC");
            if (frame != null) {
                FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
                byte[] imageData = body.getImageData();
                //????????????????????????Image??????
                java.awt.Image image = Toolkit.getDefaultToolkit().createImage(imageData, 0, imageData.length);
                BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);
                WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
                playBean.setImage(writableImage);
            }

            //???PlayBean??????????????????
            playBeanList.add(playBean);
        }

        //???PlayBeanList??????????????????????????????
        ObservableList<PlayBean> data = FXCollections.observableList(playBeanList);
        this.tableView.getItems().clear();//????????????
        this.tableView.setItems(data);
    }

    //??????????????????
    private BorderPane getTopPane() {
        //1.?????????Logo
        ImageView imgView = new ImageView("File:D:\\Java Program\\MeidoPlayerPro\\src\\main.java.img\\topandbottom\\mao.jpg");
        imgView.setFitHeight(40);//????????????????????????40??????
        imgView.setPreserveRatio(true);//????????????????????????????????????????????????

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);//?????????
        hBox.setPrefWidth(600);
        hBox.setPrefHeight(50);
        hBox.setMaxHeight(50);
        hBox.setPadding(new Insets(10,0,10,60));//????????????????????????????????????
        hBox.getChildren().add(imgView);


        //2.????????????????????????
        ImageView v1 = new ImageView(new Image("File:D:\\Java Program\\MusicProject\\src\\main\\java\\img\\topandbottom"));
        v1.setFitWidth(15);
        v1.setFitHeight(15);
        Label lab1 = new Label("", v1);
        lab1.setMinWidth(0);//??????Label???????????????
        lab1.setMinHeight(0);//??????Label???????????????
        lab1.setPrefWidth(15);
        lab1.setPrefHeight(15);

        //????????????????????????
        lab1.setOnMouseEntered(e -> v1.setImage(new Image("File:src/main/java//topandbottom/Minmize.png")));
        lab1.setOnMouseExited(e -> v1.setImage(new Image("File:src/main/java/img/topandbottom/MinmizeDark.png")));
        //??????????????????
        lab1.setOnMouseClicked(e -> staticStage.setIconified(true));//???"??????"?????????

        //3.????????????????????????
        ImageView v2 = new ImageView(new Image("File:D:\\Java Program\\MusicProject\\src\\main\\java\\img\\topandbottom"));
        v2.setFitWidth(15);
        v2.setFitHeight(15);
        Label lab2 = new Label("",v2);
        lab2.setMinWidth(0);
        lab2.setMinHeight(0);
        lab2.setPrefWidth(15);
        lab2.setPrefHeight(15);
        lab2.setOnMouseEntered(e -> v2.setImage(new Image("File:src/main/java/img/topandbottom/Maximize.png")));
        lab2.setOnMouseExited(e -> v2.setImage(new Image("File:src/main/java/img/topandbottom/MaximizeDark.png")));
        lab2.setOnMouseClicked(e -> {
            //??????????????????????????????????????????????????????
            if(!staticStage.isMaximized()){//????????????
                //?????????????????????x,y??????????????????????????????
                resetX = staticStage.getX();
                resetY = staticStage.getY();
                resetWidth = staticStage.getWidth();
                resetHeight = staticStage.getHeight();
                //?????????
                staticStage.setMaximized(true);

                //????????????
                v2.setImage(new Image("File:src/main/java//topandbottom/resetDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("File:src/main/java/img/topandbottom/reset.png")));
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("File:src/main/java/img/topandbottom/resetDark.png")));

            }else{
                //??????????????????????????????????????????????????????
                staticStage.setX(resetX);
                staticStage.setY(resetY);
                staticStage.setWidth(resetWidth);
                staticStage.setHeight(resetHeight);
                //??????????????????
                staticStage.setMaximized(false);

                //????????????
                v2.setImage(new Image("File:src/main/java//topandbottom/MaximizeDark.png"));
                lab2.setOnMouseEntered(ee -> v2.setImage(new Image("File:src/main/java/img/topandbottom/MaximizeDark.png")));
                lab2.setOnMouseExited(ee -> v2.setImage(new Image("File:src/main/java/img/topandbottom/Maximize.png")));
            }
        });


        //4.?????????????????????
        ImageView v3 = new ImageView(new Image("File:D:\\Java Program\\MusicProject\\src\\main\\java\\img\\topandbottom"));
        v3.setFitWidth(15);
        v3.setFitHeight(15);
        Label lab3 = new Label("", v3);
        lab3.setMinWidth(15);
        lab3.setMinHeight(15);
        lab3.setPrefWidth(15);
        lab3.setPrefHeight(15);
        lab3.setOnMouseEntered(e -> v3.setImage(new Image("File:src/main/java/img/topandbottom/Close.png")));
        lab3.setOnMouseExited(e -> v3.setImage(new Image("File:src/main/java/img/topandbottom/CloseDark.png")));
        lab3.setOnMouseClicked(e -> {
            //????????????????????????????????????????????????
            //????????????
            System.exit(0);//??????JVM
        });

        HBox hBox2 = new HBox(20);//??????????????????????????????10??????
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.setPrefWidth(150);
        hBox2.setPrefHeight(50);
        hBox2.getChildren().addAll(lab1,lab2,lab3);
        hBox2.setPadding(new Insets(5,0,0,40));

        //???????????????
        Rectangle rct = new Rectangle();
        rct.setX(0);
        rct.setY(0);
        rct.setWidth(100);
        rct.setHeight(2);
        //???????????????--??????
        Stop[] stops = new Stop[]{
                new Stop(0, Color.rgb(120, 8, 14)),
                new Stop(0.5, Color.RED),
                new Stop(1, Color.rgb(120, 8, 14))

        };

        rct.setFill(new LinearGradient(0,0,1,1,
                true, CycleMethod.NO_CYCLE,stops));

        BorderPane topPane = new BorderPane();
        topPane.setLeft(hBox);
        topPane.setRight(hBox2);
        topPane.setBottom(rct);

        //???rct??????????????????stage????????????
        rct.widthProperty().bind(staticStage.widthProperty());

        //??????????????????
        topPane.setOnMousePressed(e -> {
            //???????????????????????????(Scene)???X,Y??????
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        //??????????????????
        topPane.setOnMouseDragged(e -> {
            //????????????X,Y
            staticStage.setX(e.getScreenX() - mouseX);
            staticStage.setY(e.getScreenY() - mouseY);
        });
        return topPane;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
