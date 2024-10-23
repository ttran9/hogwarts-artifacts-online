package edu.tcu.cs.hogwartsartifactsonline.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiresIn = 2; // 2 hours. So token is only good for 2 hours.

        // Prepare a claim called authorities.
        String authorities = authentication.getAuthorities().stream()
//                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")); // Must be space delimited.

        // note: build pattern again.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self") // not using a dedicated authorization server.
                .issuedAt(now)
                .expiresAt(now.plus(expiresIn, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
