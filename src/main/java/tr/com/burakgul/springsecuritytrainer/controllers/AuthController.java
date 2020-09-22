package tr.com.burakgul.springsecuritytrainer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import tr.com.burakgul.springsecuritytrainer.models.User;
import tr.com.burakgul.springsecuritytrainer.repositorys.UserRepository;

@RestController
@RequestMapping("/login")
public class AuthController {

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

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        try {
            userRepository.save(new User("burak",passwordEncoder.encode("123")));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
            return ResponseEntity.ok(tokenManager.generateToken(loginDto.getUsername()));

        }catch (Exception e){
            //TODO log
            throw e;
        }
    }

}
