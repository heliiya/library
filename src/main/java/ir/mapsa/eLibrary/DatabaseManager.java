package ir.mapsa.eLibrary;

import com.mongodb.client.*;
import ir.mapsa.eLibrary.model.Admin;
import ir.mapsa.eLibrary.model.Book;
import ir.mapsa.eLibrary.model.IssuedBook;
import ir.mapsa.eLibrary.model.Librarian;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class DatabaseManager {

    private static DatabaseManager instance;
    private MongoCollection<Document> adminCollection;
    private MongoCollection<Document> librarianCollection;
    private MongoCollection<Document> bookCollection;
    private static ObjectId currentUserId;

    private DatabaseManager(){
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("library");
        adminCollection = database.getCollection("admin");
        librarianCollection = database.getCollection("librarian");
        bookCollection = database.getCollection("book");
    }

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void addAdmin(String email, String password){
        if(adminCollection.find().first() == null){
            Admin admin = new Admin(email, password);
            adminCollection.insertOne(admin.generateDocument());
        }
    }

    public boolean loginAdmin(String email, String password){
        Document document = adminCollection.find(
                and(eq("email", email), eq("password", password))).first();
        if(document != null){
            currentUserId = document.getObjectId("_id");
            return true;
        }
        return false;
    }

    public boolean loginLibrarian(String email, String password){
        Document doc = librarianCollection.find(and(
                eq("email", email),
                eq("password", password))).first();
        if(doc != null){
            currentUserId = doc.getObjectId("_id");
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        if(adminCollection.find(eq("_id", currentUserId)).first() != null)
            return true;

        return false;
    }

    public boolean hasThisLibrarian(Librarian librarian){
        if(librarianCollection.find(or(
                eq("email", librarian.getEmail()),
                eq("mobile", librarian.getMobile()))).first() != null){
            return true;
        }
        return false;
    }

    public void addLibrarian(Librarian librarian){
        librarianCollection.insertOne(librarian.generateDocument());
    }

    public ArrayList<Librarian> getAllLibrarians(){
        ArrayList<Librarian> librarians = new ArrayList<>();
        MongoCursor<Document> cursor = librarianCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                librarians.add(new Librarian(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return librarians;
    }

    public Librarian findLibrarianById(ObjectId id){
        Document doc = librarianCollection.find(eq("_id", id)).first();
        return new Librarian(doc);
    }

    public void updateLibrarian(Librarian librarian){
        librarianCollection.updateOne(eq("_id", librarian.getId()),
                new Document("$set", librarian.generateDocument()));
    }

    public void deleteLibrarian(ObjectId id){
        librarianCollection.deleteOne(new Document("_id", id));
    }

    public Book hasThisBook(Book book){
        Document doc = bookCollection.find(and(
                eq("callno", book.getCallno()),
                eq("name", book.getName()),
                eq("author", book.getAuthor()),
                eq("publisher", book.getPublisher()))).first();
        if(doc != null){
            return new Book(doc);
        }
        return null;
    }

    public void updateBookQuantity(ObjectId bookId, int newQuantity){
        bookCollection.updateOne(eq("_id", bookId),
                new Document("$set", new Document("quantity", newQuantity)));
    }

    public void addBook(Book book){
        bookCollection.insertOne(book.generateDocument());
    }

    public ArrayList<Book> getAllBooks(){
        ArrayList<Book> books = new ArrayList<>();
        MongoCursor<Document> cursor = bookCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                books.add(new Book(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return books;
    }

    public void deleteBook(ObjectId id){
        bookCollection.deleteOne(new Document("_id", id));
    }

    public Book findBookByCallno(String callno){
        Document doc = bookCollection.find(eq("callno", callno)).first();
        if(doc != null){
            return new Book(doc);
        }
        return null;
    }

    public void updateBookAfterIssue(ObjectId id, int newQuantity, int newIssuedNo){
        bookCollection.updateOne(eq("_id", id),
                new Document("$set", new Document("quantity", newQuantity).append("issuedNo", newIssuedNo)));
    }

    public void addBookToLibrarianIssuedBooks(String callno){
        Document librarianDoc = librarianCollection.find(eq("_id", currentUserId)).first();
        List<Document> documents = librarianDoc.get("issuedBooks",  List.class);
        if(documents == null){
            documents = new ArrayList<>();
        }
        IssuedBook issuedBook = new IssuedBook(callno);
        documents.add(issuedBook.generateDocument());
        librarianCollection.updateOne(eq("_id", currentUserId),
                new Document("$set", new Document("issuedBooks", documents)));
    }

    public ArrayList<IssuedBook> getMyIssuedBooks(){
        ArrayList<IssuedBook> myIssuedBooks = new ArrayList<>();

        Document librarianDoc = librarianCollection.find(eq("_id", currentUserId)).first();
        List<Document> issuedBookDocs = librarianDoc.get("issuedBooks", List.class);
        if(issuedBookDocs != null && !issuedBookDocs.isEmpty()){
            for (Document issuedBookDoc : issuedBookDocs) {
                myIssuedBooks.add(new IssuedBook(issuedBookDoc));
            }
        }
        return myIssuedBooks;
    }

    public ArrayList<Librarian> getLibrarianWhoIssuedBook(){
        ArrayList<Librarian> librarians = new ArrayList<>();
        MongoCursor<Document> cursor = librarianCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                List issuedBooks = document.get("issuedBooks", List.class);
                if(issuedBooks != null && !issuedBooks.isEmpty()){
                    Librarian librarian = new Librarian(document);
                    librarians.add(librarian);
                }
            }
        } finally {
            cursor.close();
        }
        return librarians;
    }

    public void returnBook(String callno){
        updateIssuedBooksOfLibrarian(callno);
        updateOrAddBookReturned(callno);
    }

    private void updateIssuedBooksOfLibrarian(String callno) {
        Document librarianDoc = librarianCollection.find(eq("_id", currentUserId)).first();
        List<Document> documents = librarianDoc.get("issuedBooks",  List.class);
        Document document = documents.stream().filter(
                value -> value.getString("callno").equals(callno) &&
                        !value.getBoolean("returnStatus")).findFirst().get();
        document.replace("returnStatus", true);
        librarianCollection.updateOne(eq("_id", currentUserId),
                new Document("$set", new Document("issuedBooks", documents)));

    }

    private void updateOrAddBookReturned(String callno) {
        Document bookDoc = bookCollection.find(eq("callno", callno)).first();
        if(bookDoc != null){
            bookCollection.updateOne(eq("_id", bookDoc.getObjectId("_id")),
                    new Document("$set", new Document("issuedNo",
                            bookDoc.getInteger("issuedNo")-1)
                            .append("quantity", bookDoc.getInteger("quantity")+1)));
        }else{
            addBook(new Book(callno));
        }
    }
}