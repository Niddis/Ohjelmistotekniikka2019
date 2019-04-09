package studytrackerapp.database;

import java.sql.*;
import java.util.List;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

public class SqlCourseDao implements CourseDao {
    private Database database;
    private SqlUserDao userDao;

    public SqlCourseDao(Database database) {
        this.database = database;
        this.userDao = new SqlUserDao(database);
    }

    @Override
    public Course create(Course course, User user) throws SQLException {
        Course byId = getOne(course.getName());
        if (byId != null) {
            return byId;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Course (user_id, name, done, compulsory, points) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, course.getName());
            stmt.setInt(3, course.getDone());
            stmt.setInt(4, course.getCompulsory());
            stmt.setInt(5, course.getPoints());
            
            stmt.executeUpdate();
        }
        return getOne(course.getName());
    }

    @Override
    public List<Course> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Course> getAllByUser(int userId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Course getOne(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Course WHERE name = ?");
            stmt.setString(1, name);
            
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            User user = userDao.findById(result.getInt("user_id"));
            
            return new Course(result.getInt("id"), result.getString("name"), result.getInt("compulsory"), result.getInt("points"), user);
        }
    }

    @Override
    public Course update(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}