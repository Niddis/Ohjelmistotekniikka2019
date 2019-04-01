package studytrackerapp;

import java.sql.*;
import java.util.Scanner;
import studytrackerapp.database.Database;
import studytrackerapp.ui.TextUi;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("hello");
        Scanner scanner = new Scanner(System.in);
        
        String databaseAddress = "testi";
        Database database = new Database(databaseAddress);
        
        TextUi ui = new TextUi(scanner, database);
        ui.start();
    } 
}
