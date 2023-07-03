package slash.financing.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "GACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERAGACHIBINDERA";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Using method reference of a built in mehtod
    // to extract a expiration date
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Function with generic return type
    // Basically it accepts token and a function for getting
    // a needed field of a token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        // using apply method of a function type to use a function on claims
        return claimsResolver.apply(claims);
    }

    // Generation of token withot extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generation of token
    // For now I use email as a subject and UserDetails interface only has
    // method called getUsername(), thats why. I will figure it out later
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                // setting parts of token
                .setClaims(extraClaims)
                // using email as a subject as of this moment
                .setSubject(userDetails.getUsername())
                // setting when this jwt was created for further validation
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                // using signature to sign a token and settin an algorithm
                .signWith(getSigInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Gettin all claims (parts) of jwt
    // first it sets the sign key to verify the token
    // then it users creted parser to extract the jwt claims
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // making signature using secret key
    private Key getSigInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
