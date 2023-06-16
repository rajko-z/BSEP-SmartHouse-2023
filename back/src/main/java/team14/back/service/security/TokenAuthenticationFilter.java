package team14.back.service.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import team14.back.dto.LogDTO;
import team14.back.enumerations.LogAction;
import team14.back.service.log.LogService;
import team14.back.service.user.UserServiceImpl;
import team14.back.utils.HttpUtils;
import team14.back.utils.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    private final UserDetailsService userDetailsService;

    private final LogService logService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = tokenUtils.getToken(request);
        String fingerprint = tokenUtils.getFingerprintFromCookie(request);

        try
        {
            if (authToken != null) {

                String username = tokenUtils.getUsernameFromToken(authToken);

                if (username != null) {

                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (tokenUtils.validateToken(authToken, userDetails, fingerprint)) {
                            TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                            authentication.setToken(authentication.getToken());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (UsernameNotFoundException e){
                        String CLS_NAME = UserServiceImpl.class.getName();
                        logService.addErr(new LogDTO(LogAction.UNKNOWN_USER, CLS_NAME, e.getMessage(), HttpUtils.getRequestIP(request)));
                        throw e;
                    }
                }
            }
        }
        catch (ExpiredJwtException ex) {
            // ignore
        }

        filterChain.doFilter(request, response);
    }
}
