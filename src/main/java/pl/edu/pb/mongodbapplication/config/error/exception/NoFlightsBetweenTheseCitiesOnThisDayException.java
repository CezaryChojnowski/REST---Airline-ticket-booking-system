package pl.edu.pb.mongodbapplication.config.error.exception;

public class NoFlightsBetweenTheseCitiesOnThisDayException extends RuntimeException{
    public NoFlightsBetweenTheseCitiesOnThisDayException(String message) {
        super(message);
    }
}
