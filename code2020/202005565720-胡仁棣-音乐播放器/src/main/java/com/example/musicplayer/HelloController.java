package com.example.musicplayer;

import com.leewyatt.rxcontrols.controls.RXAudioSpectrum;
import com.leewyatt.rxcontrols.controls.RXLrcView;
import com.leewyatt.rxcontrols.controls.RXMediaProgressBar;
import com.leewyatt.rxcontrols.pojo.LrcDoc;
import javafx.application.Platform;

import java.text.SimpleDateFormat;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import static com.example.musicplayer.HelloApplication.*;

public class HelloController {
    @FXML
    private ImageView triangle;//右下角的拖拽放大窗口三角区
    @FXML
    private StackPane add_music;//加号，从文件夹添加歌曲
    @FXML
    private Text music_Name;//歌曲名
    @FXML
    private Slider soundslider;//调音量的滑动条
    @FXML
    private Label soundtext;//音量的值
    @FXML
    private ListView<File> songlist;//歌单列表
    @FXML
    private ImageView music_pic;//歌单封面
    @FXML
    private Region stop_play_btn;//播放/暂停按钮
    @FXML
    private RXAudioSpectrum audio_Spectrum;//频谱可视化组件
    @FXML
    private RXMediaProgressBar progressBar;//播放进度条
    @FXML
    private RXLrcView lrcView;//歌词组件
    @FXML
    private Label timeLabel;//播放歌曲的当前时间和总歌曲时长
    @FXML
    private StackPane lastSong;//上一首,暂未用到
    @FXML
    private StackPane nextSong;//下一首,暂未用到
    @FXML
    private ToggleButton xunhuan;//播放模式

    private MediaPlayer player;//播放器，如果是局部变量过几秒就会被释放

    private float[] emptyArray = new float[128];//

    private  SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");//时间格式

    public int kkk;//循环

//    public int abab = 1;

    //监听器，若频谱数据改变则改对应组件
    private AudioSpectrumListener audio_SpectrumListener = new AudioSpectrumListener() {
        @Override
        public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
            audio_Spectrum.setMagnitudes(magnitudes);
        }
    };

    //为进度条设置当前时间
    private ChangeListener<Duration> durationChangeListener = (ob1, ov1, nv1) -> {
        progressBar.setCurrentTime(nv1);
//        System.out.println("fuckyou"+abab);
//        abab++;
        if(kkk == 1){//随时关注是否切换了播放模式
            player.setOnEndOfMedia(this::againPlay);
        }
        else {
            player.setOnEndOfMedia(this::NextSong);
        }
        changeTimeLabel(nv1);
    };


    @FXML
    //初始化操作
    void initialize(){
        kkk = 1;
        Arrays.fill(emptyArray,-60.0f);
        Image image = new Image("file:src/main/resources/img/star.jpg");
        ObservableList<File> items = songlist.getItems();//item应该就是列表中的所有元素，下面将一个个文件添加进去
        items.add(new File("src/main/resources/musicList/Asher Monroe - Try.mp3"));
        music_pic.setImage(image);
        init_SoundText();
        initListView();
        init_Stop_Play_btn();
        initProgressBar();
        xunhuan.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {//监听是否点击了切换循环模式
            @Override
            public void handle(MouseEvent event) {
                if(kkk == 1){
                    kkk = 0;
                }
                else {
                    kkk = 1;
                }
            }
        });
    }
    //初始化播放/暂停按钮
    void init_Stop_Play_btn(){
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M740.608 939.6736H289.3824c-113.1008 0-204.8-91.6992-204.8-204.8V287.2832c0-113.1008 91.6992-204.8 204.8-204.8h451.2256c113.1008 0 204.8 91.6992 204.8 204.8v447.5392c0 113.152-91.6992 204.8512-204.8 204.8512zM675.9424 466.688L459.6736 341.8112c-25.7536-14.848-56.4736-14.848-82.176 0-25.7536 14.848-41.1136 41.472-41.1136 71.168v249.7024c0 29.696 15.36 56.32 41.1136 71.168 12.8512 7.424 26.9824 11.1616 41.1136 11.1616s28.2112-3.7376 41.1136-11.1616l216.2688-124.8768c25.7536-14.848 41.1136-41.472 41.1136-71.168-0.0512-29.6448-15.4112-56.2688-41.1648-71.1168z m-38.4 75.8272l-216.2688 124.8768c-0.6656 0.4096-2.7136 1.536-5.376 0-2.7136-1.536-2.7136-3.8912-2.7136-4.6592V412.9792c0-0.768 0-3.1232 2.7136-4.6592 1.024-0.5632 1.9456-0.768 2.7136-0.768 1.28 0 2.2528 0.5632 2.6624 0.768l216.2688 124.8768c0.6656 0.4096 2.7136 1.536 2.7136 4.6592 0 3.1232-2.048 4.3008-2.7136 4.6592z");
        stop_play_btn.setShape(svgPath);
    }

    @FXML
    //关闭窗口
    private void close_window() {
        Platform.exit();
    }
    @FXML
    //最小化窗口
    private void minimize_window(){
        static_stage.setIconified(true);
    }
    @FXML
    //最大化窗口
    private void maximize_window(){
        if(!static_stage.isMaximized()){
            resetX = static_stage.getX();
            resetY = static_stage.getY();
            resetWidth = static_stage.getWidth();
            resetHeight = static_stage.getHeight();
            static_stage.setMaximized(true);
        }
        else {
            static_stage.setMaximized(false);
            static_stage.setX(resetX);
            static_stage.setY(resetY);
            static_stage.setHeight(resetHeight);
            static_stage.setWidth(resetWidth);
        }
    }
    //##########################################################################//

    @FXML
    //获取鼠标按下时窗口的X,Y，on_mouse_pressed
    private void getPos_XY(MouseEvent event){
        dragBeforeX = event.getSceneX();
        dragBeforeY = event.getSceneY();
        //System.out.println(dragBeforeX+"\n"+dragBeforeY);
    }
    @FXML
    //鼠标拖拽时
    private void pressin(MouseEvent event){
        double now_X = event.getScreenX();
        double now_Y = event.getScreenY();
        //System.out.println(now_X+"\n"+now_Y);
        static_stage.setX(now_X-dragBeforeX);
        static_stage.setY(now_Y-dragBeforeY);
    }
    //##//
    @FXML
    //改变窗口时，鼠标点击的位置
    private void getMousePos(MouseEvent event){
        mouseX = event.getScreenX();
        mouseY = event.getScreenY();
    }
    @FXML
    //改变窗口大小
    private void change_Window_Size(MouseEvent event){
        if(event.getScreenX()-mouseX+static_stage.getWidth()>=1015){
            static_stage.setWidth(static_stage.getWidth()+(event.getScreenX()-mouseX));
            mouseX = event.getScreenX();
        }
        if(event.getScreenY()-mouseY+static_stage.getHeight()>=600){
            static_stage.setHeight(static_stage.getHeight()+(event.getScreenY()-mouseY));
            mouseY = event.getScreenY();
        }

    }
    @FXML
    //当鼠标放到右下角一块三角区域时，改变鼠标样式
    private void mouse_Into_RESIZE(MouseEvent event){
        if((event.getY()<=29.5&&event.getY()>=3)&&(event.getX()<=29.5&&event.getX()>=3)){
            triangle.setCursor(Cursor.NW_RESIZE);
        }
        else{
            triangle.setCursor(Cursor.DEFAULT);
        }
        //测试范围
//        System.out.println(event.getX()+"&&"+event.getY());
    }
    //##########################################################################//
    // 鼠标移至加号区域则改变鼠标样式
    @FXML
    private void file_Add_Song_changeMou(MouseEvent event){
        if((event.getY()<=30&&event.getY()>=0)&&(event.getX()<=270&&event.getX()>=0)){
            add_music.setCursor(Cursor.HAND);
        }
        else{
            add_music.setCursor(Cursor.DEFAULT);
        }
    }
    //通过文件添加歌曲
    @FXML
    private void file_Add_Song(){
        FileChooser fileChooser = new FileChooser();
        /* 文件选择器就是弹个窗出来给你选择文件
        * 使用FileChooser.ExtensionFilter来设置了一个extension filter，它定义了MP3类型是可选择的
        * 运行后对应的扩展名过滤器将会出现在文件选择框窗体中
        * showMultipleDialog可选多个文件，放到filelist里头
        */
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mp3","*.mp3"));
        List<File> fileList = fileChooser.showOpenMultipleDialog(songlist.getScene().getWindow());
        ObservableList<File> items = songlist.getItems();//item应该就是列表中的所有元素，下面将一个个文件添加进去
        if(fileList != null){
            fileList.forEach(file -> {
                if(!items.contains(file)){
                    items.add(file);
                }
            });
        }
    }
    //##########################################################################//
    //初始化音量，将slider的值绑定到音量text上
    @FXML
    private void init_SoundText(){
        soundslider.setValue(20);
        soundtext.textProperty().bind(soundslider.valueProperty().asString("%.0f%%"));
    }
    //##########################################################################//

    //歌单列表初始化及监听鼠标点击事件，点击后则会创建player播放当前选中歌曲
    @FXML
    private void initListView(){
        songlist.setCellFactory(new Callback<>() {
            @Override
            public ListCell<File> call(ListView<File> fileListView) {//自定义listview一行包括哪些部件
                return new Music_ListCell();
            }
        });
        songlist.getSelectionModel().selectedItemProperty().addListener((ob,oldFile,newFile) ->{//监听歌曲点击事件
            if(newFile != null){
                if(player != null){//已经创建了player了，要清掉一些绑定和监听器
                    Dispose_MediaPlayer();
                }
                player = new MediaPlayer(new Media(newFile.toURI().toString()));
                //读取MP3文件，获取字节流封面并保存到本地output文件夹里面，然后再把本地图片嵌到imageView里面
                try {
                    //解析MP3文件，获取歌曲的封面和歌曲名
                    MP3File mp3File = (MP3File) AudioFileIO.read(newFile);
                    AbstractID3v2Tag tag = mp3File.getID3v2Tag();
                    AbstractID3v2Frame frame = (AbstractID3v2Frame) tag.getFrame("APIC");
                    FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
                    byte[] imageData = body.getImageData();
                    FileOutputStream fos = new FileOutputStream("src/main/resources/ouput/temp.jpg");
                    fos.write(imageData);
                    fos.close();
                    Image image = new Image("file:src/main/resources/ouput/temp.jpg");
                    music_pic.setImage(image);
                    ID3v23Frame songnameFrame = (ID3v23Frame) mp3File.getID3v2Tag().frameMap.get("TIT2");
                    String songName = songnameFrame.getContent();
//                    System.out.println(songName);
                    music_Name.setText(songName);
                } catch (CannotReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TagException e) {
                    e.printStackTrace();
                } catch (ReadOnlyFileException e) {
                    e.printStackTrace();
                } catch (InvalidAudioFrameException e) {
                    e.printStackTrace();
                }

                //设置lrc,将当前mp3文件后缀换成.lrc,如果能在绝对路径下找到这个.lrc文件，则设置歌词
                String lrcPath = newFile.getAbsolutePath().replaceAll("mp3$","lrc");
                File lrcFile = new File(lrcPath);
                if(lrcFile.exists()){
                    //lrcView是歌词组件,LrcDoc.parseLrcDoc()用来解析歌词
                    try {
                        byte[] bytes = Files.readAllBytes(lrcFile.toPath());
                        lrcView.setLrcDoc(LrcDoc.parseLrcDoc(new String(bytes,"GBK")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //歌词进度与当前播放时间绑定
                lrcView.currentTimeProperty().bind(player.currentTimeProperty());
                //频谱可视化
                player.setAudioSpectrumListener(audio_SpectrumListener);
                //进度条总时长
                progressBar.durationProperty().bind(player.getMedia().durationProperty());
                //监听当前播放时间
                player.currentTimeProperty().addListener(durationChangeListener);
                player.play();
                SVGPath svgPath1 = new SVGPath();
                svgPath1.setContent("M744.7552 939.6736H293.5296c-113.1008 0-204.8-91.6992-204.8-204.8V287.2832c0-113.1008 91.6992-204.8 204.8-204.8h451.2256c113.1008 0 204.8 91.6992 204.8 204.8v447.5392c0 113.152-91.6992 204.8512-204.8 204.8512zM396.288 713.984c-21.1968 0-38.4-17.2032-38.4-38.4V387.2768c0-21.1968 17.2032-38.4 38.4-38.4s38.4 17.2032 38.4 38.4v288.3584c0 21.1968-17.152 38.3488-38.4 38.3488zM628.2752 713.984c-21.1968 0-38.4-17.2032-38.4-38.4V387.2768c0-21.1968 17.2032-38.4 38.4-38.4s38.4 17.2032 38.4 38.4v288.3584c0 21.1968-17.2032 38.3488-38.4 38.3488z");
                stop_play_btn.setShape(svgPath1);
            }else {//当歌曲列表被清空，也会停止播放器a
                if(player != null){
                    Dispose_MediaPlayer();
                }
            }
        });
    }
    //######################################################################################//
    //调声音
    @FXML
    private void change_sound(){
        player.setVolume(soundslider.getValue()/100);
    }
    //######################################################################################//
    //播放/暂停
    @FXML
    private void Stop_or_Play(){
        if(songlist.getItems().isEmpty()){//如果列表是空的
            file_Add_Song();
            return ;
        }else if(!songlist.getItems().isEmpty()&&player == null){//列表非空但还未选中过歌曲，那就选中第一首
            songlist.getSelectionModel().select(0);
        }
//        if(player.getStatus() == MediaPlayer.Status.PLAYING)
        if(player.getStatus() == MediaPlayer.Status.PLAYING){//是播放状态就停止播放，反之就播放，并改变svg图标形状
            player.pause();
            SVGPath svgPath = new SVGPath();
            svgPath.setContent("M740.608 939.6736H289.3824c-113.1008 0-204.8-91.6992-204.8-204.8V287.2832c0-113.1008 91.6992-204.8 204.8-204.8h451.2256c113.1008 0 204.8 91.6992 204.8 204.8v447.5392c0 113.152-91.6992 204.8512-204.8 204.8512zM675.9424 466.688L459.6736 341.8112c-25.7536-14.848-56.4736-14.848-82.176 0-25.7536 14.848-41.1136 41.472-41.1136 71.168v249.7024c0 29.696 15.36 56.32 41.1136 71.168 12.8512 7.424 26.9824 11.1616 41.1136 11.1616s28.2112-3.7376 41.1136-11.1616l216.2688-124.8768c25.7536-14.848 41.1136-41.472 41.1136-71.168-0.0512-29.6448-15.4112-56.2688-41.1648-71.1168z m-38.4 75.8272l-216.2688 124.8768c-0.6656 0.4096-2.7136 1.536-5.376 0-2.7136-1.536-2.7136-3.8912-2.7136-4.6592V412.9792c0-0.768 0-3.1232 2.7136-4.6592 1.024-0.5632 1.9456-0.768 2.7136-0.768 1.28 0 2.2528 0.5632 2.6624 0.768l216.2688 124.8768c0.6656 0.4096 2.7136 1.536 2.7136 4.6592 0 3.1232-2.048 4.3008-2.7136 4.6592z");
            stop_play_btn.setShape(svgPath);
        }
        else {
            player.play();
            SVGPath svgPath = new SVGPath();
            svgPath.setContent("M744.7552 939.6736H293.5296c-113.1008 0-204.8-91.6992-204.8-204.8V287.2832c0-113.1008 91.6992-204.8 204.8-204.8h451.2256c113.1008 0 204.8 91.6992 204.8 204.8v447.5392c0 113.152-91.6992 204.8512-204.8 204.8512zM396.288 713.984c-21.1968 0-38.4-17.2032-38.4-38.4V387.2768c0-21.1968 17.2032-38.4 38.4-38.4s38.4 17.2032 38.4 38.4v288.3584c0 21.1968-17.152 38.3488-38.4 38.3488zM628.2752 713.984c-21.1968 0-38.4-17.2032-38.4-38.4V387.2768c0-21.1968 17.2032-38.4 38.4-38.4s38.4 17.2032 38.4 38.4v288.3584c0 21.1968-17.2032 38.3488-38.4 38.3488z");
            stop_play_btn.setShape(svgPath);
        }
    }
    //上一首
    @FXML
    void FindLastSong(MouseEvent event) {
        int musiclist_size = songlist.getItems().size();//获取歌曲列表的总的歌曲数
        if(musiclist_size <2){
            return ;
        }
        int index = songlist.getSelectionModel().getSelectedIndex();//获取当前选中的是第几首歌
        if(index > 0){
            songlist.getSelectionModel().select(index-1);//选中下一首，该操作和鼠标点击选中歌曲一样的
        }
        else {
            songlist.getSelectionModel().select(musiclist_size-1);//如果是第一首歌，那就跳到最后一首
        }
    }
    //下一首
    @FXML
    void FindNextSong(MouseEvent event) {
        NextSong();
    }
    //重播
    void againPlay(){
        int index = songlist.getSelectionModel().getSelectedIndex();
        songlist.getSelectionModel().clearSelection();
        songlist.getSelectionModel().select(index);
    }
    //########################################################################//
    public void NextSong(){
        int musiclist_size = songlist.getItems().size();
        if(musiclist_size <2){//列表只有一首歌或者是没有歌了
            return ;
        }
        int index = songlist.getSelectionModel().getSelectedIndex();//获取当前被选中的是列表中的第几首歌
        if(index < musiclist_size-1){
            songlist.getSelectionModel().select(index+1);//选中下一位
        }
        else {
            songlist.getSelectionModel().select(0);//到末尾就重新返回到第一个
        }
    }
    //进度条的拖动，点击处理
    private void initProgressBar(){
        EventHandler<MouseEvent> progressBarHandler = event -> {
            if(player != null){
                player.seek(progressBar.getCurrentTime());//让player播放的进度与进度条的进度一致
                changeTimeLabel(progressBar.getCurrentTime());//改变进度时间
            }
        };
        //响应鼠标点击或者拖拽事件
        progressBar.setOnMouseClicked(progressBarHandler);
        progressBar.setOnMouseDragged(progressBarHandler);
    }
    //timeLabel显示当前时间
    private void changeTimeLabel(Duration nv1) {
        String currentTime = sdf.format(nv1.toMillis());//当前时间
        String bufferedTimer = sdf.format(player.getBufferProgressTime().toMillis());//缓存时间
        timeLabel.setText(currentTime+ " / "+bufferedTimer);//设置进度时间
    }
    //彻底销毁播放器player
    private void Dispose_MediaPlayer(){
        player.stop();//停止播放
        lrcView.setLrcDoc(null);//清空歌词
        lrcView.currentTimeProperty().unbind();//歌词与当前时间解除绑定
        lrcView.setCurrentTime(Duration.ZERO);//归零
        player.setAudioSpectrumListener(null);//清空频谱组件
        progressBar.durationProperty().unbind();//进度条和当前时间解绑
        progressBar.setCurrentTime(Duration.ZERO);//解绑后才能赋值成功
        audio_Spectrum.setMagnitudes(emptyArray);
        timeLabel.setText("00:00/INF");
        player.currentTimeProperty().removeListener(durationChangeListener);
        player.dispose();
        player = null;
        Image image = new Image("file:src/main/resources/img/star.jpg");//防止切换歌曲后，mp3文件中未找到歌曲封面
        music_pic.setImage(image);
    }
}