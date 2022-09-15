import java.awt.*;
// 底层地图 用于绘制游戏底层的相关组件
//进行雷区网格绘制，调用生成地雷、数字的方法并进行绘制

public class MapBottom
{
    BottomRay bottomRay=new BottomRay(); //新建一个底层地雷类
    BottomNum bottomNum=new BottomNum(); //新建一个底层数字类

    {
        //在最开始也要调用一下生成雷、数字的方法
        bottomRay.newRay();
        bottomNum.newNum();
    }

    //重置游戏的方法（底层）
    void regame(){
        for (int i = 1; i <=GameUtil.MAP_W; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) { //首先需要重置雷区
                GameUtil.DATA_BOTTOM[i][j]=0; //全部置为0 无雷
            }
        }
        //调用生成雷、数字的方法
        bottomRay.newRay();
        bottomNum.newNum();
    }

    //绘制方法 传入画笔 绘制底层地雷、数字等元素
    void paintSelf(Graphics g)
    {
        //写一个循环绘制方法来通过画线绘制网格进行雷区的绘制
        //雷区由网格构成 通过画线来绘制雷区 网格是许多线，可通过循环绘制实现
        //画线方法 四个参数：起点坐标和终点坐标 横坐标相等为竖线，纵坐标相等为横线
        g.setColor(Color.GRAY); //线取灰色
        for (int i = 0; i <=GameUtil.MAP_W;i++)//画竖线
        {
            g.drawLine(GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET,
                    GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET+GameUtil.MAP_H*GameUtil.SQUARE_LENGTH);
        }
        for (int i = 0; i <=GameUtil.MAP_H;i++)//画横线
        {
            g.drawLine(GameUtil.OFFSET,
                    3*GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH,
                    GameUtil.OFFSET+GameUtil.MAP_W*GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH);
        }
        //使用两层循环,添加多种素材的绘制方法
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j=1; j <=GameUtil.MAP_H ; j++) {
                //雷的绘制方法
                if (GameUtil.DATA_BOTTOM[i][j] == -1) { //判断：如果是地雷，则生成
                    g.drawImage(GameUtil.lei,
                            GameUtil.OFFSET +(i-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 +(j-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }/* x:窗格左边偏移量+每个方格长度 y:竖着的偏移量+每个方格长度
                 后面的+1是把图片往下挪一个像素值，把红线露出来
                 后两个是定义图片的宽和高 使其接近网格大小，把四周的线露出来 */

                //底层数字的绘制方法
                if (GameUtil.DATA_BOTTOM[i][j]>=0) { //判断：如果是数字，则生成
                    g.drawImage(GameUtil.images[GameUtil.DATA_BOTTOM[i][j]],
                            GameUtil.OFFSET +(i-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 +(j-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }
            }
        }

        //绘制数字 显示剩余雷数（雷的总数-插旗的数量）
        GameUtil.drawWord(g,"余雷数："+(GameUtil.RAY_MAX-GameUtil.FLAG_NUM),
                 GameUtil.OFFSET,
                2*GameUtil.OFFSET,26,Color.RED);
        //绘制倒计时 单位为秒
        GameUtil.drawWord(g,"时间："+(GameUtil.END_TIME-GameUtil.START_TIME)/1000,
                (GameUtil.OFFSET+GameUtil.SQUARE_LENGTH*(GameUtil.MAP_W-2)),
                2*GameUtil.OFFSET,26,Color.RED);

        //用于在游戏窗口上方绘制不同的黄豆表情图片
        switch (GameUtil.state){
            case 0: //游戏中
                GameUtil.END_TIME=System.currentTimeMillis(); //游戏结束时间在游戏中实时刷新
                g.drawImage(GameUtil.youxi,
                        (int) (GameUtil.OFFSET+GameUtil.SQUARE_LENGTH*(GameUtil.MAP_W/2.3)),
                        GameUtil.OFFSET+6,
                        (int) (GameUtil.SQUARE_LENGTH*1.4),
                        (int) (GameUtil.SQUARE_LENGTH*1.4+2),
                        null);
                break;

            case 1: //胜利
                g.drawImage(GameUtil.yes,
                        (int) (GameUtil.OFFSET+GameUtil.SQUARE_LENGTH*(GameUtil.MAP_W/2.3)),
                        GameUtil.OFFSET+6,
                        (int) (GameUtil.SQUARE_LENGTH*1.4),
                        (int) (GameUtil.SQUARE_LENGTH*1.4+2),
                        null);
                break;

            case 2: //失败
                g.drawImage(GameUtil.over,
                        (int) (GameUtil.OFFSET+GameUtil.SQUARE_LENGTH*(GameUtil.MAP_W/2.3)),
                        GameUtil.OFFSET+6,
                        (int) (GameUtil.SQUARE_LENGTH*1.4),
                        (int) (GameUtil.SQUARE_LENGTH*1.4+2),
                        null);
                break;
            default:
        }
    }

}
