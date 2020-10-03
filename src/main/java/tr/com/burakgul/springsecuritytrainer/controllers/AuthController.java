package tr.com.burakgul.springsecuritytrainer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.burakgul.springsecuritytrainer.auth.TokenManager;
import tr.com.burakgul.springsecuritytrainer.dto.LoginDto;
import tr.com.burakgul.springsecuritytrainer.dto.SignUpDto;
import tr.com.burakgul.springsecuritytrainer.models.User;
import tr.com.burakgul.springsecuritytrainer.repositorys.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          TokenManager tokenManager,
                          UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder){
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.userRepository=userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private AuthenticationManager authenticationManager;

    private TokenManager tokenManager;

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer " + tokenManager.generateToken(loginDto.getUsername()));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
            return ResponseEntity.ok().headers(headers).body("Login Success");

        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        try {
            if(signUpDto != null && signUpDto.getUsername() != null && signUpDto.getPassword() != null){
                userRepository.save(new User(signUpDto.getUsername(),passwordEncoder.encode(signUpDto.getPassword())));
                return ResponseEntity.ok(signUpDto.getUsername() +" has been successfully registered.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The username or password is incorrect. Please try again.");

        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
