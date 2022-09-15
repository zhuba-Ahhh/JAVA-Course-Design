package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.GameData;

public class ScoreNext extends JPanel {
    GameData gameData;
    int[] offset = new int[]{0,-10,0,0,-10,0,0};
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1860979716621182121L;

    ScoreNext(GameData gameData){
        this.gameData = gameData;
        setOpaque(false);
        setBounds(233,30,90,215); //游戏画布
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("黑体",Font.PLAIN,23));
        g.drawString(gameData.getScore(), 10, 60);
        for (Point point : gameData.BLOCKS[gameData.next].points) {
            g.setColor(gameData.colors[gameData.next]);
            g.fillRect((point.x) * 20 + 35 + offset[gameData.next], (point.y) * 20 + 150, 20, 20);
            g.drawImage(new ImageIcon("./img/mask2.png").getImage(), (point.x) * 20 + 35 + offset[gameData.next],
                    (point.y) * 20 + 150, 20, 20, null);
        }
    }
}
