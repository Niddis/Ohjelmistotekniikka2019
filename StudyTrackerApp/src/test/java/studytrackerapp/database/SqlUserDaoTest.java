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
import studytrackerapp.domain.User;

public class SqlUserDaoTest {
    SqlUserDao dao;
    File testDatabase;
    
    @Before
    public void setUp() throws Exception {
        testDatabase = File.createTempFile("test", "db");
        Database database = new Database("jdbc:sqlite:test.db");

        try (Connection conn = database.getConnection()) {
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE User (id integer PRIMARY KEY, name varchar(20), username varchar(10), password varchar(15));");
        } catch (Throwable t) {
            
        } 
        dao = new SqlUserDao(database);
    }
    
    @Test
    public void newUserIsCreated() throws SQLException {
        User user = new User(1, "Terhi Testaaja", "Terhi", "Testaaja");
        User returnedUser = dao.create(user);
        assertEquals("Terhi", returnedUser.getUsername());
    }
    
    @Test
    public void nonExistingUserIsFound() throws SQLException {
        User user = dao.findByUserName("notHere");
        assertEquals(null, user);
    }
    
    @Test
    public void existingUserIsFound() throws SQLException{
        User user = dao.findByUserName("Terhi");
        assertEquals("Terhi", user.getUsername());
        assertEquals("Terhi Testaaja", user.getName());
    }
    
    @After
    public void tearDown() {
        testDatabase.delete();
    }
}
