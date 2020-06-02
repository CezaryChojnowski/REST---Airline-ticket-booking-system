package pl.edu.pb.mongodbapplication.model;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Airport {
    private String country;
    private String city;
    private String airPortName;
}
