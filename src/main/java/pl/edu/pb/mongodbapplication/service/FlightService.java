package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.config.error.exception.InvalidDataException;
import pl.edu.pb.mongodbapplication.config.error.exception.NoFlightsBetweenTheseCitiesOnThisDayException;
import pl.edu.pb.mongodbapplication.config.error.exception.NoFlightsOnThisDayException;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.repository.FlightRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:PL.exception.messages.properties")
public class FlightService {

    private final FlightRepository flightRepository;

    private final TicketService ticketService;

    final
    Environment env;


    @Autowired
    public FlightService(FlightRepository flightRepository, TicketService ticketService, Environment env) {
        this.flightRepository = flightRepository;
        this.ticketService = ticketService;
        this.env = env;
    }

    public List<Flight> findAllFlights(){
        return flightRepository.findAll();
    }

    public List<Flight> findAllFlightsByDate(LocalDate localDate){
        List<Flight> flights = flightRepository.findFlightByDate(localDate);
        if(flights.isEmpty()){
            throw new NoFlightsOnThisDayException(env.getProperty("noFlightsOnThisDay"));
        }
        return flights;
    }

    public List<Flight> findFlightsByGivenTwoCountriesAndCitiesAndDate(String countryFrom, String cityFrom, String countryTo, String cityTo, LocalDate date){
        List<Flight> flights = flightRepository.findFlightsByGivenTwoCountriesAndCitiesAndDate(countryFrom, cityFrom, countryTo, cityTo, date);
        if(!flights.isEmpty()){
            return flights;
        }
        else{
            throw new NoFlightsBetweenTheseCitiesOnThisDayException(env.getProperty("noFlightsBetween") +": " +
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
            throw new InvalidDataException(env.getProperty("allFieldsAreRequired"));
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

    public Flight editFlight(Flight flight, String flightId){
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(airPortIsValid(flight.getAirPortFrom()));
        booleanList.add(airPortIsValid(flight.getAirPortTo()));
        Flight flightResult = flightRepository.findById(flightId).get();
        flightResult.setAirPortTo(flight.getAirPortTo());
        flightResult.setAirPortFrom(flight.getAirPortFrom());
        flightResult.setPrice(flight.getPrice());
        flightResult.setTime(flight.getTime());
        flightResult.setDate(flight.getDate());
        Boolean resultValid = booleanList.contains(Boolean.TRUE);
        if(resultValid){
            throw new InvalidDataException(env.getProperty("allFieldsAreRequired"));
        }
        return flightRepository.save(flightResult);
    }
}
