package pl.edu.pb.mongodbapplication.config.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EmailConstraintValidator.class)
@Target({FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidEmail {

    String message() default "Niepoprawny format emaila, przyklad:  xyz@xyz.xyz";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
