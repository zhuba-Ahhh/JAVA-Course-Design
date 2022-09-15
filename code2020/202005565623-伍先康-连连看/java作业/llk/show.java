package llk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class show{

    BufferedImage kaishijiemian;

    public class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            // TODO Auto-generated method stub
            //画笔在image位图上话；
            g.drawImage(kaishijiemian,0,0,null);
        }
    } 

    MyCanvas drawArea = new MyCanvas();

    public void dianji() {

        drawArea.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if(x >= 401 && x <= 586 && y >= 299 && y <= 347){
                    jFrame.dispose();
                    try {
                        new guanqia().init();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    
    JFrame jFrame;
    
    public void init() throws IOException{
        jFrame = new JFrame();
        kaishijiemian = ImageIO.read(new File("swing\\back.png"));
        //添加画布
        jFrame.add(drawArea);
        //设置点击事件
        dianji();
        jFrame.setResizable(false); 
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(1000, 570);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
    public static void main(String[] args) throws IOException{
        new show().init();
    }
}


