package tr.com.burakgul.springsecuritytrainer.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Burak GUL 15.09.2020
 */
@Service
public class TokenManager {

    /**
     * Constructor injection ile environment'ı servisimize bağımlılık olarak ekliyoruz.
     * Bu sayede propertyler üzerinden bazı değerleri parametrik olarak alabileceğiz.
     *
     * @param key
     * @param expireTime
     */
    @Autowired
    public TokenManager(@Value("${jwt.secret.key : verySecretKey}") String key,
                        @Value("#{${token.expire.minute : 5} * 60 * 1000}") int expireTime) {
        this.key = key;
        this.expireTime=expireTime;
    }

    /**
     * JWT oluşturmak için kullanacağımız key'i application.properties üzerinden alıyoruz.
     */
    private final String key ;

    /**
     * Ne kadar sürede expire olacağı bilgisini de application.properties üzerinden alıyoruz.
     * 60*100 yaparak bu dakika değerini milisaniye cinsine çeviriyoruz.
     */
    private final int expireTime ;

    /**
     * Json Web Token ile ilgili detaylı bilgi için şu makaleden faydanlanabilirsiniz.
     * https://medium.com/bili%C5%9Fim-hareketi/json-web-tokens-jwt-nedir-4d10c7e692f4
     * Bu metod JWT Token'ı oluşturmaktadır.
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {
        long timeMillis = System.currentTimeMillis();
        /*pom.xml'de dahil etmiş olduğumuz JWT token bağımlılığını kullanarak tokenımızı üretiyoruz.
        Burada token'ı kütüphane kullanmadan kendi metodlarımızı yazarak da oluşturabilirdik.
        Fakat daha sonrasında muhtemelen bakım, dökümantasyon ve okunabilirlik konularında güçlük yaşayacaktık.*/

        String token = Jwts.builder() //Jwts factory tasarım kalıbını implemente eden bir sınıf. Factory tasarım kalıbı
                //ile ilgili olarak https://github.com/yusufyilmazfr/tasarim-desenleri-turkce-kaynak/tree/master/factory/java
                // adresinden örnek kodlara ulaşabilirsiniz.
                .setSubject(username) //Tokenın payloadında bulunacak subject (bu token kim için oluşturuldu) alanın setlenmesi.
                .setIssuer("burakgul.com.tr") //Issuer (oluşturan kurum kuruluş kişi) alanın setlenmesi.
                .setIssuedAt(new Date(timeMillis)) //Oluşturulma zamanının setlenmesi.
                .setExpiration(new Date(timeMillis + expireTime)) //Expire zamanının setlenmesi.
                .signWith(SignatureAlgorithm.HS512, key) //Şifreleme algoritmasının ve tokenı encrypt (şifreleme)
                // decrypt (şifre çözme) yapmasını sağlayacak gizli keyin setlenmesi.
                .compact(); //Tokenı string'e çevirecek metod.
        return token;
    }

    /**
     * Tokenın geçerli olup olmadığını doğrulamak için kullanılacak metod.
     * Burada doğrulama için iki şartımız olacak birincisi kullanıcı adımızın null olmaması.
     * İkincisi ise tokenın expire (süresi geçmiş) olmaması.
     *
     * @param token
     * @return
     */
    public Boolean hasTokenValid(String token) {
        if (getUserFromToken(token) != null && hasTokenExpire(token)) {
            return true;
        }
        return false;
    }

    /**
     * username'i token içerisinden almak için kullanacağımız metod.
     *
     * @param token
     * @return
     */
    public String getUserFromToken(String token) {
        Claims claims = parseToken(token); //Tokenı parse etmek için yazdığımız metodumuzu çağırıyoruz.
        return claims.getSubject(); //Kullanıcı adını claimsler arasından alıyoruz.

    }

    /**
     * Token'ın expire olup olmadığını kontrol edecek metod.
     *
     * @param token
     * @return
     */
    public boolean hasTokenExpire(String token) {
        Claims claims = parseToken(token); //Tokenı tekrardan parse edip claimlerimizi alıyoruz.
        Date now = new Date(System.currentTimeMillis()); //Bir zaman oluşturuyoruz.
        return claims.getExpiration().before(now); //expire time'ın yukarda oluştumuş olduğumuz zamandan sonra
        // olup olmadığını kontrol ediyoruz.
    }

    /**
     * Tokenı parse etmek için kullanacağımız metod.
     *
     * @param token
     * @return
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key) //Kullanıcıdan aldığımız tokenı parse etmek için secret keyimizi setliyoruz.
                .parseClaimsJws(token) //Parse edilecek tokenı veriyoruz bu kısım bize Jws<Claims> dönecektir.
                .getBody(); //Asıl kullanacağımız kısım olan body kısmını bu şekilde Claims nesnesi halinde almamızı sağlayacak.
    }

}
