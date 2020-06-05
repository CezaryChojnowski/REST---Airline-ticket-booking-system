package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    private String email;
    @NotEmpty(message = "{user.username.notEmpty}")
    private String username;
    @NotEmpty(message = "{user.password.notEmpty}")
    @NotEmpty(message = "{user.password.notEmpty}")
    @NotBlank(message = "{user.password.notEmpty}")
    private String password;
    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(String firstName, String lastName, String username, String email, String password, Set<Role> roles) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles=roles;
    }
}
