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
import tr.com.burakgul.springsecuritytrainer.models.User;
import tr.com.burakgul.springsecuritytrainer.repositorys.UserRepository;

/**
 * @author Burak GUL 09.22.2020
 * Authentication işlemlerini bu sınıf üzerindeki rest endpointleri ile dışarıdan erişilebilir hale getireceğiz.
 */
@RestController //Controller anotasyonunun daha özel bir halidir.
@RequestMapping("/auth") //Bu anotasyon ile gelen istekler ile sınıflar eşleştirilir.
public class AuthController {

    /*
        Loglama için bir instance oluşturuyoruz.
        Ayrıntılı bilgi için bkz: http://www.slf4j.org/manual.html
    */
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    /*
        Bu sınıf üzerinde kullanacağımız bağımlılıkları inject ediyoruz.
    */
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

    /**
     * Gelen kullanıcı adı ve parola bilgilerine göre authentication sağlanır.
     * response header'a generate edilen jwt token setlenir ve 200 http status kodu ile Login Success ibaresi response body'de gönderilir.
     * Bilgilerde bir hata varsa 400 http status kodu ile The username or password is incorrect. Please try again.
     * ibaresi response body'de gönderilir.
     * @param loginDto
     * @return
     */
    @PostMapping("/login") //auth/login 'e gelen web istekleri ile bu metodun eşleştirilmesi sağlanır.
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        try {
            if(loginDto != null && loginDto.getUsername() != null && loginDto.getPassword() != null){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization","Bearer " + tokenManager.generateToken(loginDto.getUsername()));
                return ResponseEntity.ok().headers(headers).body("Login Success");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The username or password is incorrect. Please try again.");

        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Kullanıcıdan alınan username ve password bilgisi database'e kaydedilir. 200 http status kodu ile username
     * has been successfully registered ibaresi gönderilir. Eğer bir sorun oluşursa 400 http status kodu ile
     * The username or password is incorrect. Please try again. response body'de gönderilir.
     * @param loginDto
     * @return
     */
    @PostMapping("/signup") //auth/signup 'a gelen web istekleri ile bu metodun eşleştirilmesi sağlanır.
    public ResponseEntity<String> signUp(@RequestBody LoginDto loginDto){
        try {
            if(loginDto != null && loginDto.getUsername() != null && loginDto.getPassword() != null){
                userRepository.save(new User(loginDto.getUsername(),passwordEncoder.encode(loginDto.getPassword())));
                return ResponseEntity.ok(loginDto.getUsername() +" has been successfully registered.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The username or password is incorrect. Please try again.");

        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
