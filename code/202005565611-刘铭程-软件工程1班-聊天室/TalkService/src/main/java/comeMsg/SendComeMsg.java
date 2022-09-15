package comeMsg;

import Util.JDBCUtils;
import main.MainController;
import talk.TalkService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SendComeMsg {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static void sendComeMsg(String username) {
        try {
            //与数据库建立连接
            conn = JDBCUtils.getConnection();

            //通过账号密码查询数据库
            String sql = "select friendB from relation where friendA = ?";
            pstmt = conn.prepareStatement(sql);

            //给 ? 赋值
            pstmt.setString(1, username);

            rs = pstmt.executeQuery();

            //判断是否查询到
            while (rs.next()) {
                String friendName = rs.getString(1);
                if (MainController.userMap.containsKey(friendName)) {
                    //如果该好友在线
                    TalkService.sendResult(friendName, "123123123".toCharArray(), 5, "上号提示音");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放资源
            JDBCUtils.close(pstmt, conn, rs);
        }
    }
}
