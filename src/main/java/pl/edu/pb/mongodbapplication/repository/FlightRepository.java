package pl.edu.pb.mongodbapplication.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.pb.mongodbapplication.model.Flight;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {
    List<Flight> findAll();

    @Query("{'airPortFrom.country': ?0, 'airPortFrom.city':?1, 'airPortTo.country' : ?2, 'airPortTo.city' : ?3, 'date': ?4}")
    List<Flight> findFlightsByGivenTwoCountriesAndCitiesAndDate(String countryFrom, String cityFrom, String countryTo, String cityTo, LocalDate date);

    @Query("{ '_id': ?0 }")
    Flight findById(ObjectId id);
}
