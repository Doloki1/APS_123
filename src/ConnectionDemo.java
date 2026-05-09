import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionDemo {
    String userName,password,url,driver;
    Connection con;
    Statement st;

    public ConnectionDemo() {
        userName="root";
        password="root";
        url="jdbc:mariadb://localhost:3306/msgr";
        driver="org.mariadb.jdbc.Driver";
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url, userName, password);
            st=con.createStatement();
            System.out.println("Connection is successful");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
