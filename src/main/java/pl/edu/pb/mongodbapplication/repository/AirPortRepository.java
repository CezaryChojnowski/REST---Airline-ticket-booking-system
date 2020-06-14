package pl.edu.pb.mongodbapplication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.pb.mongodbapplication.model.AirPort;
import pl.edu.pb.mongodbapplication.model.Flight;

public interface AirPortRepository extends MongoRepository<AirPort, String> {

    public AirPort findAirPortByCountryAndCity(String Country, String City);
}
