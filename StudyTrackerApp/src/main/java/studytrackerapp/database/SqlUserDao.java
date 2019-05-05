package studytrackerapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studytrackerapp.domain.User;
/**
 * Luokka tarjoaa erilaisia metodeja tietokannassa olevan User-taulun käsittelyyn.
 */
public class SqlUserDao implements UserDao {
    private Database database;

    public SqlUserDao(Database database) {
        this.database = database;
    }
    /**
     * Metodi lisää uuden käyttäjän tietokantaan.
     * @param user uusi käyttäjä
     * @return uusi User-olio
     * @throws SQLException 
     */
    @Override
    public User create(User user) throws SQLException {
        User byName = getOne(user.getUsername());
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO User (name, username, password) VALUES (?, ?, ?)");
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            
            stmt.executeUpdate();
        }
        return getOne(user.getUsername());
    }
    /**
     * Metodi hakee yhden käyttäjän tietokannasta käyttäjätunnuksen perusteella.
     * @param username käyttäjän käyttäjätunnus
     * @return uusi User-olio tai null, jos käyttäjää ei ole tietokannassa
     * @throws SQLException 
     */
    @Override
    public User getOne(String username) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name, username, password FROM User WHERE username = ?");
            stmt.setString(1, username);
            
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            
            return new User(result.getInt("id"), result.getString("name"), result.getString("username"), result.getString("password"));
        }
    }
    /**
     * Metodi hakee kaikki käyttäjät tietokannasta.
     * @return lista, joka sisältää User-olioita
     * @throws SQLException 
     */
    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User");
            
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                users.add(new User(result.getInt("id"), result.getString("name"), result.getString("username"), result.getString("password")));
            }
        }
        return users;
    }
    /**
     * Metodi hakee yhden käyttäjän tietokannasta id:n perusteella.
     * @param id käyttäjän id
     * @return uusi User-käyttäjä tai null, jos käyttäjä ei ole tietokannassa
     * @throws SQLException 
     */
    public User findById(int id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE id = ?");
            stmt.setInt(1, id);
            
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            
            return new User(result.getInt("id"), result.getString("name"), result.getString("username"), result.getString("password"));
        }
    }
    
}
