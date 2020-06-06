package pl.edu.pb.mongodbapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.CountryService;
import pl.edu.pb.mongodbapplication.service.FlightService;
import pl.edu.pb.mongodbapplication.service.TicketService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    private final TicketService ticketService;

    private final CountryService countryService;

    @Autowired
    public FlightController(FlightService flightService, TicketService ticketService, CountryService countryService) {
        this.flightService = flightService;
        this.ticketService = ticketService;
        this.countryService = countryService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Flight> findAllFlights(){
        return flightService.findAllFlights();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFlight(@Valid @RequestBody Flight flight){
        flightService.createFlight(flight);
        return ResponseEntity.ok(new MessageResponse("Flight creating successfully!"));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/locations",method = RequestMethod.GET)
    public List<Flight> findFlightsByGivenTwoCountriesAndCitiesAndDate(
            @RequestParam String countryFrom,
            @RequestParam String cityFrom,
            @RequestParam String countryTo,
            @RequestParam String cityTo,
            @RequestParam String date){
        return flightService.findFlightsByGivenTwoCountriesAndCitiesAndDate(countryFrom, cityFrom, countryTo, cityTo, date);
    }
}
