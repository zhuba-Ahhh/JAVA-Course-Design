import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JButton;
import java.io.*;
public class start {
    private JFrame f = new JFrame("开始界面");
    private class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            }
    }

    MyCanvas drawArea = new MyCanvas();

    Panel p = new Panel();
    JButton b1 = new JButton("客户机");
    JButton b2 = new JButton("服务器");
    JButton b3 = new JButton("AI对战");


    public void init() {
        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Main a = new Main();
                try {
                    a.init();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        b2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SeverMain a = new SeverMain();
                try {
                    a.init();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        b3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AI a = new AI();
                a.init();
            }
        });

        p.add(b1);
        p.add(b2);
        p.add(b3);

        f.setLocation(550,330);
        f.add(p,BorderLayout.SOUTH);
        f.add(drawArea);
        f.pack();
        f.setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        new start().init();
    }
}
