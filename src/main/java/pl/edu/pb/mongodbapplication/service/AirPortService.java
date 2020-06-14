package pl.edu.pb.mongodbapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.repository.AirPortRepository;

import java.util.List;

@Service
public class AirPortService {

    private final AirPortRepository airPortRepository;

    public AirPortService(AirPortRepository airPortRepository) {
        this.airPortRepository = airPortRepository;
    }

    public void createAirPort(AirPort airPort){
        airPortRepository.save(airPort);
    }

    public List<AirPort> getAllAirPorts() {
        return airPortRepository.findAll();
    }

    public AirPort findByCountryAndCity(String country, String city){
        return airPortRepository.findAirPortByCountryAndCity(country, city);
    }
}
