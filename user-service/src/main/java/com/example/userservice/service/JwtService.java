package com.example.userservice.service;

import com.example.userservice.entity.Token;
import com.example.userservice.entity.User;
import com.example.userservice.enums.TokenType;
import com.example.userservice.repository.TokenRepository;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret_key = "Rahmahhhhhseccreettkkeeyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy";
    private final long accessTokenValidity = 60 * 60 * 1000;
    private final JwtParser jwtParser;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    public JwtService() {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key).build();
    }

    public String generateToken(User user, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .compact();

    }

    public Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest request) {
        try {
            String bearerToken = resolveToken(request);
            if (bearerToken == null) return null;
            return jwtParser.parseClaimsJws(bearerToken).getBody();
        } catch (ExpiredJwtException ex) {
            request.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            request.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        return !expiration.before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseJwtClaims(token);
            String email = claims.getSubject();
            return (email.equals(userDetails.getUsername()) && isTokenExpired(token));
        } catch (Exception ex) {
            return false;
        }

    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = parseJwtClaims(token);
            String email = claims.getSubject();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return (isTokenExpired(token));
        } catch (Exception ex) {
            return false;
        }
    }

    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusSeconds(accessTokenValidity))
                .build();
        tokenRepository.save(token);
    }

}
