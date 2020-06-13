package pl.edu.pb.mongodbapplication.model;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AirPort {
    private String country;
    private String city;
    private String airPortName;
    public String street;
    public int numer_of_building;
}
