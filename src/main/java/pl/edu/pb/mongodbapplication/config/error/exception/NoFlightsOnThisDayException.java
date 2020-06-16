package pl.edu.pb.mongodbapplication.config.error.exception;

public class NoFlightsOnThisDayException extends RuntimeException{
    public NoFlightsOnThisDayException(String message) {
        super(message);
    }
}
