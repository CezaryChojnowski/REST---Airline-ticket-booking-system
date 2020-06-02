package pl.edu.pb.mongodbapplication.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pb.mongodbapplication.model.Flight;

import java.util.List;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {
    List<Flight> findAll();
}
