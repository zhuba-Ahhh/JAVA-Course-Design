package view;

import java.awt.Point;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.GameData;

public class GamePanel extends JPanel {
    GameData gameData;
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1860979716621182121L;

    GamePanel(GameData gameData){
        this.gameData = gameData;
        setOpaque(false);
        setBounds(15,30,200,360); //游戏画布
    }

    @Override
    public void paintComponent(Graphics g){
        for(Point point: gameData.blocks.points){
            g.setColor(gameData.colors[gameData.current]);
            g.fillRect((point.x+gameData.x)*20, (point.y+gameData.y)*20, 20, 20);
            g.drawImage(new ImageIcon("./img/mask2.png").getImage(),(point.x+gameData.x)*20, (point.y+gameData.y)*20, 20, 20,null);
        }
        for(int i=19;i>=2;i--){
            for(int j=0;j<10;j++){
                if(gameData.existBlocks[j][i]!=0){
                    g.setColor(gameData.colors[gameData.existBlocks[j][i]-1]);
                    g.fillRect((j)*20, (i-2)*20, 20, 20);
                    g.drawImage(new ImageIcon("./img/mask2.png").getImage(),(j)*20, (i-2)*20, 20, 20,null);
                }
            }
        }
    }
}
