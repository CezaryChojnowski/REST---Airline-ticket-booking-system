package pl.edu.pb.mongodbapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.DTO.CountryDTO;
import pl.edu.pb.mongodbapplication.service.CountryService;

import java.util.Set;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @RequestMapping(value = "/departingPlanes", method = RequestMethod.GET)
    public Set<CountryDTO> findAllTheCountriesThatPlanesDepartFrom(){
        return countryService.findAllTheCountriesThatPlanesDepartFrom();
    }

    @RequestMapping(value = "/airplanesArriving", method = RequestMethod.GET)
    public Set<CountryDTO> findAllCountriesToWhichPlanesDepartFromAgivenCity(@RequestParam(required = true) String Country, @RequestParam(required = true) String City){
        return countryService.findAllCountriesToWhichPlanesDepartFromAgivenCity(Country, City);
    }
}
