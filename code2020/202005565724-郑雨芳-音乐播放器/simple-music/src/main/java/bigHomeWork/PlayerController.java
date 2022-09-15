
package bigHomeWork;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.leewyatt.rxcontrols.controls.RXAudioSpectrum;
import com.leewyatt.rxcontrols.controls.RXLrcView;
import com.leewyatt.rxcontrols.controls.RXMediaProgressBar;
import com.leewyatt.rxcontrols.controls.RXToggleButton;
import com.leewyatt.rxcontrols.pojo.LrcDoc;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerController {


    @FXML
    private AnchorPane drawerpane;

    @FXML
    private BorderPane slierpane;

    @FXML
    private StackPane soundBtn;
    //fxml文件使用 onXX="#XX" 绑定控制器带有 @FXML
    @FXML
    private StackPane skinBtn;


    @FXML
    private ListView<File> listView;

    @FXML
    private RXAudioSpectrum audioSpectrum;

    @FXML
    private RXLrcView lrcView;

    @FXML
    private ToggleButton playBtn;

    @FXML
    private RXMediaProgressBar progressBar;

    @FXML
    private Label timeLabel;
    private boolean flag=true;
    private Timeline showAnim;
    private Timeline hideAnim;
    private float[] emptyArr = new float[128];
    private ContextMenu soundPopup;
    private ContextMenu skinPopup;
    private MediaPlayer player;
    private Slider soundSlider;
    private SimpleDateFormat fm = new SimpleDateFormat("mm:ss");

//  使用匿名类，复杂一点private AudioSpectrumListener audioSpectrumListener = new AudioSpectrumListener() {
//        @Override
//        public void spectrumDataUpdate(double v, double v1, float[] floats, float[] floats1) {
//            audioSpectrum.setMagnitudes(floats);
//        }
//    };
    private AudioSpectrumListener audioSpectrumListener = (v, v1, floats, floats1) -> {
        audioSpectrum.setMagnitudes(floats);
    };
    private ChangeListener<Duration> durationChangeListener = (ob1, ov1, bv1) -> {
        progressBar.setCurrentTime(bv1);
        changeTimeLabel(bv1);
    };

    private EventHandler<MouseEvent> progressBarHandler = e -> {
        if (player != null) {
            player.seek(progressBar.getCurrentTime());
            changeTimeLabel(progressBar.getCurrentTime());
        }
    };

    private void changeTimeLabel(Duration time) {
        String currentTime = fm.format(time.toMillis());
        String bufferTime = fm.format(player.getBufferProgressTime().toMillis());
        timeLabel.setText(currentTime + "/" + bufferTime);
    }


    @FXML
    void initialize() {
        initAnim();
        initSoundPopup();
        initSkinPopup();
        Arrays.fill(emptyArr, -60.0f);
        initListView();
        initProgressBar();
    }

    private void initProgressBar() {
        progressBar.setOnMouseClicked(progressBarHandler);
        progressBar.setOnMouseDragged(progressBarHandler);
    }

    private void dispose() {
        player.stop();
        player.dispose();
        lrcView.setLrcDoc(null);
//       解绑同时还原
        lrcView.currentTimeProperty().unbind();
        lrcView.setCurrentTime(Duration.ZERO);
        player.setAudioSpectrumListener(null);
        progressBar.durationProperty().unbind();
        progressBar.setCurrentTime(Duration.ZERO);
        audioSpectrum.setMagnitudes(emptyArr);
        timeLabel.setText("00:00 / 00:00");
        playBtn.setSelected(false);
        player.currentTimeProperty().removeListener(durationChangeListener);
    }

    private void initListView() {
        //设置单元格样式
        listView.setCellFactory(fileListView -> new MusicListCell());
        listView.getSelectionModel().selectedItemProperty().addListener((ob, oldFile, newFile) -> {
            if (player != null)
                dispose();
            if (newFile != null) {
                Media temp = new Media(newFile.toURI().toString());
//               System.out.println(newFile.getClass().getName().toString());打印newfile类型
                player = new MediaPlayer(new Media(newFile.toURI().toString()));
//                setVolume里面是0-1所以除个100
                player.setVolume(soundSlider.getValue() / 100);
//                lrc和mp3放在同一个文件下，只是后缀不同
                String lrcpath = newFile.getAbsolutePath().replaceAll("mp3$", "lrc");
                File lrcFile = new File(lrcpath);
                if (lrcFile.exists()) {
                    byte[] bytes;
                    try {
                        bytes = Files.readAllBytes(lrcFile.toPath());
                        lrcView.setLrcDoc(LrcDoc.parseLrcDoc(new String(bytes, EncodingDetect.detect(bytes))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                歌词和播放同步
                lrcView.currentTimeProperty().bind(player.currentTimeProperty());
//                设置频谱可视化
                player.setAudioSpectrumListener(audioSpectrumListener);
//                设置进度条
                progressBar.durationProperty().bind(player.getMedia().durationProperty());
                player.currentTimeProperty().addListener(durationChangeListener);
                //如果播放完当前歌曲, 自动播放下一首
               if(flag){
                   player.setOnEndOfMedia(this::playNextMusic);
               }
               else{
                   player.setCycleCount(MediaPlayer.INDEFINITE);
               }
                playBtn.setSelected(true);
                player.play();
            }
        });
    }

    private Stage getStage() {
        return (Stage) drawerpane.getScene().getWindow();
    }

    private void initAnim() {
        showAnim = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(slierpane.translateXProperty(), 0)));
        hideAnim = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(slierpane.translateXProperty(), 300)));
        hideAnim.setOnFinished(event -> drawerpane.setVisible(false));
    }

    private void initSoundPopup() {
        soundPopup = new ContextMenu(new SeparatorMenuItem());
        Parent soundRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sound.fxml"));
            soundRoot = loader.load();
//            另一种方法，无需再建立一个controller文件
            ObservableMap<String, Object> name = loader.getNamespace();
            soundSlider = (Slider) name.get("soundSlider");
//            监听声音滑块拖动,文本绑定在滑块上
            soundSlider.valueProperty().addListener((ob, ov, bv) -> {
                if (player != null) {
                    player.setVolume(bv.doubleValue() / 100);
                }
            });
            Label soundNum = (Label) name.get("soundNum");
//            数字要化为文本，同时格式化
            soundNum.textProperty().bind(soundSlider.valueProperty().asString("%.0f%%"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        soundPopup.getScene().setRoot(soundRoot);
    }

    //        这个skin就是直接抄sound就好
    private void initSkinPopup() {
        skinPopup = new ContextMenu(new SeparatorMenuItem());
        Parent skinRoot = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/skin.fxml"));
            skinRoot = fxmlLoader.load();
            ObservableMap<String, Object> namespace = fxmlLoader.getNamespace();
            ToggleGroup skinGroup = (ToggleGroup) namespace.get("skinGroup");
            skinGroup.selectedToggleProperty().addListener((ob, ov, nv) -> {
                RXToggleButton btn = (RXToggleButton) nv;
                String skinName = btn.getText();
                String skinUrl = getClass().getResource("/css/" + skinName + ".css").toExternalForm();
                drawerpane.getScene().getRoot().getStylesheets().setAll(skinUrl);
                skinPopup.getScene().getRoot().getStylesheets().setAll(skinUrl);
                soundPopup.getScene().getRoot().getStylesheets().setAll(skinUrl);
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        skinPopup.getScene().setRoot(skinRoot);
    }

    @FXML
    void onCloseAction(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onFullAction(MouseEvent event) {
//        获取当前屏幕
        Stage stage = getStage();
        stage.setFullScreen(!stage.isFullScreen());
    }


    @FXML
    void onMinAction(MouseEvent event) {
        Stage stage = getStage();
        stage.setIconified(true);
    }

    @FXML
    void onHideSliderPaneAction(MouseEvent event) {
        showAnim.stop();
        hideAnim.play();
    }

    @FXML
    void onShowSliderPaneAction(MouseEvent event) {
        drawerpane.setVisible(true);
        //停下隐藏动画，播放显示动画
        hideAnim.stop();
        showAnim.play();
    }

    @FXML
    void onSoundPopupAction(MouseEvent event) {
        Stage stage = getStage();
        Bounds bounds = soundBtn.localToScreen(soundBtn.getBoundsInLocal());
//        左上角就是minx，miny
        soundPopup.show(stage, bounds.getMinX() - 20, bounds.getMinY() - 165);
    }

    @FXML
    void onSkinPopupAction(MouseEvent event) {
        Stage stage = getStage();
        Bounds bounds = skinBtn.localToScreen(skinBtn.getBoundsInLocal());
        skinPopup.show(stage, bounds.getCenterX() - 150, bounds.getCenterY() + 20);
    }

    @FXML
    void onAddMusicAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();    // 创建一个文件对话框
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("mp3", "*.mp3"));     //添加过滤器
        List<File> fileList = fileChooser.showOpenMultipleDialog(getStage());// 显示文件打开对话框
        ObservableList<File> items = listView.getItems();
        if (fileList != null) {
            fileList.forEach(file -> {
                if (!items.contains(file))
                    items.add(file);
            });
        }
    }

    @FXML
    void onPlayAction(ActionEvent event) {
        if (player != null) {
            if (playBtn.isSelected()) player.play();
            else player.pause();
        }
    }

    @FXML
    void onPlayNextAction(MouseEvent event) {
        System.out.println(1888);
        playNextMusic();
    }

    @FXML
    void onPlayPrevAction(MouseEvent event) {
        if(flag){
            System.out.println(123);
            int size = listView.getItems().size();
            if (size < 2) return;
            int index = listView.getSelectionModel().getSelectedIndex();
            index = (index == 0) ? size - 1 : index - 1;
            listView.getSelectionModel().select(index);
        }
        else player.seek(new Duration(0));
    }

    private void playNextMusic() {
        if(flag){
            int size = listView.getItems().size();
            if (size < 2) return;
            int index = listView.getSelectionModel().getSelectedIndex();
            index = (index == size - 1) ? 0 : index + 1;
            listView.getSelectionModel().select(index);
        }
         else player.seek(new Duration(0));
    }

    @FXML
    void onRepeatAction(MouseEvent event) {
        if (player != null) {
            flag=!flag;
        }
    }
}
