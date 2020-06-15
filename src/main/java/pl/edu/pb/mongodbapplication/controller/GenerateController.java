package pl.edu.pb.mongodbapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.service.GenerateService;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    private final GenerateService generateService;
    public GenerateController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity generateFlight(){
        generateService.generateFlight();
        return ResponseEntity.ok(new MessageResponse("Flights successfully generated"));
    }
}
