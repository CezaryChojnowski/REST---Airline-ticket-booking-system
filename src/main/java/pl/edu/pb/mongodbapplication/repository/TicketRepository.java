package pl.edu.pb.mongodbapplication.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pb.mongodbapplication.model.Flight;
import pl.edu.pb.mongodbapplication.model.Ticket;
import pl.edu.pb.mongodbapplication.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findTicketByCode(Integer code);
    Boolean existsByCode(Integer code);
    List<Ticket> findAllByUser(User user);
    List<Ticket> findAllByFlight(Flight flight);
}
