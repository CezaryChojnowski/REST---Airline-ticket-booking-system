package pl.edu.pb.mongodbapplication.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
}
