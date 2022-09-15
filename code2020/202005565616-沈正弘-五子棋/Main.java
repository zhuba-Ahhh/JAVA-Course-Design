import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JButton;
import java.io.*;
import java.net.*;

public class Main implements Runnable{
    Thread listener = new Thread(this);    //一直监听对方发来的消息；
    
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

    Socket c;
    DataOutputStream os;
    DataInputStream is;
    public void init() throws IOException{

        c = new Socket("172.18.104.97",5500);
        os =new DataOutputStream(c.getOutputStream());
        is =new DataInputStream(c.getInputStream());

        listener.start();

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
        drawArea.addMouseListener(new MouseAdapter() {      //下白棋   boardColor =  1；
            @Override
            public void mouseClicked(MouseEvent e) {
                if(boardColor == 1){
                    xpos = (e.getX()-3)/25;
                    ypos = (e.getY()-3)/25;
                //下棋
                if(board[xpos][ypos]==0){
                    board[xpos][ypos] = boardColor;
                    drawArea.repaint();
                    boardColor = 2;
                    String line = xpos+" "+ypos;
                    try {
                        send(line);
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } 
                }
                else{
                    System.out.println("错误");
                }
                judge();
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

        try {
            Sever();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        

    }
    public  void judge() {
        //判断是否五连
        int sum1 = 0,sum2 = 0,sum3 = 0,sum4 = 0;
        int i1 = 1,i2 = 1,i3 = 1,i4 = 1;
        int j1 = 1,j2 = 1,j3 = 1,j4 = 1;
        for(int i = 1;i <= 4;i++){
            if(ypos+i1<21&&(board[xpos][ypos]==board[xpos][ypos+i1])){
                sum1++;
                i1++;
            }
            if(ypos-j1>=0&&(board[xpos][ypos]==board[xpos][ypos-j1])){
                sum1++;
                j1++;
            }
            if(xpos+i2<21&&(board[xpos][ypos]==board[xpos+i2][ypos])){
                sum2++;
                i2++;
            }
            if(xpos-j2>=0&&(board[xpos][ypos]==board[xpos-j2][ypos])){
                sum2++;
                j2++;
            }
            if(xpos+i3<21&&ypos+i3<21&&(board[xpos][ypos]==board[xpos+i3][ypos+i3])){
                sum3++;
                i3++;
            }
            if(xpos-j3>=0&&ypos-j3>=0&&(board[xpos][ypos]==board[xpos-j3][ypos-j3])){
                sum3++;
                j3++;
            }
            if(xpos-i4>=0&&ypos+i4<21&&(board[xpos][ypos]==board[xpos-i4][ypos+i4])){
                sum4++;
                i4++;
            }
            if(ypos-j4>=0&&xpos+j4<21&&(board[xpos][ypos]==board[xpos+j4][ypos-j4])){
                sum4++;
                j4++;
            }
        }
        if(sum1>=4||sum2>=4||sum3>=4||sum4>=4)
            {
                if(board[xpos][ypos]==1){
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
                if(board[xpos][ypos]==2){
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
    public void Sever() throws IOException, InterruptedException{
        while(true){
            //给出反馈；
            Thread.sleep(100);
        }
    }
    public void send(String s) throws IOException{
        os.writeUTF(s);
        os.flush();
    }
    public static void main(String[] args) throws IOException {
        new Main().init();
    }
    @Override
    public void run() {
        int x1 = 0;
        int y1 = 0;
        while(true){
            try {
                String data = is.readUTF();
                String[] a = data.split(" ");
                x1 = Integer.parseInt(a[0]);
                y1 = Integer.parseInt(a[1]);
                board[x1][y1] = 2;
                drawArea.repaint();
                xpos = x1;ypos = y1;
                boardColor = 1;
                judge();
            }catch (IOException e) {System.out.println("开始时的错误");
            e.printStackTrace(); 
            }
        }
    }
}