package pl.edu.pb.mongodbapplication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.pb.mongodbapplication.model.ERole;
import pl.edu.pb.mongodbapplication.model.Role;


public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
