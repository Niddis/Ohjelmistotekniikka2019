package studytrackerapp.ui;

import java.util.Scanner;
import studytrackerapp.database.CourseDao;
import studytrackerapp.database.UserDao;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.Service;
/**
 * Luokka sisältää sovelluksen tekstikäyttöliittymän.
 */
public class TextUi {
    private Scanner scanner;
    private Service service;

    public TextUi(Scanner scanner, UserDao userdao, CourseDao coursedao) {
        this.scanner = scanner;
        this.service = new Service(userdao, coursedao);
    }
    
    /**
     * Metodi tulostaa sovelluksen käyttöohjeen ja kysyy seuraavaa komentoa.
     * Komentoa kysytään niin kauan kuin sovellus on käynnissä.
     */
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
    
    /**
     * Metodi pyytää uuden käyttäjän nimeä, käyttäjätunnusta ja salasanaa. 
     * Tiedot välitetään sovelluslogiin metodille parametreina ja käyttäjälle 
     * kerrotaan (epä)onnistuneesta käyttäjän luomisesta.
     */
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
    
    /**
     * Metodi pyytää käyttäjätunnusta ja salasanaa. Tiedot välitetään sovelluslogiikan 
     * metodille parametreina ja (epä)onnistuneesta kirjautumisesta kerrotaan 
     * käyttäjälle.
     */
    private void loginUser() {
        System.out.print("Käyttäjätunnus: ");
        String username = scanner.nextLine();
        System.out.print("Salasana: ");
        String password = scanner.nextLine();
        boolean success = service.login(username, password);
        if (success) {
            System.out.println("Sisäänkirjautuminen onnistui!");
            service.listCoursesByUser();
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
    
    /**
     * Metodi pyytää uuden kurssin nimeä, pakollisuustietoa ja opintopisteitä.
     * Tiedot välitetään sovelluslogiikan metodille parametreina ja 
     * (epä)onnistuneesta kurssin lisäämisestä kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     */
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
            int points = 0;
            while (true) {
                System.out.print("Opintopisteet: ");
                String pointsString = scanner.nextLine();
                if (isInputAnInteger(pointsString)) {
                    points = Integer.parseInt(pointsString);
                    break;
                } else {
                    System.out.println("Anna opintopisteet kokonaislukuna.");
                }
            }
            
            boolean success = service.createNewCourse(name, compulsory, points);
            if (success) {
                System.out.println("Kurssi lisätty onnistuneesti.");
            } else {
                System.out.println("Tapahtui virhe. kurssin lisääminen epäonnistui.");
            }
        }
    }
    
    /**
     * Metodi tulostaa näytölle kirjautuneen käyttäjän kaikki kurssit ja kertoo
     * suoritettujen kurssien opintopisteiden yhteismäärän.
     */
    private void listCoursesByUser() {
        if (isUserLoggedIn()) {
            printCourses();
            System.out.println("");
            System.out.println("Suoritettuja opintoja yhteensä " + service.getPointsSum() + " op.");
        }
    }
    
    /**
     * Metodi pyytää poistettavan kurssin id:tä.
     * Tieto välitetään sovelluslogiikan metodille parametrina ja 
     * (epä)onnistuneesta kirjautumisesta kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     */
    private void deleteCourse() {
        if (isUserLoggedIn()) {
            listCoursesByUser();
            System.out.println(""); 
            int id = 0;
            while (true) {
                System.out.print("Anna poistettavan kurssin id: ");
                String idString = scanner.nextLine();
                if (isInputAnInteger(idString)) {
                    id = Integer.parseInt(idString);
                    break;
                } else {
                    System.out.println("Anna id kokonaislukuna.");
                }
            }
            boolean success = service.deleteCourse(id);
            if (success) {
                System.out.println("Kurssi poistettu.");
            } else {
                System.out.println("Poistaminen epäonnistui.");
            }
        }
    }
    
    /**
     * Metodi pyytää päivitettävän kurssin id:tä, tulostaa käyttöohjeen ja 
     * kysyy haluttua muokkaustoimenpidettä. 
     */
    private void updateCourse() {
        if (isUserLoggedIn()) {
            listCoursesByUser();
            System.out.println("");
            int id = 0;
            while (true) {
                System.out.print("Anna päivitettävän kurssin id: ");
                String idString = scanner.nextLine();
                if (isInputAnInteger(idString)) {
                    id = Integer.parseInt(idString);
                    break;
                } else {
                    System.out.println("Anna id kokonaislukuna.");
                }
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
    
    /**
     * Metodi pyytää sen kurssin id:tä, jonka suoritustietoa halutaan muuttaa.
     * Tieto välitetään sovelluslogiikan metodille parametrina ja 
     * (epä)onnistuneesta muutoksesta kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     * @param id kurssin id
     */
    private void updateCourseDone(int id) {
        boolean success = service.updateDone(id);
        int done = 0;
        for (Course course : service.getCourses()) {
            if (course.getId() == id) {
                done = course.getDone();
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
    
    /**
     * Metodi pyytää sen kurssin id:tä, jonka nimeä halutaan muuttaa.
     * Tieto välitetään sovelluslogiikan metodille parametrina ja 
     * (epä)onnistuneesta muutoksesta kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     * @param id kurssin id
     */
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
    
    /**
     * Metodi pyytää sen kurssin id:tä, jonka pakollisuustietoa halutaan muuttaa.
     * Tieto välitetään sovelluslogiikan metodille parametrina ja 
     * (epä)onnistuneesta muutoksesta kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     * @param id kurssin id
     */
    private void updateCourseCompulsory(int id) {
        boolean success = service.updateCourseCompulsory(id);
        int compulsory = 0;
        for (Course course: service.getCourses()) {
            if (course.getId() == id) {
                compulsory = course.getCompulsory();
            }
        }
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
    
    /**
     * Metodi pyytää sen kurssin id:tä, jonka opintopisteitä halutaan muuttaa.
     * Tieto välitetään sovelluslogiikan metodille parametrina ja 
     * (epä)onnistuneesta muutoksesta kerrotaan käyttäjälle. Metodi huolehtii myös 
     * käyttäjän antaman syötteen validoinnista.
     * @param id kurssin id
     */
    private void updateCoursePoints(int id) {
            int points = 0;
            while (true) {
                System.out.print("Anna kurssin uudet opintopisteet: ");
                String pointsString = scanner.nextLine();
                if (isInputAnInteger(pointsString)) {
                    points = Integer.parseInt(pointsString);
                    break;
                } else {
                    System.out.println("Anna opintopisteet kokonaislukuna.");
                }
            }
        boolean success = service.updateCoursePoints(id, points);
        if (success) {
            System.out.println("Kurssin opintopisteet päivitetty.");
        } else {
            System.out.println("Päivittäminen epäonnistui.");
        }
    }
    
    /**
     * Metodi kertoo, onko käyttäjä kirjautunut sisään.
     * @return true, jos loggedUser ei ole null, muuten false
     */
    private boolean isUserLoggedIn() {
        if (service.getLoggedUser() == null) {
            System.out.println("Kirjaudu ensin sisään");
            return false;
        }
        return true;
    }
    
    /**
     * Metodi luo Course -olioita sisältävästä listasta merkkijonoja ja tulostaa 
     * ne näytölle.
     */
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
    
    /**
     * Metodi tarkistaa, voiko annetun merkkijonon tulkita kokonaislukuna.
     * @param string merkkijono
     * @return true, jos merkkijono voidaan tulkita kokonaislukuna, muuten false
     */
    private boolean isInputAnInteger(String string) {
        Boolean isValid = false;
        if (!(string == null || string.trim().length() == 0)) {
            try {
                int points = Integer.parseInt(string);
                isValid = true;
            } catch (NumberFormatException e) {
                
            }
        }
        return isValid;
    }
}
