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
@EqualsAndHashCode(callSuper = false)
@Document(collection = "Flight")
@ToString
@PropertySource("classpath:messages.properties")
public class Flight{
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

    public Flight(@NotNull(message = "{flight.date}") LocalDate date, @NotNull(message = "{flight.time}") LocalTime time, @NotNull(message = "{flight.airPorts}") AirPort airPortFrom, @NotNull(message = "{flight.airPorts}") AirPort airPortTo, @NotNull int price) {
        this.date = date;
        this.time = time;
        this.airPortFrom = airPortFrom;
        this.airPortTo = airPortTo;
        this.price = price;
    }
}
