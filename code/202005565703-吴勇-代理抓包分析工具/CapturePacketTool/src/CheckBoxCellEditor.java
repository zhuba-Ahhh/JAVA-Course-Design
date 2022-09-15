import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.awt.Component;

class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;


    protected JCheckBox checkBox;


    public CheckBoxCellEditor() {
        checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }


    @Override
    public Object getCellEditorValue() {
        return Boolean.valueOf(checkBox.isSelected());
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        checkBox.setSelected(((Boolean) value).booleanValue());

        return checkBox;

    }
}

class CWCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    private static final long serialVersionUID = 1L;


    Border border = new EmptyBorder(1, 2, 1, 2);


    public CWCheckBoxRenderer() {
        super();
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value instanceof Boolean) {
            setSelected(((Boolean) value).booleanValue());

            // setEnabled(table.isCellEditable(row, column));
            setForeground(table.getForeground());
            setBackground(table.getBackground());

        }

        return this;
    }
}