package pl.edu.pb.mongodbapplication.config.error.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserNotFoundResponse  {
    private String message;
    private List<String> details;
    private int status;
}