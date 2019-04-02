package studytrackerapp.domain;

import studytrackerapp.database.Database;
import studytrackerapp.database.SqlUserDao;
import studytrackerapp.database.UserDao;

public class Service {
    private User loggedIn;
    private SqlUserDao userDao;
    private Database database;

    public Service(Database database) {
        this.database = database;
        this.userDao = new SqlUserDao(database);
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
}
