package pl.edu.pb.mongodbapplication.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.config.error.exception.ThePasswordCanNotBeEmptyException;
import pl.edu.pb.mongodbapplication.config.error.exception.UserNotFoundException;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.repository.UserRepository;

import java.util.Optional;


@Service
@PropertySource("classpath:PL.exception.messages.properties")
public class UserService {

    final
    PasswordEncoder encoder;

    final
    UserRepository userRepository;

    final
    Environment env;

    public UserService(PasswordEncoder encoder, UserRepository userRepository, Environment env) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.env = env;
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User findUserByEmail(String email){
        Optional<User> user = userRepository.findUserByEmail(email);
        if(!user.isPresent()){
            throw new UserNotFoundException(env.getProperty("userWithGivenEmailNotFound") + " " + email);
        }
        return user.get();
    }

    public String generateRandomStringToResetPassword(){
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils. random(length, useLetters, useNumbers);
    }

    public User setToken(String email){
        User user = findUserByEmail(email);
        user.setToken(generateRandomStringToResetPassword());
        userRepository.save(user);
        return user;
    }

    public boolean checkToken(String code, String email){
        User user = findUserByEmail(email);
        return user.getToken().equals(code);
    }

    public void updatePassword(String password, String email){
        if(password == null || password.isEmpty()){
            throw new ThePasswordCanNotBeEmptyException(env.getProperty("passwordCanNotBeEmpty"));
        }
        User user = findUserByEmail(email);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    public void resetToken(String email){
        User user = findUserByEmail(email);
        user.setToken(null);
        userRepository.save(user);
    }

}
