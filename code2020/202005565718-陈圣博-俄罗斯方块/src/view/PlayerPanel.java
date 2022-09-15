package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import model.GameData;

public class PlayerPanel extends JPanel {
    /**序列化 */
    private static final long serialVersionUID = 1817465285110139219L;
    GameData gameData;
    PlayerPanel(GameData gameData){
        this.gameData = gameData;
        setOpaque(false); //背景透明
        setBounds(13, 435, 204, 134);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("黑体",Font.PLAIN,18));
        g.setColor(new Color(255,251,240));
        int _y =20;
        for(String nick : gameData.playerData.getNicks()){
            g.drawString(nick, 30, _y);
            _y+=20;
        }
        _y =20;
        for(int score : gameData.playerData.getScore()){
            g.drawString(""+score, 120, _y);
            _y+=20;
        }
    }
}
