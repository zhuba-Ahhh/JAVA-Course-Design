package ClassCode;
import java.io.File;

import javax.swing.table.DefaultTableModel;

//��ͷģ����д
public class FileTableModel extends DefaultTableModel
{
    private static final Object[] Names= {"����","·��","��С","�޸�����"};
    
    public FileTableModel()
    {
        setColumnIdentifiers(Names);
    }

    @Override
    public Class<?> getColumnClass(int column)//�ڶ���ΪInteger����
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
    public boolean isCellEditable(int row,int column)//���ɱ༭
    {
        return false;
    }
}
