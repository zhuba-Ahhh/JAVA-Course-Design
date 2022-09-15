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

        setBackground(Color.WHITE);//������ɫ
        getTableHeader().setBackground(Color.WHITE);//��ͷ��ɫ
        
        setShowGrid(false);//����ʾ�������
        getTableHeader().setReorderingAllowed(false);//��ͷ�����϶�
        getTableHeader().setResizingAllowed(false);//�д�С�����϶��ı�
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//ֻ������ѡ��

        setRowHeight(20);//�и�
        // getColumnModel().getColumn(0).setPreferredWidth(100);//�п�
        // getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(120);
        getColumnModel().getColumn(2).setMaxWidth(100);
        getColumnModel().getColumn(3).setMaxWidth(120);
        // setPreferredScrollableViewportSize(new Dimension(350,400));//���ô�С
        setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        // getTableHeader().setDefaultRenderer(new HeaderRenderer());//��ͷ��Ⱦ��
        getColumnModel().getColumn(0).setCellRenderer(new NameRenderer());//���������Ⱦ��
        getColumnModel().getColumn(1).setCellRenderer(new PathRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new SizeRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new DateRenderer());
    }
}

// class HeaderRenderer extends DefaultTableCellRenderer//��ͷ��Ⱦ��
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

class NameRenderer extends DefaultTableCellRenderer//��������Ⱦ��
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

    private Icon getIcon(File file)//��ȡϵͳСͼ��
    {
		if (file!=null&&file.exists())
        {
			FileSystemView fileView=FileSystemView.getFileSystemView();
			return fileView.getSystemIcon(file);
		}
		return null;
	}
}

class PathRenderer extends DefaultTableCellRenderer//·������Ⱦ��
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {
        setText((String)value);
        setHorizontalAlignment(SwingConstants.LEFT);
        return this;
    }
}

class SizeRenderer extends DefaultTableCellRenderer//��С����Ⱦ��
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {   
        if(((Long)value).equals(Long.valueOf(-1)))//��СΪ-1�ж�Ϊ�ļ���,����ʾ��С
        {
            setText("");
        }
        else
        {
            double size=((Long)value).doubleValue()/(double)1024;
            setText(String.valueOf(String.format("%.0f",Math.ceil(size))+" KB"));//�ļ����ȳ���1024����ȡ��
        }

        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}

class DateRenderer extends DefaultTableCellRenderer//�޸���������Ⱦ��
{
    public Component getTableCellRendererComponent(JTable table,
    Object value,boolean isSelected,boolean hasFocus,int row,int column)
    {
        setText(new SimpleDateFormat("yyyy/MM/dd hh:mm").format((Long)value));//���ڸ�ʽ����ʾ
        setHorizontalAlignment(SwingConstants.LEFT);
        return this;
    }
}