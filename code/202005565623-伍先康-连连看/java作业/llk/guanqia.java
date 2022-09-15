package llk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class guanqia{
    BufferedImage guanqiaxuanze;
    public class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            // TODO Auto-generated method stub
            //画图
            g.drawImage(guanqiaxuanze,0,0,null);
        }
    } 
    MyCanvas drawArea = new MyCanvas();
    public void move(){
        drawArea.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(x >= 122 && x <= 314 && y >= 260 && y <= 314){
                    jFrame.setVisible(false);
                    new GameClient(4);
                }
                if(x >= 122 && x <= 314 && y >= 360 && y <= 408){
                    jFrame.setVisible(false);
                    new GameClient(6);
                }
                if(x >= 122 && x <= 314 && y >= 454 && y <= 503){
                    jFrame.setVisible(false);
                    new GameClient(8);
                }
                if(x >= 122 && x <= 314 && y >= 547 && y <= 598){
                    jFrame.setVisible(false);
                    new GameClient(10);
                }
            }
        });
    }
    JFrame jFrame;
    public void init() throws IOException{
        jFrame = new JFrame();
        guanqiaxuanze = ImageIO.read(new File("swing\\guanq.png"));
        jFrame.add(drawArea);
        move();
        jFrame.setResizable(false); 
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(470, 690);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
    public void newG()throws IOException{
        new guanqia().init();
    }
    public static void main(String[] args) throws IOException{
        new guanqia().init();
    }
}


