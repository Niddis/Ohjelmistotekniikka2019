package studytrackerapp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import studytrackerapp.database.CourseDao;
import studytrackerapp.database.UserDao;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.Service;

public class TextUi {
    private Scanner scanner;
    private Service service;

    public TextUi(Scanner scanner, UserDao userdao, CourseDao coursedao) {
        this.scanner = scanner;
        this.service = new Service(userdao, coursedao);
    }
    
    public void start() {
        System.out.println("Opintojen seurantasovellus");
        System.out.println("");
        System.out.println("1 lisää käyttäjä");
        System.out.println("2 kirjaudu sisään");
        System.out.println("3 kirjaudu ulos");
        System.out.println("4 lisää kurssi");
        System.out.println("5 näytä omat kurssit");
        System.out.println("6 poista kurssi");
        System.out.println("7 päivitä kurssin tiedot");
        System.out.println("x lopeta");
        
        while (true) {
            System.out.println("");
            System.out.print("komento: ");
            String command = scanner.nextLine();
            
            if (command.equals("x")) {
                break;
            } else if (command.equals("1")) {
                createUser();
            } else if (command.equals("2")) {
                loginUser();
            } else if (command.equals("3")) {
                logoutUser();
            } else if (command.equals("4")) {
                createCourse();
            } else if (command.equals("5")) {
                listCoursesByUser();
            } else if (command.equals("6")) {
                deleteCourse();
            } else if (command.equals("7")) {
                updateCourse();
            } else {
                System.out.println("Virheellinen komento");
            }
        }
    }
    
    private void createUser() {
        System.out.print("Nimi: ");
        String name = scanner.nextLine();
        System.out.print("Käyttäjätunnus: ");
        String username = scanner.nextLine();
        System.out.print("Salasana: ");
        String password = scanner.nextLine();
        boolean success = service.createNewUser(0, name, username, password);
        if (success) {
            System.out.println("Uusi käyttäjä luotu! Voit nyt kirjautua sisään ohjelmaan.");
        } else {
            System.out.println("Käyttäjän luominen epäonnistui. Käyttäjätunnus on jo käytössä.");
        }
    }
    
    private void loginUser() {
        System.out.print("Käyttäjätunnus: ");
        String username = scanner.nextLine();
        System.out.print("Salasana: ");
        String password = scanner.nextLine();
        boolean success = service.login(username, password);
        if (success) {
            System.out.println("Sisäänkirjautuminen onnistui!");
        } else {
            System.out.println("Virheellinen käyttäjätunnus tai salasana.");
        }
    }
    
    private void logoutUser() {
        if (service.getLoggedUser() != null) {
            service.logout();
            System.out.println("Kirjauduit ulos");
        } else {
            System.out.println("Et ole kirjautunut sisään.");
        }
    }

    private void createCourse() { 
        if (isUserLoggedIn()) {
            int compulsory = 0;
            System.out.print("Nimi: ");
            String name = scanner.nextLine();
            while (true) {
                System.out.print("Onko kurssi pakollinen (1) vai ei (0)? Syötä 1 tai 0: ");
                String compulsoryString = scanner.nextLine();
                if (compulsoryString.equals("1") || compulsoryString.equals("0")) {
                    compulsory = Integer.parseInt(compulsoryString);
                    break;
                }
            }
            System.out.print("Opintopisteet: ");
            int points = Integer.parseInt(scanner.nextLine());
            service.createNewCourse(name, compulsory, points);
        }
    }
    
    private void listCoursesByUser() {
        if (isUserLoggedIn()) {
            printCourses();
            System.out.println("");
            System.out.println("Suoritettuja opintoja yhteensä " + service.getPointsSum() + " op.");
        }
    }
    
    private void deleteCourse() {
        if (isUserLoggedIn()) {
            listCoursesByUser();
            System.out.println("");
            System.out.print("Anna poistettavan kurssin id: ");
            int id = Integer.parseInt(scanner.nextLine());
            boolean success = service.deleteCourse(id);
            if (success) {
                System.out.println("Kurssi poistettu.");
            } else {
                System.out.println("Poistaminen epäonnistui.");
            }
        }
    }
    
    private void updateCourse() {
        if (isUserLoggedIn()) {
            listCoursesByUser();
            System.out.println("");
            System.out.print("Anna päivitettävän kurssin id (0 peruuta): ");
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) {
                start();
            }
            System.out.println("1 päivitä kurssin nimi");
            System.out.println("2 päivitä kurssin suoritustieto");
            System.out.println("3 päivitä kurssin pakollisuustieto");
            System.out.println("4 päivitä kurssin opintopisteet");
            System.out.println("x peruuta");
            
            while (true) {
                System.out.println("");
                System.out.print("Valitse toiminto: ");
                String command = scanner.nextLine();
                if (command.equals("x")) {
                    break;
                } else if (command.equals("1")) {
                    updateCourseName(id);
                } else if (command.equals("2")) {
                    updateCourseDone(id);
                } else if (command.equals("3")) {
                    updateCourseCompulsory(id);
                } else if (command.equals("4")) {
                    updateCoursePoints(id);
                } else {
                    System.out.println("Virheellinen valinta.");
                }
            }
        }
    }
    
    private void updateCourseDone(int id) {
        boolean success = service.updateDone(id);
        int done = 0;
        for (Course course : service.getCourses()) {
            if (course.getId() == id) {
                done = course.getId();
            }
        }
        if (success) {
            if (done == 1) {
                System.out.println("Kurssi päivitetty suoritetuksi");
            } else {
                System.out.println("Kurssi päivitetty suorittamattomaksi");
            }
        } else {
            System.out.println("Päivittäminen epäonnistui.");
        }
    }
    
    private void updateCourseName(int id) {
        System.out.print("Anna kurssin uusi nimi: ");
        String name = scanner.nextLine();
        boolean success = service.updateCourseName(id, name);
        if (success) {
            System.out.println("Kurssin nimi päivitetty.");
        } else {
            System.out.println("Päivittäminen epäonnistui.");
        }
    }
    
    private void updateCourseCompulsory(int id) {
        int compulsory = 0;
        for (Course course: service.getCourses()) {
            if (course.getId() == id) {
                compulsory = course.getCompulsory();
            }
        }
        boolean success = service.updateCourseCompulsory(id);
        if (success) {
            if (compulsory == 1) {
                System.out.println("Kurssi päivitetty pakolliseksi");
            } else {
                System.out.println("Kurssi päivitetty valinnaiseksi");
            }
        } else {
            System.out.println("Päivittäminen epäonnistui");
        }
    }
    
    private void updateCoursePoints(int id) {
        System.out.print("Anna kurssin uudet opintopisteet: ");
        int points = Integer.parseInt(scanner.nextLine());
        boolean success = service.updateCoursePoints(id, points);
        if (success) {
            System.out.println("Kurssin opintopisteet päivitetty.");
        } else {
            System.out.println("Päivittäminen epäonnistui.");
        }
    }
    
    private boolean isUserLoggedIn() {
        if (service.getLoggedUser() == null) {
            System.out.println("Kirjaudu ensin sisään");
            return false;
        }
        return true;
    }
    
    private void printCourses() {
        if (service.getCourses().isEmpty()) {
            System.out.println("Ei kursseja.");
            return;
        }
        for (Course course: service.getCourses()) {
            System.out.print("id: " + course.getId() + ", nimi: " + course.getName() + ", " + course.getPoints()+ " op, ");
            if (course.getCompulsory() == 1) {
                System.out.print("pakollinen, ");
            } else {
                System.out.print("valinnainen, ");
            }
            if (course.getDone() == 1) {
                System.out.println("suoritettu");
            } else {
                System.out.println("suorittamatta");
            }
        }
    }
}
