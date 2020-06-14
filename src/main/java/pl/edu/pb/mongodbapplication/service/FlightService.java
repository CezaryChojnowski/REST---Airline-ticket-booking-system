package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.config.error.exception.InvalidDataException;
import pl.edu.pb.mongodbapplication.config.error.exception.NoFlightsBetweenTheseCitiesOnThisDayException;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.repository.FlightRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private final TicketService ticketService;

    private final AirPortService airPortService;

    @Autowired
    public FlightService(FlightRepository flightRepository, TicketService ticketService, AirPortService airPortService) {
        this.flightRepository = flightRepository;
        this.ticketService = ticketService;
        this.airPortService = airPortService;
    }

    public List<Flight> findAllFlights(){
        return flightRepository.findAll();
    }

    public List<Flight> findFlightsByGivenTwoCountriesAndCitiesAndDate(String countryFrom, String cityFrom, String countryTo, String cityTo, LocalDate date){
        AirPort airPortFrom = airPortService.findByCountryAndCity(countryFrom, cityFrom);
        AirPort airPortTo = airPortService.findByCountryAndCity(countryTo,cityTo);
        List<Flight> flights = flightRepository.findFlightByAirPortFromAndAirPortToAndDate(airPortFrom, airPortTo, date);
//        List<Flight> flights = flightRepository.findFlightsByGivenTwoCountriesAndCitiesAndDate(countryFrom, cityFrom, countryTo, cityTo, date);
        if(!flights.isEmpty()){
            return flights;
        }
        else{
            throw new NoFlightsBetweenTheseCitiesOnThisDayException("No flights between:" +
                    countryFrom +
                    ", " +
                    cityFrom +
                    " - " +
                    countryTo +
                    ", " +
                    cityTo +
                    " on " +
                    date);
        }
    }

    public Flight createFlight(Flight flight){
        List<Boolean> booleanList = new ArrayList<>();
        AirPort airPortFrom = flight.getAirPortFrom();
        AirPort airPortTo = flight.getAirPortTo();
        booleanList.add(airPortIsValid(airPortFrom));
        booleanList.add(airPortIsValid(airPortTo));
        Boolean resultValid = booleanList.contains(Boolean.TRUE);
        if(resultValid){
            throw new InvalidDataException("All fields are required");
        }
        return flightRepository.save(flight);
    }

    public boolean airPortIsValid(AirPort airport){
        List<Boolean> booleanList = new ArrayList<>();
        if(airport.getCountry()==null || airport.getCountry().isEmpty()){
            booleanList.add(false);
        }
        if(airport.getCity()==null || airport.getCity().isEmpty()){
            booleanList.add(false);
        }
        if(airport.getAirPortName()==null || airport.getAirPortName().isEmpty()){
            booleanList.add(false);
        }
        return booleanList.contains(Boolean.FALSE);
    }

    public void deleteFlight(String flightId){
        ticketService.deleteTicketsByFlight(flightId);
        flightRepository.deleteById(flightId);

    }

    public Flight getFlightById(String flightId) {
        return flightRepository.findById(flightId).get();
    }
}
