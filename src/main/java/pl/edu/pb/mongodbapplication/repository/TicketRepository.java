package pl.edu.pb.mongodbapplication.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pb.mongodbapplication.model.Ticket;

import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findTicketByCode(Integer code);
}
