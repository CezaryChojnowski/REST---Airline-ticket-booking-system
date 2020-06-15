package pl.edu.pb.mongodbapplication.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.DTO.TicketDTO;
import pl.edu.pb.mongodbapplication.model.Flight;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

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

    public DataHandler createTicketPDF(TicketDTO ticket) throws MessagingException {
        PdfWriter writer = null;
        Integer ticketCode = ticket.getCode();
        String fileName = new String("Ticket " + ticketCode +".pdf");
        try {
            writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph().add("Origin: " + ticket.getFlight().getAirPortFrom().getCountry() + ", " + ticket.getFlight().getAirPortFrom().getCity()));
            document.add(new Paragraph().add("Departure airport: " + ticket.getFlight().getAirPortFrom().getAirPortName() + "\n\n"));
            document.add(new Paragraph().add("Destination: " + ticket.getFlight().getAirPortTo().getCountry() + ", " + ticket.getFlight().getAirPortTo().getCity()));
            document.add(new Paragraph().add("Destination airport: " + ticket.getFlight().getAirPortTo().getAirPortName() +"\n\n"));
            document.add(new Paragraph().add("Flight date: " + ticket.getFlight().getDate()));
            document.add(new Paragraph().add("Flight time: " + ticket.getFlight().getTime()));
            document.add(new Paragraph().add("Flight price: " + ticket.getFlight().getPrice() + "\n\n\n"));
            document.add(new Paragraph().add("Ticket code: " + ticket.getCode()));
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileDataSource dataSource = new FileDataSource(fileName);
        return new DataHandler(dataSource);
    }

    public void sendEmailWithReservation(TicketDTO ticket) throws MessagingException, IOException {
        final String username = "tempinternmail@gmail.com";
        final String password = "temptemp123!";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(ticket.getUserDTO().getEmail()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(ticket.getUserDTO().getEmail()));
            msg.setSubject("Reservation");

            Multipart emailContent = new MimeMultipart();

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Your flight from " + ticket.getFlight().getAirPortFrom().getCity() + " to " + ticket.getFlight().getAirPortTo().getCity() +
                " on " + ticket.getFlight().getDate().toString() + " at " + ticket.getFlight().getTime().toString() + " is successfully booking \nConfirmation of reservation is in attachment");
            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile("Ticket " + ticket.getCode()+".pdf");

            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(pdfAttachment);
            msg.setContent(emailContent);

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

