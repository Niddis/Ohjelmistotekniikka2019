package studytrackerapp.ui;

import java.util.Scanner;
import studytrackerapp.database.Database;
import studytrackerapp.domain.Service;

public class TextUi {
    private Scanner scanner;
    private Service service;
    private Database database;

    public TextUi(Scanner scanner, Database database) {
        this.scanner = scanner;
        this.database = database;
        this.service = new Service(database);
    }
    
    public void start() {
        System.out.println("Opintojen seurantasovellus");
        System.out.println("");
        System.out.println("1 lisää käyttäjä");
        System.out.println("2 kirjaudu sisään");
        System.out.println("3 kirjaudu ulos");
        System.out.println("4 lisää kurssi");
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
        service.createNewUser(0, name, username, password);
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
        }
        System.out.println("Et ole kirjautunut sisään.");
    }

    private void createCourse() {
        
        if (service.getLoggedUser() == null) {
            System.out.println("Kirjaudu sisään luodaksesi uuden kurssin.");
        } else {
            System.out.print("Nimi: ");
            String name = scanner.nextLine();
            System.out.print("Onko kurssi pakollinen (1) vai ei (0)? Syötä 1 tai 0: ");
            int compulsory = Integer.parseInt(scanner.nextLine());
            System.out.print("Opintopisteet: ");
            int points = Integer.parseInt(scanner.nextLine());
            service.createNewCourse(0, name, compulsory, points);
        }
    }
}
