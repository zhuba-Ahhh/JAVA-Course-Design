package FrameCode;
import java.awt.*;
import javax.swing.*;
import info.monitorenter.util.Entry;
import ToolCode.GBC;
import ClassCode.Settings;

//管理筛选器对话框
public class ManagerDialog extends JDialog
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public ManagerDialog(JFrame owner,String title,Settings settings)
    {

        super(owner,title,true);
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        setLocationRelativeTo(owner);//窗口出现位置在父窗口之上
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        DefaultListModel<String> filterModel=new DefaultListModel<>();
        JList<String> filterList=new JList<>(filterModel);
        JScrollPane filterPane=new JScrollPane(filterList);
        filterPane.setPreferredSize(new Dimension(200,250));
        filterModel.addAll(settings.getFilterName());//列表内项目初始化
        filterList.setSelectedIndex(0);//选择第一项
        settings.setuseFilter(filterList.getSelectedValue());//筛选器设置为列表第一个

        JButton useButton=new JButton("使用");
        useButton.setPreferredSize(new Dimension(60,20));
        useButton.addActionListener(event->
        {
            settings.setuseFilter(filterList.getSelectedValue());
            setVisible(false);
        });

        JButton buildButton=new JButton("新建");
        buildButton.setPreferredSize(new Dimension(60,20));
        buildButton.addActionListener(event->
        {
            AddDialog addDialog=new AddDialog(this,"新建筛选器");
            addDialog.setVisible(true);
            Entry<String,String> val=addDialog.getText();
            if(val!=null)
            {
                settings.addFilter(val.getKey(),val.getValue());
                filterModel.removeAllElements();//更新列表元素
                filterModel.addAll(settings.getFilterName());
            }
        });

        JButton editButton=new JButton("编辑");
        editButton.setPreferredSize(new Dimension(60,20));
        editButton.addActionListener(event->
        {
            AddDialog addDialog=new AddDialog(this,"编辑筛选器");
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

        JButton delButton=new JButton("删除");
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
