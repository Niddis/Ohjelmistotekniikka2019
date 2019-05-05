package studytrackerapp.database;

import java.util.List;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

public interface CourseDao {
    Course create(Course course, User user) throws Exception;
    
    List<Course> getAllByUser(int userId) throws Exception;
    
    Course getOne(String name, User user) throws Exception;
    
    void update(int userId, Course course) throws Exception;
    
    void delete(int id, int userId) throws Exception;
}
