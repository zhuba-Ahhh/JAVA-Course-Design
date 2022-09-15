package dd;


import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class DrawFunction extends JPanel {

    int axis_int = 30;
    int down = 2;
    public Function tmp = new Function();
    Color color[] = new Color[10];
    Point origin = new Point(300, 300);

    private Image iBuffer;
    private Graphics2D gBuffer;
    int cnt = 0;

    public void drawmyimage(){
        if (iBuffer == null)
            {
                iBuffer = createImage(600, 600);
                gBuffer = (Graphics2D)iBuffer.getGraphics();
            }
        else
            {
                iBuffer = createImage(600,600);
                gBuffer = (Graphics2D)iBuffer.getGraphics();
            }
        gBuffer.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= 19; ++i)//背景虚线
            {
                gBuffer.drawLine(30 * i, 0, 30 * i, 600);
                gBuffer.drawLine(0, 30 * i, 600, 30 * i);
            }
        
        gBuffer.setColor(Color.BLACK);
        for (int j = 1; j <= 4; ++j)
            {
                gBuffer.drawString("-" + down * j, 300 + 2, 300 + 60 * j);
                gBuffer.drawString("" + down * j, 300 + 2, 300 - 60 * j);// y
            }
        for(int j = 1;j <= 4; ++j)
            {
                gBuffer.drawString("" + down * j, 300 + 60 * j, 300 - 2);
                gBuffer.drawString("-" + down * j, 300 - 60 * j, 300 - 2);// x
            }
        
        gBuffer.setStroke(new BasicStroke(2.0f));
        gBuffer.drawLine(origin.x, 0, origin.x, 600);// y轴
        gBuffer.drawLine(0, origin.y, 600, origin.y);// x轴
        

        gBuffer.setStroke(new BasicStroke(1.25f));
        //根据文本框绘制函数
        for (int colornum = 1; colornum <= tmp.num; ++colornum)
            {
                String str = tmp.textField[colornum].getText();
                if (str.equals(""))
                    continue;
                // Graphics g1 = iBuffer.getGraphics();
                // Graphics2D g = (Graphics2D)g1;
                gBuffer.setColor(color[colornum]);
                
                Point2D temp1, temp2;
                boolean Domainlast = true;//若为false，则说明上一个点不在函数定义域内
                boolean Domain = true;//当前点是否在定义域
                int ox = origin.x;
                int oy = origin.y;
                double x, y = 0;// 我们看到的坐标值
                                
                x = -1.0 * ox / axis_int;
                // temp1返回面板的实际坐标值（以像素为单位）

                DecimalFormat df = new DecimalFormat("0.00"); // 设置double类型小数点后位数格式
                Calculate cal = new Calculate(df.format(x));

                String res = cal.evaluate(str);

                if(res.equals("Domain error"))
                    {
                        Domain = false;
                        Domainlast = false;
                    }
                else if (res.charAt(0) != '-')
                    if (res.charAt(0) > 57 || res.charAt(0) < 48)
                        {
                            JOptionPane.showMessageDialog(null, "第" + colornum + "个函数表达式出错:\n" + res, "错误", 0);
                            return;
                        }
                    else
                        {
                            Domain = true;
                            y = Double.parseDouble(res);
                        }
                else
                    {
                        Domain = true;
                        Domainlast = true;
                        y = Double.parseDouble(res);
                    }

                temp1 = new Point2D.Double(x * axis_int + ox, oy - y * axis_int);
                for (int i = 0; i < 600; ++i)
                    {
                        x = x + 1.0 / axis_int;// 前进一个像素

                        cal = new Calculate(df.format(x));
                        res = cal.evaluate(str);

                        if(res.equals("Domain error"))
                            Domain = false;
                        else if (res.charAt(0) != '-')
                            if ((res.charAt(0) > 57 || res.charAt(0) < 48))
                                {
                                    JOptionPane.showMessageDialog(null, "第" + colornum + "个函数表达式出错:\n" + res, "错误", 0);
                                    break;
                                }
                            else
                                {
                                    Domain = true;
                                    y = Double.parseDouble(res);
                                }
                        else
                            {
                                Domain = true;
                                y = Double.parseDouble(res);
                            }
                        double truey = oy - y * axis_int;
                        if (Math.abs(y) < oy)
                            {
                                temp2 = new Point2D.Double(x * axis_int + ox, truey);
                                if(Domainlast)
                                    if(Domain)
                                        gBuffer.draw(new Line2D.Double(temp1, temp2));
                                if(Domain)
                                    Domainlast = true;
                                else
                                    Domainlast = false;
                                temp1 = temp2;
                            }
                    }
            }
    }

    public void paint(Graphics scr) {
        super.paint(scr);
        drawmyimage();
        scr.drawImage(iBuffer, 0, 0, this);
    }

    public void update(Graphics scr) {
        paint(scr);
    }

    public DrawFunction() {
        color[1] = Color.BLACK;
        color[2] = Color.RED;
        color[3] = Color.GREEN;
        color[4] = Color.PINK;
        color[5] = Color.BLUE;
        setLayout(null);
        setBorder(BorderFactory.createLoweredBevelBorder());
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        setSize(800, 600);
        tmp.Ad.addActionListener(new funadd());
        tmp.del.addActionListener(new fundel());
        add(tmp);
        tmp.setLocation(600, 0);
        addMouseWheelListener(new Mymouse());
    }


    class Mymouse implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getWheelRotation() == -1)//函数显示范围缩小
                {
                    if (axis_int < 60)
                        {
                            axis_int *= 2;
                            down /= 2;
                            repaint();
                        }
                }
            if (e.getWheelRotation() == 1)//扩大
                {
                    if (axis_int % 2 == 0)
                        {
                            axis_int /= 2;
                            down *= 2;
                            repaint();
                        }
                }
        }
    }

    class focus implements FocusListener {// 文本框失去焦点
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            repaint();
        }
    }

    class key implements KeyListener {// 文本框输入回车
        public void keyReleased(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER)
                repaint();
        }
    }

    class funadd implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (tmp.num <= 4)
                {
                    tmp.num++;
                    tmp.fun[tmp.num] = new JPanel();// 230,30
                    tmp.add(tmp.fun[tmp.num]);
                    tmp.fun[tmp.num].setLayout(null);
                    tmp.fun[tmp.num].setBorder(BorderFactory.createEtchedBorder());
                    tmp.fun[tmp.num].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                    JLabel funLabel = new JLabel("f(x):");
                    tmp.fun[tmp.num].add(funLabel);
                    funLabel.setForeground(color[tmp.num]);
                    funLabel.setOpaque(false);
                    funLabel.setBounds(10, 10, 30, 30);

                    tmp.textField[tmp.num] = new JTextField();
                    tmp.fun[tmp.num].add(tmp.textField[tmp.num]);
                    tmp.textField[tmp.num].setBounds(40, 10, 140, 30);
                    tmp.textField[tmp.num].setForeground(color[tmp.num]);
                    tmp.textField[tmp.num].addKeyListener(new key());
                    tmp.textField[tmp.num].addFocusListener(new focus());

                    tmp.fun[tmp.num].setBounds(0, tmp.num * 100, 200, 50);
                }
            else
                JOptionPane.showMessageDialog(null, "函数已有五个!", "错误", 0);
        }
    }

    class fundel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (tmp.num >= 1)
                {
                    tmp.remove(tmp.fun[tmp.num]);
                    tmp.repaint();
                    tmp.validate();
                    tmp.num--;
                    repaint();
                }
            else
                JOptionPane.showMessageDialog(null, "函数为空!", "错误", 0);
        }
    }

    class move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }

    }
}