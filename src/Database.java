import java.sql.*;
import java.util.Arrays;
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
            }else{
                System.out.println("USUÁRIO JÁ EXISTENTE!");
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

    public int criaChat(String user1, String user2, String Title) throws SQLException {
        int CID;
        String sql = "INSERT INTO Chats (Title) values('"+Title+"')";
        PreparedStatement stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int UID1 = 0;
        int UID2 = 0;
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        CID = rs.getInt(1);
        rs.close();
        stmt.close();

        sql = "SELECT UID FROM Users WHERE(Username ='"+user1+"');";

        rs = this.st.executeQuery(sql);
        while(rs.next()){
            UID1 = rs.getInt("UID");
        }
        rs.close();

        sql = "SELECT UID FROM Users WHERE(Username ='"+user2+"');";

        rs = this.st.executeQuery(sql);
        while(rs.next()){
            UID2 = rs.getInt("UID");
        }
        rs.close();

        sql = "INSERT INTO Chatlink(CID,UID) VALUES(" + CID + "," + UID1 + "),(" + CID + "," + UID2 + ");";
        this.st.executeQuery(sql);

        return CID;
    }

    public void addChat(int CID, String user) throws SQLException {
        String sql;
        ResultSet rs;
        int UID = 0;
        sql = "SELECT UID FROM Users WHERE(Username ='"+user+"');";

        rs = this.st.executeQuery(sql);
        while(rs.next()){
            UID = rs.getInt("UID");
        }
        rs.close();

        sql = "INSERT INTO Chatlink(CID,UID) VALUES(" + CID + "," + UID + ");";
        this.st.executeQuery(sql);
    }


    public static void main(String[] args) throws SQLException {


    }
}

