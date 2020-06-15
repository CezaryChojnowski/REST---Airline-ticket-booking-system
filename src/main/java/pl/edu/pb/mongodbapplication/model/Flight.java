package pl.edu.pb.mongodbapplication.model;

import lombok.*;
import org.springframework.context.annotation.PropertySource;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.RepresentationModel;

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

public class Flight extends RepresentationModel<Flight> {
    private String _id;
    @NotNull(message = "{flight.date}")
    @Indexed
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
