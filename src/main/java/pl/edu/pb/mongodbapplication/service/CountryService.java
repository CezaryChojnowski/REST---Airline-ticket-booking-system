package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.DTO.CityDTO;
import pl.edu.pb.mongodbapplication.DTO.CountryDTO;
import pl.edu.pb.mongodbapplication.model.Flight;

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

    public Set<CountryDTO> findAllTheCountriesThatPlanesDepartFrom(){
        List<Flight> flights = flightService.findAllFlights();
        Set<CountryDTO> countryDTOList = new HashSet<>();
        for(int i=0; i<flights.size(); i++){
            Set<CityDTO> cityDTOSet = new HashSet<>();
            String country = flights.get(i).getAirPortFrom().getCountry();
            for(int j=0; j<flights.size(); j++){
                if(flights.get(j).getAirPortFrom().getCountry().equals(country)){
                    String city = flights.get(j).getAirPortFrom().getCity();
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
