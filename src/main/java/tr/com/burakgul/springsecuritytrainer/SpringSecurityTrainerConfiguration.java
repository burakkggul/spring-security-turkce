package tr.com.burakgul.springsecuritytrainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tr.com.burakgul.springsecuritytrainer.auth.JwtTokenFilter;
import tr.com.burakgul.springsecuritytrainer.auth.UserDetailService;

/**
 * @author Burak GUL 16.09.2020
 *
 * Spring security otomatik konfigürasyonu devre dışı bırakmak için gerekli konfigürasyonların yapıldığı
 * Spring Security'nin WebSecurityConfigurerAdapter sınıfını extend eden sınıf.
 * Daha esnek bir yapı olması adına (birden fazla user role gibi) propertie üzerinden değil class üzerinden yapıyoruz.
 */

/*
    Spring Bean'e dahil etmek ve bunun bir konfigürasyon olduğunu belirtmek için bu anotasyonu kullanıyoruz.
 */
@Configuration
/*
    Oto konfigürasyonu etkisiz hale getirebilmek için bu anotasyon çok önemlidir.
    Bu anotasyon springSecurityFilterChain olarak bilinen bir servlet filter'ı oluşturarak
    gelen giden isteklerin filtrelenmesini ve bunun üzerinden authetication authorization işlemlerinin yapılmasını sağlar.
    Daha fazla bilgi için: https://docs.spring.io/spring-security/site/docs/4.0.1.RELEASE/reference/html/jc.html#hello-web-security-java-configuration
*/
@EnableWebSecurity
/*
    Metod bazında güvenlik kontrolü için @EnableGlobalMethodSecurity anotasyonu kullanılabilir.
    Bu anotasyon kullanıldığında metodlara @Secured anotasyonu ile o metodu hangi role sahip kullanıcının koşturacağı belirtilebilir.
    prePostEnabled = true vererek @Secured yapılan metoddan önce ve sonra @Pre... ve @Post... anotasyonları ile bazı kontroller vb işlemlerin yapılmasına izin verilir.
*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityTrainerConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Kullanacağımız bağımlılıkları inject (dependency injection) ediyoruz.
     * @param jwtTokenFilter
     * @param userDetailService
     */
    @Autowired
    public SpringSecurityTrainerConfiguration(JwtTokenFilter jwtTokenFilter,
                                              UserDetailService userDetailService) {
        this.jwtTokenFilter=jwtTokenFilter;
        this.userDetailService=userDetailService;
    }

    /**
     * AuthenticationManager oluşturmak için kullanılır. userDetailService adıyla yazmış olduğumuz UserDetailService interface'ini implemente eden classımızı
     * tanıtarak authentication'ı bu servis üzerinden yapmasını sağlıyoruz. Tabi son olarak password encode olarak kullanacağımız class'ı da bu builder'a veriyoruz.
     * Burada autowired ile oluşturmamızın sebebi burada bir bean oluşturulması ve başka classlardan da erişilebilir hale getirmektir.
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configurePasswordEncoder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(getPasswordEncoder());
    }

    private JwtTokenFilter jwtTokenFilter;

    private UserDetailService userDetailService;

    /**
     * AuthenticationManager'ı spring context'e dahil etmek için bir instance return ediyoruz.
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerInit() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Encoder yapacağımız class'ı spring context'e dahil etmek için bir instance retun ediyoruz.
     * @return
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Security konfigürasyonlarını bu metod üzerinden yapıyoruz.
     * Doğrulamayı nasıl yapacağımız, eğer izin vermek istiyorsak ki bizim uygulamamızda /auth/, /healthCheck ve /h2/
     * yollarında isteklerin reddedilmemesi her halükarda izim verilmesini istedik. Bunun dışında yine biz uygulamamızda
     * <code>.headers().frameOptions().sameOrigin()</code> ile aynı origin'e sahip iframelere (h2 konsolu iframeler ile çalışır) izin verdik.
     * Yine uygulamamızda <code>.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)</code> kullanarak Spring Security
     * tarafından HttpSession oluşturulmamasını ve bunun security context'e asla dahil edilmemesi gerektiğini bildirdik.
     * Sonrasında filtreleyecek class'ın hangisi olduğunu belirten addFilterBefor metodunu kullanıyoruz.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/auth/**","/h2/**").permitAll().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().headers().frameOptions().sameOrigin();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
