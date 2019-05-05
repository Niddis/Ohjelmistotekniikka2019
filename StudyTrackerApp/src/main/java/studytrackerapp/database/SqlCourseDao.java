package studytrackerapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;
/**
 * Luokka tarjoaa erilaisia metodeja tietokannassa olevan Course-taulun käsittelyyn.
 */
public class SqlCourseDao implements CourseDao {
    private Database database;
    private SqlUserDao userDao;

    public SqlCourseDao(Database database) {
        this.database = database;
        this.userDao = new SqlUserDao(database);
    }
    /**
     * Metodi lisää uuden kurssin tietokantaan.
     * @param course lisättävä kurssi
     * @param user kurssin luonut käyttäjä
     * @return uusi course-olio
     * @throws SQLException 
     */
    @Override
    public Course create(Course course, User user) throws SQLException {
        Course byName = getOne(course.getName(), user);
        if (byName != null) {
            return byName;
        }
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Course (user_id, name, done, compulsory, points) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, course.getName());
            stmt.setInt(3, course.getDone());
            stmt.setInt(4, course.getCompulsory());
            stmt.setInt(5, course.getPoints());
            
            stmt.executeUpdate();
        }
        return getOne(course.getName(), user);
    }

    /**
     * Metodi hakee tietokannasta kaikki kirjautuneena olevan käyttäjän kurssit.
     * @param userId kirjautuneen käyttäjän id
     * @return Lista, joka sisältää Course-olioita.
     * @throws SQLException 
     */
    @Override
    public List<Course> getAllByUser(int userId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Course WHERE user_id = ? ORDER BY name");
            stmt.setInt(1, userId);
            
            ResultSet result = stmt.executeQuery();
            User user = userDao.findById(userId);
            while (result.next()) {
                courses.add(new Course(result.getInt("id"), result.getString("name"), result.getInt("done"), result.getInt("compulsory"), result.getInt("points"), user));
            }
        }
        return courses;
    }
    /**
     * Metodi hakee yhden kurssin tietokannasta nimen perusteella.
     * @param name kurssin nimi
     * @return uusi Course-olio
     * @throws SQLException 
     */
    @Override
    public Course getOne(String name, User user) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Course WHERE name = ? AND user_id = ?");
            stmt.setString(1, name);
            stmt.setInt(2, user.getId());
            
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }
            //User user = userDao.findById(result.getInt("user_id"));
            
            return new Course(result.getInt("id"), result.getString("name"), result.getInt("done"), result.getInt("compulsory"), result.getInt("points"), user);
        }
    }
    /**
     * Metodi päivittää kurssin tiedot.
     * @param userId kirjautuneen käyttäjän id
     * @param course päivitettävä kurssi
     * @throws SQLException 
     */
    @Override
    public void update(int userId, Course course) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Course SET name = ?, done = ?, compulsory = ?, points = ? WHERE id = ? AND user_id = ?");
            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getDone());
            stmt.setInt(3, course.getCompulsory());
            stmt.setInt(4, course.getPoints());
            stmt.setInt(5, course.getId());
            stmt.setInt(6, userId);
            stmt.executeUpdate();
        }
    }
    /**
     * Metodi poistaa kurssin tietokannasta id:n perusteella.
     * @param id poistettavan kurssin id
     * @param userId kirjautuneen käyttäjän id
     * @throws SQLException 
     */
    @Override
    public void delete(int id, int userId) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Course WHERE id = ? AND user_id = ?");
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
