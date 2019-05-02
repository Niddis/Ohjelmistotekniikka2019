package studytrackerapp.database;

import java.sql.SQLException;
import java.util.List;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

public interface CourseDao {
    Course create(Course course, User user) throws SQLException;
    
    List<Course> getAll() throws SQLException;
    
    List<Course> getAllByUser(int userId) throws SQLException;
    
    Course getOne(String name, User user) throws SQLException;
    
    void update(int userId, Course course) throws SQLException;
    
    void delete(int id, int userId) throws SQLException;
}
