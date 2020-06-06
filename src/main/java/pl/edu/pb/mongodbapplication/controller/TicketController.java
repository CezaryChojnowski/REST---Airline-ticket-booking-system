package pl.edu.pb.mongodbapplication.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.mongodbapplication.DTO.TicketDTO;
import pl.edu.pb.mongodbapplication.DTO.TicketDTOForTicketsListByUser;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.service.TicketService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public TicketDTO bookingFlight(@Valid @RequestBody Flight flight){
        Ticket ticket = ticketService.bookingFlight(flight);
        return ticketService.getCreatedTicketDTO(ticket);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<TicketDTOForTicketsListByUser> getAllTicketsByUserName(
            @RequestParam String username){
        return ticketService.findAllTicketsByUser(username);
    }

}
