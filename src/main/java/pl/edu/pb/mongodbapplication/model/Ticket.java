package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "Ticket")
public class Ticket {
    private String _id;
    @DBRef
    private Flight flight;
    @DBRef
    private User user;
    private Integer code;

    public Ticket(Flight flight, User user, Integer code) {
        this.flight=flight;
        this.user=user;
        this.code=code;
    }
}
