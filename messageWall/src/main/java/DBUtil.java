import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//提供两个静态方法,分别实现数据库的连接和断开操作
public class DBUtil {

   private static DataSource dataSource=new MysqlDataSource();

    static{
        ((MysqlDataSource)dataSource).setUser("root");
        ((MysqlDataSource)dataSource).setURL("jdbc:mysql://127.0.0.1:3306/messageWall?characterEncoding=utf8&useSSL=false");
        ((MysqlDataSource)dataSource).setPassword("123456");
    }


    /**
     * 建立连接
     * @return
     * @throws SQLException
     */
   public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
   }

    /**
     * 断开连接
     */
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
               e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
