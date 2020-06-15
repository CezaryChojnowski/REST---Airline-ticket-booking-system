package pl.edu.pb.mongodbapplication.config.error.exception;

public class UsernameIsAlreadyTakenException extends RuntimeException{
    public UsernameIsAlreadyTakenException(String message) {
        super(message);
    }
}
