package pl.edu.pb.mongodbapplication.controller;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.mongodbapplication.DTO.TicketDTO;
import pl.edu.pb.mongodbapplication.DTO.TicketDTOForTicketsListByUser;
import pl.edu.pb.mongodbapplication.config.error.exception.DoNotHaveAccessToThisTicketException;
import pl.edu.pb.mongodbapplication.log.LogExecutionInfo;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.EmailService;
import pl.edu.pb.mongodbapplication.service.TicketService;
import pl.edu.pb.mongodbapplication.service.UserService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {
    private final TicketService ticketService;

    private final EmailService emailService;

    private final UserService userService;

    final
    Environment env;

    public TicketController(TicketService ticketService, EmailService emailService, UserService userService, Environment env) {
        this.ticketService = ticketService;
        this.emailService = emailService;
        this.userService = userService;
        this.env = env;
    }

    @LogExecutionInfo
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public TicketDTO bookingFlight(@Valid @RequestBody Flight flight) throws MessagingException, IOException {
        Ticket ticket = ticketService.bookingFlight(flight);
        TicketDTO ticketDTO = ticketService.getCreatedTicketDTO(ticket);
        emailService.createTicketPDF(ticketDTO);
        emailService.sendEmailWithReservation(ticketDTO);
        return ticketDTO;
    }

    @LogExecutionInfo
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<TicketDTOForTicketsListByUser> getAllTicketsByUserName(
            @RequestParam String username){
            List<TicketDTOForTicketsListByUser> ticketsListByUsers = ticketService.findAllTicketsByUser(username);
                return ticketsListByUsers;
    }

    @LogExecutionInfo
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/reservation/{code}",method = RequestMethod.GET)
    public TicketDTO checkReservation(
            @PathVariable("code") Integer code){
        Ticket ticket = ticketService.checkReservation(code);
        String emailOwnerTicket = ticket.getUser().getEmail();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userCurrentAuth = userService.getUserByUserName(authentication.getName()).getEmail();
        if(!emailOwnerTicket.equals(userCurrentAuth)){
            throw new DoNotHaveAccessToThisTicketException(env.getProperty("doNotHaveAccessToThisTicket"));
        }
        return ticketService.getCreatedTicketDTO(ticket);
    }

    @LogExecutionInfo
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTicket(@PathVariable("ticketId") String ticketId){
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new MessageResponse("Ticket deleting successfully!"));
    }

    @LogExecutionInfo
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
    public TicketDTO getTicket(@PathVariable("ticketId") String ticketId){
        return ticketService.getCreatedTicketDTO(ticketService.getTicketById(ticketId));
    }

}
