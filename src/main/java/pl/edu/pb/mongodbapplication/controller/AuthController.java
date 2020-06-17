package pl.edu.pb.mongodbapplication.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pb.mongodbapplication.config.error.exception.ThePasswordCanNotBeEmptyException;
import pl.edu.pb.mongodbapplication.config.error.exception.UsernameIsAlreadyTakenException;
import pl.edu.pb.mongodbapplication.config.jwt.JwtUtils;
import pl.edu.pb.mongodbapplication.config.services.UserDetailsImpl;
import pl.edu.pb.mongodbapplication.model.ERole;
import pl.edu.pb.mongodbapplication.model.Roles;
import pl.edu.pb.mongodbapplication.model.User;
import pl.edu.pb.mongodbapplication.payload.request.LoginRequest;
import pl.edu.pb.mongodbapplication.payload.request.SignupRequest;
import pl.edu.pb.mongodbapplication.payload.response.JwtResponse;
import pl.edu.pb.mongodbapplication.payload.response.MessageResponse;
import pl.edu.pb.mongodbapplication.repository.RoleRepository;
import pl.edu.pb.mongodbapplication.repository.UserRepository;
import pl.edu.pb.mongodbapplication.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/auth")
@PropertySource("classpath:PL.exception.messages.properties")
public class AuthController {
    final
    AuthenticationManager authenticationManager;

    final
    UserRepository userRepository;

    final
    RoleRepository roleRepository;

    final
    PasswordEncoder encoder;

    final
    JwtUtils jwtUtils;

    final
    UserService userService;

    final
    Environment env;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, UserService userService, Environment env) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.env = env;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameIsAlreadyTakenException(env.getProperty("usernameIsAlreadyTaken"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new UsernameIsAlreadyTakenException(env.getProperty("emailIsAlreadyTaken"));
        }

        Set<Roles> roles = new HashSet<>();
        Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                roles);

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new ThePasswordCanNotBeEmptyException(env.getProperty("passwordCanNotBeEmpty"));
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signupAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody SignupRequest signUpRequest) {
        Set<Roles> roles = new HashSet<>();
        Roles userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                roles);

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
