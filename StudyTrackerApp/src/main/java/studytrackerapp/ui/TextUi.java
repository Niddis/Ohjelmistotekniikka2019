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
        System.out.println("x lopeta");
        
        while (true) {
            System.out.println("");
            System.out.print("komento: ");
            String command = scanner.nextLine();
            
            if (command.equals("x")) {
                break;
            } else if (command.equals("1")) {
                createUser();
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
    
}
