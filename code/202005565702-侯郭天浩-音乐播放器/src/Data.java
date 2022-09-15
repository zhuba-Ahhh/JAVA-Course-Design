import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.*;
import javax.swing.JOptionPane;

public class Data {

    public static ArrayList<File> fileList;
    public static ArrayList<String> musicNameList;
    private static MusicPlayerUI ui;


    public static void initMusic(File file) throws IOException {
        ui = MusicPlayerUI.getUI();
        try {
            // 获取文件的资源定位对象
            URL url = file.toURI().toURL();
            // 根据资源定位对象创建媒体定位对象
            MediaLocator locator = new MediaLocator(url);
            // 根据多媒体定位对象创建播放者
            if(ui.player != null)
            {
                ui.player.close();
            }
            ui.player = Manager.createPlayer(locator);

            ui.player.addControllerListener(new Controller());
            ui.player.realize();

        } catch (NoPlayerException | IOException e) {
            JOptionPane.showMessageDialog(ui.centerPane,
            "打开文件失败，请检查选择文件是否为可播放的wav音频文件");
        }

        Pattern pattern = Pattern.compile(
            "([^<>/\\\\|:\"\"\\*\\?\\.]+)\\.\\w+$+");
        Matcher m = pattern.matcher(file.getName());
        m.find();
        String fName = m.group(1);
        ArrayList lrcList =  new LrcReader("music\\" + fName + ".lrc").getLrc();
        if(lrcList.size() != 0)
        {
            
            ui.lrcPane.setVisible(true);
            ui.centerSplitPane.setDividerLocation(200);
            ui.lrcPane.prepareDisplay(lrcList);
        }
        else
        {
            ui.lrcPane.setVisible(true);
            ui.centerSplitPane.setDividerLocation(200);
            ui.lrcPane.prepareDisplay(null);
        }

    }

    public static void loadFile(File openedFile)
    {
        ui = MusicPlayerUI.getUI();
        fileList = new ArrayList<File>();
        if(openedFile.isDirectory())
        {
            File[] tempList = openedFile.listFiles();
            for(int i = 0; i < tempList.length; i++)
            {
                if(tempList[i].isFile()
                    && tempList[i].getName().endsWith(".wav"))
                {
                    fileList.add(tempList[i]);
                } 
            }
        }
        else
        {
            if(openedFile.getName().endsWith(".wav"))
            {
                fileList.add(openedFile);
            }
        }
        if(fileList != null)
        {
            for(int i = 0; i < Data.fileList.size(); i++)
            {
                ui.musicListModel.addElement(Data.fileList.get(i));
            }
        }
    }
    
}