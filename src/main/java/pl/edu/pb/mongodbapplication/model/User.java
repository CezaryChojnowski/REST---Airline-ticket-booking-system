package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.pb.mongodbapplication.config.validator.ValidEmail;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "User")
@ToString
@PropertySource("classpath:messages.properties")
public class User {
    private String _id;
    @NotEmpty(message = "{user.firstName.notEmpty}")
    private String firstName;
    @NotEmpty(message = "{user.lastName.notEmpty}")
    private String lastName;
    @NotEmpty(message = "{user.email.notEmpty}")
    @ValidEmail
    private String email;
    @NotEmpty(message = "{user.username.notEmpty}")
    private String username;
    @NotEmpty(message = "{user.password.notEmpty}")
    private String password;
    private String token;
    @DBRef
    private Set<Roles> roles = new HashSet<>();

    public User(String firstName, String lastName, String username, String email, String password, Set<Roles> roles) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles=roles;
    }
}
