//底层数字类 在生成地雷后确定底层的数字

public class BottomNum {
    //用于生成数字
    void newNum(){
        //遍历整个二维网格数组 将存储数字的数组进行初始化
        for (int i=1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                if(GameUtil.DATA_BOTTOM[i][j]==-1){ //如果这个位置是雷
                    // 遍历雷中3*3的区域
                    for (int k =i-1; k <=i+1 ; k++) {
                        for (int l =j-1; l <=j+1 ; l++) {
                            if(GameUtil.DATA_BOTTOM[k][l]>=0){ //如果不是雷
                                GameUtil.DATA_BOTTOM[k][l]++;
                            }
                        }
                    }
                }
            }
        }
    }

}
