package eu.greyson.bank.general.config;

import eu.greyson.bank.general.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.access}")
    private Long ACCESS;

    @Value("${application.security.jwt.refresh}")
    private Long REFRESH;

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, REFRESH);
    }

    public String generateAccessToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(
        Map<String, Object> extraClaims, User user
    ) {
        return buildToken(extraClaims, user, ACCESS);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            Long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId() + "")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String id = extractUserId(token);
        return (id.equals(""+user.getId())) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
