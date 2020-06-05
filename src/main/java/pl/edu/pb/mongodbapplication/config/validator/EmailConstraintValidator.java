package pl.edu.pb.mongodbapplication.config.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("[A-Za-z0-9.]+@[A-Za-z0-9]+[.]+[A-Za-z]{2,4}");
    }
}
