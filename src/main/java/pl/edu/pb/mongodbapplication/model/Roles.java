package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "Roles")
@ToString
public class Roles {
    @Id
    private String id;

    private ERole name;
}
