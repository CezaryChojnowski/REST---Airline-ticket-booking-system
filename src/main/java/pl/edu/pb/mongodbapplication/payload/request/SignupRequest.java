package pl.edu.pb.mongodbapplication.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Set<String> roles;

}