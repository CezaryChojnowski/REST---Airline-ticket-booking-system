package pl.edu.pb.mongodbapplication.config.error;

public class NoFlightsBetweenTheseCitiesOnThisDayException extends RuntimeException{
    public NoFlightsBetweenTheseCitiesOnThisDayException(String message) {
        super(message);
    }
}
