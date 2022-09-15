/**
 *  这是一个简单的函数显示工具，用户输入简单函数，程序自动画出函数图像
 *  支持常数e, sin, cos, tan, ln, ^, 加减乘除括号以及它们的复合运算
 *  只能有一个变量，而且必须是x，比如sinx, 敲击回车结束输入
 *  显示出函数图像后，可以通过拖动滑块来改变图像显示的比例大小
 **/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class FunctionDraw extends JFrame {
	JTextField txt1; // 显示光标此时的坐标位置的文本区
	JTextField txt2; // 输入函数表达式的文本区
	JLabel label; // 提示标签
	JLabel zerolb; // 原点标签
	JSlider slider; // 调整图片放缩比例的滑块
	JPanel panel; // 包含组件的面板
	DrawFunPanel drawpanel; // 绘制函数图像的面板
	int SliderValue; // 滑块所在的刻度
	Cursor crossCursor, handCursor; // 鼠标光标
	int W, H; // 窗体的长和宽
	public FunctionDraw(String title){
		super(title); // 设置窗体标题
		W = 800;
		H = 600;
		// 创建并设置提示标签
		label = new JLabel("请直接输入函数的表达式，如sinx,函数与函数之间一定要有符号");
		label.setBounds(20,30,350,20);
		// 创建并设置文本区
		txt1 = new JTextField(40);
		txt2 = new JTextField(40);
		txt1.setEditable(false);
		txt1.setBounds(20,5,350,25);
		txt2.setBounds(20,50,350,25);
		// 设置文本区输入监听
		txt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawpanel.getInput(e.getActionCommand());
				drawpanel.repaint();
			}
		});
		// 设置文本区鼠标移动监听，显示当前坐标
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				double X, Y; // 相对坐标
				double sx, sy;// applet中实际坐标
				sx = e.getX();
				sy = e.getY();
				// 坐标变换
				X = (double)(sx - 0.5 * W - 8) / SliderValue; // 减去边框占8像素
				Y = (double)-(sy - 0.4 * H - 30) / SliderValue; // 减去标题栏占30像素
				// 设置输出格式
				DecimalFormat twoDigit = new DecimalFormat("0.000");
				txt1.setText("当前的坐标：" + "        x=" + twoDigit.format(X) + "                y=" + twoDigit.format(Y));
				// 设置光标图形
				crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
				handCursor = new Cursor(Cursor.HAND_CURSOR);
				if(sx > 0 && sx < W && sy > 0 && sy < 0.85 * H)
					setCursor(crossCursor);
				else
					setCursor(handCursor);
			}
		});
		// 创建并设置滑块
		slider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		slider.setPaintLabels(true); // 添加默认刻度标签
		slider.setPaintTicks(true); // 添加刻度标记
		slider.setMajorTickSpacing(20); // 设置主刻度间隔
		slider.setMinorTickSpacing(5); // 设置次刻度的间隔
		slider.setBounds((int)(W/1.8),(int)(H*0.02),350,60);
		// 设置滑块监听
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SliderValue = ((JSlider)e.getSource()).getValue();
				drawpanel.setValue(SliderValue);
				drawpanel.repaint();
			}
		});
		// 创建面板并添加组件
		panel = new JPanel();
		panel.setBounds(0, (int)(0.8 * H), W,(int)(0.2 * H));
		panel.setLayout(null);
		panel.add(txt1);
		panel.add(label);
		panel.add(txt2);
		panel.add(slider);
		// 创建绘制函数图像面板
		drawpanel = new DrawFunPanel();
		drawpanel.setBounds(0, 0, W, (int)(0.8 * H));
		drawpanel.setLayout(null);
		// 创建原点标签并添加
		zerolb = new JLabel("(0, 0)");
		zerolb.setBounds(W / 2, H / 2 - 75, 100, 50);
		drawpanel.add(zerolb);
		// 初始化SliderValue
		SliderValue = 1000;
		drawpanel.setValue(SliderValue);
		drawpanel.repaint();
		// 在容器中添加面板
		// Container contentpane = this.getContentPane();
		this.setLayout(null);
		this.add(panel);
		this.add(drawpanel);
	}
	public static void main(String[] args) {
		FunctionDraw frame = new FunctionDraw("函数绘图工具-简单函数绘制");
		frame.setBounds(200, 100, 816, 600);
		ImageIcon icon = new ImageIcon("tubiao.png");
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
class DrawFunPanel extends JPanel {
	int W, H;
	int y1, y2;
	int SliderValue;
	double x;
	FunctionValue fv;
	public DrawFunPanel() {fv = new FunctionValue();}
	public void getInput(String input) {fv.getInput(input);}
	public void setValue(int value){this.SliderValue = value;}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		W = this.getWidth();
		H = this.getHeight();
		// 填充背景颜色
		g.setColor(Color.white);
		g.fillRect(0, 0, W, H);
		// 绘制坐标轴
		g.setColor(Color.black);
		g.drawLine(0, H / 2 , W, H / 2); // x轴
		g.drawLine(W / 2, 0, W / 2, H); // y轴
		// 绘制箭头
		g.drawLine((int)(W / 2), 0, (int)(W / 2) + 4, 4);
		g.drawLine((int)(W / 2), 0, (int)(W / 2) - 4, 4);
		g.drawLine((int)W, (int)(H / 2), W - 4, (int)(H / 2) + 4);
		g.drawLine((int)W, (int)(H / 2), W - 4, (int)(H / 2) - 4);
		// 绘制函数图像
		g.setColor(Color.red);
		y2 = 0;
		try{
			for(int i = -W / 2; i <= W / 2; ++i){
				x = (double) i / SliderValue; // 自变量取值
				fv.setValue(x);
				y1 = (int)(fv.getValue() * SliderValue); // 计算纵坐标值
				if(y1 < -(int)(H / 2) )	continue;//将不在屏幕范围内的点过滤
				// System.out.println("x = " + x + " y1 = " + y1);
				g.fillRect((int)(W / 2) + i,(int)(H / 2) - y1, 2, 2); // 画线
				//如果两点间距离过远且不是无穷大，用直线连接
				if(i > -(int)(W / 2) + 1 && Math.abs(y2 - y1) > 1 && Math.abs(y2 - y1) < 100)
					g.drawLine((int)(W / 2) + i - 1, (int)(H / 2) - y2, (int)(W / 2) + i, (int)(H / 2) - y1);
				y2=y1;
			}
		}catch(Exception e){}
	}
}
class FunctionValue{
	private double x;
	private String str; // 标准串
	private String buf; // 存放后缀表达
	private String delim = "+-*/^sctlex";
	final private int INF = 0x3fffffff;
	public void getInput(String input) {
		// 将输入的字符串转化为标准串
		str = "";
		for(int i = 0; i < input.length(); ++i) {
			if(input.charAt(i) == ' ') continue; // 过滤空格
			else if(input.charAt(i) == '(' || input.charAt(i) == ')')	str += input.charAt(i); // 对括号的判断
			else if(i + 2 < input.length() && input.substring(i, i + 3).equals("sin")) {
				str += "s"; // 对sinx的判断
				i += 2;
			}
			else if(i + 2 < input.length() && input.substring(i, i + 3).equals("cos")) {
				str += "c"; // 对cosx的判断
				i += 2;
			}
			else if(i + 2 < input.length() && input.substring(i,  i + 3).equals("tan")) {
				str += "t"; // 对tanx的判断
				i += 2;
			}
			else if(i + 2 < input.length() && input.substring(i, i + 2).equals("ln")) {
				str += "l"; // 对lnx的判断
				i += 1;
			}
			else	str += input.charAt(i); // 其他运算符和x变量直接读入
		}
	}
	public void setValue(double num){x = num;}
	private boolean isOperator(char c){
		// 判断是否是操作符或x或e
		return (c == '+' || c == '-' || c == '*' || c == '/' ||
				c == '^' || c == 's' || c == 'c' || c == 't' ||
				c == '(' || c == ')' || c == '#' || c == 'l' ||
				c == 'o' || c == 'x' || c == 'e');
	}
	private boolean isPrecede(char c1, char c2){
		// 比较c1和c2的优先级，
		// c1比c2优先级高，返回true，否则返回false，false表示出栈
		// +,- < *,/ < ^ < sin,cos,tan
		// +,-,*,/优先级相同从左往右计算，sin,cos,tan优先级相同从右往左计算
		if((c1 == '+' || c1 == '-') && c2 == '(') return true;
		if((c1 == '*' || c1 == '/') && (c2 == '+' || c2 == '-' || c2 == '(')) return true;
		if(c1 == '^' && (c2 == '+' || c2 == '-' || c2 == '*' || c2 == '/' || c2 == '(')) return true;
		if(c1 == 's' || c1 == 'c' || c1 == 't' || c1 == 'l') return true;
		return false;
	}
	private double Operate(double x, char c){
		// 单目运算符计算
		if(c == 's') return Math.sin(x);
		else if(c == 'c') return Math.cos(x);
		else if(c == 't') return Math.tan(x);
		else if(c == 'l'){
			if(x < 0) return INF; // 过滤不在定义域的点
			else return Math.log(x);
		}
		else return INF;
	}
	private double Operate(double x, char c, double y){
		// 双目运算符计算
		if(c == '+') return x + y;
		else if(c == '-') return x - y;
		else if(c == '*') return x * y;
		else if(c == '/'){
			if(Math.abs(y) < 0.00001) return INF;
			return x / y;
		}
		else if(c == '^') return Math.pow(x, y);
		else return INF;
	}
	public void infixToPostfix(){
		// 中缀表达式转后缀表达式，并存在buf中
		buf = ""; 
		Stack st1 = new Stack();
		st1.clear();
		if(str == null) return; // 避免空指针运行时异常
		char c; // 临时变量
		for(int i = 0; i < str.length(); ++i) {
			if(str.charAt(i) == 'x' || str.charAt(i) == 'e' || (str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
				// 如果是x或者e或者数字直接输出
				buf += str.charAt(i);
			}
			else if(str.charAt(i) == '('){
				// 遇到左括号直接入栈
				st1.push(str.charAt(i));
			}
			else if(str.charAt(i) == ')'){
				// 遇到右括号将包含在括号内的元素出栈
				while(!st1.empty()){
					c = (Character) st1.peek();
					st1.pop();
					if(c == '(')
						break;
					buf += c;
				}
			}
			else{
				// 如果是运算符则需要判断
				if(st1.empty()) {
					// 如果栈为空则直接入栈
					st1.push(str.charAt(i));
				}
				else {
					// 如果栈非空则需要比较该元素与栈顶元素的优先级
					// 若比栈顶元素优先级高则入栈，否则将栈顶元素出栈，直到找到或者栈空
					while(!st1.empty()){
						c = (Character) st1.peek();
						if(isPrecede(str.charAt(i), c)){
							break;
						}
						buf += c;
						st1.pop();
					}
					st1.push(str.charAt(i));
				}
			}
		}
		// 将剩余元素出栈
		while(!st1.empty()){
			c = (Character) st1.peek();
			buf += c;
			st1.pop();
		}
		buf += '#'; // 添加结尾标识符 
		// 输出后缀表达式
		//System.out.println(buf);
	}
	public double getValue() {		
		infixToPostfix();
		// 由后缀表达式计算函数值
		// 如果是sin, cos, tan, ln单目运算符则取栈顶元素
		// 如果是+-*/^双目运算符则取栈顶前两个元素
		Stack<Double> st2 = new Stack<Double>();
		double res = 0, tmp, tmp1, tmp2;
		char c; // 临时变量
		// 以delim中的字符为分隔符
		StringTokenizer parser = new StringTokenizer(buf, delim, true);
		String token = parser.nextToken();
		while(token.charAt(0) != '#'){
			if(token.length() == 1 && isOperator(token.charAt(0))){
				c = token.charAt(0);
				if(c == 'x'){st2.push(x);}
				else if(c == 'e'){st2.push(Math.E);}
				else if(c == 's' || c == 'c' || c == 't' || c == 'l'){
					tmp = st2.peek();
					st2.pop();
					res = Operate(tmp, c);
					st2.push(res);
				}
				else if(c == '+' || c == '-' || c == '*' || c == '/' || c == '^'){
					tmp1 = st2.peek();
					st2.pop();
					tmp2 = st2.peek();
					st2.pop();
					res = Operate(tmp2, c, tmp1);
					st2.push(res);
				}
			}
			else{st2.push(Double.parseDouble(token));}
			token = parser.nextToken();
		}
		res = st2.peek();
	    // System.out.println("x = " + x + " res = " + res);
		return res;
	}
}