package pl.edu.pb.mongodbapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.mongodbapplication.config.error.exception.IncorrectTokenException;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.EmailService;
import pl.edu.pb.mongodbapplication.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/sendEmail" ,method = RequestMethod.PATCH)
    public ResponseEntity sendEmailToResetPassword(@RequestParam String email){
        User user = userService.setToken(email);
        emailService.sendEmailToResetPassword(email, user.getToken());
        return ResponseEntity.ok(new MessageResponse("Email with code sending successfully!"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity validToken(@RequestParam String email, @RequestParam String code){
        boolean result = userService.checkToken(code, email);
        if(!result){
            throw new IncorrectTokenException(code + " code is invalid");
        }
        return ResponseEntity.ok(new MessageResponse("The code is correct"));
    }

    @RequestMapping(value = "/resetPassword" ,method = RequestMethod.PATCH)
    public ResponseEntity resetPassword(@RequestParam String password, @RequestParam String email){
        userService.setToken(email);
        userService.updatePassword(password, email);
        userService.resetToken(email);
        return ResponseEntity.ok(new MessageResponse("Password updating successfully!"));
    }
}
