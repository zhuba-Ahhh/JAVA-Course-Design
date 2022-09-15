package llk;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener ,MouseListener{

	//垂直为x轴，水平为y轴
	private Image[] pics;
	public static int n;
	private int[][] map;
	//记录左上角的顶点
	private int leftX,leftY;	
	//记录第一次选中图片的id,以及数组下标
	private int FirstId,Firstx,Firsty;
	//记录连接方式
	private int linkmethod;	
	//存放拐点的数据
	private Node z1,z2;	
	//创建一个地图工具类，为map服务
	private Map maptool;
	//存储消去图案的个数
	public static int count = 0;
	//用Firstselect记录前面是否选中了图片，选中了的话，下一次选时就可以进行消除
	private boolean Firstselect = false;
	//link1~4表示不同的连接方式
	public static int shuiping = 1,chuizhi = 2,onecorner = 3,twocorner = 4;
	//图片为空
	public static int kong = -1;
	
	public GamePanel(int c){
		leftX = 140+(20*(10-c));
		leftY = 30+(20*(10-c));
		setSize(600, 600);
		n = c;
		maptool = new Map(n);
		map = maptool.getMap();		
		this.setVisible(true); 
		this.addMouseListener(this);
		getpics();   
		repaint();
	}

	//为GameClient提供新游戏刷新的数组n值
	public int getn(){
		return n;
	}

	//初始化图片数组
	private void getpics() {
		pics = new Image[n];
		for(int i=0;i<=n-1;i++){
			pics[i] = Toolkit.getDefaultToolkit().getImage("LinkGame\\yu"+(i+1)+".png");
		}		
	}
	
	//Graphics类提供基本的几何图形绘制方法，主要有：画线段、画矩形、画圆、画带颜色的图形、画椭圆、画圆弧、画多边形等。
	public void paint(Graphics g){	
		g.clearRect(0, 0, 800,700 );// 擦除某一区域（擦除后显示背景色）
        Image image = Toolkit.getDefaultToolkit().getImage("swing\\beijing.png");
		g.drawImage(image, 0, 0, 800, 700, this);
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]!=kong){
					g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
				}
			}
		}
		
	}

	//Node用来存放拐点信息
	public class Node{
		int x;
		int y;
		public Node(int x,int y){
			this.x = x;
			this.y = y;
		}
	}
	
	//link1表示是否可以水平相连
	private boolean link1(int x1, int y1, int x2, int y2) {
		//X表示行
		if(x1==x2){
			for(int i=Math.min(y1,y2)+1;i<Math.max(y1,y2);i++){
				//如果不为空，说明不可以
				if(map[x1][i]!=kong){
					return false;
				}
			}
			linkmethod = shuiping;  //记录链接方式
			return true;
		}
		return false;
	}

	//link2表示是否可以垂直相连
	private boolean link2(int x1, int y1, int x2, int y2) {
		//y表示列
		if(y1==y2){
			for(int i=Math.min(x1,x2)+1;i<Math.max(x1,x2);i++){
				if(map[i][y1]!=kong){
					return false;
				}
			}
			linkmethod = chuizhi;
			return true;
		}
		return false;
	}
	
	private boolean onecornerlink(int x1, int y1, int x2, int y2) {
		//判断对角点
		if(map[x1][y2]==kong&&link1(x1, y1, x1, y2)&&link2(x2,y2,x1,y2)){
			linkmethod = onecorner;
			z1 = new Node(x1,y2);
			return true;
		}
		if(map[x2][y1]==kong&&link1(x2, y2, x2, y1)&&link2(x1,y1,x2,y1)){
			linkmethod = onecorner;
			z1 = new Node(x2,y1);
			return true;
		}
		return false;
	}
	
	//如果在界外，则用这个函数判断，不在界外，利用onecorner判断
	//利用yansheng=1,2,3,4分别表示向上，下，左，右延伸到界外
	private boolean jiewaipanduan(int x,int y,int yansheng){
		if(yansheng==1){
			for(int i=x-1;i>=0;i--){
				if(map[i][y]!=kong){
					return false;
				}
			}
		}
		if(yansheng==2){
			for(int i=x+1;i<n;i++){
				if(map[i][y]!=kong){
					return false;
				}
			}
		}
		if(yansheng==3){
			for(int i=y-1;i>=0;i--){
				if(map[x][i]!=kong){
					return false;
				}
			}			
		}
		if(yansheng==4){
			for(int i=y+1;i<n;i++){
				if(map[x][i]!=kong){
					return false;
				}
			}
		}
		return true;
	}

	private boolean twocornerlink(int x1, int y1, int x2, int y2) {
		
		//向上查找
		for(int i=x1-1;i>=-1;i--){
			//如果超出地图外
			if(i==-1&&jiewaipanduan(x2, y2, 1)){
				//利用z1,z2记录拐点的值
				z1 = new Node(-1,y1);
				z2 = new Node(-1,y2);
				linkmethod = twocorner;
				return true;
			}
			if(i>=0&&map[i][y1]==kong){
				if(onecornerlink(i, y1, x2, y2)){
					linkmethod = twocorner;
					z1 = new Node(i,y1);
					z2 = new Node(i,y2);
					return true;
				}
			}
			else break;//如果查找路径有图片，直接结束
		}
		
		//向下查找
		for(int i=x1+1;i<=n;i++){
			if(i==n&&jiewaipanduan(x2, y2, 2)){
				z1 = new Node(n,y1);
				z2 = new Node(n,y2);
				linkmethod = twocorner;
				return true;
			}
			if(i<n&&map[i][y1]==kong){
				if(onecornerlink(i, y1, x2, y2)){
					linkmethod = twocorner;
					z1 = new Node(i,y1);
					z2 = new Node(i,y2);
					return true;
				}
			}
			else break;
		}
		
		//向左查找
		for(int i=y1-1;i>=-1;i--){
			if(i==-1&&jiewaipanduan(x2, y2, 3)){
				linkmethod = twocorner;
				z1 = new Node(x1,-1);
				z2 = new Node(x2,-1);
				return true;
			} 
			if(i>-1&&map[x1][i]==kong){
				if(onecornerlink(x1, i, x2, y2)){
					linkmethod = twocorner;
					z1 = new Node(x1,i);
					z2 = new Node(x2,i);
					return true;
				}
			}
			else break;
		}
		
		//向右查找
		for(int i=y1+1;i<=n;i++){
			if(i==n&&jiewaipanduan(x2, y2, 4)){
				z1 = new Node(x1,n);
				z2 = new Node(x2,n);
				linkmethod = twocorner;
				return true;
			}
			if(i<n&&map[x1][i]==kong){
				if(onecornerlink(x1, i, x2, y2)){
					linkmethod = twocorner;
					z1 = new Node(x1,i);
					z2 = new Node(x2,i);
					return true;
				}
			}
			else break;	
		}
		return false;
	}

	//画选中框
	private void drawSelectedBlock(int x, int y, Graphics g) {
		//创建画笔
		Graphics2D g2 = (Graphics2D) g;
		//设置画笔的宽度
		BasicStroke s = new BasicStroke(2);
		g2.setStroke(s);
		g2.setColor(Color.RED);
		g.drawRect(x+1, y+1, 48, 48);
	}

	//画线并消除一组图片，此处的x1,y1,x2,y2二维数组下标
	private void drawLink(int x1, int y1, int x2, int y2) {

		Graphics g = this.getGraphics();

		//将拐点转换成像素坐标
		Point p1 = new Point(y1*50+leftX+25,x1*50+leftY+25);	
		Point p2 = new Point(y2*50+leftX+25,x2*50+leftY+25);

		//根据不同的连接方式选择不同的画线方法
		if(linkmethod == shuiping || linkmethod == chuizhi ){
			//Graphic中提供的drawLine方法
			g.drawLine(p1.x, p1.y,p2.x, p2.y);
		}
		else if(linkmethod == onecorner){
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
			g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);
		}
		else{
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
			Point point_z2 = new Point(z2.y*50+leftX+25,z2.x*50+leftY+25);
			g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
			g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);		
		}
		//加分数，消除一对加200
		count+=200;
		GameClient.textField.setText(count+"");
		try {
			Thread.currentThread().sleep(100);//延时100ms
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//消除图片
		map[x1][y1] = kong;
		map[x2][y2] = kong;
		repaint();
		isWin();//判断游戏是否结束
	}
	
	public void clearSelectBlock(int i,int j,Graphics g){
		g.clearRect(j*50+leftX, i*50+leftY, 50, 50);
		g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
	}

	//通过遍历查找,设置提示按钮
	private boolean find() {
		//如果之前玩家选中了一个方块，清空该选中框
		if(Firstselect){
			clearSelectBlock(Firstx, Firsty, this.getGraphics());
			Firstselect = false;
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]==kong){
					continue;
				}				
				//查找第二个
				for(int x=i;x<n;x++){
					for(int y=0;y<n;y++){
						//如果图案不相等
						if(map[x][y]!=map[i][j]||(x==i&&y==j)){
							continue;
						}						  
						if(link2(x,y,i,j)||link1(x,y,i,j)||onecornerlink(x,y,i,j)||twocornerlink(x,y,i,j)){
							drawSelectedBlock(j*50+leftX, i*50+leftY, this.getGraphics());
							drawSelectedBlock(y*50+leftX, x*50+leftY, this.getGraphics());
							try {
								Thread.currentThread().sleep(500);//延时500ms
							} catch (InterruptedException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaint();
							return true;
						}
					}
				}				
			}
		}
		return false;
	}

	private void isWin() {
		
		if(count==n*n*100){
			if(n*n==100){
				String msg = "恭喜您全部通关！！请点击“是”结束游戏.";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					startNewGame(n);
				}else if(choice==0){
					System.exit(0);
				}
			}
			else if(n*n==64){
				count=0;
				String msg = "恭喜您通关！下一关就是最后一关了，难度较大，是否继续闯关？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					startNewGame(n);
				}else if(choice==0){
					startNewGame(n+2);
				}
			}
			else if(n*n==16){
				count=0;
				String msg = "这么快就过关了？恭喜恭喜！是否继续闯关？越往后难度越大哦";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					startNewGame(n);
				}else if(choice==0){
					startNewGame(n+2);
				}
			}
			else if(n*n==36){
				count=0;
				String msg = "看来第二关也难不到你啊，是否进入下一关？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					startNewGame(n);
				}else if(choice==0){
					startNewGame(n+2);
				}
			}
		}
		
	}
	
	public void startNewGame(int nn) {
		// TODO Auto-generated method stub
		if(n==4){
			if(paihang.sorce1[4]<count)
				paihang.sorce1[4]=count;
		}
		else if(n==6){
			if(paihang.sorce2[4]<count)
				paihang.sorce2[4]=count;
		}
		else if(n==8){
			if(paihang.sorce3[4]<count)
				paihang.sorce3[4]=count;
		}
		else if(n==10){
			if(paihang.sorce4[4]<count)
				paihang.sorce4[4]=count;
		}
		n=nn;
		leftX = 140+(20*(10-nn));
		leftY = 30+(20*(10-nn));
		count = 0;
		maptool = new Map(nn);		
		map = maptool.getMap();
		Firstselect = false;
		FirstId = -1;
		Firstx = -1;
		Firsty = -1;
		linkmethod = -1;
		GameClient.sur = 100;
		GameClient.currentProgress = 0;
		GameClient.textField.setText(count+"");
		getpics();
		repaint();
	}
    
	public void NewG(){
		map = maptool.getnewMap();
		repaint();
	}

	public void tishi(){
		boolean ff = find();
		if(!ff){
			//消息提示框
			JOptionPane.showMessageDialog(this, "没有可以连通的方块了，请对剩余的方块进行刷新。");
		}
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		Graphics g = this.getGraphics();
		
		int y = e.getX()-leftX;
		int x = e.getY()-leftY;
		//得到数组下标
		int i = x/50;
		int j = y/50;
		//注意不合法点击
		if(x<0||y<0)
			return ;
		//如果Firstselect为true，则为第二次选中图片了，可以进行判断消除
		if(Firstselect){
			if(map[i][j]!=kong){
				if(map[i][j]==FirstId){
					//判断是否点击的是同一张图片
					if(i==Firstx&&j==Firsty)
						return ;
					if(link2(Firstx,Firsty,i,j)||link1(Firstx,Firsty,i,j)||onecornerlink(Firstx,Firsty,i,j)||twocornerlink(Firstx,Firsty,i,j)){					
						drawSelectedBlock(j*50+leftX,i*50+leftY,g);
						drawLink(Firstx,Firsty,i,j);
						Firstselect = false;	
					}
					else{
						//对FirstId进行更新
						FirstId = map[i][j];
						clearSelectBlock(Firstx,Firsty,g);
						Firstx = i;
						Firsty = j;
						drawSelectedBlock(j*50+leftX,i*50+leftY,g);
					}
				}
				else{
					FirstId = map[i][j];
					clearSelectBlock(Firstx,Firsty,g);
					Firstx = i;
					Firsty = j;
					drawSelectedBlock(j*50+leftX,i*50+leftY,g);
				}
			}
		}
		else{
			if(map[i][j]!=kong){
				//将数据记录到FirstId中
				FirstId = map[i][j];
				Firstselect = true;
				Firstx = i;
				Firsty = j;
				drawSelectedBlock(j*50+leftX,i*50+leftY,g);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override 
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
