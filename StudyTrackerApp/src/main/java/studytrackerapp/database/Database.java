package studytrackerapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Luokka sisältää tietokannan hallinointiin vaadittavia metodeja.
 */
public class Database {
    private String databaseAddress;

    public Database(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
    /**
     * Metodi avaa yhteyden tietokantaan.
     * @return toimiva yhteys
     * @throws SQLException 
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
    /**
     * Metodi alustaa tietokannan, jos sitä ei ole jo olemassa.
     */
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
    /**
     * Metodi sisältää tietokannan alustamiseen vaadittavat sql-lauseet.
     * @return sql-lauseita sisältävä lista
     */
    private List<String> sqlStrings() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("CREATE TABLE IF NOT EXISTS User (id integer PRIMARY KEY, name varchar(50), username varchar(10), password varchar(15));");
        strings.add("CREATE TABLE IF NOT EXISTS Course (id integer PRIMARY KEY, user_id integer, name varchar(50), done integer, compulsory integer, points integer, FOREIGN KEY (user_id) REFERENCES User(id));");
        return strings;
    }
}
