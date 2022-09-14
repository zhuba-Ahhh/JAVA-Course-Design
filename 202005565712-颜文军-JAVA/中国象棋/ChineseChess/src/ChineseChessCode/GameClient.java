package ChineseChessCode;

import AI.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * @author HP电脑
 * 2021年12月21日 上午10:21:15
 * @author 颜文军
 * XTU-202005565712
 * 项目名称：ChineseChess
 * 类名称：GameClient
 **/

public class GameClient extends JFrame{

    static ChessBoard gamePanel = new ChessBoard();
    static JButton buttonGiveIn = new JButton("认输");
    static JButton buttonForPeace = new JButton("请求和棋");
    static JButton buttonStart = new JButton("开始");
    JButton buttonAskRegret = new JButton("请求悔棋");
    JTextField textIp = new JTextField("169.254.245.61");//IP
    JTextField textPort = new JTextField("3003");//对方端口
    JButton buttonAI = new JButton("AI");
    public static final short REDPLAYER = 1;
    public static final short BLACKPLAYER = 0;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();


    public GameClient(){
        JPanel panelBottom = new JPanel(new FlowLayout());
        panelBottom.add(new JLabel("输入对方IP:"));
        panelBottom.add(textIp);
        panelBottom.add(new JLabel("输入对方端口:"));
        panelBottom.add(textPort);
        panelBottom.add(buttonAskRegret);
        panelBottom.add(buttonAI);
        panelBottom.add(buttonGiveIn);
        panelBottom.add(buttonForPeace);
        panelBottom.add(buttonStart);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(gamePanel,BorderLayout.CENTER);
        this.getContentPane().add(panelBottom,BorderLayout.SOUTH);
        this.setLocation(((int)width-600)/2,0);
        this.setBackground(Color.blue); //设置窗口颜色
        this.setSize(700,730);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("中国象棋客户端");
        this.setVisible(true);
        buttonGiveIn.setEnabled(false);
        buttonAskRegret.setEnabled(false);
        buttonAI.setEnabled(true);
        buttonForPeace.setEnabled(false);
        buttonStart.setEnabled(true);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {//窗口关闭事件
            @Override
            public void windowClosing(WindowEvent e){
                try{
                    gamePanel.send("quit|");
                    System.exit(0);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        buttonGiveIn.addMouseListener(new MouseAdapter() {//认输事件
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    gamePanel.send("lose|");//发送认输信息
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        buttonAI.addMouseListener(new MouseAdapter() {//认输事件
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    ChessGame game = new ChessGame();//AI对战接口
                    game.init();
                    game.run();
                    setVisible(true);

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        buttonForPeace.addMouseListener(new MouseAdapter() {//认输事件
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    gamePanel.send("askforpeace|");//发送求和信息
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        buttonAskRegret.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(gamePanel.list.size()==0){
                    JOptionPane.showMessageDialog(null, "不能悔棋");
                    return ;
                }

                if(gamePanel.list.size()==1){
                    int flag = gamePanel.LocalPlayer==REDPLAYER?REDPLAYER:BLACKPLAYER;
                    if(flag==REDPLAYER){//如果我是色红方，判断上一步是不是对方下的，如果是，不能悔棋
                        if(gamePanel.list.get(0).index<16){
                            JOptionPane.showMessageDialog(null, "不能悔棋");
                            return ;
                        }
                    }else{
                        if(gamePanel.list.get(0).index>=16){
                            JOptionPane.showMessageDialog(null, "不能悔棋");
                            return ;
                        }
                    }

                }

                gamePanel.send("ask|");//发送请求悔棋请求

            }
        });


        buttonStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                String ip = textIp.getText();
                int otherPort = Integer.parseInt(textPort.getText());
                int receivePort;
                if(otherPort == 3003){
                    receivePort = 3004;
                }else{
                    receivePort = 3003;
                }
                gamePanel.startJoin(ip, otherPort, receivePort);
                buttonGiveIn.setEnabled(true);
                buttonAskRegret.setEnabled(true);
                buttonForPeace.setEnabled(true);
                buttonStart.setEnabled(false);
            }
        });

    }

    public static void main(String[] args) {
        GameClient Client = new GameClient();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("src/Picture/app.png");
        Client.setIconImage(image);
    }
}
