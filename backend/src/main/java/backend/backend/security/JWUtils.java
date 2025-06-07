package backend.backend.security;
import backend.backend.dao.UserDetailsInterface;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JWUtils {
    private static final String SECRET_KEY_STRING = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY1234567890abcdefghijklmnopqrstuvwxyz";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 864000000; // 10 dias

    public String generateToken(String dni, String rol){
        return Jwts.builder()
                .setSubject(dni)
                .claim("rol", rol)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String generateToken(String dni){
        return Jwts.builder()
                .setSubject(dni)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getDniFromToken(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRolFromToken(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    public boolean validateToken(String token, UserDetailsInterface userDetailsInterface){
        try{
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
