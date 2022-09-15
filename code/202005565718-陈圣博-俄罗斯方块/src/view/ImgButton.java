package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract  class ImgButton extends JButton {
    
    /**
     * 序列化
     */
    private static final long serialVersionUID = -1137580422525064041L;
    public ImgButton(ImageIcon imgic){
        //背景透明
        setContentAreaFilled(false);
        //更改图片
        setIcon(imgic);
        //去除边框
        setBorder(null);
        //取消截获按键
        setFocusable(false);
        //添加按键检测
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //写点击事件
                onClick();
            }
        });
    }
    public abstract void onClick();
}
