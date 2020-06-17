package pl.edu.pb.mongodbapplication.config.error.exception;

public class ThereAreTicketsForTheGivenFlightException extends RuntimeException{
    public ThereAreTicketsForTheGivenFlightException(String message) {
        super(message);
    }
}
