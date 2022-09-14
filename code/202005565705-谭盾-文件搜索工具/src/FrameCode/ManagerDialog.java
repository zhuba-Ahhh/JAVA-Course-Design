package FrameCode;
import java.awt.*;
import javax.swing.*;
import info.monitorenter.util.Entry;
import ToolCode.GBC;
import ClassCode.Settings;

//����ɸѡ���Ի���
public class ManagerDialog extends JDialog
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public ManagerDialog(JFrame owner,String title,Settings settings)
    {

        super(owner,title,true);
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        setLocationRelativeTo(owner);//���ڳ���λ���ڸ�����֮��
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        DefaultListModel<String> filterModel=new DefaultListModel<>();
        JList<String> filterList=new JList<>(filterModel);
        JScrollPane filterPane=new JScrollPane(filterList);
        filterPane.setPreferredSize(new Dimension(200,250));
        filterModel.addAll(settings.getFilterName());//�б�����Ŀ��ʼ��
        filterList.setSelectedIndex(0);//ѡ���һ��
        settings.setuseFilter(filterList.getSelectedValue());//ɸѡ������Ϊ�б��һ��

        JButton useButton=new JButton("ʹ��");
        useButton.setPreferredSize(new Dimension(60,20));
        useButton.addActionListener(event->
        {
            settings.setuseFilter(filterList.getSelectedValue());
            setVisible(false);
        });

        JButton buildButton=new JButton("�½�");
        buildButton.setPreferredSize(new Dimension(60,20));
        buildButton.addActionListener(event->
        {
            AddDialog addDialog=new AddDialog(this,"�½�ɸѡ��");
            addDialog.setVisible(true);
            Entry<String,String> val=addDialog.getText();
            if(val!=null)
            {
                settings.addFilter(val.getKey(),val.getValue());
                filterModel.removeAllElements();//�����б�Ԫ��
                filterModel.addAll(settings.getFilterName());
            }
        });

        JButton editButton=new JButton("�༭");
        editButton.setPreferredSize(new Dimension(60,20));
        editButton.addActionListener(event->
        {
            AddDialog addDialog=new AddDialog(this,"�༭ɸѡ��");
            String name=filterList.getSelectedValue();
            addDialog.setText(name,settings.getFilterValue(name));
            addDialog.setVisible(true);
            Entry<String,String> val=addDialog.getText();
            if(val!=null)
            {
                settings.addFilter(val.getKey(),val.getValue());
                filterModel.removeAllElements();
                filterModel.addAll(settings.getFilterName());
            }
        });

        JButton delButton=new JButton("ɾ��");
        delButton.setPreferredSize(new Dimension(60,20));
        delButton.addActionListener(event->
        {
            settings.delFilter(filterList.getSelectedValue());
            filterModel.removeAllElements();
            filterModel.addAll(settings.getFilterName());
        });
        

        add(filterPane,new GBC(0,0,1,5).setFill(GBC.BOTH));
        add(useButton,new GBC(1,0).setInsets(0,5,5,0));
        add(buildButton,new GBC(1,1).setInsets(5,5,5,0));
        add(editButton,new GBC(1,2).setInsets(5,5,5,0));
        add(delButton,new GBC(1,3).setInsets(5,5,0,0));
    }
}
