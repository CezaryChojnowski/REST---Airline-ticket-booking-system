package pl.edu.pb.mongodbapplication.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pb.mongodbapplication.config.error.exception.UserNotFoundException;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.repository.UserRepository;

import java.util.Optional;


@Service
public class UserService {

    final
    PasswordEncoder encoder;

    final
    UserRepository userRepository;

    public UserService(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
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
            throw new UserNotFoundException("User with email " + email + " not found");
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
