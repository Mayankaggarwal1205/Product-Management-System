package com.smartContactManager.controller;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import com.smartContactManager.helper.Message;
import com.smartContactManager.repo.ContactRepo;
import com.smartContactManager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ContactRepo contactRepo;

    // method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);
        model.addAttribute("user",user);
    }


    // home dashboard
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title","User Dashboard");
        return "user_dashboard";
    }

    // open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title","Add Product");
        model.addAttribute("contact", new Contact());
        return "add_contact_form";
    }

    // processing add contact handler
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact,
            @RequestParam("imageUrl")MultipartFile file,
            HttpSession session, Principal principal, Model model){

        try{
            String name = principal.getName();
            User user = this.userRepo.getUserByUsername(name);

            // processing and uploading file
            if(file.isEmpty()){
                // if file is empty then message
                System.out.println("File is empty");
                contact.setImage("contact.png");
            } else{
                // upload file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());
                File savefile = new ClassPathResource("static").getFile();
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded!");
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepo.save(user);
            System.out.println(contact);

            // success message
            session.setAttribute("message",new Message("Your product is added successfully","success"));

        } catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();

            // error message
            session.setAttribute("messgae",new Message("Something went wrong!! Try again...","danger"));
        }
        return "add_contact_form";
    }

    // show contacts handler
    // per page only 5 contacts
    // current page = 0th page

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal){
        model.addAttribute("title","View-Products");

        // contact ki list ko bhjna h

        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);
//        List<Contact> contacts = user.getContacts();

        // 1. current page --> that we have to pass
        // 2. no of contants per page --> that we have to pass
        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Contact> contacts = this.contactRepo.findContactByUser(user.getId(), pageRequest);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());

        return "show_contacts";
    }

    // showing particular contact
    @GetMapping("/{cId}/contact/")
    public String showContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal){
        Optional<Contact> contact = this.contactRepo.findById(cId);
        Contact contact1 = contact.get();

        //
        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);

        if(user.getId() == contact1.getUser().getId()) {
            model.addAttribute("contact", contact1);
        }

        return "contact_detail";
    }


    // delete contact
    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable Integer cId, Model model, Principal principal, HttpSession session){

        Contact contact = this.contactRepo.findById(cId).get();

        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);

        List<Contact> contacts = user.getContacts();
        contacts.remove(contact);

        this.userRepo.save(user);

        session.setAttribute("message", new Message("Product deleted successfully", "success"));

        return "redirect:/user/show-contacts/0";
    }




//    Post method ka url copy krke agar hum paste kre dobara toh voh url expire ho jaaat h voh chalta nhi h voh sirf
//    click hone pr chlata h post method ka URL
//    But getmapping ka url copy krke kahi pr bhi chala sakte h hum
//    Post method safe URL hota hain

    // update contact
//    @GetMapping("/open-contact/{cId}")
//    public String updateForm(Model model){
//        model.addAttribute("title","Update Contact");
//        return "update_form";
//    }


    // update form handler
    @PostMapping("/update-contact/{cId}")
    public String updateForm(@PathVariable Integer cId, Model model){
        Optional<Contact> contactOptional = this.contactRepo.findById(cId);
        Contact contact = contactOptional.get();
        model.addAttribute("title","Update Product");
        model.addAttribute("contact", contact);
        return "update_form";
    }

    // processing update form handler
    @PostMapping("/process-update")
    public String updateHandler(@ModelAttribute Contact contact,
                                @RequestParam("imageUrl") MultipartFile file,
                                HttpSession session,
                                Model model, Principal principal){

        try{

            // old contact detail
            Optional<Contact> byId = this.contactRepo.findById(contact.getCId());
            Contact oldContact = byId.get();

            if(!file.isEmpty()){
                // file work
                // rewrite

                // delete old photo
                File deleteFile = new ClassPathResource("static").getFile();
                File file1 = new File(deleteFile, oldContact.getImage());
                file1.delete();


                // update new photo
                File savefile = new ClassPathResource("static").getFile();
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());

            } else{
                contact.setImage(oldContact.getImage());
            }

            String name = principal.getName();
            User user = this.userRepo.getUserByUsername(name);
            contact.setUser(user);
            this.contactRepo.save(contact);
            session.setAttribute("message", new Message("Your product has been updated...","success"));

        } catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/user/"+contact.getCId()+"/contact/";
    }

    // profile handler
    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title","User-Profile");
        return "profile";
    }

    // open setting handler
    @GetMapping("/settings")
    public String settingHandler(){
        return "settings";
    }

    // processing setting handler changing passowrd
    // @RequestParam is used to fetch data from html that we have entered
    @PostMapping("/change-password")
    public String changePasswordHandler(@RequestParam("oldPassword") String oldPassword,
                                        @RequestParam("newPassword") String newPassword,
                                        HttpSession session,
                                        Model model, Principal principal){
        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);

        if(this.passwordEncoder.matches(oldPassword, user.getPassword())){
            // change password
            user.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepo.save(user);
            session.setAttribute("message", new Message("Your password has been changed successfully","alert alert-success"));
        } else{
            // error
            session.setAttribute("message", new Message("Please enter correct old password...","alert alert-danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

}
