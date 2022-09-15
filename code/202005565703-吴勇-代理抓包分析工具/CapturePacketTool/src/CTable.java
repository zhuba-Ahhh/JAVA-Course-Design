import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CTable extends JTable {

    /**
     *
     */
    Pattern pattern = Pattern.compile("[\\d]*$");  
    private static final long serialVersionUID = 1L;
    public CTable(){
        super();
    }
    public CTable(DefaultTableModel dtm){
        super(dtm);
        // System.out.println(this.getRowCount());
    }
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if(aValue instanceof String){
            String s = (String) aValue;
            if(pattern.matcher(s).matches()){
                super.setValueAt(aValue, row, column);
            }
        }
        else{
            super.setValueAt(aValue, row, column);
        }
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 2 || column == 3 || column == 4)
            return true;
        return false;
    }
}