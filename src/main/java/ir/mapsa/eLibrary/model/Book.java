package ir.mapsa.eLibrary.model;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Book {
    private ObjectId id;
    private String callno;
    private String name;
    private String author;
    private String publisher;
    private int quantity;
    private int issuedNo;

    public Book() {
    }

    public Book(Document doc) {
        id = doc.getObjectId("_id");
        callno = doc.getString("callno");
        name = doc.getString("name");
        author = doc.getString("author");
        publisher = doc.getString("publisher");
        quantity = doc.getInteger("quantity");
        issuedNo = doc.getInteger("issuedNo");
    }


    /**
     *
     * maybe admin delete book and after that, librarian return that book,
     * so we should add this book while we just have callno of that book.
     *
     */
    public Book(String callno) {
        this.callno = callno;
        name = "Unknown";
        author = "Unknown";
        publisher = "Unknown";
        quantity = 1;
        issuedNo = 0;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCallno() {
        return callno;
    }

    public void setCallno(String callno) {
        this.callno = callno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIssuedNo() {
        return issuedNo;
    }

    public void setIssuedNo(int issuedNo) {
        this.issuedNo = issuedNo;
    }

    public String getValidationErrorTxt(){
        if(isInvalid(callno))
            return "Invalid callno";

        if(isInvalid(name))
            return "Invalid name";

        if(isInvalid(author))
            return "Invalid author";

        if(isInvalid(publisher))
            return "Invalid publisher";

        if(quantity <= 0 || quantity > 10000)
            return "Quantity must be between 1 and 10000";

        if(issuedNo < 0 || issuedNo > quantity)
            return "IssuedNo must be between 0 and quantity";

        return null;
    }

    private boolean isInvalid(String text){
        return text == null || "null".equals(text) || text.trim().isEmpty() || text.length()>20;
    }

    public Document generateDocument(){
        Document doc = new Document();
        doc.append("callno", callno);
        doc.append("name", name);
        doc.append("author", author);
        doc.append("publisher", publisher);
        doc.append("quantity", quantity);
        doc.append("issuedNo", issuedNo);
        return doc;
    }

}