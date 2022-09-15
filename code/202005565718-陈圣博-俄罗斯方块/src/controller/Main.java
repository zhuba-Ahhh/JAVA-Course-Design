package controller;
import model.GameData;
import view.AlertDialog;
/**
 * 层与层之间的关系
 */
import view.MainWin;

public class Main {
    public static void main(String[] args) {
        //实例化操作
        Opration opration = new Opration();
        //加载数据
        GameData gameData = new GameData();
        //将数据和窗口相关联
        MainWin mainwin = new MainWin(opration,gameData);
        //将窗口和操作区关联
        opration.setWin(mainwin);
        //将数据和操作区关联
        opration.setData(gameData);
        //启用线程
        new AutoDown(gameData,mainwin).start();
        mainwin.setVisible(true);
    }
}

class AutoDown extends Thread {
    private GameData gameData;
    private MainWin mainWin;

    AutoDown(GameData gameData, MainWin mainWin) {
        this.gameData = gameData;
        this.mainWin = mainWin;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (gameData.state == 1) {
                    if (gameData.move(false, 1)) {
                        mainWin.getScoreNext().repaint();
                    }
                    ;
                    mainWin.getGamePanel().repaint();
                    sleep(1000);
                } else if(gameData.state == 3){
                    gameData.init();
                    //System.out.println("游戏结束");
                    mainWin.alert(AlertDialog.OVER);
                    mainWin.getStst().setText(gameData.stst_text[gameData.state]);
                    gameData.state = 4;
                }
                else {
                    sleep(150);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}