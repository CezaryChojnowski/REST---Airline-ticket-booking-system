package pl.edu.pb.mongodbapplication.config.error.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
