import javax.swing.*;
import java.awt.*;
//帮助界面

public class Help {
    void window()
    {
        //如果点击到了帮助的按钮 即鼠标坐标位于帮助框中
        if (GameUtil.MOUSE_X > 5 && GameUtil.MOUSE_X < 71 && GameUtil.MOUSE_Y > 35 && GameUtil.MOUSE_Y < 73) {
            JFrame jf = new JFrame();//新建一个JFrame窗口
            jf.setSize(900, 700);//设置窗体的宽为900 高为700
            jf.getContentPane().setBackground(Color.PINK);
            jf.setLocationRelativeTo(null);//设置窗口居中
            jf.setLayout(null);//设置布局
            //设置文本内容
            JLabel word1 = new JLabel("扫雷规则");
            JLabel word2 = new JLabel("选择难度时，简单程度地图大小12*12，15颗雷；困难程度地图大小20*16，99颗雷。");
            JLabel word3=new JLabel("游戏中的左上角是剩余雷数，随插旗而减少；右上角是从开始到现在的游戏时间。");
            JLabel word4 =new JLabel("游戏中，点击左键翻开格子，格子底下可能是雷也可能是数字或空白");
            JLabel word5=new JLabel("翻开格子时，底下的数字代表周围3*3区域格子的雷数，空白代表周围无雷。");
            JLabel word6=new JLabel("在你认为底下有雷时，点击右键插旗，再点一下取消插旗。");
            JLabel word7=new JLabel("在把数字周围的雷插完棋后，右键点数字就能翻开周围3*3区域的格子。");
            JLabel word8=new JLabel("踩到雷或者把在把棋子插完后有插错棋子的情况就失败了，");
            JLabel word9=new JLabel("把所有的非雷格子点开或者把所有棋子都插对就胜利。");
            JLabel word10=new JLabel("在任何时候点击游戏上方的黄豆表情就能重新开始。");
            JLabel word11=new JLabel("在任何时候点击鼠标的滚轮就能重新进行难度选择。");
            JLabel word12=new JLabel("关闭窗口选择难度开始游戏吧，祝你顺利! （＾ｖ＾）");
            //设置字体格式大小和位置
            word1.setFont(new Font("宋体",Font.BOLD, 33));
            word1.setBounds(350, 20, 280, 30);
            word2.setFont(new Font("宋体",Font.BOLD, 22));
            word2.setBounds(20, 80,900,20);
            word3.setFont(new Font("宋体",Font.BOLD, 22));
            word3.setBounds(20, 120,900,25);
            word4.setFont(new Font("宋体",Font.BOLD, 22));
            word4.setBounds(20, 160,900,25);
            word5.setFont(new Font("宋体",Font.BOLD, 22));
            word5.setBounds(20, 200,900,25);
            word6.setFont(new Font("宋体",Font.BOLD, 22));
            word6.setBounds(20, 240,900,25);
            word7.setFont(new Font("宋体",Font.BOLD, 22));
            word7.setBounds(20, 280,900,25);
            word8.setFont(new Font("宋体",Font.BOLD, 22));
            word8.setBounds(20, 320,900,25);
            word9.setFont(new Font("宋体",Font.BOLD, 22));
            word9.setBounds(20, 360,900,25);
            word10.setFont(new Font("宋体",Font.BOLD, 22));
            word10.setBounds(20, 400,900,25);
            word11.setFont(new Font("宋体",Font.BOLD, 22));
            word11.setBounds(20, 440,900,25);
            word12.setFont(new Font("宋体",Font.BOLD, 26));
            word12.setBounds(20, 525,900,25);
            //将文字加入
            jf.add(word1);
            jf.add(word2);
            jf.add(word3);
            jf.add(word4);
            jf.add(word5);
            jf.add(word6);
            jf.add(word7);
            jf.add(word8);
            jf.add(word9);
            jf.add(word10);
            jf.add(word11);
            jf.add(word12);
            jf.setVisible(true); //设置窗口可见
        }
    }

}
