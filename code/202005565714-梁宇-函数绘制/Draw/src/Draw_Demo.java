package src;

import dd.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public class Draw_Demo extends JFrame {

    DrawFunction x = new DrawFunction();
    JDialog some = new JDialog(this,"注意事项",true);

    public Draw_Demo() {
        super();
        setLayout(null);
        setTitle("Function Drawing Tool");
        setSize(800, 640);

        getContentPane().add(x);
        x.setLocation(0, 0);
        x.tmp.some.addActionListener(new Myaction());

        // addWindowListener(new Mywindow());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class Myaction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JLabel talk = new JLabel("请不要随意省略乘号，以保证最终绘制的函数尽量正确\n",JLabel.CENTER);
            JLabel talk1 = new JLabel("该软件只对运算符类进行报错，若出现\\0等表达式,默认函数定义域为空集\n",JLabel.CENTER);
            JLabel talk2 = new JLabel("默认函数算符优先级高于其他运算符优先级,所以请尽量保证不省略括号\n",JLabel.CENTER);
            some.add(talk,BorderLayout.CENTER);
            some.add(talk1,BorderLayout.NORTH);
            some.add(talk2,BorderLayout.SOUTH);
            // talk.setBounds(100, 50,200,150);
            some.setSize(400,300);
            some.setLocationRelativeTo(null);
            some.setVisible(true);
        }

    }

    // class Mywindow implements WindowListener {
    //     public void windowOpened(WindowEvent e) {}
    //     public void windowClosing(WindowEvent e) {}
    //     public void windowClosed(WindowEvent e) {}
    //     public void windowIconified(WindowEvent e) {}
    //     public void windowActivated(WindowEvent e) {}
    //     public void windowDeactivated(WindowEvent e) {}
    //     public void windowDeiconified(WindowEvent e) {
    //         for(int i=1;i<=x.tmp.num;++i)
    //             {
    //                 x.draw(x.tmp.textField[i].getText(), i);
    //             }
    //     }
    // }

    public static void main(String[] args) {
        new Draw_Demo();
    }
}