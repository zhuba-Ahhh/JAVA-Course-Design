import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.media.Player;
import javax.media.Time;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class RunMusicThread extends Thread{
    MusicPlayerUI ui = MusicPlayerUI.getUI();
    // flag为1表示正在操作进度条
    boolean flag = false;
    @Override
    public void run() {
        MouseInputListener l = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                flag = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ui.player.setMediaTime(new Time((float)ui.musicSlider.getValue()));
                flag = false;
            }
            
        };
        ui.musicSlider.addMouseListener(l);


        while(true)
        {
            if(ui.player != null && ui.player.getState() == Player.Started)
            {
                int nowTime = (int)ui.player.getMediaTime().getSeconds();
                // 设置当前播放时间
                int nowMinutes = nowTime / 60;
                String stringMinutes = Integer.toString(nowMinutes);
                if(stringMinutes.length() < 2)
                {
                    stringMinutes = "0" + stringMinutes;
                }
                int nowSeconds = nowTime % 60;
                String stringSeconds = Integer.toString(nowSeconds);
                if(stringSeconds.length() < 2)
                {
                    stringSeconds = "0" + stringSeconds;
                }
                ui.nowTime.setText(stringMinutes + ':' + stringSeconds);
                // 设置当前进度条位置
                if(flag == false)
                {
                    ui.musicSlider.setValue(nowTime);
                }
                // 设置当前音量
                ui.player.getGainControl().setLevel(
                    (float)(ui.volumSlider.getValue() / 10.0));

                // 设置歌词显示
                if(ui.lrcPane.getList() != null && ui.lrcPane.getList().size() != 0)
                {
                    LyricDisplayer lrcPlay = ui.lrcPane;
                    ArrayList<LrcLine> lyrics = lrcPlay.getList();
                    for(int i = 0; i < lyrics.size(); i++)
                    {
                        if(lyrics.get(i).getTime() ==
                            (long)ui.player.getMediaTime().getSeconds())
                        {
                            lrcPlay.displayLyric(i);
                        }
                    }
                    
                }

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
