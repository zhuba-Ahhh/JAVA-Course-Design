import java.awt.Component;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MyListCellRenderer extends DefaultListCellRenderer {
    public MyListCellRenderer(){
        super();
    }
    @Override
    public Component getListCellRendererComponent(
        JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        File f = (File)value;
        Pattern pattern = Pattern.compile("([^<>/\\\\|:\"\"\\*\\?\\.]+)");
        Matcher m = pattern.matcher(f.getName());
        m.find();
        String fName = m.group(0);
        setText(fName);
        return this;
    }
}