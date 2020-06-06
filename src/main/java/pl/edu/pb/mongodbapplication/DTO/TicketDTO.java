package pl.edu.pb.mongodbapplication.DTO;

import lombok.*;
import pl.edu.pb.mongodbapplication.model.Flight;


@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class TicketDTO {
    private String _id;
    private Flight flight;
    private UserDTO userDTO;
    private Integer code;
}
