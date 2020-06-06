package pl.edu.pb.mongodbapplication.config.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.pb.mongodbapplication.config.error.response.*;

import org.springframework.security.access.AccessDeniedException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

    @Value("${noFlights}")
    private String noFlights;

    @Value("${missingParameter}")
    private String missingParameter;

    @Value("${validationFailed}")
    private String validationFailed;

    @Value("${reservationNotFound}")
    private String reservationNotFound;

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

    @ExceptionHandler(NoFlightsBetweenTheseCitiesOnThisDayException.class)
    public ResponseEntity<Object> handleNoFlightsBetweenTheseCitiesOnThisDayException(NoFlightsBetweenTheseCitiesOnThisDayException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        NoFlightsBetweenTheseCitiesOnThisDayResponse error = new NoFlightsBetweenTheseCitiesOnThisDayResponse(noFlights, details, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        ReservationNotFoundResponse error = new ReservationNotFoundResponse(reservationNotFound, details, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException exception) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
        InvalidDataResponse error = new InvalidDataResponse(validationFailed, details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        MissingServletRequestParameterResponse error = new MissingServletRequestParameterResponse(missingParameter, details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(
                error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            details.add(error.getMessage());
        }
        ConstraintViolationResponse constraintViolationResponse =
                new ConstraintViolationResponse(validationFailed ,details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(constraintViolationResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> details = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            error.getField() + ": " +
                    details.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//            error.getObjectName() + ": " +
                    details.add(error.getDefaultMessage());
        }
        MethodArgumentNotValidResponse methodArgumentNotValidResponse = new MethodArgumentNotValidResponse(validationFailed, details, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<Object>(methodArgumentNotValidResponse, HttpStatus.BAD_REQUEST);
    }

}
