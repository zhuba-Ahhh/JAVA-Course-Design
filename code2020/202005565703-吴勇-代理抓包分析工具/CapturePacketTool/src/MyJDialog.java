import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// import javax.swing.BorderLayout;
import java.awt.BorderLayout;

public class MyJDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private boolean state = false;
    public boolean GetState() {
        return state;
    }
    JButton okBtn = new JButton("确定");
    JButton exitBtn = new JButton("取消");
    public MyJDialog(JFrame frame, String content, String title) {
        super(frame, title, true);
        setLayout(new BorderLayout());
        setBounds(120, 120, 250, 250);
        JLabel label = new JLabel(content);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        okBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                state = true;
                dispose();
            }
            
        });
        exitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                state = false;
                dispose();
            }
            
        });
        add(label, BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(okBtn);
        panel.add(exitBtn);
        add(panel, BorderLayout.SOUTH);
        setVisible(true);
    }
}