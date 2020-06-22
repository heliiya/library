package ir.mapsa.eLibrary.controller;

import ir.mapsa.eLibrary.helper.AlertResult;
import ir.mapsa.eLibrary.model.Librarian;
import ir.mapsa.eLibrary.service.LibrarianService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LibrarianController {

    @Autowired
    LibrarianService librarianService;

    @GetMapping("/addLibrarian")
    public String showAddLibrarianForm(Librarian librarian) {
        return "addLibrarian";
    }

    @PostMapping("/addLibrarian")
    public ModelAndView addLibrarian(Librarian librarian) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("addLibrarian");
        AlertResult result = librarianService.addLibrarian(librarian);
        mav.addObject(result.getAlertEnum().getName(), result.getAlertTxt());
        return mav;
    }

    @GetMapping("/viewLibrarian")
    public String showLibrarianTable(Model model) {
        model.addAttribute("librarians", librarianService.getAllLibrarians());
        return "viewLibrarian";
    }

    @GetMapping("/editLibrarian")
    public ModelAndView showEditLibrarianForm(@RequestParam("id") ObjectId id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("librarian", librarianService.findLibrarianById(id));
        mav.setViewName("editLibrarian");
        return mav;
    }

    @PostMapping("/editLibrarian")
    public ModelAndView editLibrarian(Librarian librarian) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("editLibrarian");
        AlertResult alertResult = librarianService.editLibrarian(librarian);
        mav.addObject(alertResult.getAlertEnum().getName(), alertResult.getAlertTxt());
        return mav;
    }

    @GetMapping("/deleteLibrarian")
    public String deleteLibrarian(@RequestParam("id") ObjectId id) {
        librarianService.deleteLibrarian(id);
        return "redirect:/viewLibrarian";
    }

}