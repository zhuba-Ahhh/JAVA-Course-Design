package FriendsService;

import Util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendsService {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static ArrayList<String> getFriendsList(String userName) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            conn = JDBCUtils.getConnection();
            String sql = "select friendB from relation where friendA = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("friendB"));
            }

        } catch (SQLException e) {
            System.out.println("获取好友列表异常");
            e.printStackTrace();
        }finally {
            JDBCUtils.close(pstmt,conn,rs);
        }

        return list;
    }
}
