package studytrackerapp.database;

import java.sql.*;
import java.util.*;
import studytrackerapp.domain.User;

public interface UserDao {
    User create(User user) throws SQLException;
    
    User findByUserName(String username) throws SQLException;
    
    List<User> getAll() throws SQLException;
}
