package pl.edu.pb.mongodbapplication.config.error;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
