import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcReader {
    // 用于读取文件
    BufferedReader bufferReader = null;

    public String title = "";
    public String artist = "";
    public String album = "";
    public String resource = ""; // 歌词来源
    public String lrcMaker = ""; // 歌词制作者

    // 存放歌词的队列
    ArrayList <LrcLine> lines = new ArrayList<LrcLine>();

    public LrcReader(String fileName) throws IOException
    {
        try {
            // 文件输入流
            FileInputStream fStream = new FileInputStream(
                URLDecoder.decode(fileName, "UTF-8"));
            // 读取文件
            bufferReader = new BufferedReader(
                new InputStreamReader(fStream, "UTF-8"));
            readData();
            
        } catch (Exception e) {
            if(MusicPlayerUI.getUI().lrcPane.lrcList != null)
            {
                MusicPlayerUI.getUI().lrcPane.lrcList.clear();
                MusicPlayerUI.getUI().lrcPane.displayLyric(-1);
            }
        }
    }

    public ArrayList getLrc()
    {
        return lines;
    }

    private void readData() throws IOException
    {
        lines.clear();
        String singleLine;
        while((singleLine = bufferReader.readLine()) != null)
        {
            if(singleLine.trim().equals(""))
            {
                continue;
            }
            
            if(title == null || title.trim().equals(""))
            {
                // 判断当前行是否为title
                Pattern pattern = Pattern.compile("\\[ti:(.+?)\\]");
                Matcher matcher = pattern.matcher(singleLine);
                if(matcher.find())
                {
                    title = matcher.group(1);
                    continue;
                }
            }
            if(album == null || album.trim().equals(""))
            {
                // 判断当前行是否为album
                Pattern pattern = Pattern.compile("\\[al:(.+?)\\]");
                Matcher matcher = pattern.matcher(singleLine);
                if(matcher.find())
                {
                    title = matcher.group(1);
                    continue;
                }
            }
            if(artist == null || artist.trim().equals(""))
            {
                // 判断当前行是否为artist
                Pattern pattern = Pattern.compile("\\[ar:(.+?)\\]");
                Matcher matcher = pattern.matcher(singleLine);
                if(matcher.find())
                {
                    title = matcher.group(1);
                    continue;
                }
            }
            if(resource == null || resource.trim().equals(""))
            {
                // 判断当前行是否为resource
                Pattern pattern = Pattern.compile("\\[re:(.+?)\\]");
                Matcher matcher = pattern.matcher(singleLine);
                if(matcher.find())
                {
                    title = matcher.group(1);
                    continue;
                }
            }
            if(lrcMaker == null || lrcMaker.trim().equals(""))
            {
                // 判断当前行是否为title
                Pattern pattern = Pattern.compile("\\[by:(.+?)\\]");
                Matcher matcher = pattern.matcher(singleLine);
                if(matcher.find())
                {
                    title = matcher.group(1);
                    continue;
                }
            }

            // 读取并分析歌词

            // 本行包含的时间个数
            int timeNum = 0;

            // 第一次切分
            String []firstCut = singleLine.split("\\]");

            // 第二次切分
            for(int i = 0; i < firstCut.length; i++)
            {
                // 两次切分后得到的是不含括号的字符串，将其放回第一次切分时的位置
                // 这样得到的firstCut就是最终结果
                String []secontCut = firstCut[i].split("\\[");
                if(secontCut.length != 0)
                {
                    firstCut[i] = secontCut[secontCut.length - 1];
                }
                else
                {
                    firstCut[0] = "";
                }

                if(isTime(firstCut[i]))
                {
                    timeNum++;
                }
            }

            // 当同一段歌词出现在不同时间时就会有多个[mm:ss:ff]形式的歌词
            // 将它们分别存入歌词表中
            for(int i = 0; i < timeNum; i++)
            {
                LrcLine tempLine = new LrcLine();
                tempLine.setTime(firstCut[i]);
                if(timeNum < firstCut.length)
                {
                    // 当该行歌词不为空时
                    tempLine.setLyric(firstCut[firstCut.length - 1]);
                }
                lines.add(tempLine);
            }

        }
        sortLines();
    }

    private boolean isTime(String str)
    {
        String []strCut = str.split(":|\\.");
        if((strCut.length != 3 && strCut.length != 2))
        {
            return false;
        }
        try {
            for(int i = 0; i < strCut.length; i++)
            {
                Integer.parseInt(strCut[i]);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
        
    }

    private void sortLines()
    {
        // 用选择排序将歌词表按照时间顺序排序
        for(int i = 0; i < lines.size() - 1; i++)
        {
            int index = i;
            boolean flag = false;
            long maxSub = Long.MAX_VALUE;
            for(int j = i + 1; j < lines.size(); j++)
            {
                long sub;
                sub = lines.get(index).getTime() - lines.get(j).getTime();
                if(sub <= 0)
                {
                    continue;
                }
                flag = true;
                index = j;
            }
            if(flag)
            {
                lines.add(index + 1, lines.get(i));
                lines.add(i + 1, lines.get(index));
                lines.remove(i);
                lines.remove(index);
            }
        }
    }
    public void printLrc()
    {
        System.out.println("歌曲名: " + title);  
        System.out.println("演唱者: " + artist);  
        System.out.println("专辑名: " + album);  
        System.out.println("来源: " + resource);
        System.out.println("歌词制作: " + lrcMaker);
        System.out.println(lines.size());
        for(int i = 0; i < lines.size(); i++)
        {
            System.out.println(lines.get(i).getTime()
                + " " + lines.get(i).getLrc());
        }
    }
}
