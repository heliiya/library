package ir.mapsa.eLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        databaseManager.addAdmin("admin@gmail.com", "!Admin123!");
    }

}