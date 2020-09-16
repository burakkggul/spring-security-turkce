package tr.com.burakgul.springsecuritytrainer.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.burakgul.springsecuritytrainer.models.User;

/**
 * @author Burak GUL 16.09.2020
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
