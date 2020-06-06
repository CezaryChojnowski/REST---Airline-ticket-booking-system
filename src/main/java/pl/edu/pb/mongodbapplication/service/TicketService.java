package pl.edu.pb.mongodbapplication.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.repository.TicketRepository;
import pl.edu.pb.mongodbapplication.repository.UserRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TicketService {

    final
    TicketRepository ticketRepository;

    final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Ticket bookingFlight(Flight flight){
        Integer ticketCode = generateTicketCode();
        User user = getLoggedUser();
        Ticket ticket = new Ticket(flight, user, ticketCode);
        return ticketRepository.save(ticket);
    }

    public int generateTicketCode() {
        int code;
        List<Ticket> ticketList = ticketRepository.findAll();
        List<Ticket> tempListTickets;
        do {
            code = new Random().nextInt(1000 + 1) + 10000;
            int finalCode = code;
            tempListTickets = ticketList
                    .stream()
                    .filter(c -> c.equals(finalCode))
                    .collect(Collectors
                            .toList());
        }while(!tempListTickets.isEmpty());
        return code;
    }

    public UserDetails getLoggedUserDetails(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

    public User getLoggedUser(){
        return userRepository.findByUsername(getLoggedUserDetails().getUsername()).get();
    }
}
