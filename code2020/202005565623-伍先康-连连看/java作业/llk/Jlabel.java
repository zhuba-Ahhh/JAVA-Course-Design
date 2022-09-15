package llk;

import javax.swing.*;
import java.awt.*;

public class Jlabel {

    public Jlabel() {
        JFrame jf = new JFrame("帮助");
        jf.setSize(500, 500);
        Box vBox = Box.createVerticalBox();
        JLabel label01 = new JLabel();
        label01.setText("提示1:如果找不到可以消除的一组，可以点击提示。");
        label01.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label02 = new JLabel();
        label02.setText("提示2:如果整个图中不存在可以消除的一组，请点击刷新。");
        label02.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label03 = new JLabel();
        label03.setText("···游戏介绍: “连连看”是一款来源于我国台湾的桌面小游戏，主要考验的是");
        label03.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label04 = new JLabel();
        label04.setText("玩家们的眼力，在有限的时间内，只要能把所有能连接的相同图案，两个");
        label04.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label05 = new JLabel();
        label05.setText("两个的找出来，每找到一对，它们就会自动消失，只要在规定时间内能把");
        label05.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label06 = new JLabel();
        label06.setText("所有的图案全部消完即可获得胜利。所谓能够连接，是指无论横向还是纵");
        label06.setFont(new Font(null, Font.PLAIN, 20)); 
        JLabel label07 = new JLabel();
        label07.setText("向，从一个图案到另一个图案之间的连线拐角不能超过两个（中间的直线");
        label07.setFont(new Font(null, Font.PLAIN, 20)); 
        JLabel label08 = new JLabel();
        label08.setText("不超过三根），其中连线不能从尚未消去的图案上经过。");
        label08.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label09 = new JLabel();
        label09.setText("···提示:");
        label09.setFont(new Font(null, Font.PLAIN, 20));  
        JLabel label10 = new JLabel();
        label10.setText("注意：由于游戏较为简单，为使排行榜更具大众化，设置满分的成绩只记");
        label10.setFont(new Font(null, Font.PLAIN, 20)); 
        JLabel label11 = new JLabel();
        label11.setText("录在“第一名”，请谅解。");
        label11.setFont(new Font(null, Font.PLAIN, 20));  
        vBox.add(label03);
        vBox.add(label04);
        vBox.add(label05);
        vBox.add(label06);
        vBox.add(label07);
        vBox.add(label08);
        vBox.add(label09);
        vBox.add(label01);
        vBox.add(label02);
        vBox.add(label10);
        vBox.add(label11);
        jf.setContentPane(vBox);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }
    public static void main(String[] args) {
        new Jlabel();
    }
    public void setText(String string) {
    }
}