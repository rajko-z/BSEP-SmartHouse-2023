package team14.back.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import team14.back.config.JWTConfig;
import team14.back.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenUtils {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String AUDIENCE_WEB = "web";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String FINGERPRINT_CLAIM = "fingerprint";

    private final JWTConfig jwtConfig;

    public String generateToken(String username, String fingerprint) {
        return Jwts.builder()
                .setIssuer(jwtConfig.appName())
                .setSubject(username)
                .setAudience(AUDIENCE_WEB)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .claim(FINGERPRINT_CLAIM, generateFingerprintHash(fingerprint))
                .signWith(SIGNATURE_ALGORITHM, jwtConfig.jwtSecret())
                .compact();
    }

    public String generateFingerprint() {
        final SecureRandom secureRandom = new SecureRandom();
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);
        return DatatypeConverter.printHexBinary(randomFgp);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails userDetails, String fingerprint) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        boolean usernameValid = username != null && userDetails.getUsername().equals(username);
        boolean isCreatedAfterLastPasswordReset = user.getLastPasswordResetDate() == null ||  (created != null && created.after(user.getLastPasswordResetDate()));
        boolean isAlgorithmValid = SIGNATURE_ALGORITHM.getValue().equals(getAlgorithmFromToken(token));
        boolean isFingerPrintValid = isFingerprintValid(token, fingerprint);

        return usernameValid &&
               isCreatedAfterLastPasswordReset &&
               isFingerPrintValid &&
               isAlgorithmValid;
    }

    private boolean isFingerprintValid(String token, String fingerprint) {
        if (fingerprint == null || fingerprint.trim().length() == 0) {
            return false;
        }
        String fingerprintHash = generateFingerprintHash(fingerprint);
        String fingerprintFromToken = getFingerprintFromToken(token);

        return fingerprintHash.equals(fingerprintFromToken);
    }

    private Date getIssuedAtDateFromToken(String token) {
        try {
            Claims claims =  Jwts.parser()
                    .setSigningKey(jwtConfig.jwtSecret())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getIssuedAt();
        } catch (Exception e) {
            return null;
        }
    }

    private String getAlgorithmFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtConfig.jwtSecret())
                    .parseClaimsJws(token)
                    .getHeader()
                    .getAlgorithm();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims =  Jwts.parser()
                    .setSigningKey(jwtConfig.jwtSecret())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private String getFingerprintFromToken(String token) {
        try {
            Claims claims =  Jwts.parser()
                    .setSigningKey(jwtConfig.jwtSecret())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(FINGERPRINT_CLAIM, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtConfig.jwtExpiresIn());
    }

    public String getFingerprintFromCookie(HttpServletRequest request) {
        String userFingerprint = null;
        if (request.getCookies() != null && request.getCookies().length > 0) {
            List<Cookie> cookies = Arrays.stream(request.getCookies()).toList();
            Optional<Cookie> cookie = cookies.stream().filter(c -> "Fingerprint".equals(c.getName())).findFirst();

            if (cookie.isPresent()) {
                userFingerprint = cookie.get().getValue();
            }
        }
        return userFingerprint;
    }

    private String generateFingerprintHash(String userFingerprint) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(userFingerprintDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
