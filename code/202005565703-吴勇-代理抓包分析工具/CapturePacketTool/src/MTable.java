import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;

public class MTable extends JTable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String[] gPacketsTypeList;
    public MTable(String[][] rowData, String[] columnNames){
        super(rowData, columnNames);
    }
    public MTable(DefaultTableModel dt){
        super(dt);
    }
    public void SetPacketsTypeList(String[] packetTypeList) {
        gPacketsTypeList = packetTypeList;
    }
    @Override
    public JTableHeader getTableHeader() {
        JTableHeader tableHeader = super.getTableHeader();
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer)tableHeader.getDefaultRenderer();
        hr.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        hr.setBackground(new Color(255, 255, 255, 255));
        return tableHeader;
    }

    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        DefaultTableCellRenderer cr = (DefaultTableCellRenderer)super.getDefaultRenderer(columnClass);
        // cr.setBackground(Color.BLUE);
        return cr;
    }
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        return super.prepareRenderer(renderer, row, column);
    }
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        DefaultTableCellRenderer cr = (DefaultTableCellRenderer)super.getCellRenderer(row, column);
        cr.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        // 根据协议类型绘制行的颜色
            switch(gPacketsTypeList[row])
            {
                case "TCP":
                cr.setBackground(new Color(231,230,255,255));
                //231,230,255,255
                break;
                case "UDP":
                cr.setBackground(new Color(218,238,255,255));
                //218,238,255,255
                break;
                case "ICMP":
                cr.setBackground(new Color(252,224,255,255));
                //252,224,255,255
                break;
                case "ARP":
                cr.setBackground(new Color(250,240,215,255));
                //250,240,215,255
                break;
                default:
                cr.setBackground(Color.WHITE);
                break;
            }
        return cr;
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}