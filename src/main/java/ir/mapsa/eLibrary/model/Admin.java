package ir.mapsa.eLibrary.model;

import org.bson.Document;

public class Admin {
    private String email;
    private String password;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Document generateDocument(){
        Document doc = new Document();
        doc.append("email", email);
        doc.append("password", password);
        return doc;
    }
}
