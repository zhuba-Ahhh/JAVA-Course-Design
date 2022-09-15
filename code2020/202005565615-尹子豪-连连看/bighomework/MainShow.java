package bighomework;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;
/*
 * 连连看首页界面
 */
public class MainShow extends JFrame implements MouseListener{
 
    //首页背景图片 Play键More键
    private ImageIcon img,start;        
    //首页图片宽和高 和临时变量
    private int IMGW, IMGH,STARTW,STARTH;
    //选择开始界面还是关卡界面
    private int option;
    public static void main(String[] args){
        new MainShow();
    }
    public MainShow() 
    {
        option=0;
        setTitle("连连看小游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        img = new ImageIcon("0123.jpg");      //背景图片
        IMGH = img.getIconHeight();                        //得到图片高
        IMGW = img.getIconWidth();                        //得到图片宽
        this.setBounds(200,200,IMGW,IMGH);
        start= new ImageIcon("start.png");
        STARTH = start.getIconHeight();                        //得到图片高
        STARTW = start.getIconWidth();
        this.addMouseListener(this);
		this.setFocusable(true);
        setVisible(true);
        repaint();
    }
    public void init(){//变成关卡模式
        option=1;
        repaint();
    }
    public void paint(Graphics g){
        g.clearRect(0, 0, 800, 700);
        Image background=Toolkit.getDefaultToolkit().getImage("0123"+".jpg");
        g.drawImage(background,0,0,IMGW,IMGH,this);
        if(option==0){
            Image start=Toolkit.getDefaultToolkit().getImage("start"+".png");
            g.drawImage(start,IMGW/2-90,IMGH/2,this);
            // Image more=Toolkit.getDefaultToolkit().getImage("more"+".png");
            // g.drawImage(more,IMGH/2-90,IMGH/2+STARTH,STARTW,STARTH,this);
        }
        else{
            Image easy=Toolkit.getDefaultToolkit().getImage("easy"+".png");
            g.drawImage(easy,(IMGW/2)-102,(IMGH/2),193,23,this);
            Image normal=Toolkit.getDefaultToolkit().getImage("normal"+".png");
            g.drawImage(normal,(IMGW/2)-102,(IMGH/2)+23,193,23,this);
            Image hard=Toolkit.getDefaultToolkit().getImage("hard"+".png");
            g.drawImage(hard,(IMGW/2)-102,(IMGH/2)+46,193,23,this);
        }
    }
    @Override
	public void mousePressed(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        if(option==0){
            if(x>=IMGW/2-90&&x<=STARTW+IMGW/2-90&&y>=IMGH/2&&y<=STARTH+IMGH/2){
                init();
            }
            // if(x>=IMGW/2-90&&x<=STARTW+IMGW/2-90&&y>IMGH/2+STARTH&&y<=2*STARTH+IMGH/2){
            //     setVisible(false);
            //     try {
            //         new Connection(10, 0);
            //     } catch (UnknownHostException e1) {
            //         // TODO Auto-generated catch block
            //         e1.printStackTrace();
            //     } catch (IOException e1) {
            //         // TODO Auto-generated catch block
            //         e1.printStackTrace();
            //     } catch (InterruptedException e1) {
            //         // TODO Auto-generated catch block
            //         e1.printStackTrace();
            //     }
            // }
        }
        else{
            if(x>=(IMGW/2)-102&&x<=(IMGW/2)-102+193){
                if(y>=(IMGH/2)&&y<(IMGH/2)+23){
                    setVisible(false);
                    new GameClient(4, 1);
                }
                else if(y>=(IMGH/2)+23&&y<(IMGH/2)+46){
                    setVisible(false);
                    new GameClient(10, 2);
                }
                else if(y>=(IMGH/2)+46&&y<=(IMGH/2)+69){
                    setVisible(false);
                    new GameClient(10, 3);
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override 
	public void mouseReleased(MouseEvent e) {
    }
    @Override
	public void mouseEntered(MouseEvent e) {
    }
    @Override
	public void mouseExited(MouseEvent e) {
    }
}
