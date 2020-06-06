package pl.edu.pb.mongodbapplication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.pb.mongodbapplication.model.ERole;
import pl.edu.pb.mongodbapplication.model.Roles;


public interface RoleRepository extends MongoRepository<Roles, String> {
    Optional<Roles> findByName(ERole name);
}
