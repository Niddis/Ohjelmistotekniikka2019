package studytrackerapp.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

public class SqlCourseDaoTest {
    SqlCourseDao courseDao;
    SqlUserDao userDao;
    File testDatabase;
    User user;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = File.createTempFile("course_test", "db");
        Database database = new Database("jdbc:sqlite:course_test.db");

        database.init();
        
        userDao = new SqlUserDao(database);
        courseDao = new SqlCourseDao(database);
        
        user = userDao.create(new User(1, "Terhi Testaaja", "Terhi", "Testaaja"));
        courseDao.create(new Course(0, "Ohpe", 1, 5, user), user);
    }
    
    @Test
    public void newCourseIsCreated() throws SQLException {
        Course course = new Course(0, "Ohte", 1, 5, user);
        Course returnedCourse = courseDao.create(course, user);
        assertEquals("Ohte", returnedCourse.getName());
    }
    
    @Test
    public void nonExistingCourseIsFound() throws SQLException {
        Course course = courseDao.getOne("notExisting");
        assertEquals(null, course);
    }
    
    @Test
    public void allCoursesAreListedByUser() throws SQLException {
        List<Course> courses = new ArrayList<>();
        courses = courseDao.getAllByUser(1);
        assertEquals(1, courses.size());
    }
    
    @Test
    public void courseIsDeleted() throws SQLException {
        courseDao.delete(2, 1);
        Course course = courseDao.getOne("Ohte");
        assertEquals(null, course);
    }
    
    /*@Test
    public void existingCourseIsFound() throws SQLException{
        Course course = courseDao.getOne("Ohte");
        assertEquals("Ohte", course.getName());
        assertEquals(5, course.getPoints());
    }*/
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }
}
