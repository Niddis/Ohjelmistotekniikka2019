package studytrackerapp;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;
import studytrackerapp.database.Database;
import studytrackerapp.database.SqlCourseDao;
import studytrackerapp.database.SqlUserDao;
import studytrackerapp.ui.TextUi;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        String databaseAddress = properties.getProperty("databaseAddress");
        
        Database database = new Database(databaseAddress);
        SqlUserDao userdao = new SqlUserDao(database);
        SqlCourseDao coursedao = new SqlCourseDao(database);
        database.init();
        
        TextUi ui = new TextUi(scanner, userdao, coursedao);
        ui.start();
    } 
}
