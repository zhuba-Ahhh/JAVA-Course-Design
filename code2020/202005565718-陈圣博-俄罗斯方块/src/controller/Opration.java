package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.GameData;
import view.AlertDialog;
import view.ImgButton;
import view.MainWin;

public class Opration {
    MainWin mainWin;
    GameData gameData;
    public OpButton left; //设置按钮
    public OpButton righ;
    public OpButton down;
    public OpButton rota;
    public JButton stst;
    public ImgButton sett;
    public ImgButton logi;

    abstract class OpButton extends ImgButton{
        private static final long serialVersionUID = 8887869599341266432L;
        OpButton(ImageIcon imgic){
            super(imgic);
        }
        @Override
        public void onClick(){
            if(gameData.state == 1){
                doClick();
            }
        }
        abstract public void doClick();
    }

    Opration(){
        left = new OpButton(new ImageIcon("img/left.png")){

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void doClick() {
                gameData.move(true, -1);
                mainWin.getGamePanel().repaint();
            }

        };
        righ = new OpButton(new ImageIcon("img/righ.png")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void doClick() {
                gameData.move(true, 1);
                mainWin.getGamePanel().repaint();
            }
        };
        down = new OpButton(new ImageIcon("img/down.png")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void doClick() {
                if(gameData.move(false, 1)){
                    mainWin.getScoreNext().repaint();
                };
                mainWin.getGamePanel().repaint();
            }
        };
        rota = new OpButton(new ImageIcon("img/rota.png")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void doClick() {
                gameData.rote();
                mainWin.getGamePanel().repaint();
            }
        };
        stst = new JButton("开始");
        stst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //写点击事件
                if(gameData.state == 1){
                    gameData.state = 2;
                }else{
                    gameData.state = 1;
                }
                stst.setText(gameData.stst_text[gameData.state]);
            }
        });

        sett = new ImgButton(new ImageIcon("img/sett.png")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                mainWin.alert(AlertDialog.SETT);
            }
        };
        logi = new ImgButton(new ImageIcon("img/logi.png")){
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                mainWin.alert(AlertDialog.LOGI);
            }
        };
    }

    /**
     * 关联视图
     * @param mainwin
     */
    public void setWin(MainWin mainwin) {
        this.mainWin = mainwin;
        this.mainWin.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    down.onClick();
                }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    left.onClick();
                }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    righ.onClick();
                }else if(e.getKeyCode()==KeyEvent.VK_UP){
                    rota.onClick();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            
        });
    }
    /**
     * 关联model
     * @param gameData
     */
    public void setData(GameData gameData) {
        this.gameData = gameData;
    }
    
}
