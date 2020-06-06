package pl.edu.pb.mongodbapplication.config.error;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
