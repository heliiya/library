package ir.mapsa.eLibrary.controller;

import ir.mapsa.eLibrary.helper.AlertResult;
import ir.mapsa.eLibrary.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/")
    public String showLoginPage(){
        return "loginPage";
    }

    @GetMapping("/adminHome")
    public String showAdminHomePage(){
        return "adminHome";
    }

    @PostMapping("/adminHome")
    public ModelAndView loginAdmin(@RequestParam("adminEmail") String adminEmail,
                                   @RequestParam("adminPassword") String adminPassword) {
        ModelAndView mav = new ModelAndView();
        AlertResult result = homeService.loginAdmin(adminEmail, adminPassword);
        switch(result.getAlertEnum()){
            case ERROR:
            default:
                mav.setViewName("loginPage");
                mav.addObject("adminError", result.getAlertTxt());
                break;
            case SUCCESS:
                mav.setViewName("adminHome");
                break;
        }
        return mav;
    }

    @GetMapping("/librarianHome")
    public String showLibrarianHomePage(){
        return "librarianHome";
    }

    @PostMapping("/librarianHome")
    public ModelAndView loginLibrarian(@RequestParam("librarianEmail") String librarianEmail,
                                       @RequestParam("librarianPassword") String librarianPassword) {
        ModelAndView mav = new ModelAndView();
        AlertResult result = homeService.loginLibrarian(librarianEmail, librarianPassword);
        switch(result.getAlertEnum()){
            case ERROR:
            default:
                mav.setViewName("loginPage");
                mav.addObject("librarianError", result.getAlertTxt());
                break;
            case SUCCESS:
                mav.setViewName("librarianHome");
                break;
        }
        return mav;
    }
}