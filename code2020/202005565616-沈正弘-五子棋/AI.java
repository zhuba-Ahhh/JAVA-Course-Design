import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
public class AI {

    private JFrame f = new JFrame("五子棋");
    //当前棋子颜色
    int boardColor = 1;

    //声明一个二维数组，记录棋子颜色
    int [][] board = new int[21][21];

    //选择框坐标
    int select_X = -1;
    int select_Y = -1;

    MusicPlay music;

    int xpos = -1,ypos = -1;

    private class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            //棋盘
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0,0, 530, 530);
            g.setColor(Color.BLACK);
            for(int i = 0;i <= 20;i++){
                g.drawLine(15, 15+i*25, 515, 15+i*25);
                g.drawLine(15+i*25, 15, 15+i*25, 515);
            }
            //选择框
            g.setColor(Color.red);
            if(select_X >= 0&&select_Y >= 0){
                 g.drawRect(select_X*25+3, select_Y*25+3,24,24);
            }
           
            //棋子
            for(int i = 0;i <= 20;i++){
                for(int j = 0;j <= 20;j++){
                    if(board[i][j]==2){
                        g.setColor(Color.BLACK);
                        g.fillOval(3+25*i,3+25*j,24,24);
                    }
                    if(board[i][j]==1){
                        g.setColor(Color.white);
                        g.fillOval(3+25*i,3+25*j,24,24);
                    }
                }
            }
        }
    }

    MyCanvas drawArea = new MyCanvas();

    Panel p = new Panel();
    JButton b1 = new JButton("打开音乐");
    JButton b2 = new JButton("关闭音乐");

    public void init(){
        f.setBounds(450,150,530,530);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        b1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                music = new MusicPlay();
                music.start();
            }
        });
        b2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                music.stop();
            }
        });

        p.add(b1);
        p.add(b2);

        //鼠标移动
        drawArea.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                
                select_X = (e.getX()-3)/25;
                select_Y = (e.getY()-3)/25;

                drawArea.repaint();
            }
        });
        //鼠标点击
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xpos = (e.getX()-3)/25;
                ypos = (e.getY()-3)/25;
                //下棋
                if(boardColor==1){
                    if(board[xpos][ypos]==0){
                        board[xpos][ypos] = boardColor;
                        boardColor = 2;
                        drawArea.repaint();
                    }
                    else{
                        System.out.println("错误");
                    }
                    judge(xpos,ypos);
                }
                //AI
                if(boardColor==2){
                    int flat = 0;
                    for(int i = 0;i < 21;i++){
                        for(int j = 0;j < 21 ;j++){
                            int sum1 = 0,sum2 = 0,sum3 = 0,sum4 = 0;
                            int i1 = 1,i2 = 1,i3 = 1,i4 = 1;
                            int j1 = 1,j2 = 1,j3 = 1,j4 = 1;
                            for(int k = 1;k <= 4;k++){
                                if(j+i1<21&&(1==board[i][j+i1])){
                                    sum1++;
                                    i1++;
                                }
                                if(j-j1>=0&&(1==board[i][j-j1])){
                                   sum1++;
                                    j1++;
                                }
                                if(i+i2<21&&(1==board[i+i2][j])){
                                    sum2++;
                                    i2++;
                                }
                                if(i-j2>=0&&(1==board[i-j2][j])){
                                    sum2++;
                                    j2++;
                                }
                                if(i+i3<21&&j+i3<21&&(1==board[i+i3][j+i3])){
                                    sum3++;
                                    i3++;
                                }
                                if(i-j3>=0&&j-j3>=0&&(1==board[i-j3][j-j3])){
                                    sum3++;
                                    j3++;
                                }
                                if(i-i4>=0&&j+i4<21&&(1==board[i-i4][j+i4])){
                                    sum4++;
                                    i4++;
                                }
                                if(j-j4>=0&&i+j4<21&&(1==board[i+j4][j-j4])){
                                    sum4++;
                                    j4++;
                                }
                            }
                            if((sum1>=3||sum2>=3||sum3>=3||sum4>=3)&&board[i][j]==0){
                                board[i][j] = 2;
                                judge(i,j);
                                flat = 1;
                                i = 21;j = 21;
                            }
                        }
                    }
                    if(flat == 0){
                        for(int i = 0;i < 21;i++){
                            for(int j = 0;j < 21 ;j++){
                                int sum1 = 0,sum2 = 0,sum3 = 0,sum4 = 0;
                                int i1 = 1,i2 = 1,i3 = 1,i4 = 1;
                                int j1 = 1,j2 = 1,j3 = 1,j4 = 1;
                                for(int k = 1;k <= 4;k++){
                                    if(j+i1<21&&(2==board[i][j+i1])){
                                        sum1++;
                                        i1++;
                                    }
                                    if(j-j1>=0&&(2==board[i][j-j1])){
                                       sum1++;
                                        j1++;
                                    }
                                    if(i+i2<21&&(2==board[i+i2][j])){
                                        sum2++;
                                        i2++;
                                    }
                                    if(i-j2>=0&&(2==board[i-j2][j])){
                                        sum2++;
                                        j2++;
                                    }
                                    if(i+i3<21&&j+i3<21&&(2==board[i+i3][j+i3])){
                                        sum3++;
                                        i3++;
                                    }
                                    if(i-j3>=0&&j-j3>=0&&(2==board[i-j3][j-j3])){
                                        sum3++;
                                        j3++;
                                    }
                                    if(i-i4>=0&&j+i4<21&&(2==board[i-i4][j+i4])){
                                        sum4++;
                                        i4++;
                                    }
                                    if(j-j4>=0&&i+j4<21&&(2==board[i+j4][j-j4])){
                                        sum4++;
                                        j4++;
                                    }
                                }
                                if((sum1>=1||sum2>=1||sum3>=1||sum4>=1)&&board[i][j]==0){
                                    board[i][j] = 2;
                                    judge(i,j);
                                    flat = 1;
                                    i = 21;j = 21;
                                }
                            }
                        }
                    }
                    if(flat == 0){
                        board[10][10] = 2;
                    }
                    boardColor = 1;
                    drawArea.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                select_X = -1;
                select_Y = -1;
                drawArea.repaint();
            }
        });


        drawArea.setPreferredSize(new Dimension(530, 530));
        f.add(p,BorderLayout.SOUTH);
        f.add(drawArea);
        f.pack();
        f.setVisible(true);

    }
    public  void judge(int x,int y) {
        //判断是否五连
        int sum1 = 0,sum2 = 0,sum3 = 0,sum4 = 0;
        int i1 = 1,i2 = 1,i3 = 1,i4 = 1;
        int j1 = 1,j2 = 1,j3 = 1,j4 = 1;
        for(int i = 1;i <= 4;i++){
            if(y+i1<21&&(board[x][y]==board[x][y+i1])){
                sum1++;
                i1++;
            }
            if(y-j1>=0&&(board[x][y]==board[x][y-j1])){
                sum1++;
                j1++;
            }
            if(x+i2<21&&(board[x][y]==board[x+i2][y])){
                sum2++;
                i2++;
            }
            if(x-j2>=0&&(board[x][y]==board[x-j2][y])){
                sum2++;
                j2++;
            }
            if(x+i3<21&&y+i3<21&&(board[x][y]==board[x+i3][y+i3])){
                sum3++;
                i3++;
            }
            if(x-j3>=0&&y-j3>=0&&(board[x][y]==board[x-j3][y-j3])){
                sum3++;
                j3++;
            }
            if(x-i4>=0&&y+i4<21&&(board[x][y]==board[x-i4][y+i4])){
                sum4++;
                i4++;
            }
            if(y-j4>=0&&x+j4<21&&(board[x][y]==board[x+j4][y-j4])){
                sum4++;
                j4++;
            }
        }
        if(sum1>=4||sum2>=4||sum3>=4||sum4>=4)
            {
                if(board[x][y]==1){
                    boardColor = 1;
                    String msg = "白棋胜，是否开始新局？";
				    int type = JOptionPane.YES_NO_OPTION;
				    String title = "白棋胜利";
				    int choice = 0;
				    choice = JOptionPane.showConfirmDialog(null,msg,title,type);
				    if(choice==1){
					    System.exit(0);
				    }else if(choice == 0){
                        for(int i = 0;i<21;i++){
                            for(int j = 0;j<21;j++){
                                board[i][j] = 0;
                            }
                        }
				    }
                }
                if(board[x][y]==2){
                    String msg = "黑棋胜，是否开始新局？";
				    int type = JOptionPane.YES_NO_OPTION;
				    String title = "黑棋胜利";
				    int choice = 0;
				    choice = JOptionPane.showConfirmDialog(null,msg,title,type);
				    if(choice==1){
					    System.exit(0);
				    }else if(choice == 0){
                        for(int i = 0;i<21;i++){
                            for(int j = 0;j<21;j++){
                                board[i][j] = 0;
                            }
                        }
				    }
                }
            }
    }
    public static void main(String[] args) {
        new AI().init();
    }
}
