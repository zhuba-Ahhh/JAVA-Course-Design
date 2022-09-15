package dd;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;

public class Function extends JPanel{

    public int num = 0;//记录函数的个数(最多5个)
    public JPanel fun[] = new JPanel[10];
    public JTextField textField[] = new JTextField[10];
    public JButton Ad = new JButton("新增函数(最多5个)");
    public JButton del = new JButton("删除末尾函数");
    public JButton some = new JButton("注意事项");

    public Function() {
        setLayout(null);
        setBorder(BorderFactory.createEtchedBorder());
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        setSize(200,600);

        //顶上标签
        JLabel tmp = new JLabel("函数", JLabel.CENTER);
        tmp.setOpaque(true);
        tmp.setBackground(Color.DARK_GRAY);
        tmp.setForeground(Color.white);
        add(tmp);
        tmp.setBounds(0, 0, 200, 25);//剩余575

        add(Ad);
        Ad.setContentAreaFilled(false);
        Ad.setFocusPainted(false);//去焦点
        // Ad.setBorderPainted(false);//去边框
        Ad.setContentAreaFilled(false);
        Ad.setBounds(0,25,150,25);//剩余510

        add(del);
        del.setContentAreaFilled(false);
        del.setFocusPainted(false);//去焦点
        // Ad.setBorderPainted(false);//去边框
        del.setContentAreaFilled(false);
        del.setBounds(0,50,150,25);//剩余485

        add(some);
        some.setBounds(0, 75, 150, 25);
        some.setFocusPainted(false);
        some.setContentAreaFilled(false);
    }

}