package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.repository.FlightRepository;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> findAllFlights(){
        return flightRepository.findAll();
    }

    public List<Flight> findFlightsByGivenTwoCountriesAndCitiesAndDate(String countryFrom, String cityFrom, String countryTo, String cityTo, String date ){
        return flightRepository.findFlightsByGivenTwoCountriesAndCitiesAndDate(countryFrom, cityFrom, countryTo, cityTo, date);
    }

}
