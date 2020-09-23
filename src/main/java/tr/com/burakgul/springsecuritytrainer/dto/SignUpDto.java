package tr.com.burakgul.springsecuritytrainer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Burak GUL 23.09.2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String username;
    private String password;
}
