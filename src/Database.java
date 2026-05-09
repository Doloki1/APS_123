import java.sql.*;
import java.util.Objects;

public class Database {
    String userName,password,url,driver;
    Connection con;
    Statement st;

    public Database() {

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

    public void salvaMensagem(String msg, String username, int CID) throws SQLException {
        String sql;
        int UID = 0;
        if (!msg.isBlank() && msg != null && !username.isBlank() && username != null) {

            ResultSet rs = this.st.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");

            while (rs.next()) {
                UID = rs.getInt("UID");
            }
            sql = "INSERT INTO Messages(MTIME,Message,UID,CID) Values (NOW(),\"" + msg + "\"," + UID + "," + CID + ");";

            this.st.executeQuery(sql);
        }
    }

    public void salvaUsuario(String username,String password) throws SQLException {

        if (!username.isBlank() && username != null) {

            ResultSet rs = this.st.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
            String str = "";
            String sql;
            while (rs.next()) {
                str = rs.getString("Username");
            }

            if( str.isBlank() || str == null) {

                sql = "INSERT INTO Users(Username,password) VALUES ('"+username+"','"+password+"')";
                this.st.executeQuery(sql);
            }
        }

    }

    public Boolean login(String username,String Password) throws SQLException {

        ResultSet rs = this.st.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "'");
        String name = "";
        String pass = "";
        String sql;

        while (rs.next()) {
            name = rs.getString("Username");
            pass = rs.getString("password");
        }
        if(!name.isBlank()){
            return Objects.equals(Password, pass);
        }else{
            return false;
        }
    }


    public static void main(String[] args) throws SQLException {
        Database db = new Database();

    }
}

