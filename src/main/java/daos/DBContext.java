package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBContext {

    protected Connection connection;
    public DBContext()
    {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=OnlineShopDBv2 ;encrypt=true;trustServerCertificate=true;";
            String username = "sa";
            String password = "1234";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        try{
            DBContext db = new DBContext();
            System.out.println("ok");
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }
}
