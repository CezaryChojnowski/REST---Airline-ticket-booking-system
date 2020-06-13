package pl.edu.pb.mongodbapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.AirPortService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/airports")
public class AirPortController {

    public final AirPortService airPortService;

    public AirPortController(AirPortService airPortService) {
        this.airPortService = airPortService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/list" ,method = RequestMethod.POST)
    public ResponseEntity createAirPorts(@Valid @RequestBody List<AirPort> airPorts){
        for(int i=0; i<airPorts.size(); i++){
            airPortService.createAirPort(airPorts.get(i));
        }
        return ResponseEntity.ok(new MessageResponse("Flights creating successfully!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<AirPort> getAllAirPorts(){
        return airPortService.getAllAirPorts();
    }

}
