package pl.edu.pb.mongodbapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/departingPlanes", method = RequestMethod.GET)
    public Set<CountryDTO> findAllTheCountriesThatPlanesDepartFrom(){
        return countryService.findAllTheCountriesThatPlanesDepartFrom();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/airplanesArriving", method = RequestMethod.GET)
    public Set<CountryDTO> findAllCountriesToWhichPlanesDepartFromAgivenCity(
            @RequestParam String Country,
            @RequestParam String City){
        return countryService.findAllCountriesToWhichPlanesDepartFromAgivenCity(Country, City);
    }
}
