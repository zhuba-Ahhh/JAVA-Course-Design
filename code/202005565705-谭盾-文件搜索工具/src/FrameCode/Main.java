package FrameCode;
import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ToolCode.*;

public class Main
{
    public static void main(String[] args)
    {
        FontSet.loadIndyFont();
        EventQueue.invokeLater(()->
        {
            UserFrame frame=new UserFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("�ļ����ҹ���");
            frame.setIconImage(new ImageIcon("icon\\icon.png").getImage());
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }
}
