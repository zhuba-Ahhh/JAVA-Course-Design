package ClassCode;
import java.io.File;

import javax.swing.table.DefaultTableModel;

//表头模型重写
public class FileTableModel extends DefaultTableModel
{
    private static final Object[] Names= {"名称","路径","大小","修改日期"};
    
    public FileTableModel()
    {
        setColumnIdentifiers(Names);
    }

    @Override
    public Class<?> getColumnClass(int column)//第二行为Integer类型
    {
        if(column==0)
            return File.class;
        else if(column==1)
            return String.class;
        else if(column==2)
            return Long.class;
        else if(column==3)
            return Long.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row,int column)//不可编辑
    {
        return false;
    }
}
