package studytrackerapp.database;

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
import studytrackerapp.domain.User;

public class SqlUserDaoTest {
    SqlUserDao dao;
    Database database;
    
    @Before
    public void setUp() throws Exception {
        database = new Database("jdbc:sqlite:sta_test.db");
        database.init();
        
        dao = new SqlUserDao(database);
    }
    
    @Test
    public void newUserIsCreated() throws SQLException {
        User user = new User(1, "Terhi Testaaja", "Terhi", "Testaaja");
        User returnedUser = dao.create(user);
        String name = returnedUser.getUsername();
        assertEquals("Terhi", name);
    }
    
    @Test
    public void nonExistingUserIsFound() throws SQLException {
        User user = dao.getOne("notHere");
        assertEquals(null, user);
    }
    
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
