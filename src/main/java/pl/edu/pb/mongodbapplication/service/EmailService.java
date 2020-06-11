package pl.edu.pb.mongodbapplication.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.Flight;

@Service
public class EmailService {

    public final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendEmailToUsersWithInformationAboutTheCanceledFlight(String email, Flight flight){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your flight is cancelled");
        message.setText("Flight from " + flight.getAirPortFrom().getCity() + " to " + flight.getAirPortTo().getCity() +
                " on " + flight.getDate().toString() + " at " + flight.getTime().toString() + " has been canceled.");
        emailSender.send(message);
    }

    public void sendEmailToResetPassword(String email, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset your password");
        message.setText("Your reset code: " + code);
        emailSender.send(message);
    }
}

