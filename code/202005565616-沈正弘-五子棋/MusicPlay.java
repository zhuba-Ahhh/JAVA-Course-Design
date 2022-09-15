import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
public class MusicPlay extends Thread{

    public  void run() {
        //1 获取你要播放的音乐文件
        File file = new File("music.wav");
        //2、定义一个AudioInputStream用于接收输入的音频数据
        AudioInputStream am;
        //3、使用AudioSystem来获取音频的音频输入流(处理（抛出）异常)
        try {
            am = AudioSystem.getAudioInputStream(file);
            AudioFormat af = am.getFormat();
        
        //4、使用AudioFormat来获取AudioInputStream的格式
        
        //5、一个源数据行
        SourceDataLine sd ;
        //6、获取受数据行支持的音频格式DataLine.info
        //DataLine.Info dl = new DataLine.Info(SourceDataLine.class, af);
        //7、获取与上面类型相匹配的行 写到源数据行里 二选一
        try {
            sd = AudioSystem.getSourceDataLine(af);
            sd.open();
            //9、允许某个数据行执行数据i/o
            sd.start();
            //10、写数据
            int sumByteRead = 0; //读取的总字节数
            byte [] b = new byte[320];//设置字节数组大小
            //11、从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
            while (sumByteRead != -1) {//-1代表没有 不等于-1时就无限读取
                sumByteRead = am.read(b, 0, b.length);//12、读取哪个数组
                if(sumByteRead >= 0 ) {//13、读取了之后将数据写入混频器,开始播放
                    sd.write(b, 0, b.length);
                }
            }
            
            //关闭
            sd.drain();
            sd.close();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//便捷写法
        //sd = (SourceDataLine) AudioSystem.getLine(dl);
        //8、打开具有指定格式的行，这样可以使行获得资源并进行操作
        
        
        } catch (UnsupportedAudioFileException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
    }
}
