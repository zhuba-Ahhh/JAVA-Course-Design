package FrameCode;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import info.monitorenter.util.Entry;
import ToolCode.GBC;

//����ɸѡ���Ի���
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
        super(owner,title,true);//true��ʾ������������
        setResizable(false);//��������
        setSize(WIDTH,HEIGHT);//���ô�С
        setLocationRelativeTo(owner);//���ڳ���λ���ڸ�����֮��
        setLayout(new GridBagLayout());//����������
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//���ùرն���

        nameField=new JTextField("",25);
        searchField=new JTextField("",25);

        deterButton=new JButton("ȷ��");
        deterButton.setPreferredSize(new Dimension(60,20));
        deterButton.setEnabled(false);
        deterButton.addActionListener(event->
        {
            res=new Entry<>(nameField.getText(),searchField.getText());//ȷ�����ʼ����ֵ
            dispose();
        });

        nameField.addKeyListener(new KeyAdapter()//�ı������Ӽ���:��Ϊ����ʹȷ��������
        {
            public void keyReleased(KeyEvent e)
            {
                if(nameField.getText().isEmpty())
                    deterButton.setEnabled(false);
                else
                    deterButton.setEnabled(true);
            }
        });

        cancelButton=new JButton("ȡ��");
        cancelButton.setPreferredSize(new Dimension(60,20));
        cancelButton.addActionListener(event->
        {
            dispose();
        });

        add(new JLabel("����:"),new GBC(0,0));
        add(nameField,new GBC(1,0,3,1).setInsets(0,5,5,0));
        add(new JLabel("����:"),new GBC(0,1));
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
