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
    private List<Course> courses;
    private List<User> users;

    public Service(Database database) {
        this.database = database;
        this.userDao = new SqlUserDao(database);
        this.courseDao = new SqlCourseDao(database);
        this.courses = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    
    public User getLoggedUser() {
        return loggedIn;
    }
    
    public void logout() {
        loggedIn = null;
        courses.clear();
    }
    
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }
    
    /**
     * Metodi kirjaa käyttäjän sisään ohjelmaan.
     * @param username käyttäjän käyttäjätunnus
     * @param password käyttäjän salasana
     * @return true, jos kirjautuminen onnistuu, muuten false
     */
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
    /**
     * Metodi luo uuden käyttäjän.
     * @param id käyttäjän id (oletusarvoisesti 0)
     * @param name käyttäjän nimi
     * @param username käyttäjän käyttäjätunnus
     * @param password käyttäjän salasana
     * @return true, jos uuden käyttäjän luominen onnistuu, muuten false
     */
    public boolean createNewUser(int id, String name, String username, String password) {
        try {
            users = userDao.getAll();
        } catch (Exception e) {
            
        }
        for (User user: users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        User user = new User(id, name, username, password);
        try {
            userDao.create(user);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    /**
     * Metodi luo uuden kurssin.
     * @param id kurssin id (oletusarvoisesti 0)
     * @param name kurssin nimi
     * @param compulsory 1 jos kurssi on pakollinen, 0 jos se on valinnainen
     * @param points kurssista saatavat opintopisteet
     * @return true, jos kurssin luominen onnistuu, muuten false
     */
    public boolean createNewCourse(int id, String name, int compulsory, int points) {
        Course course = new Course(id, name, compulsory, points, loggedIn);
        if (loggedIn == null) {
            return false;
        }
        try {
            Course returnedCourse = courseDao.create(course, loggedIn);
            courses.add(returnedCourse);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Metodi poistaa kurssin.
     * @param id poistettavan kurssin id
     * @return true, jos poistaminen onnistuu, muuten false
     */
    public boolean deleteCourse(int id) {
        Course course = findCourse(id);
        try {
            courseDao.delete(id, getLoggedUser().getId());
            courses.remove(course);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Metodi hakee kaikki kirjautuneen käyttäjän kurssit.
     * @return Lista, joka sisältää Course-olioita
     */
    public List<Course> listCoursesByUser() {
        List<Course> noCourses = new ArrayList<>();
        if (loggedIn == null) {
            return noCourses;
        }
        try {
            courses = courseDao.getAllByUser(loggedIn.getId());
        } catch (Exception e) {
            
        }
        //System.out.println("Servicekurssit: " + courses);
        return courses;
    }
    /**
     * Metodi päivittää kurssin done-attribuutin.
     * @param id päivitettävän kurssin id
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean setCourseDone(int id) {
        Course course = findCourse(id);
        if (course.getDone() == 1) {
            course.setDone(0);
        } else {
            course.setDone(1);
        }
        try {
            courseDao.update(loggedIn.getId(), course);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean updateCourse(int id, String name, int done, int compulsory, int points) {
        Course course = findCourse(id);
        try {
            courseDao.update(loggedIn.getId(), course);
            course.setName(name);
            course.setDone(done);
            course.setCompulsory(compulsory);
            course.setPoints(points);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Metodi päivittää kurssin nimen.
     * @param id päivitettävän kurssin id
     * @param name päivitettävän kurssin uusi nimi
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateCourseName(int id, String name) {
        Course course = findCourse(id);
        course.setName(name);
        try {
            courseDao.update(loggedIn.getId(), course);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Metodi päivittää kurssin compulsory-attribuutin arvon. Jos alkuperäinen arvo
     * on 1 (pakollinen) se vaihdetaan 0:ksi (valinnainen) ja päinvastoin.
     * @param id päivitettävän kurssin id
     * @param compulsory päivitettävän kurssin alkuperäinen compulsory:n arvo
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateCourseCompulsory(int id) {
        Course course = findCourse(id);
        if (course.getCompulsory() == 1) {
            course.setCompulsory(0);
        } else {
            course.setCompulsory(1);
        }
        try {
            courseDao.update(loggedIn.getId(), course);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Metodi päivittää kurssin opintopisteet.
     * @param id päivitettävän kurssin id
     * @param points päivitettävän kurssin uudet opintopisteet
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateCoursePoints(int id, int points) {
        Course course = findCourse(id);
        course.setPoints(points);
        try {
            courseDao.update(loggedIn.getId(), course);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Metodi laskee kirjautuneen käyttäjän kurssien opintopisteiden summan.
     * @return opintopisteiden summa
     */
    public int sumPoints() {
        int sum = 0;
        for (Course course: courses) {
            sum += course.getPoints();
        }
        return sum;
    }
    
    private Course findCourse(int id) {
        for (Course course: courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }
}
