package ChineseChessCode;

import javax.swing.*;
import java.awt.*;

/**
 * @author HP电脑
 * 2021年12月20日 下午9:10:31
 * @author 颜文军
 * XTU-202005565712
 * 项目名称：ChineseChess
 * 类名称：Chess
 **/

public class Chess {

    public static final short REDPLAYER = 1;
    public short player;//玩家
    public String typeName;
    public int x,y;//网格地图对应的二维数组的下标
    private Image chessImage;//棋子的图案
    private final int leftX=52;
    private final int leftY=2;

    public Chess(short player,String typeName,int x,int y){//棋子图片
        this.player = player;
        this.typeName = typeName;
        this.x = x;
        this.y = y;
        if(player == REDPLAYER){//判断为红色方
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
        }else{//黑色方
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

    public void setPos(int x,int y){//设置正向棋子坐标
        this.x = x;
        this.y = y;
    }

    public void ReversePos(){//翻转棋子坐标
        x = 9 - x;
        y = 8 - y;
    }

    protected void paint(Graphics g,JPanel i){
        g.drawImage(chessImage, leftX+y*67, leftY+x*60, 55, 55, i);
    }

    //绘制选中框
    public void DrawSelectedChess(Graphics g){
        g.drawRect(leftX+y*67, leftY+x*60, 55, 55);
    }

}