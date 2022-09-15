package bighomework;


import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
public class Connection extends JFrame implements ActionListener{
	ConnectionSever connectionSever ;
	JButton quit = new JButton("退出");
	JButton help= new JButton("帮助");
	JButton music= new JButton("音乐");
	static JTextField textField = new JTextField(10);
	int level;
	MusicPlay Music;
	public Connection(int n,int level)throws UnknownHostException, IOException, InterruptedException{
		this.level=level;
        connectionSever=new ConnectionSever(10,n);
		JLabel label1 = new JLabel("已消去方块数量:");
		JPanel panel = new JPanel(new BorderLayout());
		textField.setEditable(false);
		connectionSever.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());
		panel.add(label1);
		panel.add(textField);
		panel.add(quit);
		panel.add(help);
		panel.add(music);
		this.setResizable(false);
	   	this.setLocation(50, 130);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel,BorderLayout.SOUTH);
		this.getContentPane().add(connectionSever,BorderLayout.CENTER);
		this.setSize(1610,700);
		this.setTitle("连连看游戏2");//窗口标题为连连看游戏
		this.setVisible(true);
        quit.addActionListener(this);
		help.addActionListener(this);
		music.addActionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点×之后能关闭窗口
        connectionSever.client();
	}
    public void actionPerformed(ActionEvent e) { //监听事件，即按哪些按钮进行那些操作
        if(e.getSource()==quit){
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
     public static void main(String[] args)throws UnknownHostException, IOException, InterruptedException{
        new Connection(10,0);
    }

}
