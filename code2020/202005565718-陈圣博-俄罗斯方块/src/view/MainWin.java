package view;

import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.Opration;
import model.GameData;

public class MainWin extends JFrame{
    Opration opration;
    GameData gameData;
    GamePanel gamePanel;
    Container mainpane;
    StaticPanel staticPanel;
    ScoreNext scoreNext;
    PlayerPanel playerPanel;
    /**
     * 序列化
     */
    private static final long serialVersionUID=7232361914612049231L;

    public MainWin(Opration opration, GameData gameData){
        this.gameData = gameData;
        this.opration = opration;
        mainpane = getLayeredPane(); //总窗格
        setBounds(100,50,360,600);//前两个参数离左上角距离，后两个参数为宽高
        setResizable(false);//用户不可拉伸窗口
        setLayout(null);//自由布局
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //退出设置
        //设置背景
        setBack();
        //绘制区域
        staticPanel = new StaticPanel(opration);
        mainpane.add(staticPanel);
        //添加游戏方块
        setGamePanel();
        //添加窗口提示
        setScoreNext();
        //添加游戏记录
        playerPanel = new PlayerPanel(gameData);
        mainpane.add(playerPanel);
        mainpane.setComponentZOrder(playerPanel, 0);
        //设置图层顺序
        setZindex();
        //设置获得按键的权力
        setFocusable(true);
    }

    /**
     * 添加窗口提示
     */
    private void setScoreNext() {
        scoreNext = new ScoreNext(gameData);
        mainpane.add(scoreNext);
    }


    /**
     * 设置图层顺序
     */
    private void setZindex() {
        mainpane.setComponentZOrder(staticPanel, 1);
        mainpane.setComponentZOrder(gamePanel, 0);
        mainpane.setComponentZOrder(scoreNext, 0);
    }
    /**
     * 设置背景
     */
    void setBack(){
        ImageIcon imgic = new ImageIcon("img/bg.png"); //添加背景图片
        JLabel jl = new JLabel(imgic);
        jl.setBounds(0,0,360,600);//头两个参数是图片到窗口的距离
        getContentPane().add(jl); //加图片
    }

    void setGamePanel(){
        gamePanel = new GamePanel(gameData);
        mainpane.add(gamePanel);
    }

    /**
     * 获取游戏区
     */
    public GamePanel getGamePanel(){
        return gamePanel;
    }

    /**
     * 获取整个画布
     * @return
     */
    public StaticPanel getStaticPanel(){
        return staticPanel;
    }

    /**
     * 获取分数提示
     */
    public ScoreNext getScoreNext(){
        return scoreNext;
    }

    /**
     * 获取流程控制按钮
     */
    public JButton getStst(){
        return staticPanel.stst;
    }

    /**
     * 弹窗
     * @param string 信息
     */
    public void alert(int model) {
        //JOptionPane.showMessageDialog(this,s);
        int _state = gameData.state;
        gameData.state = 2;
        AlertDialog.getInstance(this, gameData, model).openDialog();
        gameData.state = _state;
    }
}