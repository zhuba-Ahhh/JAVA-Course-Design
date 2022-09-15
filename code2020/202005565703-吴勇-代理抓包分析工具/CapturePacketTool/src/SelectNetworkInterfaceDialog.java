import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jpcap.NetworkInterface;

public class SelectNetworkInterfaceDialog extends JDialog{
    /**
     *
     */
    private static final long serialVersionUID = 1606081152119700152L;
    DefaultTableModel tableModel;
    JScrollPane scrollArea;
    CTable table;
    String[] tableCol = {"接口", "类型", "混杂", "最大捕获长度(字节)", "最大捕获包数"};
    Object[][] tableRows;
    JCheckBox[] checkBoxs = new JCheckBox[4];
    JButton buttonOK = new JButton("确定");
    JButton buttonEXIT = new JButton("取消");
    JLabel desLabel2 = new JLabel("请选择要捕获的包类型(注:选择混杂模式会使下列选择失效):");
    String[] filterStringValues = {" or arp", " or icmp", " or tcp", " or udp"};
    StringBuffer filterString;
    boolean isMixModes;
    int snaplens;
    int maxCapPacketsCnt;
    int selectedInterfacesIndex;
    boolean cap = false;    //按确定则cap为true，否则为false

    // public int[] GetSelectedInterfaces(){
    //     return selectedInterfaces;
    // }
    public boolean GetCapState() {
        return cap;
    }

    public String GetfilterString() {
        return filterString.toString();
    }

    public int Getsnaplens() {
        return snaplens;
    }

    public int GetmaxCapPacketsCnt() {
        return maxCapPacketsCnt;
    }

    public int GetselectedInterfacesIndex() {
        return selectedInterfacesIndex;
    }

    public boolean GetisMixMode() {
        return isMixModes;
    }

    public SelectNetworkInterfaceDialog(JFrame frame, jpcap.NetworkInterface[] networkInterfacesList) {
        super(frame, "选择网络接口及相关配置", true);
        // JFrame mainWin = new JFrame("选择网络接口及相关配置");
        setSize(630, 400);
        setLayout(null);
        // mainWin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // 表格实现
        tableModel = new DefaultTableModel(tableRows, tableCol);
        table = new CTable(tableModel);
        scrollArea = new JScrollPane(table);
        scrollArea.setBounds(10, 10, 600, 250);
        scrollArea.setViewportView(table);
        add(scrollArea);
        table.getColumnModel().getColumn(2).setCellEditor(new CheckBoxCellEditor());
        table.getColumnModel().getColumn(2).setCellRenderer(new CWCheckBoxRenderer());
        for(int i = 0; i < networkInterfacesList.length; i++)
        {
            NetworkInterface ni = networkInterfacesList[i];
            Object[] tmp = {ni.description, ni.datalink_name, new Boolean(false), 2048 + "", 200 + ""};
            tableModel.addRow(tmp);
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setRowHeight(30);
        // table.setColumnSelectionAllowed(false);
        
        // 提示标签
        desLabel2.setBounds(10, 270, 500, 40);
        add(desLabel2);

        // 复选框实现
        checkBoxs[0] = new JCheckBox("ARP", true);
        checkBoxs[1] = new JCheckBox("ICMP", true);
        checkBoxs[2] = new JCheckBox("TCP", true);
        checkBoxs[3] = new JCheckBox("UDP", true);


        int wid = 10;
        for(int i = 0; i < 4; i++)
        {
            checkBoxs[i].setBounds(wid, 300, 80, 30);
            wid += 80;
            add(checkBoxs[i]);
        }

        buttonOK.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedInterfacesIndex = table.getSelectedRow();
                if(selectedInterfacesIndex != -1) {
                    cap = true;
                    isMixModes = (boolean) tableModel.getValueAt(selectedInterfacesIndex, 2);
                    snaplens = Integer.parseInt((String) tableModel.getValueAt(selectedInterfacesIndex, 3));
                    maxCapPacketsCnt = Integer.parseInt((String) tableModel.getValueAt(selectedInterfacesIndex, 4)) ;
                    filterString = new StringBuffer("");
                    filterString.setLength(0);
                    for(int i = 0; i < 4; i++)
                    {
                        if(checkBoxs[i].isSelected())
                            filterString.append(filterStringValues[i]);
                    }
                    if(filterString.length() > 0) {
                        // filterString = filterString.substring(3, filterString.length());
                        filterString.delete(0, 4);
                    }
                    else {
                        filterString.setLength(0);
                    }
                    dispose();
                }
            }
            
        });
        buttonEXIT.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                cap = false;
                dispose();
            }
            
        });
        buttonOK.setBounds(450, 330, 70, 25);
        buttonEXIT.setBounds(530, 330, 70, 25);
        add(buttonOK);
        add(buttonEXIT);
        setVisible(true);
    }
}