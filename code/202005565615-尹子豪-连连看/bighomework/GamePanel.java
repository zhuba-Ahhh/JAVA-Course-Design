package bighomework;



import java.awt.Color;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener ,MouseListener,KeyListener{
	public Image[] pics;//图片数组
	public int n;//行列数
	public int[][] map;//存储地图信息
	public int leftX = 140,leftY = 80;//row,column表示人物坐标；leftX,leftY记载左上角图片位置
	public boolean isClick = false;//标记是否第一次选中图片
	public int clickId,clickX,clickY;//记录首次选中图片的id,以及数组下标
	public int linkMethod;//连接方式
	public Node z1,z2;//存储拐角点的信息
	public Map mapUtil;//地图工具类
	public static int count = 0;//存储消去图案的个数
	public static final int LINKBYHORIZON = 1,LINKBYVERTICAL = 2,LINKBYTURNONECE = 3,LINKBYTWOCORNER = 4;
	public static final int BLANK_STATE = -1;//表示当前没有方块
	public int level=0;//用于表示当前模式以及难度
	public GamePanel(int count,int n,int level){
		setSize(400, 400);
		this.n = n;
		this.level=level;
		mapUtil = new Map(count, n);
		map = mapUtil.getMap();//获取初始时，图片种类为count,行列数为n的地图信息
		this.setVisible(true); 
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);//是键盘的按键能够聚焦
		getPics(level);
		repaint();
	}
	//更新图片
	public void init(int count,int n,int level){
		this.n = n;
		this.level=level;
		mapUtil = new Map(count, n);
		map = mapUtil.getMap();//获取初始时，图片种类为count,行列数为n的地图信息
		getPics(level);
		repaint();
	}
	//初始化图片数组
	public void getPics(int level) {
		pics = new Image[10];
		if(level<=2){
			for(int i=0;i<=9;i++){
				pics[i] = Toolkit.getDefaultToolkit().getImage("picture/pic"+(i+1)+".png");
			}
		}
		else{
			for(int i=0;i<=9;i++){
				pics[i] = Toolkit.getDefaultToolkit().getImage("LinkGame1/pic"+(i+1)+".png");
			}
		}		
	}
    //将图片画出来以及
	public void paint(Graphics g){
		g.clearRect(0, 0, 800, 700);
		Image ig=Toolkit.getDefaultToolkit().getImage("back"+".png");
		g.drawImage(ig,0,0,800,700,this);
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(isBlocked(i, j)){
					g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
				}
			}
		}
		
	}
	//判断是否可以水平相连
	public boolean horizon(int x1,int y1,int x2,int y2){
        if(x1==x2&&y1==y2){
            return false;
        }
        if(x1!=x2){
            return false;
        }
        int start=Math.min(y1, y2);
        int end=Math.max(y1, y2);
        for(int i=start+1;i<end;i++){
            if(isBlocked(x1, i)){
                return false;
            }
        }
        linkMethod=LINKBYHORIZON;
        return true;
    }
    //判断当前位置是否有方块
    public boolean isBlocked(int x,int y){
        if(map[x][y]!=BLANK_STATE){
            return true;
        }
        else return false;
    }
	//判断是否可以垂直连接
	public boolean vertical(int x1,int y1,int x2,int y2){
        if(x1==x2&&y1==y2){
            return false;
        }
        if(y1!=y2){
            return false;
        }
        int start=Math.min(x1, x2);
        int end=Math.max(x1, x2);
        for(int i=start+1;i<end;i++){
            if(isBlocked(i, y1)){
                return false;
            }
        }
        linkMethod=LINKBYVERTICAL;
        return true;
    }
	//判断是否可以通过一个拐点相连
	public boolean trunOnce(int x1, int y1, int x2, int y2) {
		if(x1==x2&&y1==y2){
            return false;
        }
        int cx=x2,cy=y1;
        if(!isBlocked(cx, cy)){
            if(vertical(x1, y1, cx, cy)&&horizon(x2, y2, cx, cy)){
                linkMethod=LINKBYTURNONECE;
                z1=new Node(cx, cy);
                return true;
            }
        }
        int dx=x1,dy=y2;
        if(!isBlocked(dx, dy)){
            if(horizon(x1, y1, dx, dy)&&vertical(dx, dy, x2, y2)){
                linkMethod=LINKBYTURNONECE;
                z1=new Node(dx, dy);
                return true;
            }
        }
        return false;
	}
	//判断是否可以通过两个拐点相连
	public boolean twoCornerLink(int x1, int y1, int x2, int y2) {
		for(int i=-1;i<=n;i++){
			//与第一个点垂直相连
            if(vertical(x1, y1, i, y1)){
                //两个拐点在地图区域之外
                if((i==-1||i==n)&&vertical(x2, y2, i,y2)){
                    z1 = new Node(i,y1);
                    z2 = new Node(i,y2);
                    linkMethod = LINKBYTWOCORNER;
                    return true;
                }
                if(i>=0&&i<n&&!isBlocked(i, y1)){
                    if(trunOnce(i, y1, x2, y2)){
                        linkMethod = LINKBYTWOCORNER;
                        z1 = new Node(i,y1);
                        z2 = new Node(i,y2);
                        return true;
                    }
                }
            }
            //与第一个点水平相连
            else if(horizon(x1,y1, x1, i)){
                //两个拐点在地图区域之外
                if((i==-1||i==n)&&horizon(x2, y2, x2,i)){
                    linkMethod = LINKBYTWOCORNER;
                    z1 = new Node(x1,i);
                    z2 = new Node(x2,i);
                    return true;
                } 
                if(i>=0&&i<n&&!isBlocked(x1, i)){
                    if(trunOnce(x1, i, x2, y2)){
                        linkMethod = LINKBYTWOCORNER;
                        z1 = new Node(x1,i);
                        z2 = new Node(x2,i);
                        return true;
                    }
                }
            }	
		}
		return false;
	}
	//画选中框
	public void drawSelectedBlock(int x, int y, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;//生成Graphics对象
		g2.setColor(Color.RED);
		g.drawRect(x+1, y+1, 48, 48);
	}
	//画线
	public void drawLink(int x1, int y1, int x2, int y2) {
		Graphics g = this.getGraphics();//得到画笔
		Point p1 = new Point(y1*50+leftX+25,x1*50+leftY+25);
		Point p2 = new Point(y2*50+leftX+25,x2*50+leftY+25);
		if(linkMethod == LINKBYHORIZON || linkMethod == LINKBYVERTICAL){//直接连接
			g.drawLine(p1.x, p1.y,p2.x, p2.y);
			
		}
        else if(linkMethod ==LINKBYTURNONECE){//一个拐点
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);//将拐点转换成像素坐标
			g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);		
		}
        else{//两个拐点
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
			Point point_z2 = new Point(z2.y*50+leftX+25,z2.x*50+leftY+25);
			g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
			g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);		
		}
		count+=2;//消去的方块数目+2
		GameClient.textField.setText(count+"");
		try {
			Thread.currentThread().sleep(500);//延时500ms
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();//刷新画面
		map[x1][y1] = BLANK_STATE;
		map[x2][y2] = BLANK_STATE;
		isWin();//判断游戏是否结束
	}
    //清空所选框
	public void clearSelectBlock(int i,int j,Graphics g){
		g.clearRect(j*50+leftX, i*50+leftY, 50, 50);//这个会清空矩形中的所有内容
		g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
	}
	//提示，如果有可以连接的方块就消去并且返回true
	public boolean find2Block() {
		if(isClick){//如果之前玩家选中了一个方块，清空该选中框
			clearSelectBlock(clickX, clickY, this.getGraphics());
		    isClick = false;
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(!isBlocked(i, j)){
					continue;
				}				
				for(int p=0;p<n;p++){
					for(int q=0;q<n;q++){
						  if(map[p][q]!=map[i][j]||(p==i&&q==j)){//如果图案不相等
							  continue;
						  }						  
						  if(vertical(p,q,i,j)||horizon(p,q,i,j)||trunOnce(p,q,i,j)||twoCornerLink(p,q,i,j)){
							  drawSelectedBlock(j*50+leftX, i*50+leftY, this.getGraphics());
							  drawSelectedBlock(q*50+leftX, p*50+leftY, this.getGraphics());
							  drawLink(p, q, i, j);
							  repaint();
							  return true;
						  }
				
					}
				}				
			}
		}
		isWin();
		return false;
	}
    //判断是否过关以及选择是否开始新的游戏
	public void isWin() {
		if(count==n*n){
			if(level==0||level>=3){
				String msg = "恭喜您通关成功，是否开始新局？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					System.exit(0);
				}else if(choice == 0){
					startNewGame();
				}
			}
			else if(level<=2){
				String msg = "恭喜您闯关关成功，是否挑战更高难度？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg,title,type);
				if(choice==1){
					System.exit(0);
				}else if(choice == 0){
					init(10, 10, level+1);
					count=0;
					GameClient.textField.setText(count+"");
				}
			}
		}
	}
	//开始新的游戏
	public void startNewGame() {
		count = 0;
		mapUtil = new Map(10,n);		
		map = mapUtil.getMap();
		isClick = false;
		clickId = -1;
		clickX = -1;
		clickY = -1;
		linkMethod = -1;
		this.addKeyListener(this);
		this.setFocusable(true);
		GameClient.textField.setText(count+"");
		repaint();
	}
	public class Node{
		int x;
		int y;
		public Node(int x,int y){
			this.x = x;
			this.y = y;
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_A){//打乱地图			
			map = mapUtil.getResetMap();
			repaint();
		}
		else if(e.getKeyCode() == KeyEvent.VK_D){//智能提示
			if(!find2Block()){
				JOptionPane.showMessageDialog(this, "没有可以连通的方块了,请按A进行重列");
			}
			isWin();//判断是否游戏结束
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Graphics g = this.getGraphics();
		int x = e.getX()-leftX;//点击位置x-偏移量x
		int y = e.getY()-leftY;//点击位置y-偏移量y
		int i = y/50;//对应数组行数,根据像素坐标转换成数组下标坐标//这里要调换位置因为判断是否可以连接时用的时相反的
		int j = x/50;//对应数组列数
		if(x<0||y<0||i>=n||j>=n){//超出地图范围
			JOptionPane.showMessageDialog(this, "您的点击错误");
			return ;
		}
		if(isClick){//第二次点击
			if(isBlocked(i, j)){
				if(map[i][j]==clickId){//点击的是相同图片Id,但不是重复点击同一图片
					if(i==clickX&&j==clickY)
					return ;
					if(vertical(clickX,clickY,i,j)||horizon(clickX,clickY,i,j)||trunOnce(clickX,clickY,i,j)||twoCornerLink(clickX,clickY,i,j)){//如果可以连通，画线连接，然后消去选中图片并重置第一次选中标识						
						drawSelectedBlock(j*50+leftX,i*50+leftY,g);
						drawLink(clickX,clickY,i,j);//画线连接
						isClick = false;	

					}
					else{//不能连接
						clickId = map[i][j];//重新选中图片并画框
						clearSelectBlock(clickX,clickY,g);
						clickX = i;
						clickY = j;
						drawSelectedBlock(j*50+leftX,i*50+leftY,g);
					}
				}
				else{//不是同一张图片
					clickId = map[i][j];//重新选中图片并画框
					clearSelectBlock(clickX,clickY,g);
					clickX = i;
					clickY = j;
					drawSelectedBlock(j*50+leftX,i*50+leftY,g);
				}
			}	
		}
        else{//第一次点击
			if(isBlocked(i, j)){
				//选中图片并画框
				clickId = map[i][j];
				isClick = true;
				clickX = i;
				clickY = j;
				drawSelectedBlock(j*50+leftX,i*50+leftY,g);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {	

	}
	@Override 
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
			
	}

}
