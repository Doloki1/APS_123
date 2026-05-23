import GUI.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception {
        String test = "Você:123123123";
        System.out.println(test.substring(0,test.indexOf(':')));
    }

    public static JSONArray convert(ResultSet rs) throws Exception {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }

}
