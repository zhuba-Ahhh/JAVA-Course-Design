package FriendsService;

/**
 * 添加好友
 * 返回值涵义
 * 0---成功
 * 1---错误
 * 2---用户不存在
 */

import Util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddFriendService {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static int addFriend(String username, String friendName) {
        try {
            //与数据库建立连接
            conn = JDBCUtils.getConnection();

            //查询数据库
            //查询是否存在要添加的用户
            String sql = "select username from users_informations where username = ?";
            pstmt = conn.prepareStatement(sql);
            //给 ? 赋值
            pstmt.setString(1, friendName);

            //执行
            rs = pstmt.executeQuery();

            if(!rs.next()){
                //不存在该用户
                return 2;
            }

            //查询是否已是好友关系
            sql = "select friendA,friendB from relation where friendA = ? and friendB = ?";
            pstmt = conn.prepareStatement(sql);

            System.out.println(username + " " + friendName);

            //给 ? 赋值
            pstmt.setString(1, username);
            pstmt.setString(2, friendName);

            rs = pstmt.executeQuery();

            //确认是否为好友关系
            if (rs.next()) {
                //是好友关系，返回失败信号
                return 1;
            } else {
                //执行添加
                sql = "insert into relation values (?,?)";
                pstmt = conn.prepareStatement(sql);

                //给 ? 赋值
                pstmt.setString(1, username);
                pstmt.setString(2, friendName);

                //执行
                pstmt.execute();

                //删除对方好友列表中的该用户
                sql = "insert into relation values (?,?)";
                pstmt = conn.prepareStatement(sql);

                //给 ? 赋值
                pstmt.setString(1, friendName);
                pstmt.setString(2, username);

                //执行
                pstmt.execute();

                return 0;
            }
        } catch (SQLException e) {
            System.out.println("添加出现异常");
            return 1;
        } finally {
            //释放资源
            JDBCUtils.close(pstmt, conn, rs);
        }
    }
}
