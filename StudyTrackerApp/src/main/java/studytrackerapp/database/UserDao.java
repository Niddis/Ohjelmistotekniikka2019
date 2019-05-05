package studytrackerapp.database;

import java.util.List;
import studytrackerapp.domain.User;

public interface UserDao {
    User create(User user) throws Exception;
    
    User getOne(String username) throws Exception;
    
    List<User> getAll() throws Exception;
}
