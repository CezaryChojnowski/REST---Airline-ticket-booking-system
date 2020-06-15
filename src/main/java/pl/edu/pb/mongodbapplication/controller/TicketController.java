package pl.edu.pb.mongodbapplication.controller;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.mongodbapplication.DTO.TicketDTO;
import pl.edu.pb.mongodbapplication.DTO.TicketDTOForTicketsListByUser;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.EmailService;
import pl.edu.pb.mongodbapplication.service.TicketService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {
    private final TicketService ticketService;

    private final EmailService emailService;

    public TicketController(TicketService ticketService, EmailService emailService) {
        this.ticketService = ticketService;
        this.emailService = emailService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public TicketDTO bookingFlight(@Valid @RequestBody Flight flight) throws MessagingException, IOException {
        Ticket ticket = ticketService.bookingFlight(flight);
        TicketDTO ticketDTO = ticketService.getCreatedTicketDTO(ticket);
        emailService.createTicketPDF(ticketDTO);
        emailService.sendEmailWithReservation(ticketDTO);
        return ticketDTO;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TicketDTOForTicketsListByUser>> getAllTicketsByUserName(
            @RequestParam String username){
            List<TicketDTOForTicketsListByUser> ticketsListByUsers = ticketService.findAllTicketsByUser(username);
                for (TicketDTOForTicketsListByUser ticket : ticketsListByUsers) {
                    String ticketId = ticket.get_id();
                    Link selfLink = linkTo(TicketController.class).slash(ticketId).withSelfRel();
                    ticket.add(selfLink);
                }
                return new ResponseEntity<>(ticketsListByUsers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/reservation/{code}",method = RequestMethod.GET)
    public TicketDTO checkReservation(
            @PathVariable("code") Integer code){
        Ticket ticket = ticketService.checkReservation(code);
        return ticketService.getCreatedTicketDTO(ticket);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTicket(@PathVariable("ticketId") String ticketId){
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new MessageResponse("Ticket deleting successfully!"));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
    public TicketDTO getTicket(@PathVariable("ticketId") String ticketId){
        return ticketService.getCreatedTicketDTO(ticketService.getTicketById(ticketId));
    }

}
