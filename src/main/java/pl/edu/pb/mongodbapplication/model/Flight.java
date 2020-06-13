package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "Flight")
@ToString
@PropertySource("classpath:messages.properties")
public class Flight {
    private String _id;
    @NotNull(message = "{flight.date}")
    private LocalDate date;
    @NotNull(message = "{flight.time}")
    private LocalTime time;
    @NotNull(message = "{flight.airPorts}")
    private AirPort airPortFrom;
    @NotNull(message = "{flight.airPorts}")
    private AirPort airPortTo;
    @NotNull
    private int price;
}
