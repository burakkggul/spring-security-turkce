package tr.com.burakgul.springsecuritytrainer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Burak GUL 17.09.2020
 * Bu class ile kullanıcıdan verileri alıp uygulama içerisinde taşımak için kullanacağız.
 * Database'e kaydetmek kullanıcı bilgilerinin database'de bulunan verilerle karşılaştırılması gibi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
