package studytrackerapp.database;

import java.sql.*;
import java.util.List;
import studytrackerapp.domain.User;

public class SqlUserDao implements UserDao {
    private Database database;

    public SqlUserDao(Database database) {
        this.database = database;
    }
    
    

    @Override
    public User create(User user) throws SQLException {
        User byName = findByUserName(user.getUsername());
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
        return findByUserName(user.getUsername());
    }

    @Override
    public User findByUserName(String username) throws SQLException {
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

    @Override
    public List<User> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
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
