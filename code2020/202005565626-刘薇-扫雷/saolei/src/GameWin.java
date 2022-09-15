import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//包含main函数的程序入口类

public class GameWin extends JFrame // 继承JFrame类 监听鼠标键盘事件功能
{
    int width=2*GameUtil.OFFSET+GameUtil.MAP_W*GameUtil.SQUARE_LENGTH;//窗口宽度
    int height=4*GameUtil.OFFSET+GameUtil.MAP_H*GameUtil.SQUARE_LENGTH;//窗口高度

    Image offScreenImage=null; //创建一个新画布 解决顶层与底层地图不断重复绘制而带来的抖动问题

    MapBottom mapBottom=new MapBottom(); //新建底层地图类
    MapTop mapTop=new MapTop(); //新建顶层地图类
    GameSelect gameSelect=new GameSelect(); //新建难度选择类
    Help help=new Help(); // 新建帮助窗口类
    boolean begin=false; //是否选择难度 false表示未选择 true表示选择

    //launch方法 用于创建游戏
    void launch()
    {
        GameUtil.START_TIME=System.currentTimeMillis(); //记录开始时间
        this.setVisible(true); //设置窗口是否可见
        this.setResizable(false);
        if(GameUtil.state==3){ //如果在难度选择状态里
            this.setSize(500,500);//设置窗口大小500*500
        }
        else {
            this.setSize(width,height); //除开游戏选择状态 窗口大小与选择的难度有关
        }
        this.setLocationRelativeTo(null); //设置窗口配置 居中显示
        this.setTitle("扫雷游戏"); //设置窗口标题“扫雷游戏”
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //设置窗口关闭的方法

        //鼠标事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); //鼠标点击
                switch (GameUtil.state) { //按照游戏的不同状态对鼠标点击进行对应的操作
                    case 0: //游戏中点击鼠标为扫雷
                        if (e.getButton() == 1) { //鼠标左键被点击
                            GameUtil.MOUSE_X = e.getX(); //记录鼠标左键的横坐标
                            GameUtil.MOUSE_Y = e.getY(); //记录鼠标左键的纵坐标
                            GameUtil.LEFT = true; //修改鼠标左键状态
                        }
                        if (e.getButton() == 3) { //鼠标右键被点击
                            GameUtil.MOUSE_X = e.getX(); //记录鼠标右键的横坐标
                            GameUtil.MOUSE_Y = e.getY(); //记录鼠标右键的纵坐标
                            GameUtil.RIGHT = true; //修改鼠标右键状态
                        }

                    case 1:
                    case 2:
                        if (e.getButton() == 1) { //鼠标左键被点击
                            //如果点击的是窗口中笑脸图案区域 则充值游戏
                            if (e.getX() > (int) (GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W / 2.3))
                                    && e.getX() <
                                    (int) (GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W / 2.3))
                                    + (int) (GameUtil.SQUARE_LENGTH * 1.4)
                                    && e.getY() > GameUtil.OFFSET + 6
                                    && e.getY() < GameUtil.OFFSET + 6 + (int) (GameUtil.SQUARE_LENGTH * 1.4 + 2)) {
                                GameUtil.RIGHT=GameUtil.LEFT=false;
                                mapBottom.regame(); //重置底层
                                mapTop.regame(); //重置顶层
                                GameUtil.FLAG_NUM = 0; //释放所有棋子数
                                GameUtil.START_TIME = System.currentTimeMillis(); //游戏重置时也重新记录开始时间
                                GameUtil.state = 0; //状态变为游戏开始
                            }
                        }
                        if(e.getButton()==2){ //按了鼠标滚轮
                            GameUtil.state=3; //难度选择状态
                            begin=true;
                        }
                        //删除case0 case1的break 使游戏能在任一游戏状态下进行重置和选择难度
                        break;

                    case 3: //游戏在选择难度状态下
                        if (e.getButton() == 1) { //如果点击鼠标左键
                            GameUtil.MOUSE_X = e.getX(); //记录鼠标左键的横坐标
                            GameUtil.MOUSE_Y = e.getY(); //记录鼠标左键的纵坐标
                            begin = gameSelect.hard(); //判断是否进行了难度选择 是否可以开始
                            help.window(); //是否点击了帮助界面，是否弹出帮助窗口
                        }
                        break;
                    default:
                }
            }
        });

        while (true){ //避免需要放大缩小才能出现正确窗格
            begin(); //时刻调用开始方法
            repaint(); //不停调用绘制方法
            try {
                Thread.sleep(40); //添加一个延时 不刷新太快每40ms刷新一次
            } catch (InterruptedException e) { //抛出异常
                e.printStackTrace();
            }
        }
    }

    //是否可以开始游戏
    void begin(){
        if (begin){
            begin=false; //释放状态
            gameSelect.hard(GameUtil.level); //载入选择难度对应的参数
            dispose(); //关闭窗口
            GameWin gameWin=new GameWin(); //重新加载GameWin类 来建立窗口
            GameUtil.START_TIME=System.currentTimeMillis(); //重置开始时间
            GameUtil.FLAG_NUM=0; //重置棋子数
            GameUtil.RIGHT=GameUtil.LEFT=false;
            mapBottom.regame();
            mapTop.regame(); //重新开始游戏
            gameWin.launch(); // 重新创建游戏
        }
    }

    //创建paint的绘制方法用于绘制组件
    public void paint(Graphics g)
    {
        if (GameUtil.state == 3) { //如果是选择难度的状态
            g.setColor(Color.PINK); //给窗口设置颜色
            g.fillRect(0,0,500,500); //进行填充
            gameSelect.paintSelf(g); //绘制选择难度的窗口组件
        }
        else { //其他状态
            offScreenImage = this.createImage(width, height); //初始化新画布组件 宽高与窗口相同
            Graphics gImage = offScreenImage.getGraphics(); //设置一个画笔
            gImage.setColor(Color.PINK); //给画布设置背景颜色
            gImage.fillRect(0, 0, width, height); //并填充整个画布
            //这两个地图都绘制在这个画布中 解决抖动
            mapBottom.paintSelf(gImage); //调用这个画笔 进行底层地图绘制
            mapTop.paintSelf(gImage); //传入画笔 进行顶层地图绘制
            g.drawImage(offScreenImage, 0, 0, null);//添加一个绘制方法 将画布绘制到窗口中
        }
    }

    //程序入口main函数
    public static void main(String[] args)
    {
        GameWin gameWin=new GameWin();
        gameWin.launch();
    }

}
