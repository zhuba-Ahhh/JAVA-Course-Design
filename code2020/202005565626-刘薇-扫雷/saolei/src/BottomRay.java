// 底层地雷类 用于随机生成初始化地雷

public class BottomRay {
    //定义一维数组来存放雷区坐标 相连两个数代表一个地雷坐标
    //即i为横坐标 i+1为纵坐标
    static int[] rays=new int[GameUtil.RAY_MAX*2]; //大小乘2
    boolean isPlace=true; //表示是否放置 true表示可以
    int x,y; //设置地雷的横纵坐标

    //用于随机生成地雷
    void newRay()
    {
        for (int i = 0; i < rays.length ; i=i+2) { //随机生成地雷坐标，并赋值到一维数组中
           x=(int)(Math.random()*GameUtil.MAP_W+1); //1到宽 横坐标
           y=(int)(Math.random()*GameUtil.MAP_H+1); //1到高 纵坐标
            //判断并避免地雷重合 即随机生成的坐标重合
            for (int j = 0; j < i; j=j+2) {
                if (x == rays[j] && y == rays[j + 1]) { //判断是否重合
                    i = i - 2; //使i-2，回退到上一个坐标
                    isPlace = false; //表示不可放置
                    break;
                }
            }
            if(isPlace){ //循环判断后 如果是可以放置的 则将坐标放入数组
                 rays[i]=x;
                 rays[i+1]=y;
                }
            isPlace=true; //重新设为可放置状态
            }
        //将生成的坐标放置到二维数组即网格中
        for (int i = 0; i <GameUtil.RAY_MAX*2 ; i=i+2) {
            GameUtil.DATA_BOTTOM[rays[i]][rays[i+1]]=-1; //-1表示地雷
        }
    }

}
