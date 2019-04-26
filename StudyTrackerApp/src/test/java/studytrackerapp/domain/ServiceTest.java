package studytrackerapp.domain;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import studytrackerapp.database.Database;
import studytrackerapp.database.SqlCourseDao;
import studytrackerapp.database.SqlUserDao;
import studytrackerapp.domain.User;

public class ServiceTest {
    //File testDatabase;
    SqlUserDao userDao;
    SqlCourseDao courseDao;
    Service service;
    Database database;
    
    @Before
    public void setUp() throws Exception {
        //testDatabase = File.createTempFile("service_test", "db");
        database = new Database("jdbc:sqlite:service_test.db");

        database.init();
        userDao = new SqlUserDao(database);
        courseDao = new SqlCourseDao(database);
        service = new Service(database);
    }
    
    @Test
    public void newUserIsCreated() {
        boolean newUser = service.createNewUser(0, "Terhi Testaaja", "Terhi", "Testaaja");
        assertEquals(true, newUser);
    }
    
    @Test
    public void existingUserCouldLogin() {
        service.createNewUser(0, "Terhi Testaaja", "Terhi", "Testaaja");
        boolean loggedIn = service.login("Terhi", "Testaaja");
        assertEquals(true, loggedIn);
    }
    
    @Test
    public void nonExistingUserCouldNotLogin() {
        boolean loggedIn = service.login("Hello", "World");
        assertEquals(false, loggedIn);
    }
    
    @Test
    public void newCourseIsCreated() {
        service.createNewUser(0, "Terhi Testaaja", "Terhi", "Testaaja");
        service.login("Terhi", "Testaaja");
        boolean success = service.createNewCourse(0, "Ohte", 0, 5);
        assertEquals(true, success);
    }
    
    @Test
    public void existingCourseIsDeleted() {
        service.createNewUser(0, "Terhi Testaaja", "Terhi", "Testaaja");
        service.login("Terhi", "Testaaja");
        service.createNewCourse(0, "Ohte", 0, 5);
        boolean success = service.deleteCourse(1);
        assertEquals(true, success);
    }
    
    /*@Test
    public void existingCoursenameIsUpdated() {
        service.createNewUser(0, "Terhi Testaaja", "Terhi", "Testaaja");
        service.login("Terhi", "Testaaja");
        service.createNewCourse(0, "Ohte", 0, 5);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1, "Ohte", 0, 5, service.getLoggedUser()));
        
        boolean success = service.updateCourseName(1, "Ohja");
        assertEquals(true, success);
    }*/
    
    @After
    public void tearDown() throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DROP TABLE Course");
            stmt.executeUpdate();
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DROP TABLE User");
            stmt.executeUpdate();
        }
    }
}
