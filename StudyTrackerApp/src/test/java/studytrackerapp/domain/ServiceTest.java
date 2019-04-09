package studytrackerapp.domain;

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
import studytrackerapp.database.Database;
import studytrackerapp.database.SqlUserDao;
import studytrackerapp.domain.User;

public class ServiceTest {
    File testDatabase;
    SqlUserDao userDao;
    Service service;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = File.createTempFile("service_test", "db");
        Database database = new Database("jdbc:sqlite:service_test.db");

        database.init();
        userDao = new SqlUserDao(database);
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
}
