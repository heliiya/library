package ir.mapsa.eLibrary.model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class Librarian {
    private ObjectId id;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private List<IssuedBook> issuedBooks;

    public Librarian() {
    }

    public Librarian(Document doc) {
        id = doc.getObjectId("_id");
        name = doc.getString("name");
        email = doc.getString("email");
        password = doc.getString("password");
        mobile = doc.getString("mobile");
        issuedBooks = doc.get("issuedBooks", List.class);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<IssuedBook> getIssuedBooks() {
        return issuedBooks;
    }

    public void setIssuedBooks(List<IssuedBook> issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

    public String getValidationErrorTxt(){
        if(name == null || "null".equals(name) || name.trim().isEmpty() || name.length()>20)
            return "Invalid name";
        if(email == null || !email.matches("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b"))
            return "Invalid email";
        if(password == null || !password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,20}$"))
            return "Password must be between 4 and 20 characters, " +
                    "and include at least one upper case and lower case letter, and one numeric digit.";
        if(mobile == null || !mobile.matches("09[0-9]{9}"))
            return "Invalid mobile number";

        return null;
    }

    public Document generateDocument(){
        Document doc = new Document();
        doc.append("name", name);
        doc.append("email", email);
        doc.append("password", password);
        doc.append("mobile", mobile);
        doc.append("issuedBooks", issuedBooks);
        return doc;
    }
}