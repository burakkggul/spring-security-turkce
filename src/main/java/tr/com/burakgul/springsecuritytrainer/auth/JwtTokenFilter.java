package tr.com.burakgul.springsecuritytrainer.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tr.com.burakgul.springsecuritytrainer.models.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Burak GUL
 * SpringSecurityTrainerConfiguration classında configure metodunun almış olduğu
 * HttpSecurity argümanına eklemiş olduğumuz filter class'ı olarak bu classı kullanacağız.
 * Bu class OncePerRequestFilter classını extend eden bir classtır.
 * Gelen istekler üzerinde controller'a gitmeden önce yapılması gereken işlemler OncePerRequestFilter'ın
 * doFilterInternal metodunu override etmemiz gerekmektedir. Gelen her istek ilk olarak bu metodda yapılan işlemlerden geçmektedir.
 */
@Component //Spring Context'e bu class'dan bir bean oluşturarak dahil etmek için bu anotasyonu kullanıyoruz.
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    public JwtTokenFilter(TokenManager tokenManager, UserDetailService userDetailService){
        this.tokenManager = tokenManager;
        this.userDetailService = userDetailService;
    }

    private TokenManager tokenManager;
    private UserDetailService userDetailService;

    /**
     * Bu metod gelen her isteği karşılamaktadır.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 FilterChain filterChain) throws ServletException, IOException {

        /*
            Öncelikle gelen istekten token'ı almak için header'dan Authorization'ı istiyoruz.
            Eğer token varsa token'ın core hali yani "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJidXJhayIsImlzcyI6ImJ1cmF
            rZ3VsLmNvbS50ciIsImlhdCI6MTYwMTEyOTIxMiwiZXhwIjoxNjAxMTI5NTEyfQ.1yu2aMoY6iL2HPDrz2PhkYpmV3Cp3x39HxMyWTN
            Hj89xmh7XYE-a7ZJt9tCnSLRlNaxZfKoJOgsO4q0tYLd0Jg" stringine benzer bir string gelecektir.
            yoksa bu değer null olacaktır.
        */
        final String tokenCore = httpServletRequest.getHeader("Authorization");
        String token = null;
        String username = null;

        /*
            Burada gelen istekte token olup olmadığını kontrol ediyoruz. Eğer varsa Bearer kısmını atarak token kısmını alıyoruz.
            Sonrasında tokenManager'dan bu token'ın username'ini ne olduğuna dair bilgiyi istiyoruz. Burada username bilgisi gelmiyorsa bu token hatalı bir tokendır
            bu yüzden bir sonraki if bloğuna girmeyecek ve spring security context'e authenticate olamayacaktır.
            Bir hata oluşması durumunda hatayı loglayarak devam ediyoruz.
        */
        if (tokenCore != null && tokenCore.contains("Bearer") && tokenCore.split(" ").length >1) {
            token = tokenCore.split(" ")[1];
            try {
                username = tokenManager.getUserFromToken(token);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        /*
            token ve username değişkenlerinin null olup olmadığını ve Context'in authenticate olup olmadığını kontol ediyoruz
            eğer authenticate olunmamışsa token'ın geçerli olup olmadığını kontrol ediyoruz. Token geçerli ise UsernamePasswordAuthenticationToken'ı
            username ile oluşturup context'e authentication'ı setliyoruz.
        */
        if(token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(tokenManager.hasTokenValid(token)){
                UserDetails user = this.userDetailService.loadUserByUsername(username);
                if(Objects.nonNull(user)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                            null,
                            new ArrayList<>());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        /*
        Servlet Filter chain of responsibility tasarım kalıbını implemente eden bir class olduğu için bu kısımda bir
        sonraki adıma devam etmesini sağlıyoruz.
        Detaylı bilgi için bkz:
        https://github.com/yusufyilmazfr/tasarim-desenleri-turkce-kaynak/tree/master/chain-of-responsibility/java
        https://www.mehmetcemyucel.com/2014/chain-of-responsibility-design-pattern/        */
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
