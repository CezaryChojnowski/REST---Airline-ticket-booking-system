package pl.edu.pb.mongodbapplication.config.error.exception;

public class ThePasswordCanNotBeEmptyException extends RuntimeException{
    public ThePasswordCanNotBeEmptyException(String message) {
        super(message);
    }
}
