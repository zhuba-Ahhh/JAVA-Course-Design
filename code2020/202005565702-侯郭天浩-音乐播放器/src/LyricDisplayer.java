import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;  

public class LyricDisplayer extends JPanel{

    // 当前行颜色
    protected final Color CURRENT_LINE_COLOR = Color.green;

    // 其他行颜色
    protected final Color OTHER_LINE_COLOR = Color.GRAY;

    // 显示的其余行数
    protected final int UP_DOWN_LINES = 8;

    // 需要显示的歌词表
    protected ArrayList<LrcLine> lrcList;
    // 需要显示的歌词标记
    protected int index;
    // 先将歌词绘制在image上在使用paint方法显示在屏幕上
    protected Image bufferImage = null;
    // image的尺寸
    private Dimension bufferedSize;

    public void prepareDisplay(ArrayList<LrcLine> lrcList) 
    {
        this.lrcList = lrcList;
        this.index = 0;
        this.setFont(new Font("微软雅黑", Font.PLAIN, 20));
    }
     
    public void displayLyric(int index) 
    {
        if(index == -1)
        {
            this.drawBufferImage();
            this.paint(this.getGraphics());
        }
        else if(this.index != index)
        {
            this.index = index;
            this.drawBufferImage();
            this.paint(this.getGraphics());
        }
    }
    
    public ArrayList getList()
    {
        return lrcList;
    }

    private void drawLineinMiddle(
        int height, String lyric, Graphics2D g2d, Color color)
    {
        // 在g2d的中间绘制歌词
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(color);

        // x为开始绘制歌词的位置，使歌词居中
        int x = (this.getWidth() - fm.stringWidth(lyric)) / 2;
        g2d.drawString(lyric, x, height);

    }

    public void drawBufferImage()
    {
        // 绘制image图像
        Image tempBufferedImage = this.createImage(
            this.getWidth(), this.getHeight());
            this.bufferedSize = this.getSize();
            
        Graphics2D g2d = (Graphics2D) tempBufferedImage.getGraphics();
        
        // 设置字体
        g2d.setFont(new Font("楷体", Font.PLAIN, 25));

        // 设置采用抗锯齿模式绘制
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(this.lrcList != null && this.lrcList.size() != 0)
        {
            g2d.setFont(new Font("楷体", Font.PLAIN, 35));
            this.drawLineinMiddle(this.getHeight() / 2,
                this.lrcList.get(index).getLrc(), g2d, this.CURRENT_LINE_COLOR);
            
            // 每行歌词高度差
            int subHeight = g2d.getFontMetrics().getHeight() + 5;
            g2d.setFont(new Font("楷体", Font.PLAIN, 25));
            for(int i = index - UP_DOWN_LINES; i < index; i++)
            {
                // 绘制上方已播放过的歌词
                if(i < 0)
                {
                    continue;
                }
                if(index - i > UP_DOWN_LINES / 2)
                {
                    // 选择距离较远的一半歌词设置递减的透明显示,ratio为透明度
                    float ratio = (float)(UP_DOWN_LINES - (index - i)) 
                        / (UP_DOWN_LINES / 2) * 0.8f;
                    g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, ratio));
                }
                else
                {
                    // 距离较近的一般歌词直接显示
                    g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 1.0f));
                }
                this.drawLineinMiddle(
                    this.getHeight() / 2 - (index - i) * subHeight,
                    lrcList.get(i).getLrc(), g2d, this.OTHER_LINE_COLOR);
            }

            for(int i = index + 1; i < index + UP_DOWN_LINES; i++)
            {
                // 绘制下方还未播放的歌词
                if(i >= lrcList.size())
                {
                    break;
                }
                if(i - index > UP_DOWN_LINES / 2)
                {
                    // 选择距离较远的一半歌词设置递减的透明显示,ratio为透明度
                    float ratio = (float)(UP_DOWN_LINES - (i - index)) 
                        / (UP_DOWN_LINES / 2) * 0.8f;
                    g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, ratio));
                }
                else
                {
                    // 距离较近的一般歌词直接显示
                    g2d.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 1.0f));
                }
                this.drawLineinMiddle(
                    this.getHeight() / 2 + (i - index) * subHeight,
                    lrcList.get(i).getLrc(), g2d, this.OTHER_LINE_COLOR);
            }

        }
        else
        {
            this.drawLineinMiddle(this.getHeight() / 2,
                "未找到相应的歌词文件", g2d, this.CURRENT_LINE_COLOR);
        }
        this.bufferImage = tempBufferedImage;

    }
    public void paint(Graphics g)
    {
        // 将image图像刷新在屏幕上
        if(!this.isVisible())
        {
            return;
        }
        super.paint(g);

        if(this.bufferImage == null ||
            this.getWidth() != bufferedSize.getWidth() ||
            this.getHeight() != bufferedSize.getHeight())
        {
            // 当图像没有绘制或者大小发生变化时重新绘制
            this.drawBufferImage();
        }
        g.drawImage(bufferImage, 0, 0, null);
    }
}
