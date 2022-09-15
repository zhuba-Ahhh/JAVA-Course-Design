import java.awt.*;
//难度选择类

public class GameSelect {
    //用于判断鼠标是否点击到了难度选择框
    boolean hard(){
        if(GameUtil.MOUSE_X>100&&GameUtil.MOUSE_X<400){ //两个框横坐标相同 只需要判断纵坐标
            if(GameUtil.MOUSE_Y>145&&GameUtil.MOUSE_Y<245){ // 点击到了简单界面
                GameUtil.level=1; //简单程度
                GameUtil.state=0; //进入游戏状态
                return true;
            }
            if(GameUtil.MOUSE_Y>310&&GameUtil.MOUSE_Y<410){ // 点击到了困难界面
                GameUtil.level=2; //困难程度
                GameUtil.state=0; //进入游戏状态
                return true;
            }
        }
        return false;
    }

    //用于绘制进行难度选择的窗口以及帮助窗口
    void paintSelf(Graphics g){
        g.setColor(Color.BLACK); //窗口框为黑色

        GameUtil.drawWord(g,"难度选择",192,100,28,Color.BLACK);

        g.drawRect(100,145,300,100);
        GameUtil.drawWord(g,"简单",220,205,30,Color.BLACK);

        g.drawRect(100,310,300,100);
        GameUtil.drawWord(g,"困难",220,370,30,Color.BLACK);

        g.drawRect(5,35,66,38);
        GameUtil.drawWord(g,"帮助",18,62,19,Color.BLACK);
    }

    //用于给不同难度重置雷数和地图大小
    void hard(int level){
        switch (level){
            case 1: //简单
                GameUtil.RAY_MAX=15; //15个雷
                GameUtil.MAP_W=12;
                GameUtil.MAP_H=12; //地图大小12*12
                break;

            case 2: //困难
                GameUtil.RAY_MAX=99; //99个雷
                GameUtil.MAP_W=20;
                GameUtil.MAP_H=16; //地图大小20*16
                break;
            default:
        }
    }

}



