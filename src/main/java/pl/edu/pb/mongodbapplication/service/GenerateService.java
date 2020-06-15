package pl.edu.pb.mongodbapplication.service;

import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;

import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GenerateService {

    private final FlightService flightService;

    public GenerateService(FlightService flightService) {
        this.flightService = flightService;
    }

    public void generateFlight(){
        List<AirPort> airPorts = new ArrayList<>();
        airPorts.add(new AirPort("USA", "New York", "John F. Kennedy"));
        airPorts.add(new AirPort("USA", "Los Angeles", "Los Angeles International Airport"));
        airPorts.add(new AirPort("Poland", "Warsaw", "Chopin AirPort"));
        airPorts.add(new AirPort("Germany", "Berlin", "Berlin-Tempelhof AirPort"));
        airPorts.add(new AirPort("France", "Paris", "Paris-Roissy-Charles de Gaulle AirPort"));
        airPorts.add(new AirPort("Spain", "Madrid", "Madrid-Barajas AirPort"));
        airPorts.add(new AirPort("Portugal", "Lisbon", "Lisbon-Portela AirPort"));
        airPorts.add(new AirPort("Russia", "Moscow", "Moscow-Domodiedowo AirPort"));
        airPorts.add(new AirPort("United Arab Emirates", "Dubai", "Dubai International Airport"));
        airPorts.add(new AirPort("Poland", "Radom", "Radom AirPort"));

        LocalDate date = LocalDate.parse("2020-06-20", DateTimeFormatter.ISO_DATE);
        LocalDate dateLimit = LocalDate.parse("2020-06-27", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("09:00", DateTimeFormatter.ISO_TIME);
        LocalTime timeLimit = LocalTime.parse("21:00", DateTimeFormatter.ISO_TIME);
        int lenght = airPorts.size();
        for(LocalDate dateTemp=date; dateTemp.isBefore(dateLimit); dateTemp = dateTemp.plusDays(1)) {
            for(LocalTime timeTemp = time; timeTemp.isBefore(timeLimit); timeTemp = timeTemp.plusHours(3)) {
                for (int i = 0; i < lenght; i++) {
                    for (int j = 0; j < lenght; j++) {
                        if (airPorts.get(i).equals(airPorts.get(j))) {
                            continue;
                        }
                        Flight flight = new Flight();
                        flight.setDate(dateTemp);
                        flight.setTime(timeTemp);
                        flight.setAirPortFrom(airPorts.get(i));
                        flight.setAirPortTo(airPorts.get(j));
                        flight.setPrice(getRandomNumberInRange(20,150));
                        flightService.createFlight(flight);
                    }
                }
                for (int i = lenght; i <= 0; i--) {
                    for (int j = lenght; j <= 0; j--) {
                        if (airPorts.get(i).equals(airPorts.get(j))) {
                            continue;
                        }
                        Flight flight = new Flight();
                        flight.setDate(dateTemp);
                        flight.setTime(timeTemp);
                        flight.setAirPortFrom(airPorts.get(i));
                        flight.setAirPortTo(airPorts.get(j));
                        flight.setPrice(getRandomNumberInRange(20,150));
                        flightService.createFlight(flight);
                    }
                }
            }
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
