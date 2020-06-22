package ir.mapsa.eLibrary.service;

import ir.mapsa.eLibrary.DatabaseManager;
import ir.mapsa.eLibrary.helper.AlertEnum;
import ir.mapsa.eLibrary.helper.AlertResult;
import ir.mapsa.eLibrary.model.Librarian;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LibrarianService {

    public AlertResult addLibrarian(Librarian librarian) {
        String errorTxt = librarian.getValidationErrorTxt();
        if (errorTxt != null){
            return new AlertResult(AlertEnum.ERROR, errorTxt);
        }

        DatabaseManager manager = DatabaseManager.getInstance();
        if(manager.hasThisLibrarian(librarian)){
            return new AlertResult(AlertEnum.ERROR, "This librarian has already been added");
        }

        manager.addLibrarian(librarian);
        return new AlertResult(AlertEnum.SUCCESS, "Librarian added");
    }

    public ArrayList<Librarian> getAllLibrarians() {
        DatabaseManager manager = DatabaseManager.getInstance();
        return manager.getAllLibrarians();
    }

    public Librarian findLibrarianById(ObjectId id) {
        DatabaseManager manager = DatabaseManager.getInstance();
        return manager.findLibrarianById(id);
    }

    public AlertResult editLibrarian(Librarian librarian) {
        String errorTxt = librarian.getValidationErrorTxt();
        if (errorTxt != null){
            return new AlertResult(AlertEnum.ERROR, errorTxt);
        }
        DatabaseManager manager = DatabaseManager.getInstance();
        manager.updateLibrarian(librarian);
        return new AlertResult(AlertEnum.SUCCESS, "Librarian updated");
    }

    public void deleteLibrarian(ObjectId id) {
        DatabaseManager.getInstance().deleteLibrarian(id);
    }
}