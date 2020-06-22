package ir.mapsa.eLibrary.service;

import ir.mapsa.eLibrary.DatabaseManager;
import ir.mapsa.eLibrary.helper.AlertEnum;
import ir.mapsa.eLibrary.helper.AlertResult;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    public AlertResult loginAdmin(String email, String password) {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        boolean isLogin = databaseManager.loginAdmin(email, password);
        AlertResult alertResult;
        if(isLogin){
            alertResult = new AlertResult(AlertEnum.SUCCESS, "");
        }else{
            alertResult = new AlertResult(AlertEnum.ERROR, "Invalid username and password");
        }
        return alertResult;
    }

    public AlertResult loginLibrarian(String email, String password) {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        boolean isLogin = databaseManager.loginLibrarian(email, password);
        AlertResult alertResult;
        if(isLogin){
            alertResult = new AlertResult(AlertEnum.SUCCESS, "");
        }else{
            alertResult = new AlertResult(AlertEnum.ERROR, "Invalid username and password");
        }
        return alertResult;
    }

}