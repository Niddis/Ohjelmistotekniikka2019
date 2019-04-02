package studytrackerapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String databaseAddress;

    public Database(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    
    public void init() {
        List<String> strings = sqlStrings();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            for (String s : strings) {
                st.executeUpdate(s);
            }

        } catch (Throwable t) {
            //System.out.println(t.getMessage());
        }
    }
    
    private List<String> sqlStrings() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("CREATE TABLE User (id integer PRIMARY KEY, name varchar(20), username varchar(10), password varchar(15));");

        return strings;
    }
}
