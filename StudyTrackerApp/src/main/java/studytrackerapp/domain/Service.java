package studytrackerapp.domain;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import studytrackerapp.database.CourseDao;
import studytrackerapp.database.UserDao;
/**
 * Luokka sisältää sovelluksen sovelluslogiikan.
 */
public class Service {
    private User loggedIn;
    private UserDao userDao;
    private CourseDao courseDao;
    private List<Course> courses;
    private List<Course> coursesFromFile;
    private List<User> users;
    private SimpleIntegerProperty sumValue;
    private int pointsSum;

    public Service(UserDao userdao, CourseDao coursedao) {
        this.userDao = userdao;
        this.courseDao = coursedao;
        this.courses = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public Service(UserDao userdao, CourseDao coursedao, String coursesFile) {
        this.userDao = userdao;
        this.courseDao = coursedao;
        this.courses = new ArrayList<>();
        this.users = new ArrayList<>();
        this.coursesFromFile = new ArrayList<>();
        this.sumValue = new SimpleIntegerProperty(0);
        readCourseFile(coursesFile);
    }
    
    public User getLoggedUser() {
        return loggedIn;
    }
    /**
     * Metodi muuttaa loggedIn -muuttujan nulliksi ja tyhjentää courses -listan.
     */
    public void logout() {
        loggedIn = null;
        courses.clear();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Course> getCoursesFromFile() {
        return coursesFromFile;
    }

    public int getPointsSum() {
        return pointsSum;
    }

    public SimpleIntegerProperty getSumValue() {
        sumValue.set(pointsSum);
        return sumValue;
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
            user = userDao.getOne(username);
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
     * @param name kurssin nimi
     * @param compulsory 1 jos kurssi on pakollinen, 0 jos se on valinnainen
     * @param points kurssista saatavat opintopisteet
     * @return true, jos kurssin luominen onnistuu, muuten false
     */
    public boolean createNewCourse(String name, int compulsory, int points) {
        Course course = new Course(0, name, 0, compulsory, points, loggedIn);
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
        if (course == null) {
            return false;
        }
        try {
            courseDao.delete(id, getLoggedUser().getId());
            courses.remove(course);
        } catch (Exception e) {
            return false;
        }
        sumPoints();
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
        sumPoints();
        return courses;
    }
    /**
     * Metodi päivittää kurssin done-attribuutin.
     * @param id päivitettävän kurssin id
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateDone(int id) {
        Course course = findCourse(id);
        if (course == null) {
            return false;
        }
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
    /**
     * Metodi päivittää kurssin kaikki tiedot.
     * @param id kurssin id
     * @param name kurssin uusi nimi
     * @param done kurssin uusi done-arvo
     * @param compulsory kurssin uusi compulsory-arvo
     * @param points kurssin uudet opintopisteet
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateCourse(int id, String name, int done, int compulsory, int points) {
        Course course = findCourse(id);
        if (course == null) {
            return false;
        }
        try {
            course.setName(name);
            course.setDone(done);
            course.setCompulsory(compulsory);
            course.setPoints(points);
            courseDao.update(loggedIn.getId(), course);   
        } catch (Exception e) {
            return false;
        }
        sumPoints();
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
        if (course == null) {
            return false;
        }
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
     * @return true, jos päivitys onnistuu, muuten false
     */
    public boolean updateCourseCompulsory(int id) {
        Course course = findCourse(id);
        if (course == null) {
            return false;
        }
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
        if (course == null) {
            return false;
        }
        course.setPoints(points);
        try {
            courseDao.update(loggedIn.getId(), course);
        } catch (Exception e) {
            return false;
        }
        sumPoints();
        return true;
    }
    /**
     * Metodi laskee kirjautuneen käyttäjän kurssien opintopisteiden summan.
     * @return opintopisteiden summa
     */
    private void sumPoints() {
        int sum = 0;
        for (Course course: courses) {
            if (course.getDone() == 1) {
                sum += course.getPoints();
            }
        }
        
        pointsSum = sum;
    }
    /**
     * Metodi järjestää kirjautuneen käyttäjän kurssit parametrina saadun merkkijonon perusteella.
     * @param sorter merkkijono, jonka perusteella järjestäminen tehdään
     */
    public void sortCoursesList(String sorter) {
        if (sorter.equals("nimi")) {
            courses.sort((course1, course2) -> course1.getName().compareTo(course2.getName()));
        } else if (sorter.equals("suoritettu")) {
            courses.sort((course1, course2) -> course2.getDone() - course1.getDone());
        } else if (sorter.equals("pakollinen")) {
            courses.sort((course1, course2) -> course2.getCompulsory() - course1.getCompulsory());
        } else {
            courses.sort((course1, course2) -> course1.getPoints() - course2.getPoints());
        }
    }
    
    private Course findCourse(int id) {
        for (Course course: courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }
    /**
     * Metodi käy tekstitiedoston läpi rivi kerrallaan ja luo jokaisesta rivistä Course -olion.
     * Luodut oliot lisätään courses -muuttujaan.
     * @param file tiedoston nimi
     */
    private void readCourseFile(String file) {
        try {
            Files.lines(Paths.get(file))
                .map(line -> line.split(";"))
                .map(parts -> new Course(parts[0], Integer.valueOf(parts[1]), Integer.valueOf(parts[2])))
                .forEach(course -> coursesFromFile.add(course));
        } catch (Exception e) {

        }
        
        coursesFromFile.sort((course1, course2) -> (course1.getName().compareTo(course2.getName())));
    }
}
