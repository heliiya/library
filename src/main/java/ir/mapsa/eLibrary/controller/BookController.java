package ir.mapsa.eLibrary.controller;

import ir.mapsa.eLibrary.helper.AlertResult;
import ir.mapsa.eLibrary.model.Book;
import ir.mapsa.eLibrary.service.BookService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/addBook")
    public String showAddBookForm(Book book) {
        return "addBook";
    }

    @PostMapping("/addBook")
    public ModelAndView addBook(Book book) {
        ModelAndView mav = new ModelAndView();
        AlertResult alertResult = bookService.addBook(book);
        mav.addObject(alertResult.getAlertEnum().getName(), alertResult.getAlertTxt());
        mav.setViewName("addBook");
        return mav;
    }

    @GetMapping("/viewBook")
    public String showBookTable(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("isAdmin", bookService.isAdmin());
        return "viewBook";
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") ObjectId id) {
        bookService.deleteBook(id);
        return "redirect:/viewBook";
    }

    @GetMapping("/issueBook")
    public String showIssueBookForm() {
        return "issueBook";
    }

    @PostMapping("/issueBook")
    public ModelAndView issueBook(@RequestParam("callno") String callno) {
        ModelAndView mav = new ModelAndView();
        AlertResult alertResult = bookService.issueBook(callno);
        mav.addObject(alertResult.getAlertEnum().getName(), alertResult.getAlertTxt());
        mav.setViewName("issueBook");
        return mav;
    }

    @GetMapping("/viewMyIssuedBook")
    public String showMyIssuedBookTable(Model model) {
        model.addAttribute("issuedBooks", bookService.getMyIssuedBooks());
        return "viewMyIssuedBook";
    }

    @GetMapping("/returnBook")
    public String returnBook(@RequestParam("callno") String callno) {
        bookService.returnBook(callno);
        return "redirect:/viewMyIssuedBook";
    }

    @GetMapping("/viewIssuedBook")
    public String showIssuedBookTable(Model model) {
        model.addAttribute("librarians", bookService.getLibrarianWhoIssuedBook());
        return "viewIssuedBook";
    }

}