package studytrackerapp.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studytrackerapp.database.Database;
import studytrackerapp.database.SqlCourseDao;
import studytrackerapp.database.SqlUserDao;
import studytrackerapp.database.UserDao;

public class Service {
    private User loggedIn;
    private SqlUserDao userDao;
    private SqlCourseDao courseDao;
    private Database database;

    public Service(Database database) {
        this.database = database;
        this.userDao = new SqlUserDao(database);
        this.courseDao = new SqlCourseDao(database);
    }
    
    public User getLoggedUser() {
        return loggedIn;
    }
    
    public void logout() {
        loggedIn = null;
    }
    
    public boolean login(String username, String password) {
        User user;
        try {
            user = userDao.findByUserName(username);
        } catch (Exception e) {
            return false;
        }
        
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        loggedIn = user;
        return true;
    }
    
    public boolean createNewUser(int id, String name, String username, String password) {
        User user = new User(id, name, username, password);
        try {
            userDao.create(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    public boolean createNewCourse(int id, String name, int compulsory, int points) {
        Course course = new Course(id, name, compulsory, points, loggedIn);
        if (loggedIn == null) {
            return false;
        }
        try {
            courseDao.create(course, loggedIn);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean deleteCourse(int id) {
        try {
            courseDao.delete(id, getLoggedUser().getId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public List<Course> listCoursesByUser() {
        List<Course> courses = new ArrayList<>();
        try {
            courses = courseDao.getAllByUser(loggedIn.getId());
        } catch (Exception e) {
            
        }
        return courses;
    }
    
    public boolean setCourseDone(int id) {
        try {
            courseDao.setDone(id, loggedIn.getId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean updateCourseName(int id, String name) {
        try {
            courseDao.updateName(id, loggedIn.getId(), name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
