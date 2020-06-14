package pl.edu.pb.mongodbapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.AirPortService;
import pl.edu.pb.mongodbapplication.service.GenerateService;

import java.util.List;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    private final GenerateService generateService;

    private final AirPortService airPortService;

    public GenerateController(GenerateService generateService, AirPortService airPortService) {
        this.generateService = generateService;
        this.airPortService = airPortService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity generateFlight(){
        List<AirPort> airPortList = airPortService.getAllAirPorts();
        generateService.generateFlight(airPortList);
        return ResponseEntity.ok(new MessageResponse("JEBANE STUDIA"));
    }
}
