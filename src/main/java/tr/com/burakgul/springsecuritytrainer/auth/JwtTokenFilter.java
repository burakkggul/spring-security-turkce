package tr.com.burakgul.springsecuritytrainer.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Burak GUL
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    public JwtTokenFilter(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    private TokenManager tokenManager;

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 FilterChain filterChain) throws ServletException, IOException {

        final String tokenCore = httpServletRequest.getHeader("Authorization");
        String token = null;
        String username = null;

        if (tokenCore != null && tokenCore.contains("Bearer")) {
            token = tokenCore.split(" ")[1];
            try {
                username = tokenManager.getUserFromToken(token);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if(token != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(tokenManager.hasTokenValid(token)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                        null,
                        new ArrayList<>());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
