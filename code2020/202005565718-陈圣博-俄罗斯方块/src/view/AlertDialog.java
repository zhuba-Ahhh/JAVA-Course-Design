package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.GameData;

public class AlertDialog extends JDialog {
    // 弹窗模式
    public static final int OVER = 0;
    public static final int LOGI = 1;
    public static final int SETT = 2;
    /**序列化 */
    private static final long serialVersionUID = -2648491452710740342L;
    /**实例化自己 */
    static AlertDialog alertDialog = null;
    /**工厂模式 实现父类 */
    static Changer changer;
    //需要修改的对象
    JLabel buttonLabel;
    JPanel mainpanel;
    GameData gameData;
    MainWin mainWin;
    /**私有化构造函数 */
    private AlertDialog(MainWin mainWin,GameData gameData){
        super(mainWin,true);
        setSize(340, 248);
        setLocationRelativeTo(mainWin);
        this.gameData = gameData;
        this.mainWin = mainWin;
        //设置背景
        JLabel bgLabel = new JLabel(new ImageIcon("img/alert.png"));
        getContentPane().add(bgLabel);
        setUndecorated(true);
        //设置按钮字体
        buttonLabel = new JLabel("默认",JLabel.CENTER);
        buttonLabel.setFont(new Font("华文彩云",Font.BOLD,20));
        buttonLabel.setForeground(new Color(163,40,28));
        buttonLabel.setBounds(222,212,98,27);
        getLayeredPane().add(buttonLabel);
        //中央画布
        mainpanel = new JPanel();
        mainpanel.setBounds(0, 51, 340, 161);
        mainpanel.setLayout(null);
        mainpanel.setOpaque(false);
        getLayeredPane().add(mainpanel);
        //添加点击事件
        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX()>274 && e.getX()<321){
                    if(e.getY()>16 && e.getY()<51){
                        closeDialog();
                    }
                }
                if(e.getX()>222 && e.getX()<320){
                    if(e.getY()>212 && e.getY()<239){
                        changer.onclick();
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {} 
        });
    }
    /**对外接口 */
    public static AlertDialog getInstance(MainWin mainWin,GameData gameData,int model){
        synchronized(AlertDialog.class){ //关键字锁住类
            if(alertDialog == null){
                alertDialog = new AlertDialog(mainWin, gameData);
            }
            switch(model){
                case OVER:
                    changer = new OverChanger(alertDialog);
                    break;
                case LOGI:
                    changer = new LogiChanger(alertDialog);
                    break;
                case SETT:
                    changer = new SettChanger(alertDialog);
                    break;
            }
            changer.changeView();
            return alertDialog;
        }
    }
    public void openDialog(){ //打开弹窗
        setVisible(true);
    }
    public void closeDialog(){
        setVisible(false);
    }
}


interface changeable{
    public void onclick();
    public void changeView();
}

abstract class Changer implements changeable{};

class OverChanger extends Changer{
    AlertDialog ad;
    OverChanger(AlertDialog ad){
        this.ad=ad;
    }
    @Override
    public void onclick() {
        ad.gameData.playerData.add(ad.gameData.score);
        ad.gameData.playerData.getInfo();
        ad.gameData.score = 0;
        ad.mainWin.repaint();
        ad.closeDialog();
    }
    @Override
    public void changeView() {
        ad.buttonLabel.setText("确定");
        JLabel overLabel = new JLabel("游戏结束，分数："+ad.gameData.score,JLabel.CENTER);
        overLabel.setFont(new Font("华文彩云",Font.BOLD,20));
        overLabel.setForeground(new Color(163,40,28));
        overLabel.setBounds(0,0,340,161);
        ad.mainpanel.removeAll();
        ad.mainpanel.add(overLabel);
    }
}

class LogiChanger extends Changer{
    AlertDialog ad;
    JLabel noteLabel;
    JTextField nickField;
    JPasswordField passField;
    LogiChanger(AlertDialog ad){
        this.ad=ad;
    }

    @Override
    public void onclick() {
        // System.out.println("登录");
        if (nickField.getText().equals("")) {
            noteLabel.setText("用户名不能为空");
        } else {
            if (ad.gameData.playerData.Login(nickField.getText(), new String(passField.getPassword()))) {
                ad.gameData.nick = nickField.getText();
                ad.closeDialog();
            } else {
                noteLabel.setText("该昵称已被注册，您需要正确的密码");
            }
        }
    }
    @Override
    public void changeView() {
        ad.buttonLabel.setText("登录/注册");
        ad.buttonLabel.setFont(new Font("黑体",Font.BOLD,20));
        //昵称和密码
        JLabel nickLabel = new JLabel("昵称:",JLabel.CENTER);
        nickLabel.setFont(new Font("华文彩云",Font.BOLD,20));
        nickLabel.setForeground(new Color(163,40,28));
        nickLabel.setBounds(41,19,80,30);
        JLabel passLabel = new JLabel("密码",JLabel.CENTER);
        passLabel.setFont(new Font("华文彩云",Font.BOLD,20));
        passLabel.setForeground(new Color(163,40,28));
        passLabel.setBounds(41,73,80,30);
        //昵称密码的输入框
        nickField = new JTextField(ad.gameData.nick,20);
        passField = new JPasswordField(20);
        nickField.setBounds(144,19,135,30);
        passField.setBounds(144,73,135,30);
        //提示框
        noteLabel = new JLabel(ad.gameData.nick.equals("") ? "" : "您已登录了");
        noteLabel.setForeground(Color.red);
        noteLabel.setBounds(35,112,300,30);
        ad.mainpanel.removeAll();
        ad.mainpanel.add(nickLabel);
        ad.mainpanel.add(passLabel);
        ad.mainpanel.add(nickField);
        ad.mainpanel.add(passField);
        ad.mainpanel.add(noteLabel);
    }
}

class SettChanger extends Changer{
    AlertDialog ad;
    JLabel shezhiLabel;
    JTextField shezhiField;
    SettChanger(AlertDialog ad){
        this.ad=ad;
    }

    @Override
    public void onclick() {
        if(shezhiField.getText().equals("是"))
        {
            ad.gameData.playerData.shanchu();
        }
        // System.out.println("应用");
        // if(shezhiField.getText().equals("是")){
        //     try {
        //         Connection conn;
        //         Statement stmt;
        //         Class.forName("org.sqlite.JDBC");
        //         conn = DriverManager.getConnection("jdbc:sqlite:data/player.db");
        //         stmt = conn.createStatement();
        //         System.out.println("数据库连接成功");
        //         String sql="delete from scoreList where score!=0";//生成一条sql语句
        //         stmt.executeUpdate(sql);//执行sql语句
        //         System.out.println("删除成功");
        //     } catch (ClassNotFoundException e) {
        //         System.out.println("驱动加载失败");
        //         e.printStackTrace();
        //     } catch (SQLException e) {
        //         System.out.println("数据库连接失败");
        //         e.printStackTrace();
        //     }
            
        // }
        ad.closeDialog();
    }

    @Override
    public void changeView() {
        ad.buttonLabel.setText("应用");
        //是否清空数据库
        shezhiLabel = new JLabel("是否清空记录：",JLabel.CENTER);
        shezhiLabel.setFont(new Font("华文彩云",Font.BOLD,15));
        shezhiLabel.setForeground(new Color(163,40,28));
        shezhiLabel.setBounds(10,72,120,30);
        //填写的数据框
        shezhiField = new JTextField("",20);
        shezhiField.setBounds(144,72,135,30);

        ad.mainpanel.removeAll();
        ad.mainpanel.add(shezhiLabel);
        ad.mainpanel.add(shezhiField);
    }
}