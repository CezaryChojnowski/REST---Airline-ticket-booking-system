package pl.edu.pb.mongodbapplication.DTO;

import lombok.*;
import pl.edu.pb.mongodbapplication.model.Flight;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class TicketDTOForTicketsListByUser {
    private String _id;
    private Flight flight;
    private Integer code;
}
