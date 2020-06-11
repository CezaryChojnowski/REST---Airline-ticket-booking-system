package pl.edu.pb.mongodbapplication.config.error.exception;

public class IncorrectTokenException extends RuntimeException{

    public IncorrectTokenException(String message) {
        super(message);
    }
}
