package ir.mapsa.eLibrary.service;

import ir.mapsa.eLibrary.DatabaseManager;
import ir.mapsa.eLibrary.helper.AlertEnum;
import ir.mapsa.eLibrary.helper.AlertResult;
import ir.mapsa.eLibrary.model.Book;
import ir.mapsa.eLibrary.model.IssueBook;
import ir.mapsa.eLibrary.model.Librarian;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookService {
    public AlertResult addBook(Book book) {
        String errorTxt = book.getValidationErrorTxt();
        if (errorTxt != null) {
            return new AlertResult(AlertEnum.ERROR, errorTxt);
        }

        DatabaseManager manager = DatabaseManager.getInstance();
        Book lastBook = manager.hasThisBook(book);
        if (lastBook != null) {
            int newQuantity = lastBook.getQuantity() + book.getQuantity();
            book.setQuantity(newQuantity);
            manager.updateBookQuantity(lastBook.getId(), newQuantity);
            return new AlertResult(AlertEnum.SUCCESS,
                    "This book has already been saved, so added quantity to that");
        }

        manager.addBook(book);
        return new AlertResult(AlertEnum.SUCCESS, "Book added");
    }

    public ArrayList<Book> getAllBooks() {
        return DatabaseManager.getInstance().getAllBooks();
    }

    public boolean isAdmin() {
        return DatabaseManager.getInstance().isAdmin();
    }

    public void deleteBook(ObjectId id) {
        DatabaseManager.getInstance().deleteBook(id);
    }

    public AlertResult issueBook(String callno) {
        String errorTxt = IssueBook.getValidationErrorTxt(callno);
        if(errorTxt != null){
            return new AlertResult(AlertEnum.ERROR, errorTxt);
        }

        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Book book = databaseManager.findBookByCallno(callno);
        if(book == null){
            return new AlertResult(AlertEnum.ERROR, "Does't exist this book");
        }

        int quantity = book.getQuantity();
        if(quantity == 0){
            return new AlertResult(AlertEnum.ERROR, "The quantity of this book was finished");
        }

        ArrayList<IssueBook> myIssuedBooks = databaseManager.getMyIssuedBooks();
        IssueBook issuedBook = myIssuedBooks.stream().filter(
                value -> value.getCallno().equals(callno) && !value.isReturnStatus()).findFirst().orElse(null);
        if(issuedBook != null){
            return new AlertResult(AlertEnum.ERROR, "This book has already been issued");
        }

        int newQuantity = quantity-1;
        int newIssuedNo = book.getIssuedNo()+1;
        book.setQuantity(newQuantity);
        book.setIssuedNo(newIssuedNo);
        databaseManager.updateBookAfterIssue(book.getId(), newQuantity, newIssuedNo);
        databaseManager.addBookToLibrarianIssuedBooks(callno);
        return new AlertResult(AlertEnum.SUCCESS, "This book issued");
    }

    public ArrayList<IssueBook> getMyIssuedBooks(){
        return DatabaseManager.getInstance().getMyIssuedBooks();
    }

    public void returnBook(String callno) {
        DatabaseManager.getInstance().returnBook(callno);
    }

    public ArrayList<Librarian> getLibrarianWhoIssuedBook(){
        return DatabaseManager.getInstance().getLibrarianWhoIssuedBook();
    }

}