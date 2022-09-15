package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import view.MainWin;

public class PlayerData {
    MainWin mainWin;
    Connection conn;
    Statement stmt;
    List<String> nickList;
    List<Integer> scoreList;
    String currentNick = "未知玩家";
    String currentPass = "";
    public PlayerData() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:data/player.db");
            stmt = conn.createStatement();
            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        createTable();
        getInfo();
    }
    void createTable(){
        String sql = "create table players(id INTEGER PRIMARY KEY AUTOINCREMENT, nick CHAR(20) NOT NULL,"+
        " pass CHAR(20) NOT NULL, score INT(5) NOT NULL)";
        try {
            stmt.execute(sql);
            System.out.println("数据表创建成功");
        } catch (SQLException e) {
            System.out.println("数据库已经存在");
        }
    }
    public void add(int score){
        String sql = "insert into players (nick, pass, score) values('"+ currentNick +"', '"+ currentPass +"', "+ score +")";
        try {
            stmt.execute(sql);
            System.out.println("数据表插入成功");
        } catch (SQLException e) {
            System.out.println("数据库插入失败");
            e.printStackTrace();
        }
    }
    public void getInfo(){
        String sql = "select nick, score from players order by score desc limit 4";
        nickList = new ArrayList<String>();
        scoreList = new ArrayList<Integer>();
        try {
            ResultSet res = stmt.executeQuery(sql);
            while(res.next()){
                System.out.println(res.getString("nick") + res.getInt("score"));
                nickList.add(res.getString("nick"));
                scoreList.add(res.getInt("score"));
            }
            System.out.println("数据表插入成功");
        } catch (SQLException e) {
            System.out.println("数据库插入失败");
            e.printStackTrace();
        }
    }
    public List<String> getNicks(){
        return nickList;
    }
    public List<Integer> getScore(){
        return scoreList;
    } 
    public boolean Login(String nick,String pass){
        String sql = "select pass from players where nick ='"+ nick +"'";
        try {
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()){
                if(!pass.equals(res.getString("pass"))){
                    return false;
                }
            }
            currentNick = nick;
            currentPass = pass;
            System.out.println("登录成功");
        } catch (SQLException e) {
            System.out.println("登录失败");
            e.printStackTrace();
        }
        return true;
    }

    public void shanchu(){
        String sql="delete from players where score!=0";//生成一条sql语句
        try {
            stmt.executeUpdate(sql);
            System.out.println("删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }//执行sql语句
        //this.mainWin = mainWin;
        //mainWin.getStaticPanel().repaint();
    }
}

