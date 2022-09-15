package bighomework;


import java.awt.*;
import javax.swing.*;


import java.awt.event.*;

public class GameClient extends JFrame implements ActionListener{
	GamePanel gamePanel ;
	JButton restart = new JButton("重来一局");
	JButton quit = new JButton("退出");
	JButton help= new JButton("帮助");
	JButton music=new JButton("音乐");
	JPanel panel1=new JPanel();
	JLabel jLabel=new JLabel("只要将相同的两张图片用三根以内的直线连在一起消除即可，按A即可重列，按D即可自动连接");
	static JTextField textField = new JTextField(10);
	int level;
	MusicPlay Music;
	public GameClient(int n,int level){
		this.level=level;
		panel1.add(jLabel);
        gamePanel=new GamePanel(10,n,level);
		JLabel label1 = new JLabel("已消去方块数量:");
		JPanel panel = new JPanel(new BorderLayout());
		textField.setEditable(false);
		gamePanel.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());
		panel.add(label1);
		panel.add(textField);
		panel.add(restart);
		panel.add(quit);
		panel.add(help);
		panel.add(music);
	   	this.setLocation(400, 190);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel,BorderLayout.SOUTH);
		this.getContentPane().add(gamePanel,BorderLayout.CENTER);
		this.getContentPane().add(panel1,BorderLayout.NORTH);
		this.setSize(800,700);
		this.setTitle("连连看游戏");//窗口标题为连连看游戏
		this.setVisible(true);
		restart.setEnabled(true);
		quit.setEnabled(true);
		restart.addActionListener(this);
        quit.addActionListener(this);
		help.addActionListener(this);
		music.addActionListener(this);
		this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点×之后能关闭窗口
	}
    public void actionPerformed(ActionEvent e) { //监听事件，即按哪些按钮进行那些操作
        if(e.getSource()==restart){
            textField.setText("0");
			gamePanel.startNewGame();
        }
        else if(e.getSource()==quit){
            System.exit(0);
        }
		else if(e.getSource()==help){
			JOptionPane.showMessageDialog(this, "按A可以重列，按D可以自动连接");
		}
		else if(e.getSource()==music){
			String msg = "打开或关闭音乐？";
			int type = JOptionPane.YES_NO_OPTION;
			String title = "音乐";
			int choice = 0;
			choice = JOptionPane.showConfirmDialog(null, msg,title,type);
			if(choice==1){
				Music.stop();
			}
			else if(choice == 0){
				Music=new MusicPlay();
				Music.start();
			}
		}
    }
	
     public static void main(String[] args) {
        new GameClient(10,0);
    }

}
