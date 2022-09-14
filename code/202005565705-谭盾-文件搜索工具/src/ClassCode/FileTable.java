package ClassCode;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;

public class FileTable extends JTable
{
    public FileTable(TableModel model)
    {
        super(model);

        setBackground(Color.WHITE);//背景颜色
        getTableHeader().setBackground(Color.WHITE);//表头颜色
        
        setShowGrid(false);//不显示表格线条
        getTableHeader().setReorderingAllowed(false);//表头不可拖动
        getTableHeader().setResizingAllowed(false);//列大小不可拖动改变
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//只允许单行选择

        setRowHeight(20);//行高
        // getColumnModel().getColumn(0).setPreferredWidth(100);//列宽
        // getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(120);
        getColumnModel().getColumn(2).setMaxWidth(100);
        getColumnModel().getColumn(3).setMaxWidth(120);
        // setPreferredScrollableViewportSize(new Dimension(350,400));//设置大小
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        // getTableHeader().setDefaultRenderer(new HeaderRenderer());//表头渲染器
        getColumnModel().getColumn(0).setCellRenderer(new NameRenderer());//各行添加渲染器
        getColumnModel().getColumn(1).setCellRenderer(new PathRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new SizeRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new DateRenderer());
    }
}

// class HeaderRenderer extends DefaultTableCellRenderer//表头渲染器
// {
//     public Component getTableCellRendererComponent(JTable table,
//     Object value,boolean isSelected,boolean hasFocus,int row,int column)
//     {
//         setText(value.toString());
//         setHorizontalAlignment(SwingConstants.LEFT);
//         setBorder(BorderFactory.createEtchedBorder());
//         return this;
//     }
// }

class NameRenderer extends DefaultTableCellRenderer//名称列渲染器
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {
        
        setIcon(getIcon((File)value));
        setText(((File)value).getName());
        if(isSelected)
        {
            setBackground(new Color(184,207,229));
        }
        else
        {
            setBackground(Color.WHITE);
        }
        return this;
    }

    private Icon getIcon(File file)//获取系统小图标
    {
		if (file!=null&&file.exists())
        {
			FileSystemView fileView=FileSystemView.getFileSystemView();
			return fileView.getSystemIcon(file);
		}
		return null;
	}
}

class PathRenderer extends DefaultTableCellRenderer//路径列渲染器
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {
        setText((String)value);
        setHorizontalAlignment(SwingConstants.LEFT);
        return this;
    }
}

class SizeRenderer extends DefaultTableCellRenderer//大小列渲染器
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {   
        if(((Long)value).equals(Long.valueOf(-1)))//大小为-1判断为文件夹,不显示大小
        {
            setText("");
        }
        else
        {
            double size=((Long)value).doubleValue()/(double)1024;
            setText(String.valueOf(String.format("%.0f",Math.ceil(size))+" KB"));//文件长度除以1024向上取整
        }

        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}

class DateRenderer extends DefaultTableCellRenderer//修改日期列渲染器
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {
        setText(new SimpleDateFormat("yyyy/MM/dd hh:mm").format((Long)value));//日期格式化显示
        setHorizontalAlignment(SwingConstants.LEFT);
        return this;
    }
}