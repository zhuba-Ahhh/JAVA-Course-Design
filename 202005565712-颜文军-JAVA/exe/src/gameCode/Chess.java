package gameCode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class Chess {

    public static final short REDPLAYER = 1;
    public static final short BLACKPLAYER = 0;
    public short player;
    public String typeName;
    public int x,y;//网格地图对应的二维数组的下标
    private Image chessImage;//棋子图案
    private int leftX=52,leftY=2;

    public Chess(short player,String typeName,int x,int y){
        this.player = player;
        this.typeName = typeName;
        this.x = x;
        this.y = y;
        if(player == REDPLAYER){
            switch (typeName){
                case "帅":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess7.png");
                    break;
                case "仕":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess8.png");
                    break;
                case "相":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess9.png");
                    break;
                case "马":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess10.png");
                    break;
                case "车":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess11.png");
                    break;
                case "炮":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess12.png");
                    break;
                case "兵":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess13.png");
                    break;
            }
        }else{
            switch(typeName){
                case "将":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess0.png");
                    break;
                case "士":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess1.png");
                    break;
                case "象":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess2.png");
                    break;
                case "马":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess3.png");
                    break;
                case "车":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess4.png");
                    break;
                case "炮":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess5.png");
                    break;
                case "卒":
                    chessImage = Toolkit.getDefaultToolkit().getImage("src/Picture/chess6.png");
                    break;
            }
        }
    }

    public void setPos(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void ReversePos(){
        x = 9 - x;
        y = 8 - y;
    }

    protected void paint(Graphics g,JPanel i){
        g.drawImage(chessImage, leftX+y*67, leftY+x*60, 55, 55,(ImageObserver)i);
    }

    //绘画选中框
    public void DrawSelectedChess(Graphics g){
        g.drawRect(leftX+y*67, leftY+x*60, 55, 55);
    }

}
