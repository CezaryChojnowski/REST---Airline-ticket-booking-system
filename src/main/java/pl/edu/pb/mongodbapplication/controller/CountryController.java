package pl.edu.pb.mongodbapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(method = RequestMethod.GET)
    public Set<CountryDTO> findAllTheCountriesThatPlanesDepartFrom(){
        return countryService.findAllTheCountriesThatPlanesDepartFrom();
    }
}
