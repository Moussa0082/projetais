package projet.ais.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import projet.ais.models.Acteur;
import projet.ais.repository.ActeurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
    @Autowired
    ActeurRepository acteurRepository;

    public String generateToken(Acteur acteur) {
        Acteur ac = acteurRepository.findByUsername(acteur.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("idActeur", ac.getIdActeur());
        claims.put("nomActeur", ac.getNomActeur());
        claims.put("telephone", ac.getTelephoneActeur());
        claims.put("whatsApp", ac.getWhatsAppActeur());
        claims.put("adresse", ac.getAdresseActeur());
        claims.put("username", ac.getUsername());
        claims.put("localite", ac.getLocaliteActeur());
        return createToken(claims, acteur.getCodeActeur());
    }
    
 

    private String createToken(Map<String, Object> claims, String subject) {
        // System.out.println("Claims ajoutés : " + claim!s);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire les informations personnalisées (nom, téléphone, etc.)
    public Map<String, Object> extractAdditionalInfo(String token) {
        final Claims claims = extractAllClaims(token);
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("idActeur", claims.get("idActeur"));
        additionalInfo.put("nomActeur", claims.get("nomActeur"));
        additionalInfo.put("username", claims.get("username"));
        additionalInfo.put("telephoneActeur", claims.get("telephoneActeur"));
        additionalInfo.put("whatsAppActeur", claims.get("whatsAppActeur"));
        additionalInfo.put("adresseActeur", claims.get("adresseActeur"));
        additionalInfo.put("localiteActeur", claims.get("localiteActeur"));

        System.out.println("JWT Claims: " + claims);
        System.out.println("Extracted Info: " + additionalInfo);

        return additionalInfo;
    }
}
