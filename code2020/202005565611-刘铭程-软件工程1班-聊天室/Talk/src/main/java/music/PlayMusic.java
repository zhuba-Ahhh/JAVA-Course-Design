package music;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class PlayMusic {
    //消息提示音
    public static void playMsgMusic() {
        try {
            //加载音频文件
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("../Talk/src/main/resources/music/Global.wav"));

            //获取音频格式
            AudioFormat aif = ais.getFormat();

            //根据指定的信息构建数据行的信息对象，其中包含单个音频格式
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);

            //获取与指定的Line.Info对象中的描述匹配的行
            final SourceDataLine sdl;
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();

            //音量控件
            //FloatControl.Type.MASTER_GAIN---代表一条线上整体收益的控制
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);

            //播放
            int nByte = 0;
            final int size = 1024 * 64;
            byte[] buffer = new byte[size]; //缓冲区
            while (nByte != -1) {
                    nByte = ais.read(buffer, 0, size);
                    sdl.write(buffer, 0, nByte);
            }
            sdl.stop();
        } catch (Exception e) {
            System.out.println("消息音频");
        }
    }

    //登录提示音
    public static void playComeMusic() {
        try {
            //加载音频文件
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("../Talk/src/main/resources/music/system.wav"));

            //获取音频格式
            AudioFormat aif = ais.getFormat();

            //根据指定的信息构建数据行的信息对象，其中包含单个音频格式
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);

            //获取与指定的Line.Info对象中的描述匹配的行
            final SourceDataLine sdl;
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();

            //音量控件
            //FloatControl.Type.MASTER_GAIN---代表一条线上整体收益的控制
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);

            //播放
            int nByte = 0;
            final int size = 1024 * 64;
            byte[] buffer = new byte[size]; //缓冲区
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, size);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();
        } catch (Exception e) {
            System.out.println("登录音频");
        }
    }
}
