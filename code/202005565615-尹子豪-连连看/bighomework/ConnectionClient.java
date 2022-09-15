package bighomework;




import java.awt.BasicStroke;
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
import java.io.*;
import java.net.*;
public class ConnectionClient extends JPanel implements ActionListener ,MouseListener,KeyListener, Runnable{
	public Image[] pics;//图片数组
	public int n;//行列数
	public int[][] map;//存储本地地图信息
    public int[][] map2;//存储对手地图信息
	public int leftX = 140,leftY = 80;//row,column表示人物坐标；leftX,leftY记载左上角图片位置
	public boolean isClick = false;//标记是否第一次选中图片
	public int clickId,clickX,clickY;//记录首次选中图片的id,以及数组下标
	public int linkMethod;//连接方式
	public Node z1,z2;//存储拐角点的信息
	public Map mapUtil;//地图工具类
	public static int count = 0;//存储消去图案的个数
	public static final int LINKBYHORIZON = 1,LINKBYVERTICAL = 2,LINKBYTURNONECE = 3,LINKBYTWOCORNER = 4;
	public static final int BLANK_STATE = -1;//表示当前没有方块
    ServerSocket sever;
    DataInputStream input;
    DataOutputStream output;
    Socket s;
    Thread listener = new Thread(this);
    boolean flag_connect=false;//用于表示是否
    int prx,pry,lax,lay;//用于表示要发送给对方的坐标
	public ConnectionClient(int count,int n)throws UnknownHostException, IOException,InterruptedException{
		setSize(600, 600);
		this.n = n;
		mapUtil = new Map(count, n);
		map = mapUtil.getMap();//获取初始时，图片种类为count,行列数为n的地图信息
        map2=new int[10][10];
        s = new Socket("172.17.141.178",5500);
        output =new DataOutputStream(s.getOutputStream());
        input =new DataInputStream(s.getInputStream());
        listener.start();
        sendMap(map);
		this.setVisible(true); 
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setFocusable(true);
		getPics();
		repaint();
	}
    public void sendMap(int [][]map)throws IOException{
		String s="-";
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                s+=map[i][j];
            }
        }
        send(s);
    }
	//用于不断的发送消息
    public void client() throws UnknownHostException, IOException, InterruptedException{
        String line;
        while(true){
            while(flag_connect == false){       
                try {
                    Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            line = prx+" "+pry+" "+lax+" "+lay;
            flag_connect=false;
            send(line);    
            Thread.sleep(500);
        }
    }
	//发送消息
    public void send(String s) throws IOException{
        output.writeUTF(s);
        output.flush();
    }
	//用于不断地接收消息
    @Override
    public void run() {
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        while(true){
            try {
                String data = input.readUTF();
                if(data.charAt(0)=='-'){
                    int index=1;
                    for(int i=0;i<10;i++){
                        for(int j=0;j<10;j++){
							if(data.charAt(index)=='-'){
								map2[i][j]=-1;
								index+=2;
							}
							else
                            map2[i][j]=data.charAt(index++)-'0';
                        }
                    }
					repaint();
                }
				else if(data.charAt(0)=='+'){
					JOptionPane.showMessageDialog(this, "很遗憾，您输了");
					System.exit(0);
				}
                else{
                    String[] a = data.split(" ");
                    x1 = Integer.parseInt(a[0]);
                    y1 = Integer.parseInt(a[1]);
                    x2 = Integer.parseInt(a[2]);
                    y2 = Integer.parseInt(a[3]);
                    map2[x2][y2] = BLANK_STATE;
                    map2[x1][y1] = BLANK_STATE;
					repaint();
                }
                repaint();    
            }catch (IOException e) {System.out.println("开始时的错误");
            // TODO Auto-generated catch block
            e.printStackTrace();}       
        }
    }
	//初始化图片数组
	public void getPics() {
		pics = new Image[10];
		for(int i=0;i<=9;i++){
			pics[i] = Toolkit.getDefaultToolkit().getImage("bighomework/picture/pic"+(i+1)+".png");
		}		
	}
    //将图片画出来以及
	public void paint(Graphics g){
		g.clearRect(0, 0, 800, 700);
		Image ig=Toolkit.getDefaultToolkit().getImage("bighomework/back"+".png");
		g.drawImage(ig,0,0,800,700,this);
        g.drawImage(ig,810,0,800,700,this);
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(isBlocked(i, j)){
					g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
				}
                if(map2[i][j]!=BLANK_STATE){
                    g.drawImage(pics[map2[i][j]], leftX+j*50+810,leftY+i*50,50,50,this);
                }
			}
		}
		
	}
	//判断是否可以水平相连
	public boolean horizon(int x1,int y1,int x2,int y2){//水平能不能消除
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
	public boolean vertical(int x1,int y1,int x2,int y2){//垂直能不能消除
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
		BasicStroke s = new BasicStroke(1);//宽度为1的画笔
		g2.setStroke(s);
		g2.setColor(Color.ORANGE);
		g.drawRect(x+1, y+1, 48, 48);
	}
	//画线，此处的x1,y1,x2,y2二维数组下标
	//用于消除方块和画线
	public void drawLink(int x1, int y1, int x2, int y2) {
		Graphics g = this.getGraphics();
		Point p1 = new Point(y1*50+leftX+25,x1*50+leftY+25);
		Point p2 = new Point(y2*50+leftX+25,x2*50+leftY+25);
		if(linkMethod == LINKBYHORIZON || linkMethod == LINKBYVERTICAL){
			g.drawLine(p1.x, p1.y,p2.x, p2.y);
			//System.out.println("无拐点画线");
		}
        else if(linkMethod ==LINKBYTURNONECE){
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);//将拐点转换成像素坐标
			g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);
			//System.out.println("单拐点画线");			
		}
        else{
			Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
			Point point_z2 = new Point(z2.y*50+leftX+25,z2.x*50+leftY+25);
			g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
			g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
			g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);
			//System.out.println("双拐点画线");			
		}
		count+=2;//消去的方块数目+2
		ConnectionShow.textField.setText(count+"");
		try {
			Thread.currentThread().sleep(500);//延时500ms
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map[x1][y1] = BLANK_STATE;
		map[x2][y2] = BLANK_STATE;
        prx=x1;
        pry=y1;
        lax=x2;
        lay=y2;
        repaint();//刷新画面
        flag_connect=true;
		isWin();//判断游戏是否结束
	}
    //清空所选框
	public void clearSelectBlock(int i,int j,Graphics g){
		g.clearRect(j*50+leftX, i*50+leftY, 50, 50);
		g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
//		System.out.println("清空选定"+i+","+j);
	}
	//提示，如果有可以连接的方块就消去并且返回true
	public boolean find2Block() {
//		boolean isFound = false;
		if(isClick){//如果之前玩家选中了一个方块，清空该选中框
			clearSelectBlock(clickX, clickY, this.getGraphics());
		    isClick = false;
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(!isBlocked(i, j)){
					continue;
				}				
				for(int p=i;p<n;p++){
					for(int q=0;q<n;q++){
						  if(map[p][q]!=map[i][j]||(p==i&&q==j)){//如果图案不相等
							  continue;
						  }						  
						  if(vertical(p,q,i,j)||horizon(p,q,i,j)||trunOnce(p,q,i,j)||twoCornerLink(p,q,i,j)){
							  drawSelectedBlock(j*50+leftX, i*50+leftY, this.getGraphics());
							  drawSelectedBlock(q*50+leftX, p*50+leftY, this.getGraphics());
							  drawLink(p, q, i, j);
							  try {
								sendMap(map);
							} catch (IOException e) {
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
		isWin();
		return false;
	}
    //判断是否过关以及选择是否开始新的游戏
	public void isWin() {
		//向对面发送一个消息
		if(count==n*n){
			JOptionPane.showMessageDialog(this, "恭喜您获胜");
			try {
				send("+");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	//开始新的游戏
	public void startNewGame() {
		// TODO Auto-generated method stub
		count = 0;
		mapUtil = new Map(10,n);		
		map = mapUtil.getMap();
		isClick = false;
		clickId = -1;
		clickX = -1;
		clickY = -1;
		linkMethod = -1;
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
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_A){//打乱地图			
			map = mapUtil.getResetMap();
			try {
				sendMap(map);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			repaint();
		}
		if(e.getKeyCode() == KeyEvent.VK_D){//智能提示
			if(!find2Block()){
				JOptionPane.showMessageDialog(this, "没有可以连通的方块了");
			}
			isWin();//判断是否游戏结束
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Graphics g = this.getGraphics();
		int x = e.getX()-leftX;//点击位置x-偏移量x
		int y = e.getY()-leftY;//点击位置y-偏移量y
		int i = y/50;//对应数组行数,根据像素坐标转换成数组下标坐标
		int j = x/50;//对应数组列数
		if(x<0||y<0||i>=n||j>=n){//超出地图范围
			JOptionPane.showMessageDialog(this, "您的点击有错误");
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

					}else{
						clickId = map[i][j];//重新选中图片并画框
						clearSelectBlock(clickX,clickY,g);
						clickX = i;
						clickY = j;
						drawSelectedBlock(j*50+leftX,i*50+leftY,g);
					}
				}else{
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
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
