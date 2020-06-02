package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "Flight")
@ToString
public class Flight {
    @Id
    private Object _id;
    private String date;
    private Airport airPortFrom;
    private Airport airPortTo;
}
