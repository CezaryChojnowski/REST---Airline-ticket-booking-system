package pl.edu.pb.mongodbapplication.DTO;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
public class CountryDTO {
    private String state;
    private Set<CityDTO> cities;
}
