package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Opration;

public class StaticPanel extends JPanel{ //画布设置
    JButton left; //设置按钮
    JButton righ;
    JButton down;
    JButton rota;
    JButton stst;
    JButton sett;
    JButton logi;
    
    /**
     * 序列化
     */
    private static final long serialVersionUID = 991741388603869262L;

    StaticPanel(Opration opration){
        left = opration.left;
        righ = opration.righ;
        down = opration.down;
        rota = opration.rota;
        stst = opration.stst;
        sett = opration.sett;
        logi = opration.logi;
        setBounds(0,0,360,600);//也是覆盖在窗口之上
        setLayout(null);
        setOpaque(false);//背景颜色
        setstst();
        left.setBounds(233, 255, 45, 45); //设置按钮位置，大小
        righ.setBounds(278, 255, 45, 45);
        down.setBounds(233, 300, 45, 45);
        rota.setBounds(278, 300, 45, 45);
        stst.setBounds(233, 350, 90, 50);
        sett.setBounds(240, 510, 48, 48);
        logi.setBounds(290, 510, 48, 48);
        add(left); //添加按钮
        add(righ);
        add(down);
        add(rota);
        add(stst);
        add(sett);
        add(logi);
    }

    /**
     * 设置按钮
     */
    private void setstst() {
        stst.setContentAreaFilled(false);
        stst.setFocusPainted(false);
        stst.setFont(new Font("华文新魏",Font.PLAIN,25));
        stst.setForeground(Color.WHITE);
        stst.setFocusable(false);
    }

    @Override //检查后面写的是否是重载方法
    public void paintComponent(Graphics g){ //画笔绘制区域
        //继承父类绘制
        super.paintComponent(g);

        g.setColor(new Color(150,150,150,70)); //颜色设置
        //主屏
        g.fillRect(15, 30, 10*20, 360); //位置大小设置,横向10个方块，纵向18个方块
        //排名区
        g.fillRect(15, 405, 200, 130);
        //右侧排版
        g.fillRect(223, 20, 110, 400);
        g.setColor(new Color(2,2,2,50));
        //得分区
        g.fillRect(233, 30, 90, 70);
        //提示区
        g.fillRect(233, 105, 90, 140);
        //操作区
        g.fillRect(233, 255, 90, 90);
        //边框
        g.setColor(Color.white); //游戏区描白色的边框
        ((Graphics2D)g).setStroke(new BasicStroke(3L)); //画笔转换成二维画笔才能改宽度信息
        g.drawRect(13,28,204,364); //排行榜描白色的边框
        g.drawRect(13,403,204,134);
        ((Graphics2D)g).setStroke(new BasicStroke(1L));
        g.setFont(new Font("黑体",Font.PLAIN,23)); //字体,粗或斜,大小
        g.setColor(Color.white);
        g.drawString("得分", 240, 53); //添加字，字的位置
        g.drawString("下一个",236,140);
        g.drawString("排行榜",25,435);
    }
}
