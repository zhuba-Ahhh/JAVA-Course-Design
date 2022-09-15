package ChineseChessCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author HP电脑
 * 2021年12月21日 下午6:18:39
 * @author 颜文军
 * XTU-202005565712
 * 项目名称：ChineseChess
 * 类名称：ChessBoard
 **/

public class ChessBoard extends JPanel implements Runnable{

    public static final short REDPLAYER = 1;
    public static final short BLACKPLAYER = 0;
    public Chess[] chess = new Chess[32];//棋子数组
    public int[][] map = new int[10][9];//存储棋盘布局的信息的10行9列数组
    public int[][] map1 = new int[10][9];//对长将情况的棋盘存储信息
    public int[][] map2 = new int[10][9];
    public int[][] map3 = new int[10][9];
    public int times = 0;
    private Chess firstChess = null;
    private Chess secondChess = null;
    private boolean isFirstClick = true;//标记是否第一次点击
    private int x1,y1,x2,y2;
    private int tempX,tempY;
    private boolean isMyTurn = true;//标记是否自己执子
    public short LocalPlayer = REDPLAYER;//记录当前执子方
    private String message;//提示信息
    private boolean flag = false;
    private int otherPort=3003;//对方端口
    private int receivePort=3004;//本地端口
    public ArrayList<Node> list = new ArrayList<>();//存储棋谱
    private String ip = "169.254.245.61";


    /**
     * 初始化棋盘布局信息为空
     */
    private void initMap(){
        int i,j;
        for(i=0;i<10;i++){
            for(j=0;j<9;j++){
                map[i][j]= -1;
            }
        }
    }


    public ChessBoard(){
        initMap();
        message = "程序处于等待联机状态!";

        //监测鼠标
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(!isMyTurn){
                    message = "现在该对方走棋";
                    repaint();
                    return ;
                }
                selectedChess(e);
                repaint();
            }

            //选取棋子
            private void selectedChess(MouseEvent e) {

                int index1,index2;//保存第一次和第二次被单击的棋子对应数组下标
                if(isFirstClick){//第一次点击
                    firstChess = analyse(e.getX(),e.getY());
                    x1 = tempX;//记录点击处位于棋盘第几行
                    y1 = tempY;
                    if(firstChess != null){
                        if(firstChess.player != LocalPlayer){
                            message = "点击成对方棋子了";
                            return ;
                        }
                        isFirstClick = false;
                    }
                }
                else{
                    secondChess = analyse(e.getX(), e.getY());
                    x2 = tempX;
                    y2 = tempY;
                    if(secondChess!=null){//如果第二次点击选中了棋子
                        if(secondChess.player == LocalPlayer){//如果第二次点击的棋子是自己的棋子，则对第一次选中的棋子进行更换
                            firstChess = secondChess;
                            x1 = tempX;
                            y1 = tempY;
                            secondChess = null;
                            return ;
                        }
                    }

                    if(secondChess == null){//如果目标处没有棋子,判断是否可以走棋
                        if(IsAbleToMove(firstChess,x2,y2)){
                            if(!BeCheckedByDesign(firstChess, x2, y2)){
                                index1 = map[x1][y1];
                                map[x1][y1] = -1;
                                map[x2][y2] = index1;
                                chess[index1].setPos(x2, y2);
                                //send
                                send("move"+"|"+index1 + "|"+(9-x2)+"|"+(8-y2)+"|"+(9-x1)+"|"+(8-y1)+"|"+"-1|");
                                list.add(new Node(index1,x2,y2,x1,y1,-1));//存储我方下棋信息
                                //置第一次选中标记量为空
                                isFirstClick = true;
                                repaint();
                                SetMyTurn(false);//该对方了
                            }
                            else {
                                message = "不能送将";
                                return;
                            }
                        }else{
                            message = "不符合走棋规则";
                        }
                        return ;
                    }

                    if(IsAbleToMove(firstChess, x2, y2)){//可以吃子
                        if(!BeCheckedByDesign(firstChess,x2,y2)) {
                            isFirstClick = true;
                            index1 = map[x1][y1];
                            index2 = map[x2][y2];
                            map[x1][y1] = -1;
                            map[x2][y2] = index1;
                            chess[index1].setPos(x2, y2);
                            chess[index2] = null;
                            repaint();
                            send("move" + "|" + index1 + "|" + (9 - x2) + "|" + (8 - y2) + "|" + (9 - x1) + "|" + (8 - y1) + "|" + index2 + "|");
                            list.add(new Node(index1, x2, y2, x1, y1, index2));//记录我方下棋信息
                            SetMyTurn(false);//该对方了
                        }
                        else {
                            message = "不能送将";
                        }

                    }else{//不能吃子
                        message = "不能吃子";
                    }

                }

            }

            private Chess analyse(int x, int y) {

                int leftX = 52,leftY = 2;
                int index_x=-1,index_y=-1;//记录点击处是第几行第几列
                for(int i=0;i<=9;i++){
                    for(int j=0;j<=8;j++){
                        Rectangle r = new Rectangle(leftX+j*67, leftY+i*60, 55, 55);
                        if(r.contains(x,y)){
                            index_x = i;
                            index_y = j;
                            break;
                        }
                    }
                }
                tempX = index_x;
                tempY = index_y;

                if(index_x == -1){//没有点击到任何棋盘可点击处
                    return null;
                }

                if(map[index_x][index_y]==-1){
                    System.out.println("点击了("+tempX+","+tempY+")");
                    return null;
                }else{
                    System.out.println("点击了("+tempX+","+tempY+"),此处的棋子是"+chess[map[index_x][index_y]].typeName);
                    return chess[map[index_x][index_y]];
                }

            }
        });
    }

    private void SetMyTurn(boolean b) {

        isMyTurn = b;
        if(b){
            message = "请您开始走棋";
        }else{
            message = "对方正在思考";
        }

    }

    /**
     * 将棋子回退到上一步的位置，并把棋子未回退前的棋盘位置信息清空
     */
    private void rebackChess(int index,int x,int y,int oldX,int oldY){
        chess[index].setPos(oldX, oldY);
        map[oldX][oldY] = index;//棋子放回到(oldX,oldY)
        map[x][y] = -1;//棋盘里原有棋子位置信息清除
    }

    /**
     * 将一个被吃了的子重新放回到棋盘，传入参数说明：index棋子数组下标，第x行，第y列
     */

    private void resetChess(int index,int x,int y){
        short temp = index<16?BLACKPLAYER:REDPLAYER;//存储是哪方的棋子
        String name = null;//存储棋子上的字
        switch(index){//根据棋子索引，得到棋子上面的字
            case 0:name="将";break;
            case 1:
            case 2:name="士";break;
            case 3:
            case 4:name="象";break;
            case 5:
            case 6:
            case 21:
            case 22:
                name="马";break;
            case 7:
            case 8:
            case 23:
            case 24:
                name="车";break;
            case 9:
            case 10:
            case 25:
            case 26:
                name="炮";break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:name="卒";break;
            case 16:name="帅";break;
            case 17:
            case 18:name="仕";break;
            case 19:
            case 20:name="相";break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:name="兵";break;
        }

        chess[index] = new Chess(temp,name,x,y);
        map[x][y] = index;//将棋子放回到棋盘
    }

    public void startJoin(String ip,int otherPort,int receivePort){
        flag = true;
        this.otherPort = otherPort;
        this.receivePort = receivePort;
        this.ip = ip;
        System.out.println("能帮我连接到"+ip+"吗");
        send("join|");
        Thread th = new Thread(this);
        th.start();
        //
    }

    /**
     * 初始化游戏
     */
    public void startNewGame(short player){
        initMap();
        initChess();
        if(player == BLACKPLAYER){
            reverseBoard();
        }
        repaint();
    }

    /**
     * 初始化棋子布局
     */
    private void initChess(){
        //布置黑方棋子
        chess[0] = new Chess(BLACKPLAYER,"将",0,4);//第0行第4列
        map[0][4] = 0;
        chess[1] = new Chess(BLACKPLAYER,"士",0,3);//第0行第3列
        map[0][3] = 1;
        chess[2] = new Chess(BLACKPLAYER,"士",0,5);//第0行第5列
        map[0][5] = 2;
        chess[3] = new Chess(BLACKPLAYER,"象",0,2);//第0行第2列
        map[0][2] = 3;
        chess[4] = new Chess(BLACKPLAYER,"象",0,6);//第0行第6列
        map[0][6] = 4;
        chess[5] = new Chess(BLACKPLAYER,"马",0,1);//第0行第1列
        map[0][1] = 5;
        chess[6] = new Chess(BLACKPLAYER,"马",0,7);//第0行第7列
        map[0][7] = 6;
        chess[7] = new Chess(BLACKPLAYER,"车",0,0);//第0行第0列
        map[0][0] = 7;
        chess[8] = new Chess(BLACKPLAYER,"车",0,8);//第0行第8列
        map[0][8] = 8;
        chess[9] = new Chess(BLACKPLAYER,"炮",2,1);//第2行第1列
        map[2][1] = 9;
        chess[10] = new Chess(BLACKPLAYER,"炮",2,7);//第2行第7列
        map[2][7] = 10;
        for(int i=0;i<5;i++){//5个黑方卒布局
            chess[11+i] = new Chess(BLACKPLAYER,"卒",3,i*2);
            map[3][i*2] = 11+i;
        }


        //布置红方棋子
        chess[16] = new Chess(REDPLAYER,"帅",9,4);//第9行第4列
        map[9][4] = 16;
        chess[17] = new Chess(REDPLAYER,"仕",9,3);//第9行第3列
        map[9][3] = 17;
        chess[18] = new Chess(REDPLAYER,"仕",9,5);//第9行第5列
        map[9][5] = 18;
        chess[19] = new Chess(REDPLAYER,"相",9,2);//第9行第2列
        map[9][2] = 19;
        chess[20] = new Chess(REDPLAYER,"相",9,6);//第9行第6列
        map[9][6] = 20;
        chess[21] = new Chess(REDPLAYER,"马",9,1);//第9行第1列
        map[9][1] = 21;
        chess[22] = new Chess(REDPLAYER,"马",9,7);//第9行第7列
        map[9][7] = 22;
        chess[23] = new Chess(REDPLAYER,"车",9,0);//第9行第0列
        map[9][0] = 23;
        chess[24] = new Chess(REDPLAYER,"车",9,8);//第9行第8列
        map[9][8] = 24;
        chess[25] = new Chess(REDPLAYER,"炮",7,1);//第7行第1列
        map[7][1] = 25;
        chess[26] = new Chess(REDPLAYER,"炮",7,7);//第7行第7列
        map[7][7] = 26;

        for(int i=0;i<5;i++){//5个红方兵布局
            chess[27+i] = new Chess(REDPLAYER,"兵",6,i*2);
            map[6][i*2] = 27+i;
        }

    }

    /**
     * 倒置棋盘
     */

    private void reverseBoard(){
        //对棋子的位置进行互换
        for(int i=0;i<32;i++){
            if(chess[i]!=null){
                chess[i].ReversePos();
            }
        }

        //对两方的棋盘信息进行倒置互换
        for(int i=0;i<5;i++){
            for(int j=0;j<9;j++){
                int temp = map[i][j];
                map[i][j] = map[9-i][8-j];
                map[9-i][8-j] = temp;
            }
        }

    }

    /**
     *对场景对象进行绘画
     */

    @Override
    public void paint(Graphics g){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize.getWidth();
        screenSize.getHeight();
        g.clearRect(0,0, this.getWidth(), this.getHeight());
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage("src/Picture/ChessBoard.png");
        g.drawImage(backgroundImage,50,0,600,600,this);
        for(int i=0;i<32;i++){
            if(chess[i]!=null){
                chess[i].paint(g, this);
            }
        }
        if(firstChess!=null){
            firstChess.DrawSelectedChess(g);
        }
        if(secondChess!=null){
            secondChess.DrawSelectedChess(g);
        }
        g.drawString(message, 290, 620);

    }

    /**
     *判断是否可以落子
     */
    private boolean IsAbleToMove(Chess firstChess, int x, int y) {

        int oldX,oldY;
        oldX = firstChess.x;
        oldY = firstChess.y;
        String chessName = firstChess.typeName;
        if(chessName.equals("将")||chessName.equals("帅")){

            if((x-oldX)*(y-oldY)!=0){//如果是斜着走
                return false;
            }
            if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果横走或竖走超过一格
                return false;
            }
            //如果超出九宫格区域
            return (x <= 2 || x >= 7) && y >= 3 && y <= 5;
        }

        if(chessName.equals("士")||chessName.equals("仕")){
            if((x-oldX)*(y-oldY)==0){//如果横走或者竖走
                return false;
            }
            if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果横向或者纵向的位移量大于1，即不是斜走一格
                return false;
            }
            //如果超出九宫格区域
            return (x <= 2 || x >= 7) && y >= 3 && y <= 5;
        }

        if(chessName.equals("相")||chessName.equals("象")){
            if((x-oldX)*(y-oldY)==0){//如果横走或者竖走
                return false;
            }
            if(Math.abs(x-oldX)!=2||Math.abs(y-oldY)!=2){//如果横向或者纵向的位移量不同时为2，即不是走田字
                return false;
            }
            if(x<5){//如果象越过“楚河-汉界”
                return false;
            }
            if(x>9){//如果象超出棋盘边界
                return false;
            }

            int i=0,j=0;//记录象眼位置
            if(x-oldX==2){//象向下跳
                i=oldX+1;
            }
            if(x-oldX==-2){//象向上跳
                i=oldX-1;
            }
            if(y-oldY==2){//象向右跳
                j=oldY+1;
            }
            if(y-oldY==-2){//象向左跳
                j=oldY-1;
            }
            //被堵象眼
            return map[i][j] == -1;
        }

        if(chessName.equals("马")){
            if(Math.abs(x-oldX)*Math.abs(y-oldY)!=2){//如果横向位移量乘以竖向位移量不等于2,即如果马不是走日字
                return false;
            }
            if(x-oldX==2){//如果马向下跳，并且横向位移量为1，纵向位移量为2
                if(map[oldX+1][oldY]!=-1){//如果被绊马脚
                    return false;
                }
            }
            if(x-oldX==-2){//如果马向上跳，并且横向位移量为1，纵向位移量为2
                if(map[oldX-1][oldY]!=-1){//如果被绊马脚
                    return false;
                }
            }
            if(y-oldY==2){//如果马向右跳，并且横向位移量为2，纵向位移量为1
                if(map[oldX][oldY+1]!=-1){//如果被绊马脚
                    return false;
                }
            }

            if(y-oldY==-2){//如果马向左跳，并且横向位移量为2，纵向位移量为1
                //如果被绊马脚
                return map[oldX][oldY - 1] == -1;
            }

            return true;
        }

        if(chessName.equals("车")){
            if((x-oldX)*(y-oldY)!=0){//如果横向位移量和纵向位移量同时都不为0，说明车在斜走，故return false
                return false;
            }
            if(x!=oldX){//如果车纵向移动
                if(oldX>x){//将判断过程简化为纵向从上往下查找中间是否有其他子
                    int t = x;
                    x = oldX;
                    oldX = t;
                }
                for(int i=oldX+1;i<x;i++){
                    if(map[i][oldY]!=-1){//如果中间有其他子
                        return false;
                    }
                }
            }

            if(y!=oldY){//如果车横向移动
                if(oldY>y){//将判断过程简化为横向从左到右查找中间是否有其他子
                    int t = y;
                    y = oldY;
                    oldY = t;
                }
                for(int i=oldY+1;i<y;i++){
                    if(map[oldX][i]!=-1){//如果中间有其他子
                        return false;
                    }
                }
            }

            return true;
        }

        if(chessName.equals("炮")){
            boolean swapFlagX = false;//记录纵向棋子是否交换过
            boolean swapFlagY = false;//记录横向棋子是否交换过
            if((x-oldX)*(y-oldY)!=0){//如果棋子斜走
                return false;
            }
            int c = 0;//记录两子中间有多少个子
            if(x!=oldX){//如果炮是纵向移动
                if(oldX>x){//简化后续判断
                    int t = x;
                    x = oldX;
                    oldX = t;
                    swapFlagX = true;
                }
                for(int i=oldX+1;i<x;i++){
                    if(map[i][oldY]!=-1){//如果中间有子
                        c += 1;
                    }
                }

            }
            if(y!=oldY){//如果炮是横向 移动
                if(oldY>y){//简化后续判断
                    int t = y;
                    y = oldY;
                    oldY = t;
                    swapFlagY = true;
                }
                for(int i=oldY+1;i<y;i++){
                    if(map[oldX][i]!=-1){//如果中间有子
                        c += 1;
                    }
                }
            }

            if(c>1){//中间超过一个子
                return false;
            }

            if(c==0){//如果中间没有子
                if(swapFlagX){//如果之间交换过，需要重新交换回来
                    int t = x;
                    x = oldX;
                    oldX = t;
                }
                if(swapFlagY){
                    int t = y;
                    y = oldY;
                    oldY = t;
                }
                if(map[x][y]!=-1){//如果目标处有子存在，则不能移动
                    return false;
                }
            }

            if(c==1){//如果中间只有一个子
                if(swapFlagX){//如果之间交换过，需要重新交换回来
                    x = oldX;
                }
                if(swapFlagY){
                    y = oldY;
                }
                //如果目标处没有棋子，即不能打空炮
                return map[x][y] != -1;
            }

            return true;
        }

        if(chessName.equals("卒")||chessName.equals("兵")){
            if((x-oldX)*(y-oldY)!=0){//如果斜走
                return false;
            }
            if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果一次移动了一格以上
                return false;
            }

            if(oldX>=5){//如果兵未过河，则只能向上移动,不能左右移动
                if(Math.abs(y-oldY)>0){//没过河尝试左右移动
                    return false;
                }
                //兵向下移动
            }   //如果已经过河，可以进行上左右移动，但不能进行向下移动
                //兵向下移动

            return x - oldX != 1;
        }

        return false;
    }

    /**
     *判断此时是否正被将军
     */
    private boolean beingChecked(int[][] map){
        int[] GNorth;
        int[] GSouth;
        int[] GEast;
        int[] GWest;
        int chessNumInLine=0;
        int Gx=9,Gy=4;
        if(LocalPlayer== REDPLAYER) {

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if (map[i][j] == 16) {
                        Gx = i;
                        Gy = j;
                    }
                }
            }
            //System.out.println(Gx);
            //System.out.println(Gy);

            //获取行棋后将的十字方向棋子
            GSouth = new int[10 - Gx];
            GNorth = new int[Gx];
            GEast = new int[8 - Gy];
            GWest = new int[Gy];
            for (int i = Gx + 1,j=0; i < 10; i++,j++) {
                if (GSouth.length == 1) {
                    break;
                } else {
                    GSouth[j]=map[i][Gy];
                }
            }
            for (int i = Gx - 1,j=0; i >= 0; i--,j++) {
                GNorth[j]=map[i][Gy];
            }
            for (int i = Gy + 1,j=0; i < 9; i++,j++) {
                GEast[j]=map[Gx][i];
            }
            for (int i = Gy - 1,j=0; i >= 0; i--,j++) {
                GWest[j]=map[Gx][i];
            }

            //判断马将
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if (map[i][j] == 5 || map[i][j] == 6) {
                        if (Math.abs(i - Gx) == 2 && Math.abs(j - Gy) == 1) {
                            //马腿算法
                            if(i-Gx==2&&map[i-1][j]==-1){
                                return true;
                            }
                            else if(Gx-i==2&&map[i+1][j]==-1){
                                return true;
                            }
                        }
                        else if(Math.abs(j - Gy) == 2 && Math.abs(i - Gx) == 1){
                            //马腿算法
                            if(j-Gy==2&&map[i][j-1]==-1){
                                return true;
                            }
                            else if(Gy-j==2&&map[i][j+1]==-1){
                                return true;
                            }
                        }
                    }
                }
            }

            //判断兵将
            for (int i = 11; i < 16; i++) {
                if (map[Gx - 1][Gy] == i) {
                    return true;
                }
            }
            for (int i = 11; i < 16; i++) {
                if (map[Gx][Gy + 1] == i) {
                    return true;
                }
            }
            for (int i = 11; i < 16; i++) {
                if (map[Gx][Gy - 1] == i) {
                    return true;
                }
            }

            //<判断将所在直线将> 车和将帅实质是一样的

            //(1)判断车将
            for (int i = 0; i < GSouth.length; i++) {
                if(chessNumInLine==0){
                    if (GSouth[i] == 7 || GSouth[i] == 8) {
                        return true;
                    }
                    else if(GSouth[i]!=-1){
                        chessNumInLine++;
                    }
                }else if (chessNumInLine == 1) {
                    if (GSouth[i] == 9 || GSouth[i] == 10) {
                        return true;
                    }
                    else if(GNorth[i]!=-1){
                        chessNumInLine++;
                    }
                }
            }
            chessNumInLine=0;

            //(2)上方照面将
            for (int j : GNorth) {
                if (chessNumInLine == 0) {
                    if (j == 0 || j == 7 || j == 8) {
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 9 || j == 10) {
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                } else {
                    break;
                }
            }

            chessNumInLine=0;
            for (int j : GEast) {

                if (chessNumInLine == 0) {
                    if (j == 0 || j == 7 || j == 8) {
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 9 || j == 10) {
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                } else {
                    break;
                }
            }

            chessNumInLine=0;
            for (int j : GWest) {

                if (chessNumInLine == 0) {
                    if (j == 7 || j == 8) {
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 9 || j == 10) {
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                } else {
                    break;
                }
            }
            chessNumInLine=0;
        }

        if (LocalPlayer == BLACKPLAYER) {
            //获取行棋后己将的位置
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if (map[i][j] == 0) {
                        Gx = i;
                        Gy = j;
                    }
                }
            }
            //获取行棋后将的十字方向棋子
            GSouth = new int[10 - Gx];
            GNorth = new int[Gx];
            GEast = new int[8 - Gy];
            GWest = new int[Gy];
            for (int i = Gx + 1,j=0; i < 10; i++,j++) {
                if (GSouth.length == 1) {
                    break;
                } else {
                    GSouth[j]=map[i][Gy];
                }
            }
            for (int i = Gx - 1,j=0; i >= 0; i--,j++) {
                GNorth[j]=map[i][Gy];
            }
            for (int i = Gy + 1,j=0; i < 9; i++,j++) {
                GEast[j]=map[Gx][i];
            }
            for (int i = Gy - 1,j=0; i >= 0; i--,j++) {
                GWest[j]=map[Gx][i];
            }
            //判断马将
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 9; j++) {
                    if (map[i][j] == 21 || map[i][j] == 22) {
                        if (Math.abs(i - Gx) == 2 && Math.abs(j - Gy) == 1) {
                            //马腿算法
                            if(i-Gx==2&&map[i-1][j]==-1){
                                return true;
                            }
                            else if(Gx-i==2&&map[i+1][j]==-1){
                                return true;
                            }
                        }
                        else if(Math.abs(j - Gy) == 2 && Math.abs(i - Gx) == 1){
                            //马腿算法
                            if(j-Gy==2&&map[i][j-1]==-1){
                                return true;
                            }
                            else if(Gy-j==2&&map[i][j+1]==-1){
                                return true;
                            }
                        }
                    }
                }
            }

            //判断兵将
            for (int i = 27; i < 32; i++) {
                if (map[Gx - 1][Gy] == i) {
                    return true;
                }
            }
            for (int i = 27; i < 32; i++) {
                if (map[Gx][Gy + 1] == i) {
                    return true;
                }
            }
            for (int i = 27; i < 32; i++) {
                if (map[Gx][Gy - 1] == i) {
                    return true;
                }
            }

            //判断将所在直线将

            //判断车将和照面将
            for (int j : GSouth) {
                if (chessNumInLine == 0) {
                    if (j == 23 || j == 24) {
                        //System.out.println("1");
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 25 || j == 26) {
                        //System.out.println("2");
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                }
            }

            chessNumInLine=0;
            for (int j : GNorth) {

                if (chessNumInLine == 0) {
                    if (j == 16 || j == 23 || j == 24) {
                        //System.out.println("3");
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 25 || j == 26) {
                        //System.out.println("4");
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                }
            }

            chessNumInLine=0;
            for (int j : GEast) {
                if (chessNumInLine == 0) {
                    if (j == 23 || j == 24) {
                        //System.out.println("5");
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 25 || j == 26) {
                        //System.out.println("6");
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                }
            }

            chessNumInLine=0;
            for (int j : GWest) {

                if (chessNumInLine == 0) {
                    if (j == 23 || j == 24) {
                        //System.out.println("7");
                        return true;
                    } else if (j != -1) {
                        chessNumInLine++;
                    }
                } else if (chessNumInLine == 1) {
                    if (j == 25 || j == 26) {
                        //System.out.println("8");
                        return true;
                    } else if (j != -1) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否长将 长将算法
     */
    private void isLongChecked(int times){
        if(times==0){
            for(int i=0;i<10;i++){
                System.arraycopy(map[i], 0, map1[i], 0, 9);
            }
        }else if(times==1){
            for(int i=0;i<10;i++){
                System.arraycopy(map[i], 0, map2[i], 0, 9);
            }
            if(Arrays.deepEquals(map2, map1)){
                times++;
                this.times = times;
            }
            else {
                this.times=0;
            }
        }else if(times==2) {
            for (int i = 0; i < 10; i++) {
                System.arraycopy(map[i], 0, map3[i], 0, 9);
            }
            if (Arrays.deepEquals(map3, map2)) {
                times++;
                this.times = times;
            }
            else {
                this.times=0;
            }
        }
    }

    /**
     *判断是否送将 即每走一步都使用判断是否正被将军算法遍历 若执棋手的下一步导致自己被将军则就判断为送将
     */
    private boolean BeCheckedByDesign(Chess firstChess,int x2,int y2){
        int[][] mapNext = new int[10][9];
        int index1;
        for(int i = 0; i < 10; i++ ){
            System.arraycopy(map[i], 0, mapNext[i], 0, 9);
        }
        index1 = map[firstChess.x][firstChess.y];
        mapNext[x2][y2]= index1;
        mapNext[firstChess.x][firstChess.y]=-1;
        return beingChecked(mapNext);
    }

    /**
     *
     * 判断是否死棋
     */

    private boolean isDeadLock(int[][]map){
        if(LocalPlayer==REDPLAYER){
            for(int i=0;i<10;i++){
                for(int j=0;j<9;j++){
                    if(map[i][j]>=16){
                        for(int m=0;m<10;m++){
                            for(int n=0;n<9;n++){
                                if(IsAbleToMove(chess[map[i][j]],m,n)&&map[m][n]==-1){
                                    if(!BeCheckedByDesign(chess[map[i][j]],m,n)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        else if(LocalPlayer==BLACKPLAYER){
            for(int i=0;i<10;i++){
                for(int j=0;j<9;j++){
                    if(map[i][j]>=0&&map[i][j]<=15){
                        for(int m=0;m<10;m++){
                            for(int n=0;n<9;n++){
                                if(IsAbleToMove(chess[map[i][j]],m,n)&&map[m][n]==-1){
                                    if(!BeCheckedByDesign(chess[map[i][j]],m,n)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * 发送信息
     *
     */
    public void send(String str) {
        try (DatagramSocket s = new DatagramSocket()) {
            byte[] buffer;
            buffer = str.getBytes();
//			InetAddress ia = InetAddress.getLocalHost();//获取本机地址
            InetAddress ia = InetAddress.getByName(ip);//获取目标地址
            System.out.println("请求连接的ip是" + ip);
            DatagramPacket dgp = new DatagramPacket(buffer, buffer.length, ia, otherPort);
            s.send(dgp);
            System.out.println("发送信息:" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            System.out.println("我的端口"+receivePort);
            DatagramSocket s = new DatagramSocket(receivePort);
            byte[] data = new byte[100];
            DatagramPacket dgp = new DatagramPacket(data, data.length);
            while(flag){
                s.receive(dgp);
                String strData = new String(data);
                String[] array;
                array = strData.split("\\|");
                switch (array[0]) {
                    case "join": //对局被加入，我是黑方
                        LocalPlayer = BLACKPLAYER;
                        startNewGame(LocalPlayer);
                        SetMyTurn(LocalPlayer == REDPLAYER);
                        //发送联机成功信息
                        send("conn|");

                        break;
                    case "conn": //我成功加入别人的对局，联机成功。我是红色方
                        LocalPlayer = REDPLAYER;
                        startNewGame(LocalPlayer);
                        SetMyTurn(LocalPlayer == REDPLAYER);

                        break;
                    case "succ":
                        if (array[1].equals("黑方赢了")) {
                            if (LocalPlayer == REDPLAYER) {
                                JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始", "你输了", JOptionPane.DEFAULT_OPTION);
                            } else {
                                JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始", "你赢了", JOptionPane.DEFAULT_OPTION);
                            }
                        }
                        if (array[1].equals("红方赢了")) {
                            if (LocalPlayer == REDPLAYER) {
                                JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始", "你赢了", JOptionPane.DEFAULT_OPTION);

                            } else {
                                JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始", "你输了", JOptionPane.DEFAULT_OPTION);
                            }
                        }
                        message = "你可以重新开局";
                        GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了

                        //
                        break;
                    case "move":
                        //对方的走棋信息，move|棋子索引号|x|y|oldX|oldY|被吃棋子索引
                        System.out.println("接受信息:" + array[0] + "|" + array[1] + "|" + array[2] + "|" + array[3] + "|" + array[4] + "|" + array[5] + "|" + array[6] + "|");
                        int index = Short.parseShort(array[1]);
                        x2 = Short.parseShort(array[2]);
                        y2 = Short.parseShort(array[3]);
                        int oldX = Short.parseShort(array[4]);//棋子移动前所在行数

                        int oldY = Short.parseShort(array[5]);//棋子移动前所在列数

                        int eatChessIndex = Short.parseShort(array[6]);//被吃掉的棋子索引

                        list.add(new Node(index, x2, y2, oldX, oldY, eatChessIndex));//记录下棋信息

                        message = "对方将棋子\"" + chess[index].typeName + "\"移动到了(" + x2 + "," + y2 + ")\n现在该你走棋";
                        Chess c = chess[index];
                        x1 = c.x;
                        y1 = c.y;

                        index = map[x1][y1];

                        int index2 = map[x2][y2];
                        map[x1][y1] = -1;
                        map[x2][y2] = index;
                        chess[index].setPos(x2, y2);
                        if (index2 != -1) {// 如果吃了子，则取下被吃掉的棋子
                            chess[index2] = null;
                        }
                        repaint();
                        if (!beingChecked(map)) {
                            times = 0;
                        }
                        if (beingChecked(map) && !isDeadLock(map)) {
                            message = "您正在被将军";
                            isLongChecked(times);
                            if (times == 3) {//将军次数多余三次即为长将
                                send("longcheckwin|");
                                JOptionPane.showConfirmDialog(null, "对方长将，您赢了", "游戏结束", JOptionPane.DEFAULT_OPTION);
                                message = "被长将判您赢，游戏结束";
                                SetMyTurn(false);
                                GameClient.buttonStart.setEnabled(true);
                            }
                        }
                        if (isDeadLock(map)) {
                            send("beKilled|");
                            JOptionPane.showConfirmDialog(null, "您被绝杀了！！！", "游戏结束", JOptionPane.DEFAULT_OPTION);
                            message = "绝杀！游戏结束";
                            SetMyTurn(false);
                            GameClient.buttonStart.setEnabled(true);
                        }
                        //System.out.println("2");
                        isMyTurn = true;
                        break;
                    case "quit":
                        JOptionPane.showConfirmDialog(null, "对方退出了，游戏结束！", "提示", JOptionPane.DEFAULT_OPTION);
                        message = "对方退出了，游戏结束！";
                        GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了

                        break;
                    case "lose":
                        JOptionPane.showConfirmDialog(null, "恭喜你，对方认输了！", "你赢了", JOptionPane.DEFAULT_OPTION);
                        SetMyTurn(false);
                        GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了

                        break;
                    case "beKilled":
                        JOptionPane.showConfirmDialog(null, "恭喜你，你绝杀了对方！", "你赢了", JOptionPane.DEFAULT_OPTION);
                        message = "绝杀!!!";
                        SetMyTurn(false);
                        GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了

                        break;
                    case "longcheckwin":
                        JOptionPane.showConfirmDialog(null, "对不起，你长将对方被判负了", "你输了", JOptionPane.DEFAULT_OPTION);
                        message = "违背了长将规则";
                        SetMyTurn(false);
                        GameClient.buttonStart.setEnabled(true);
                        break;
                    case "askforpeace": {
                        String msg = "对方请求和棋，是否同意？";
                        int type = JOptionPane.YES_NO_OPTION;
                        String title = "请求和棋";
                        int choice;
                        choice = JOptionPane.showConfirmDialog(null, msg, title, type);
                        if (choice == 1) {//否,拒绝和棋
                            send("refuseforpeace|");
                            message = "您拒绝了和棋请求";
                        } else if (choice == 0) {//是,同意和棋
                            send("agreeforpeace|");
                            JOptionPane.showConfirmDialog(null, "和棋", "游戏结束", JOptionPane.DEFAULT_OPTION);
                            message = "您同意了和棋请求";
                            SetMyTurn(false);
                            GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
                        }
                        break;
                    }
                    case "ask": {//对方请求悔棋

                        String msg = "对方请求悔棋，是否同意？";
                        int type = JOptionPane.YES_NO_OPTION;
                        String title = "请求悔棋";
                        int choice;
                        choice = JOptionPane.showConfirmDialog(null, msg, title, type);
                        if (choice == 1) {//否,拒绝悔棋
                            send("refuse|");
                        } else if (choice == 0) {//是,同意悔棋
                            send("agree|");
                            message = "同意了对方的悔棋，对方正在思考";
                            SetMyTurn(false);//对方下棋

                            Node temp = list.get(list.size() - 1);//获取棋谱最后一步棋的信息
                            list.remove(list.size() - 1);//移除
                            if (LocalPlayer == REDPLAYER) {//假如我是红色方

                                if (temp.index >= 16) {//上一步是我下的，需要回退两步
                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }
                                    temp = list.get(list.size() - 1);
                                    list.remove(list.size() - 1);

                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }
                                } else {//上一步是对方下的，需要回退一步

                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }

                                }

                            } else {//假如我是黑方

                                if (temp.index < 16) {//上一步是我下的，需要回退两步

                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }

                                    temp = list.get(list.size() - 1);
                                    list.remove(list.size() - 1);

                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }

                                } else {//上一步是对方下的，需要回退一步

                                    rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                    if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                        resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                    }

                                }

                            }

                            repaint();
                        }

                        break;
                    }
                    case "agreeforpeace":
                        JOptionPane.showConfirmDialog(null, "对方同意了和棋请求，游戏结束", "游戏结束", JOptionPane.DEFAULT_OPTION);
                        message = "和棋，游戏结束！";
                        SetMyTurn(false);
                        GameClient.buttonStart.setEnabled(true);

                        break;
                    case "agree": //对方同意悔棋
                        JOptionPane.showMessageDialog(null, "对方同意了你的悔棋请求");
                        Node temp = list.get(list.size() - 1);//获取棋谱最后一步棋的信息

                        list.remove(list.size() - 1);//移除

                        if (LocalPlayer == REDPLAYER) {//假如我是红色方

                            if (temp.index >= 16) {//上一步是我下的，回退一步即可

                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }
                            } else {//上一步是对方下的，需要回退两步
                                //第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }

                                temp = list.get(list.size() - 1);
                                list.remove(list.size() - 1);
                                //第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }

                            }

                        } else {//假如我是黑色方

                            if (temp.index < 16) {//上一步是我下的，回退一步即可

                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }
                            } else {//上一步是对方下的，需要回退两步
                                //第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }

                                temp = list.get(list.size() - 1);
                                list.remove(list.size() - 1);
                                //第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
                                rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
                                if (temp.eatChessIndex != -1) {//如果上一步吃了子，将被吃的子重新放回到棋盘
                                    resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
                                }

                            }


                        }
                        SetMyTurn(true);
                        repaint();
                        break;
                    case "refuse": //对方拒绝悔棋
                        JOptionPane.showMessageDialog(null, "对方拒绝了你的悔棋请求");
                        break;
                    case "refuseforpeace": //对方拒绝悔棋
                        JOptionPane.showMessageDialog(null, "对方拒绝了你的和棋请求");
                        break;
                }


            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
