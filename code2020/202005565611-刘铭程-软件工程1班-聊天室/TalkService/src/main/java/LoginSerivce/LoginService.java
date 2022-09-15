package LoginSerivce;

/**
 * 判断账号密码是否正确
 * 返回值涵义
 * 0---登录成功
 * 1---用户名或密码错误
 * 2---登录异常
 */

import Util.JDBCUtils;
import user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static int loginResult(User user) {
        try {
            //与数据库建立连接
            conn = JDBCUtils.getConnection();

            //通过账号密码查询数据库
            String sql = "select username,password from users_informations where username = ? and password = ?";
            pstmt = conn.prepareStatement(sql);

            //给 ? 赋值
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            rs = pstmt.executeQuery();

            //判断是否查询到
            if(rs.next()){
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        } finally {
            //释放资源
            JDBCUtils.close(pstmt,conn,rs);
        }

        return 1;
    }
}
