package pl.edu.pb.mongodbapplication.config.error.exception;


public class DoNotHaveAccessToThisTicketException extends RuntimeException{
    public DoNotHaveAccessToThisTicketException(String message) {
        super(message);
    }
}

