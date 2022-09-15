package FrameCode;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import info.monitorenter.util.Entry;
import ToolCode.GBC;

//增加筛选器对话框
public class AddDialog extends JDialog
{
    private static final int WIDTH = 350;
    private static final int HEIGHT = 150;

    private JTextField nameField;
    private JTextField searchField;
    private JButton deterButton;
    private JButton cancelButton;
    private Entry<String,String> res;

    public AddDialog(JDialog owner,String title)
    {
        super(owner,title,true);//true表示阻塞其他窗口
        setResizable(false);//不可缩放
        setSize(WIDTH,HEIGHT);//设置大小
        setLocationRelativeTo(owner);//窗口出现位置在父窗口之上
        setLayout(new GridBagLayout());//网格组排序
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//设置关闭动作

        nameField=new JTextField("",25);
        searchField=new JTextField("",25);

        deterButton=new JButton("确定");
        deterButton.setPreferredSize(new Dimension(60,20));
        deterButton.setEnabled(false);
        deterButton.addActionListener(event->
        {
            res=new Entry<>(nameField.getText(),searchField.getText());//确定则初始化键值
            dispose();
        });

        nameField.addKeyListener(new KeyAdapter()//文本框增加监听:不为空则使确定键可用
        {
            public void keyReleased(KeyEvent e)
            {
                if(nameField.getText().isEmpty())
                    deterButton.setEnabled(false);
                else
                    deterButton.setEnabled(true);
            }
        });

        cancelButton=new JButton("取消");
        cancelButton.setPreferredSize(new Dimension(60,20));
        cancelButton.addActionListener(event->
        {
            dispose();
        });

        add(new JLabel("名称:"),new GBC(0,0));
        add(nameField,new GBC(1,0,3,1).setInsets(0,5,5,0));
        add(new JLabel("搜索:"),new GBC(0,1));
        add(searchField,new GBC(1,1,3,1).setInsets(5,5,5,0));
        add(new JLabel("                                                   "),new GBC(0,2,2,1));
        add(deterButton,new GBC(2,2).setInsets(5,0,0,5).setAnchor(GBC.EAST));
        add(cancelButton,new GBC(3,2).setInsets(5,0,0,0).setAnchor(GBC.EAST));
    }

    public void setText(String key,String value)
    {
        nameField.setText(key);
        searchField.setText(value);
        deterButton.setEnabled(true);
    }

    public Entry<String,String> getText()
    {
        return res;
    }
}
