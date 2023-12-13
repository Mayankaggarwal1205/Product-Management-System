package com.smartContactManager.controller;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import com.smartContactManager.helper.Message;
import com.smartContactManager.repo.UserRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Getter
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About");
        return "about";
    }

    @GetMapping("/signup/")
    public String signup(Model model){
        model.addAttribute("title","Register");
        model.addAttribute("user", new User());
        return "register";
    }

    // this handler for registering user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                               @RequestParam(value = "agreement",defaultValue = "false")
                               boolean agreement, Model model, HttpSession session){
//        binding result will contain all values of validation on which we have applied validation
        try{
            if(!agreement){
                System.out.println("You are not agreed with agreement");
                throw new Exception("You haven't agreed with agreement!!");
            }

            if(result.hasErrors()){
                System.out.println("Erorr " + result.toString());
                model.addAttribute("user", user);
                return "register";
            }

            user.setEnabled(true);
            user.setRole("ROLE_USER");
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User save = this.userRepo.save(user);

            System.out.println("Agreement " + agreement);
            System.out.println("User" + save);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("User successfully registered!!",
                    "alert-success"));
            return "register";
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong!!",
                    "alert-danger"));
            return "register";
        }

    }

//    handler for custom login page
    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title","Login Page");
        return "login";
    }
}
