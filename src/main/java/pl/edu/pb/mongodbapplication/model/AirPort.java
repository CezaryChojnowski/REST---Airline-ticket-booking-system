package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "Airport")
public class AirPort {
    private String _id;
    private String country;
    private String city;
    private String airPortName;
}
