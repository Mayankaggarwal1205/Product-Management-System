package com.smartContactManager.controller;

import com.smartContactManager.entity.User;
import com.smartContactManager.helper.Message;
import com.smartContactManager.repo.UserRepo;
import com.smartContactManager.service.EmailService;
import com.smartContactManager.service.EmailServiceAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {

    Random random = new Random(1000);

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceAttachment emailServiceAttachment;

    // forgot user password handler
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model, Principal principal, HttpSession session){
        return "forgot_email_form";
    }

    // RequestParam is used to fetch the entered data
    @PostMapping("/send-OTP")
    public String sendOTP(@RequestParam("email") String email, HttpSession session) {

        // generating OTP of 4 digits
        int otp = random.nextInt(9999);
        System.out.println(otp);

        // write code for sending OTP
        String subject = "OTP Confirmation";
//        String text = "OTP : "+ otp ;

        String text =
                "<div style='border: 1px solid #e2e2e2; padding: 20px;'>"
                + "<h1>"
                + "OTP : "
                + "<b>" + otp + "</b>"
                + "</h1>"
                + "</div>";

        String to = email;
        String from = "gargmayank1205@gmail.com";

        Boolean flag = this.emailService.sendEmail(to, from, subject, text);

        if(flag){
            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            session.setAttribute("message", new Message(" OTP has been sent to your registered mail ID !!","alert alert-success"));
            return "verify_otp";
        } else{
            session.setAttribute("message", new Message("Check your Email ID","danger"));
            return "forgot_email_form";
        }

    }

    // verifying OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp, HttpSession session){
        int myOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");
        if(myOtp == otp){
            // password change form
            User user = this.userRepo.getUserByUsername(email);
            if(user == null){
                // error
                session.setAttribute("message",
                        new Message("User does not found with this Email","danger"));
                return "forgot_email_form";
            } else{
                // send change password form
            }
            return "change_password_form";
        } else{
            session.setAttribute("message",
                    new Message("OTP you have entered is invalid","alert alert-danger"));
            return "verify_otp";
        }
    }

    // change password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newPassword") String password, HttpSession session){
        String email = (String) session.getAttribute("email");
        User user = this.userRepo.getUserByUsername(email);
        user.setPassword(this.passwordEncoder.encode(password));
        this.userRepo.save(user);
        return "redirect:/signin?change=password changed successfully...";
    }

}
