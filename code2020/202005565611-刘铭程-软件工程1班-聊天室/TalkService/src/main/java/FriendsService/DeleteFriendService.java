package FriendsService;

/**
 * 删除好友
 * 返回值涵义
 * 0---成功
 * 1---错误
 */

import Util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteFriendService {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static int deleteFriend(String username, String friendName) {
        try {
            //与数据库建立连接
            conn = JDBCUtils.getConnection();

            //查询数据库
            String sql = "select friendA,friendB from relation where friendA = ? and friendB = ?";
            pstmt = conn.prepareStatement(sql);

            System.out.println(username + " " + friendName);

            //给 ? 赋值
            pstmt.setString(1, username);
            pstmt.setString(2, friendName);

            rs = pstmt.executeQuery();

            //确认是否为好友关系
            if (rs.next()) {
                //执行删除
                sql = "delete from relation where friendA = ? and friendB = ?";
                pstmt = conn.prepareStatement(sql);

                //给 ? 赋值
                pstmt.setString(1, username);
                pstmt.setString(2, friendName);

                //执行
                pstmt.execute();

                //删除对方好友列表中的该用户
                sql = "delete from relation where friendA = ? and friendB = ?";
                pstmt = conn.prepareStatement(sql);

                //给 ? 赋值
                pstmt.setString(1, friendName);
                pstmt.setString(2, username);

                //执行
                pstmt.execute();

                return 0;
            } else {
                //不是好友关系，返回失败信号
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        } finally {
            //释放资源
            JDBCUtils.close(pstmt, conn, rs);
        }
    }
}
