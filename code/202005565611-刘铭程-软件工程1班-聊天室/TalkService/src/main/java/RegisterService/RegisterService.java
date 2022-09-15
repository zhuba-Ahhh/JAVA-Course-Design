package RegisterService;

import Util.JDBCUtils;
import user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterService {
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    public static int registerResult(User user) {
        try {
            //与数据库建立连接
            conn = JDBCUtils.getConnection();

            //通过账号密码查询数据库
            String sql = "select username,password from users_informations where username = ?";
            pstmt = conn.prepareStatement(sql);

            System.out.println(user.getUsername() + " " + user.getPassword());

            //给 ? 赋值
            pstmt.setString(1, user.getUsername());

            rs = pstmt.executeQuery();

            //判断是否查询到
            if (rs.next()) {
                //用户名存在
                return 1;
            } else {
                sql = "insert into users_informations values(?,?)";
                pstmt = conn.prepareStatement(sql);

                //给 ? 赋值
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2,user.getPassword());

                //将用户名 密码存入数据库
                pstmt.execute();

                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        } finally {
            //释放资源
            JDBCUtils.close(pstmt, conn, rs);
        }
    }
}