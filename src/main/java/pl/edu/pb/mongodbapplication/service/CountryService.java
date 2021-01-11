package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.DTO.CityDTO;
import pl.edu.pb.mongodbapplication.DTO.CountryDTO;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CountryService {

    final
    FlightService flightService;

    @Autowired
    public CountryService(FlightService flightService) {
        this.flightService = flightService;
    }

    public Set<CountryDTO> findAllCountriesToWhichPlanesDepartFromAgivenCity(String country, String city) {
        List<Flight> flightList = flightService.findAllFlights();
        Set<CountryDTO> countryList = new HashSet<>();
        for(int i=0; i<flightList.size(); i++) {
            if (flightList.get(i).getAirPortFrom().getCountry().equals(country)) {
                Set<CityDTO> citiesSet = new HashSet<>();
                String countryName = flightList.get(i).getAirPortTo().getCountry();
                for(int j=0; j<flightList.size(); j++){
                    if(flightList.get(j).getAirPortTo().getCountry().equals(countryName) && flightList.get(j).getAirPortFrom().getCity().equals(city)) {
                        String cityName = flightList.get(j).getAirPortTo().getCity();
                        CityDTO cityDTO = new CityDTO(cityName);
                        citiesSet.add(cityDTO);
                    }
                }
                if(citiesSet.isEmpty()){
                    continue;
                }
                CountryDTO countryDTO = new CountryDTO(countryName, citiesSet);
                countryList.add(countryDTO);
            }
        }
        return countryList;
    }

    public Set<AirPort> getAllDistinctAirPortsFrom(){
        List<Flight> flights = getAllFlights();
        Set<AirPort> airPorts = new HashSet<>();
        for(int i=0; i<flights.size(); i++){
            airPorts.add(flights.get(i).getAirPortFrom());
        }
        return airPorts;
    }

    public List<Flight> getAllFlights(){
        return flightService.findAllFlights();
    }

    public Set<CountryDTO> findAllTheCountriesThatPlanesDepartFrom(){
        Set<AirPort> airPorts = getAllDistinctAirPortsFrom();
        List<AirPort> airPortsFrom = new ArrayList<>(airPorts);
        Set<CountryDTO> countryDTOList = new HashSet<>();
        for(int i=0; i<airPorts.size(); i++){
            Set<CityDTO> cityDTOSet = new HashSet<>();
            String country = airPortsFrom.get(i).getCountry();
            for(int j=0; j<airPorts.size(); j++){
                if(airPortsFrom.get(j).getCountry().equals(country)){
                    String city = airPortsFrom.get(j).getCity();
                    CityDTO cityDTO = new CityDTO(city);
                    cityDTOSet.add(cityDTO);
                }
            }
            CountryDTO countryDTO = new CountryDTO(country, cityDTOSet);
            countryDTOList.add(countryDTO);
        }
        return countryDTOList;
    }
}
