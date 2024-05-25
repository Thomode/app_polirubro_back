package app.polirubro.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Resource
    private Environment env;

    public String getToken(UserDetails user) {
        // Add role claim for authorization
        Map<String, Object> extraClaims = new HashMap<>();
        GrantedAuthority authority = user.getAuthorities().toArray(new GrantedAuthority[0])[0];
        String role = authority.getAuthority();
        extraClaims.put("role", role);

        return getToken(extraClaims, user);
    }

    /**
     * Generates a token with extra . This is to provide flexibility to add extra claims to the token
     * @param extraClaim extra claims to be added to the token
     * @param user user to be added to the token
     * @return the generated token
     */
    private String getToken(Map<String, Object> extraClaim, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaim)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +
                        1000 * 60 * Long.parseLong(env.getProperty("token.jwt.duration"))))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("token.jwt.key"));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        return getClaim(token, claims -> claims.get("role", String.class));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (Exception ex) {
            System.out.println("Invalid JWT token");
        }
        return false;
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }
}
