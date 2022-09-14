package FrameCode;
import java.io.*;
import java.net.*;
import java.util.Comparator;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import ClassCode.*;
import ToolCode.*;

//用户主界面对话框
public class UserFrame extends JFrame
{
    private static final int WIDTH=600;
    private static final int HEIGHT=600;

    private static String searchName;
    private static Thread workThread;

    public UserFrame()
    {
        setSize(WIDTH,HEIGHT);//设置框架大小
        getContentPane().setBackground(Color.WHITE);//背景颜色
        setLayout(new GridBagLayout());//网格组布局

        
        Settings settings=new Settings(new File("Filter.properties"));
        ManagerDialog managerDialog=new ManagerDialog(this,"管理筛选器",settings);
        JTextField searchField=new JTextField("");


        FileTableModel fileModel=new FileTableModel();
        FileTable fileTable=new FileTable(fileModel);
        JScrollPane filePane=new JScrollPane(fileTable);
        filePane.getViewport().setBackground(Color.WHITE);//滚动界面背景颜色


        TableRowSorter<FileTableModel> sorter=new TableRowSorter<>(fileModel);
        fileTable.setRowSorter(sorter);//添加默认排序器
        sorter.setComparator(0,new Comparator<File>()//为第一列重载排序方式
        {
            public int compare(File a,File b)
            {
                if(a.isDirectory()&&b.isDirectory())
                    return a.getName().compareTo(b.getName());
                else if(a.isDirectory()&&b.isFile())
                    return -1;
                else if(a.isFile()&&b.isDirectory())
                    return 1;
                else if(a.isFile()&&b.isFile())
                    return a.getName().compareTo(b.getName());
                return 0;
            }
        });


        JMenuBar menuBar=new JMenuBar();//顶部菜单
        menuBar.setBackground(Color.WHITE);
        JMenu searchMenu=new JMenu("设置");
        menuBar.add(searchMenu);
        JMenu helpMenu=new JMenu("帮助");
        menuBar.add(helpMenu);

        JMenuItem fItem1=new JMenuItem("设置搜索目录");
        searchMenu.add(fItem1);
        searchMenu.addSeparator();
        JCheckBoxMenuItem fItem2=new JCheckBoxMenuItem("区分大小写");
        searchMenu.add(fItem2);
        JCheckBoxMenuItem fItem3=new JCheckBoxMenuItem("全字匹配");
        searchMenu.add(fItem3);
        JCheckBoxMenuItem fItem4=new JCheckBoxMenuItem("仅显示文件夹");
        searchMenu.add(fItem4);
        searchMenu.addSeparator();
        JCheckBoxMenuItem fItem5=new JCheckBoxMenuItem("使用正则表达式");
        searchMenu.add(fItem5);
        searchMenu.addSeparator();
        JCheckBoxMenuItem fItem6=new JCheckBoxMenuItem("使用筛选器");
        searchMenu.add(fItem6);
        JMenuItem fItem7=new JMenuItem("管理筛选器");
        searchMenu.add(fItem7);
        JMenuItem fItem8=new JMenuItem("作者博客");
        helpMenu.add(fItem8);
        JMenuItem fItem9=new JMenuItem("关于");
        helpMenu.add(fItem9);

        fItem1.addActionListener(event->
        {
            JFileChooser chooser=new JFileChooser();
            chooser.setCurrentDirectory(new File(settings.getsearchPath()));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result=chooser.showDialog(UserFrame.this,"选择目录");
            if(result==JFileChooser.APPROVE_OPTION)
                settings.setsearchPath(chooser.getSelectedFile().getPath());
        });     
        fItem2.addActionListener(event->
        {
            if(fItem2.isSelected())
            {
                settings.setisCaps(true);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
            else
            {
                settings.setisCaps(false);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
        });       
        fItem3.addActionListener(event->
        {
            if(fItem3.isSelected())
            {
                settings.setisMatch(true);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
            else
            {
                settings.setisMatch(false);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
        });
        fItem4.addActionListener(event->
        {
            if(fItem4.isSelected())//仅显示文件夹的过滤器
            {
                fItem6.setEnabled(false);
                settings.setisOnlyDir(true);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
            else if(!fItem4.isSelected()&&!fItem5.isSelected())
            {
                fItem6.setEnabled(true);
                settings.setisOnlyDir(false);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
        });
        fItem5.addActionListener(event->
        {
            if(fItem5.isSelected())
            {
                fItem2.setEnabled(false);
                fItem3.setEnabled(false);
                fItem6.setEnabled(false);
                settings.setisRegEx(true);
            }
            else
            {
                fItem2.setEnabled(true);
                fItem3.setEnabled(true);
                settings.setisRegEx(false);
                if(!fItem4.isSelected())
                    fItem6.setEnabled(true);
            }
        });       
        fItem6.addActionListener(event->
        {
            if(fItem6.isSelected())
            {
                fItem4.setEnabled(false);
                fItem5.setEnabled(false);
                settings.setisFilter(true);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
            else
            {
                fItem4.setEnabled(true);
                fItem5.setEnabled(true);
                settings.setisFilter(false);
                sorter.setRowFilter(new FileTableFilter<FileTableModel,Integer>(searchName,settings));
            }
        });
        fItem7.addActionListener(event->
        {
            managerDialog.setVisible(true);
        });
        fItem8.addActionListener(event->
        {
            try
            {
                Desktop.getDesktop().browse(new URI(settings.getWeb()));
            } catch (IOException | URISyntaxException e)
            {
                e.printStackTrace();
            }
        });
        fItem9.addActionListener(event->
        {
            JOptionPane.showInternalMessageDialog(null,"作者:202005565705 谭盾\n"+"版本:V1.0测试版本\n"+"说明:XTU Java课程设计",
            "关于",JOptionPane.INFORMATION_MESSAGE);
        });


        JPopupMenu popup=new JPopupMenu();//右键弹出菜单
        JMenuItem pItem1=new JMenuItem("打开");
        popup.add(pItem1);
        JMenuItem pItem2=new JMenuItem("打开位置");
        popup.add(pItem2);
        JMenuItem pItem3=new JMenuItem("复制路径");
        popup.add(pItem3);
        JMenuItem pItem4=new JMenuItem("识别内码");
        popup.add(pItem4);

        pItem1.addActionListener(event->
        {
            int row=fileTable.getSelectedRow();
            String path=(String)fileTable.getValueAt(row,1);
            try
            {
                Desktop.getDesktop().open(new File(path));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        pItem2.addActionListener(event->
        {
            try
            {
                Runtime.getRuntime().exec("explorer.exe /select,"+(String)fileTable.getValueAt(fileTable.getSelectedRow(),1));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        pItem3.addActionListener(event->
        {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = new StringSelection((String)fileTable.getValueAt(fileTable.getSelectedRow(),1));
            clipboard.setContents(trans, null);
        });
        pItem4.addActionListener(event->
        {
            int row=fileTable.getSelectedRow();
            CodeThread thread=new CodeThread((File)fileTable.getValueAt(row,0));
            thread.start();
            try
            {
                thread.join();//阻塞当前线程直到子线程得到值
                JOptionPane.showInternalMessageDialog(null,"此文件内码为:\n"+thread.getCode(),"提示",JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });


        fileTable.addMouseListener(new MouseAdapter()//表格鼠标双击和右键单击事件监听
        {
            public void mouseClicked(MouseEvent event) 
            {
                if(event.getButton()==MouseEvent.BUTTON1&&event.getClickCount()==2)
                {
                    int row=fileTable.getSelectedRow();
                    String path=(String)fileTable.getValueAt(row,1);
                    try {
                        Desktop.getDesktop().open(new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(event.getButton()==MouseEvent.BUTTON3&&event.getClickCount()==1)
                {
                    int r=fileTable.rowAtPoint(event.getPoint());
                    fileTable.setRowSelectionInterval(r,r);
                    if(((File)fileTable.getValueAt(fileTable.getSelectedRow(),0)).isDirectory())//文件夹禁用识别内码
                        pItem4.setEnabled(false);
                    else
                        pItem4.setEnabled(true);
                    popup.show(event.getComponent(),event.getX(),event.getY());
                }
            }
        });


        JButton interruptButton=new JButton(new ImageIcon("icon\\guanbi.png"));//停止搜索
        interruptButton.setBackground(Color.WHITE);
        interruptButton.setMargin(new Insets(0,0,0,0));
        interruptButton.setPreferredSize(new Dimension(19,19));
        interruptButton.addActionListener(event->
        {
            if(workThread!=null)
                workThread.stop();
            System.out.println(workThread.isAlive());
        });

        JButton searchButton=new JButton(new ImageIcon("icon\\sousuo.png"));//开始搜索
        searchButton.setBackground(Color.WHITE);
        searchButton.setMargin(new Insets(0,0,0,0));
        searchButton.setPreferredSize(new Dimension(38,19));
        searchButton.addActionListener(event->
        {
            if(workThread!=null)
                workThread.stop();
            fileModel.setRowCount(0);
            searchName=searchField.getText();
            String express;
            if(settings.getisRegEx())
                express=searchName;
            else
                express=".*(?i)"+searchName+".*";
            SearchThread newSearch=new SearchThread(new File(settings.getsearchPath()),express,fileModel);
            workThread=new Thread(newSearch);
            workThread.start();
        });

        setJMenuBar(menuBar);//加入框架和布局
        add(searchField,new GBC(0,0).setFill(GBC.HORIZONTAL).setWeight(100,0));
        add(interruptButton,new GBC(1,0).setFill(GBC.NONE).setWeight(0,0));
        add(searchButton,new GBC(2,0).setFill(GBC.NONE).setWeight(0,0));
        add(filePane,new GBC(0,1,3,1).setFill(GBC.BOTH).setWeight(100,100));

        try//设置为当前系统观感
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}