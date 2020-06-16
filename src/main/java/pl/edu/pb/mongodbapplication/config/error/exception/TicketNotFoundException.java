package pl.edu.pb.mongodbapplication.config.error.exception;


public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(String message) {
        super(message);
    }
}
