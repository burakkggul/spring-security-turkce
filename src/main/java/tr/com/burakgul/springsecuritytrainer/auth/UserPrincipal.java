package tr.com.burakgul.springsecuritytrainer.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tr.com.burakgul.springsecuritytrainer.models.User;

import java.util.Collection;

/**
 * @author Burak GUL 16.09.2020
 * Spring security user authentication'ı gerçekleştirmek için UserDetails interface'ini implemente eden bir class'a ihtiyaç duyuyor.
 * Bu yüzden UserDetails'ı implemente edip metodlarını override ediyoruz.
 */
public class UserPrincipal implements UserDetails {

    private User user;//User bilgilerini alabilmek için User modelimizi ekliyoruz.

    public UserPrincipal(User user) {
        this.user = user;
    }


    /**
     * Role tanımı ile ilgili düzenlemeleri bu metod üzerinden yapabiliriz. Şu an ihtiyaç duymadığımız için
     * null dönecek şekilde bırakıyoruz.
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * password bilgisini almak için kullanacağımız metod.
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * username bilgisini almak için kullanacağımız metod.
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Hesabın süresinin dolup dolmadığını kontrol etmek için kullanılır.
     * Eğer süresi dolmuş ise false, dolmamış ise true gönderilir.
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Hesabın kilitli olup olmadığını belirtmek için kullanılır.
     * true döndürülmesi hesabın kilitli olmadığını anlamına gelir.
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Kullanıcının erişim anahtarının (parola) süresinin dolup dolmadığını anlamak için kullanılır.
     * Parola'nın süresi dolmamış ise true döner.
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Hesabın aktif olup olmadığını anlamak için kullanılır.
     * true döndürülmesi hesabın aktif olduğu anlamına gelir.
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
