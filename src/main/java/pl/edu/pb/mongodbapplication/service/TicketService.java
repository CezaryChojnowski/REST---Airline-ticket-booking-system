package pl.edu.pb.mongodbapplication.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.DTO.TicketDTO;
import pl.edu.pb.mongodbapplication.DTO.TicketDTOForTicketsListByUser;
import pl.edu.pb.mongodbapplication.DTO.UserDTO;
import pl.edu.pb.mongodbapplication.config.error.exception.*;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.repository.FlightRepository;
import pl.edu.pb.mongodbapplication.repository.TicketRepository;
import pl.edu.pb.mongodbapplication.repository.UserRepository;

import java.util.*;

@Service
@PropertySource("classpath:PL.exception.messages.properties")
public class TicketService {

    final
    TicketRepository ticketRepository;

    final UserRepository userRepository;

    final FlightRepository flightRepository;

    final EmailService emailService;

    final
    Environment env;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, FlightRepository flightRepository, EmailService emailService, Environment env){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.emailService = emailService;
        this.env = env;
    }

    public Ticket bookingFlight(Flight flight){
        Integer ticketCode = generateTicketCode();
        User user = getLoggedUser();
        Ticket ticket = new Ticket(flight, user, ticketCode);
        return ticketRepository.save(ticket);
    }

    public TicketDTO getCreatedTicketDTO(Ticket ticket){
        User user = ticket.getUser();
        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail());
        return new TicketDTO(ticket.get_id(),ticket.getFlight(), userDTO, ticket.getCode());
    }

    public int generateTicketCode() {
        int code;
        boolean existsCode;
        do {
            code = new Random().nextInt(1000 + 1) + 10000;
            existsCode = ticketRepository.existsByCode(code);
        }while(existsCode);
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

    public Ticket checkReservation(Integer number_of_reservation) {
        Optional<Ticket> ticket = ticketRepository.findTicketByCode(number_of_reservation);
        boolean ticketExists = ticket.isPresent();
        if(!ticketExists){
            throw new ReservationNotFoundException(env.getProperty("reservationWithGivenTicketCodeNotFound") + " " + number_of_reservation);
        }
        return ticket.get();
    }

    public List<TicketDTOForTicketsListByUser> findAllTicketsByUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            throw new UserNotFoundException(env.getProperty("reservationWithGivenTicketCodeNotFound") + " " + username);
        }
        List<Ticket> tickets = ticketRepository.findAllByUser(user.get());
        return getTicketsDTOByUser(tickets);
    }

    public List<TicketDTOForTicketsListByUser> getTicketsDTOByUser(List<Ticket> tickets){
        List<TicketDTOForTicketsListByUser> ticketDTOForTicketsListByUserList = new ArrayList<>();
        for(Ticket ticket: tickets){
            ticketDTOForTicketsListByUserList.add(new TicketDTOForTicketsListByUser(ticket.get_id(),ticket.getFlight(), ticket.getCode()));
        }
        return ticketDTOForTicketsListByUserList;
    }

    public void deleteTicketsByFlight(String flightId){
        String email;
        Optional<Flight> flight = flightRepository.findById(flightId);
        if(!flight.isPresent()){
            throw new FlightNotFoundException(env.getProperty("flightWithGivenFlightIdNotFound") + " " + flightId);
        }
        List<Ticket> tickets = ticketRepository.findAllByFlight(flight.get());
        if(!tickets.isEmpty()){
            for (Ticket ticket : tickets) {
                email = ticket.getUser().getEmail();
                emailService.sendEmailToUsersWithInformationAboutTheCanceledFlight(email, flight.get());
                ticketRepository.delete(ticket);
            }
        }
    }
    public void deleteTicket(String ticketId){
            ticketRepository.deleteById(ticketId);
    }

    public Ticket getTicketById(String ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if(!ticket.isPresent()){
            throw new TicketNotFoundException(env.getProperty("ticketWithGivenTicketIdNotFound") + " " + ticketId);
        }
        return ticket.get();
    }

    public void checkIfThereAreTicketsForTheGivenFlight(String flightId){
        Optional<Flight> flight = flightRepository.findById(flightId);
        if(!flight.isPresent()){
            throw new FlightNotFoundException(env.getProperty("flightWithGivenFlightIdNotFound") + " " + flightId);
        }
        List<Ticket> tickets = ticketRepository.findAllByFlight(flight.get());
        boolean thereAreTicketsForTheGivenFlight = !tickets.isEmpty();
        if(thereAreTicketsForTheGivenFlight){
            throw new ThereAreTicketsForTheGivenFlightException(env.getProperty("thereAreTicketsForTheGivenFlight"));
        }
    }
}
