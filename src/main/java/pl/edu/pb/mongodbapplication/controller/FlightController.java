package pl.edu.pb.mongodbapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.service.CountryService;
import pl.edu.pb.mongodbapplication.service.FlightService;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    private final CountryService countryService;

    @Autowired
    public FlightController(FlightService flightService, CountryService countryService) {
        this.flightService = flightService;
        this.countryService = countryService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Flight> findAllFlights(){
        return flightService.findAllFlights();
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
