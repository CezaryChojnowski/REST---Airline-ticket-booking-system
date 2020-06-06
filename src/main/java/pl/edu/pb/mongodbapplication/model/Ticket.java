package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "Ticket")
public class Ticket implements Comparator<Ticket> {
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

    @Override
    public int compare(Ticket o1, Ticket o2) {
        return o2.getFlight().getDate().compareTo(o1.getFlight().getDate());
    }
}
