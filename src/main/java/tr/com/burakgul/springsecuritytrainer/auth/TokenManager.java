package tr.com.burakgul.springsecuritytrainer.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Burak GUL
 */
@Service
public class TokenManager {

    /**
     * Constructor injection ile environment'ı servisimize bağımlılık olarak ekliyoruz.
     * Bu sayede propertyler üzerinden bazı değerleri parametrik olarak alabileceğiz.
     *
     * @param environment
     */
    @Autowired
    public TokenManager(Environment environment) {
        this.environment = environment;
    }

    private Environment environment;

    /**
     * JWT oluşturmak için kullanacağımız key'i application.properties üzerinden alıyoruz.
     */
    private final String key = environment.getProperty("jwt.secret.key", "verySecretKey");

    /**
     * Ne kadar sürede expire olacağı bilgisini de application.properties üzerinden alıyoruz.
     * 60*100 yaparak bu dakika değerini milisaniye cinsine çeviriyoruz.
     */
    private final int expireTime = Integer.parseInt(environment.getProperty("token.expire.minute", "5")) * 60 * 1000;

    /**
     *
     * Token'ı oluştuyoruz.
     * @param username
     * @return
     */
    public String generateToken(String username) {
        long timeMillis = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuer("burakgul.com.tr")
                .setIssuedAt(new Date(timeMillis))
                .setExpiration(new Date(timeMillis + expireTime))
                .signWith(SignatureAlgorithm.ES256, key)
                .compact();
        return null;

    }

    public Boolean hasTokenValid(String token) {
        return null;

    }

    public String getUserFromToken(String token) {
        return null;

    }

}
