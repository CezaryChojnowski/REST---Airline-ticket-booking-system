package pl.edu.pb.mongodbapplication.service;

import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;

import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class GenerateService {

    private final AirPortService airPortService;

    private final FlightService flightService;

    public GenerateService(AirPortService airPortService, FlightService flightService) {
        this.airPortService = airPortService;
        this.flightService = flightService;
    }

    public void generateFlight(List<AirPort> airPortList){
        LocalDate date = LocalDate.parse("2020-06-20", DateTimeFormatter.ISO_DATE);
        LocalDate dateLimit = LocalDate.parse("2020-06-24", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("09:00", DateTimeFormatter.ISO_TIME);
        LocalTime timeLimit = LocalTime.parse("21:00", DateTimeFormatter.ISO_TIME);
        int lenght = airPortList.size();
        for(LocalDate dateTemp=date; dateTemp.isBefore(dateLimit); dateTemp = dateTemp.plusDays(1)) {
            for(LocalTime timeTemp = time; timeTemp.isBefore(timeLimit); timeTemp = timeTemp.plusHours(3)) {
                for (int i = 0; i < lenght; i++) {
                    for (int j = 0; j < lenght; j++) {
                        if (airPortList.get(i).equals(airPortList.get(j))) {
                            continue;
                        }
                        Flight flight = new Flight();
                        flight.setDate(dateTemp);
                        flight.setTime(timeTemp);
                        flight.setAirPortFrom(airPortList.get(i));
                        flight.setAirPortTo(airPortList.get(j));
                        flight.setPrice(getRandomNumberInRange(20,150));
                        flightService.createFlight(flight);
                    }
                }
                for (int i = lenght; i <= 0; i--) {
                    for (int j = lenght; j <= 0; j--) {
                        if (airPortList.get(i).equals(airPortList.get(j))) {
                            continue;
                        }
                        Flight flight = new Flight();
                        flight.setDate(dateTemp);
                        flight.setTime(timeTemp);
                        flight.setAirPortFrom(airPortList.get(i));
                        flight.setAirPortTo(airPortList.get(j));
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
