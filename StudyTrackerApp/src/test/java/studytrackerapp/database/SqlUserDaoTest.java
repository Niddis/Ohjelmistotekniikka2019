package studytrackerapp.database;

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import studytrackerapp.domain.User;

public class SqlUserDaoTest {
    SqlUserDao dao;
    
    @Before
    public void setUp() throws Exception {
        Database database = new Database("jdbc:sqlite:sta_test.db");
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
}
