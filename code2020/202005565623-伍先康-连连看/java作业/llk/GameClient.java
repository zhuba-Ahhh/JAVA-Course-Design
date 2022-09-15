package llk;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameClient {
	static int MIN_PROGRESS = 0;  
    static int MAX_PROGRESS = 100;
	//sur表示当前的进度%数值
    static int sur=MAX_PROGRESS;
    static int currentProgress = MIN_PROGRESS;

	JProgressBar progressBar;
	JFrame jf = new JFrame();
	static JTextField textField = new JTextField(10);

	MusicPlay Music;

	public GameClient(int n){
		
		//从文件中读入数据到排行榜
		new paihang(n);

		GamePanel panel2 = new GamePanel(n);

		JButton button0 = new JButton("排行榜");
		JButton button1 = new JButton("新游戏/保存当前分数");
		JButton button2 = new JButton("退出游戏");
		JButton button3 = new JButton("帮助");
		JButton button4 = new JButton("刷新");
		JButton button5 = new JButton("提示");
		JButton button6 = new JButton("音乐设置");

		JPanel panel3 = new JPanel(new BorderLayout());
		JLabel label1 = new JLabel("分数");
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel03 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());

		JProgressBar a = Jprocessbar(n);

		JLabel shijian = new JLabel("游戏开始后,请在规定时间内完成哦'◡'",JLabel.CENTER);
		shijian.setFont(new Font(null, Font.PLAIN, 20));// 设置字体，null 表示使用默认字体
		//设置不可编辑
		textField.setEditable(false);

		panel03.add(label1);
		panel03.add(textField);

		panel3.add(shijian,BorderLayout.NORTH);
		panel3.add(panel03,BorderLayout.SOUTH);
		panel3.add(a,BorderLayout.CENTER);

		panel.add(button0);
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);
		panel.add(button4);
		panel.add(button5);
		panel.add(button6);

		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(panel,BorderLayout.SOUTH);
		jf.getContentPane().add(panel2,BorderLayout.CENTER);
		jf.getContentPane().add(panel3,BorderLayout.NORTH);

		jf.setSize(800,700);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("连连看小游戏");

		jf.setVisible(true);
		jf.setResizable(false);   //窗口大小固定
		jf.setLocationRelativeTo(null);	//置于屏幕中央
		
		button0.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new paihang();
			}
		});

		button1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				jf.dispose();
				textField.setText("0");
				sur = MAX_PROGRESS;
		 		currentProgress = MIN_PROGRESS;
				panel2.startNewGame(panel2.getn());
				jf.setVisible(true);
			}
		});
		
		button2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
		});

		button3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new Jlabel();
			}
		});

		button4.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				panel2.NewG();
			}
		});
		
		button5.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				panel2.tishi();
			}
		});

		button6.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				String msg = "是否播放背景音乐";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "音乐播放";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					Music.stop();
				}else if(choice==0){
					Music=new MusicPlay();
					Music.start();
				}
			}
		});
	}
	
	public JProgressBar Jprocessbar(int n) {
        currentProgress=0;
        sur=MAX_PROGRESS;
        progressBar = new JProgressBar();
        // 设置进度的 最小值 和 最大值
        progressBar.setMinimum(MIN_PROGRESS);
        progressBar.setMaximum(MAX_PROGRESS);

        // 设置当前进度值
        progressBar.setValue(currentProgress);

        // 绘制百分比文本（进度条中间显示的百分数）
        progressBar.setStringPainted(true); // 设置进度条显示数字字符
        progressBar.setString("即将开始计时！请注意");
        progressBar.setBackground(Color.WHITE);
        progressBar.setForeground(new Color(150,170,35));   

        // 模拟延时操作进度
        new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentProgress++;
                sur--;
                progressBar.setString("时间消耗中..."+"("+sur+"%"+")");
                if (currentProgress > MAX_PROGRESS) {
					sur=MAX_PROGRESS;
                    currentProgress = MIN_PROGRESS;
                }
                else if(currentProgress==MAX_PROGRESS){
					progressBar.setString("时间用完！！");
					String msg = "时间到！是否重来？";
					int type = JOptionPane.YES_NO_OPTION;
					String title = "";
					int choice = 0;
					choice = JOptionPane.showConfirmDialog(null, msg,title,type);
					if(choice==1){
						System.exit(0);
					}else if(choice == 0){
						//只能关闭一个窗口，最初的窗口
						jf.dispose();
						GamePanel.count = 0;
						textField.setText("0");
						sur = MAX_PROGRESS;
						currentProgress = MIN_PROGRESS;
						new GameClient(GamePanel.n);
					}
                }
                progressBar.setValue(currentProgress);//设置当前进度值
            }
        }).start();
		return progressBar;
    }
	
	public static void main(String[] args) {
		new GameClient(4);
	}
}
