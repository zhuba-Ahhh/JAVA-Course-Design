import java.awt.*;
// 工具类 存放静态参数以及创建工具方法

public class GameUtil
{
    //地图相关参数
    static int RAY_MAX=100; //地雷个数
    static int MAP_W=21; //地图的宽：横着的格子数量
    static int MAP_H=17; //地图的高：竖着的格子数量
    static int OFFSET=45; //雷区偏移量
    static int SQUARE_LENGTH=50; //格子边长
    static int FLAG_NUM=0; //插旗数量

    //鼠标相关的参数
    static int MOUSE_X; //鼠标横坐标
    static int MOUSE_Y; //鼠标纵坐标
    static boolean LEFT=false; //左键
    static boolean RIGHT=false; //右键

    //游戏状态 0表示游戏中 1表示胜利 2表示失败 3表示难度选择
    static int state=3;

    //游戏难度 1表示简单 2表示困难
    static int level;

    //用于绘制倒计时
    static long START_TIME; //开始时间
    static long END_TIME; //结束时间

    // 定义二维数组存放底层元素 -1代表雷、0代表空、1-8代表对应数字
    static int[][]DATA_BOTTOM=new int[MAP_W+2][MAP_H+2];
    /* 建立二维数组时横纵个数都加二 而显示的窗口长宽不变
       是为了在显示底层数字在对窗口中每个坐标周围的3*3区域进行遍历时
       不会由于在边缘区域访问而导致数组越界
    */

    //创建一个装载顶部元素的二维数组 -1表示无覆盖 0表示覆盖 1表示插旗 2表示插错旗
    static int[][]DATA_TOP=new int[MAP_W+2][MAP_H+2];
    //同样横纵个数加2

    //载入图片
    static Image lei=Toolkit.getDefaultToolkit().getImage("image/lei.jpeg");//地雷
    static Image top=Toolkit.getDefaultToolkit().getImage("image/top.png");//顶层
    static Image flag=Toolkit.getDefaultToolkit().getImage("image/flag.png");//棋子
    static Image noflag=Toolkit.getDefaultToolkit().getImage("image/noflag.png");//插错旗
    static Image youxi=Toolkit.getDefaultToolkit().getImage("image/youxi.png");//游戏中
    static Image yes=Toolkit.getDefaultToolkit().getImage("image/yes.png");//胜利
    static Image over=Toolkit.getDefaultToolkit().getImage("image/over.jpg");//失败
    static Image[] images =new Image[9]; //存放数字的数组
    static {
        for (int i = 0; i<=8 ; i++) { //载入0-8的数字图片
            images[i]=Toolkit.getDefaultToolkit().getImage("image/num/"+i+".png");
        }
    }

    //绘制字符串方法
    static void drawWord(Graphics g,String str,int x,int y,int size,Color color){
        g.setColor(color); //设置颜色
        g.setFont(new Font("宋体",Font.BOLD,size)); //字体样式
        g.drawString(str,x,y);
    }

}
