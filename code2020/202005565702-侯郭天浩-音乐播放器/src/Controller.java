import java.io.File;
import java.io.IOException;


import javax.media.*;


public class Controller extends ControllerAdapter {

    @Override
    public void realizeComplete(RealizeCompleteEvent e) {
        // 识别多媒体文件完成后的操作
        super.realizeComplete(e);
        
        MusicPlayerUI ui = MusicPlayerUI.getUI();
        ui.player.prefetch();
    }

    @Override
    public void prefetchComplete(PrefetchCompleteEvent e) {
        // 预加载多媒体文件
        super.prefetchComplete(e);
        
        MusicPlayerUI ui = MusicPlayerUI.getUI();
        // 获取音量大小
        float playVolume = (float)(ui.volumSlider.getValue() / 10.0);
        ui.player.getGainControl().setLevel(playVolume);
        // 获取音乐时长
        int maxTime = (int)ui.player.getDuration().getSeconds();
        int maxMinutes = maxTime / 60;
        int maxSeconds = maxTime % 60;
        String stringMinutes = Integer.toString(maxMinutes);
        String stringSeconds = Integer.toString(maxSeconds);
        ui.maxTime.setText(stringMinutes + ':' + stringSeconds);
        ui.musicSlider.setEnabled(true);
        ui.musicSlider.setMaximum(maxTime);

        ui.closeButton.setEnabled(true);
        
        ui.player.start();
        ui.lrcPane.displayLyric(-1);
    }
    
    @Override
    public void endOfMedia(EndOfMediaEvent e) {
        super.endOfMedia(e);
        MusicPlayerUI ui = MusicPlayerUI.getUI();
        if(ui.listIndex < ui.musicListModel.size() - 1)
        {
            ui.player.close();
            ui.listIndex++;
            ui.musicList.setSelectedIndex(ui.listIndex);
            try {
                Data.initMusic((File)ui.musicList.getSelectedValue());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else
        {
            ui.listIndex = 0;
            ui.musicList.setSelectedIndex(MusicPlayerUI.NULL_TARGET);
            closePlayer();
        }
    }

    static public void closePlayer()
    {
        MusicPlayerUI ui = MusicPlayerUI.getUI();
        ui.player.close();
        ui.player = null;
        ui.musicSlider.setValue(0);
        ui.musicSlider.setEnabled(false);
        ui.closeButton.setEnabled(false);
        ui.nowTime.setText("00:00");
        ui.maxTime.setText("00:00");
    }

}