import java.awt.*;
// 顶层地图类 用于绘制顶层组件棋子等 翻开格子的方法以及失败胜利的逻辑等

public class MapTop {
    //格子的整数位置 整数坐标
    int temp_x,temp_y;

    //重置游戏的方法（顶层）
    void regame(){
        for (int i = 1; i <=GameUtil.MAP_W; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) { //首先需要重置雷区
                GameUtil.DATA_TOP[i][j]=0; //顶层全部置为0 覆盖状态
            }
        }
    }

    //扫雷过程点击鼠标左右键的判断逻辑 胜利失败翻开等
    void logic(){
        temp_x=temp_y=0; //赋初值 避免temp会一直指向鼠标最后点击的区域
        if(GameUtil.MOUSE_X>GameUtil.OFFSET&&GameUtil.MOUSE_Y>GameUtil.OFFSET*3)
        { //保证temp>0 避免出现负数进行向下取整 使非雷区区域被计算成雷区区域
            //将鼠标的坐标进行计算 转换为每个雷区格子的整数横纵坐标
            temp_x=(GameUtil.MOUSE_X-GameUtil.OFFSET)/GameUtil.SQUARE_LENGTH+1;
            temp_y=(GameUtil.MOUSE_Y-GameUtil.OFFSET*3)/GameUtil.SQUARE_LENGTH+1;
        }

        //如果鼠标点击位置在雷区格子里
        if(temp_x>=1&&temp_x<=GameUtil.MAP_W
                &&temp_y>=1&&temp_y<=GameUtil.MAP_H){
            // 只有顶层在覆盖状态下即未被点击时 判断是鼠标左右键才有意义
            if(GameUtil.LEFT){ //左键被点击
                if(GameUtil.DATA_TOP[temp_x][temp_y]==0){ //如果顶层是覆盖状态
                    GameUtil.DATA_TOP[temp_x][temp_y]=-1; //顶层变为不覆盖 显露底层的状态 即翻开这个格子
                }
                spaceOpen(temp_x,temp_y); //在鼠标左键被点击时 调用打开空格的方法
                GameUtil.LEFT=false; //释放鼠标左键状态
            }
            if (GameUtil.RIGHT){ //右键被点击
                if(GameUtil.DATA_TOP[temp_x][temp_y]==0){ //如果顶层是覆盖状态
                    GameUtil.DATA_TOP[temp_x][temp_y]=1; //覆盖则插旗
                    GameUtil.FLAG_NUM++; //插旗数加
                }
                else if(GameUtil.DATA_TOP[temp_x][temp_y]==1){ //如果顶层是插旗状态
                    GameUtil.DATA_TOP[temp_x][temp_y]=0; //再点击右键表示取消插旗 恢复覆盖
                    GameUtil.FLAG_NUM--; //插旗数减
                }
                else if(GameUtil.DATA_TOP[temp_x][temp_y]==-1){ //如果是被翻开状态
                    numOpen(temp_x,temp_y); //调用翻开数字方法
                }
                GameUtil.RIGHT=false; //释放右键状态
            }
        }
        boom(); //判定失败
        victory(); //判定胜利
    }

    //右键翻开数字的方法
    void numOpen(int x,int y) {
        int count = 0;
        if (GameUtil.DATA_BOTTOM[x][y] > 0) { //保证右键点击的格子是数字
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) { //统计周围3*3区域的棋子数
                    if (GameUtil.DATA_TOP[i][j] == 1) { //是棋子
                        count++;
                    }
                }
            }
            if (count == GameUtil.DATA_BOTTOM[x][y]) { //如果周围棋子数量等于底层的数字
                //证明该格子周围的雷已经被全部判断出来了 可以翻开周围3*3为翻开的格子
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (GameUtil.DATA_TOP[i][j]!=1){ //如果没有插旗
                            GameUtil.DATA_TOP[i][j]=-1; //则翻开
                        }
                        //如果翻开是空格 则进行递归翻开 须保证在雷区中
                        if(i>=1&&i<=GameUtil.MAP_W&&j>=1&&j<=GameUtil.MAP_H){
                            spaceOpen(i,j);
                        }
                    }
                }
            }
        }
    }

    //失败判定方法 true表示失败 false表示未失败
    boolean boom(){
        if (GameUtil.FLAG_NUM==GameUtil.RAY_MAX) { //如果棋子数等于雷总数
            for (int i = 1; i <= GameUtil.MAP_W; i++) {
                for (int j = 1; j <= GameUtil.MAP_H; j++) { //遍历整个雷区
                    if (GameUtil.DATA_TOP[i][j] == 0) { //如果有被覆盖（未翻开）的格子
                        GameUtil.DATA_TOP[i][j] = -1; //则打开所有未打开的格子
                    }
                }
            }
        }
        for (int i = 1; i <=GameUtil.MAP_W; i++) {
            for (int j = 1; j <=GameUtil.MAP_H; j++) { //遍历整个雷区
                //如果底层为雷且顶层未覆盖 即有雷露出 则失败
                if (GameUtil.DATA_BOTTOM[i][j]==-1&&GameUtil.DATA_TOP[i][j]==-1){
                    GameUtil.state=2; //失败状态
                    seeBoom(); //
                    return true;
                }
            }
        }
        return false;
    }

    //失败则显示所有的雷的方法
    void seeBoom(){
        for (int i = 1; i <=GameUtil.MAP_W; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) { //遍历整个雷区
                //如果底层为雷且顶层不是棋子 显示所有雷
                if (GameUtil.DATA_BOTTOM[i][j] == -1 && GameUtil.DATA_TOP[i][j] != 1) {
                    GameUtil.DATA_TOP[i][j] = -1; //变为不覆盖 显示所有雷
                }
                //如果底层不是雷且顶层是棋子 显示插错棋
                if (GameUtil.DATA_BOTTOM[i][j] != -1 && GameUtil.DATA_TOP[i][j] == 1) {
                    GameUtil.DATA_TOP[i][j] = 2; //插错棋状态
                }
            }
        }
    }

    //胜利判断方法 true表示胜利 false表示未胜利
    boolean victory() {
        if (GameUtil.state==2) {
            return false;
        }
        int count = 0; //统计用
        for (int i = 1; i <= GameUtil.MAP_W; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) { //遍历整个雷区
                if (GameUtil.DATA_TOP[i][j] != -1) {
                    count++; //统计未打开格子数
                }
            }
        }
        //如果未翻开格子数等于雷数则胜利
        if (count == GameUtil.RAY_MAX) {
            GameUtil.state=1; //胜利状态
            for (int i = 1; i <= GameUtil.MAP_W; i++) {
                for (int j = 1; j <= GameUtil.MAP_H; j++) {
                    //并把所有未翻开的格子都插上棋
                    if(GameUtil.DATA_TOP[i][j]==0){
                        GameUtil.DATA_TOP[i][j]=1;
                    }
                }
            }
            return true;
        }
        return false;
    }

    //递归打开空格的方法 由于空格周围一定没有雷 可以直接打开周围3*3区域的格子 并以此递归
    void spaceOpen(int x,int y){ //两个参数 鼠标位于格子的坐标
        if(GameUtil.DATA_BOTTOM[x][y]==0) { //如果底层为空=0，直接翻开周围3*3的格子
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    //周围也有格子被覆盖 才进行递归继续翻开
                    if (GameUtil.DATA_TOP[i][j] != -1) { //若该格子顶层被覆盖 即未被打开
                        if (GameUtil.DATA_TOP[i][j] == 1) { //如果这个格子上是棋子
                            GameUtil.FLAG_NUM--; //把它翻开为空并释放棋子数
                        }
                        GameUtil.DATA_TOP[i][j] = -1; //令顶层为-1 表示未覆盖即翻开状态
                        //防止递归访问的数组（格子）越界 必须在雷区中
                        if (i >= 1 && i <= GameUtil.MAP_W && j >= 1 && j <= GameUtil.MAP_H) {
                            spaceOpen(i, j);
                        }
                    }
                }
            }
        }
    }

    //绘制方法 传入画笔 绘制顶层的棋子等元素
    void paintSelf(Graphics g)
    {
        logic(); //判断不同的格子分别需要什么元素
        //使用两层循环,添加绘制方法
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j=1; j <=GameUtil.MAP_H ; j++) {
                //覆盖的绘制
                if (GameUtil.DATA_TOP[i][j] == 0) { //判断：如果为0 是覆盖，则生成覆盖的图案
                    g.drawImage(GameUtil.top,
                            GameUtil.OFFSET +(i-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 +(j-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }/* x:窗格左边偏移量+每个方格长度 y:竖着的偏移量+每个方格长度
                 后面的+1是把图片往下挪一个像素值，把线露出来
                 后两个是定义图片的宽和高 使其接近网格大小，把四周线露出来
                 与底层的绘制坐标相同 */

                //插旗的绘制
                if (GameUtil.DATA_TOP[i][j] ==1) { //判断：如果为1 是插旗，则生成
                    g.drawImage(GameUtil.flag,
                            GameUtil.OFFSET +(i-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 +(j-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }

                //插错旗的绘制
                if (GameUtil.DATA_TOP[i][j] == 2) { //判断：如果为2 是插错棋，则生成
                    g.drawImage(GameUtil.noflag,
                            GameUtil.OFFSET +(i-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 +(j-1)* GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }
            }
        }
    }

}
