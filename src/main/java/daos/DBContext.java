package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBContext {

    protected Connection connection;
    public DBContext()
    {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=OnlineShopDB;trustServerCertificate=true";
            String username = "sa";
            String password = "123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }
    public static void main(String[] args) {
        DBContext dbContext = new DBContext();
        Connection conn = dbContext.connection;

        if (conn != null) {
            System.out.println("✅ Kết nối đến cơ sở dữ liệu thành công!");
        } else {
            System.out.println("❌ Kết nối thất bại. Vui lòng kiểm tra URL, username, password, hoặc trạng thái SQL Server.");
        }
    }
}
