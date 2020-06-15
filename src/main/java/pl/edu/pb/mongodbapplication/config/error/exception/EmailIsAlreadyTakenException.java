package pl.edu.pb.mongodbapplication.config.error.exception;

public class EmailIsAlreadyTakenException extends RuntimeException{
    public EmailIsAlreadyTakenException(String message) {
        super(message);
    }
}
