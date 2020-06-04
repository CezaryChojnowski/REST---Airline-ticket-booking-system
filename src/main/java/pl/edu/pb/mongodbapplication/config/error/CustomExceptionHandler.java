package pl.edu.pb.mongodbapplication.config.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.pb.mongodbapplication.config.error.response.AccessDeniedResponse;
import pl.edu.pb.mongodbapplication.config.error.response.AuthenticationResponse;

import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${loginFailed}")
    private String loginFailed;

    @Value("${accessDenied}")
    private String accessDenied;

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        AuthenticationResponse error = new AuthenticationResponse(loginFailed, details, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        AccessDeniedResponse error = new AccessDeniedResponse(accessDenied, details, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity(error, HttpStatus.FORBIDDEN);
    }

}
