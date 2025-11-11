package jwtrest;

import static jwtrest.Constants.REMEMBERME_VALIDITY_SECONDS;
import io.jsonwebtoken.*;
import java.io.Serializable;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;

@Named
public class TokenProvider implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TokenProvider.class.getName());
    private static final String AUTHORITIES_KEY = "auth";

    private String privateKey;
    private String publicKey;
    private PrivateKey myprivateKey;
    private PublicKey mypublicKey;
    private long tokenValidity;
    private long tokenValidityForRememberMe;

    @PostConstruct
    public void init() {
        this.privateKey = "-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBe+pg8m/jkWQCgkTdJJDihWAWmc5SyJ25TgIO791q/OYbAJLvO/vmn80svEwxAZ07s58lZlFn5Ozpq+EuoBclICJ5tvPLyJMdIop4pta7BnDBENEKztiJ7LIe3wlinPH9TfV7/ZTUfUGpD/F0OvnKodXrl+PVJ2r6AzEpYS0LQ/ZAmCzZfK6IJEoZYvLwPLcPVfv5s/MCVE/uj4tRUl0IY4V1FS16SdANUpnU8U+RuTjWONomf8d3wvlAM2WUW09mFpbWLV6BFkwVTCHeElbFvZpN7XlrO7G5NhBGB9lqNxZP9/Qvux4qlwqAwrLSeh1nsAOVWRJ2w9GExJVBTgVlAgMBAAECggEAAdD/PgpAnScOBI0DBv1zI8FDSesHOhU6j9UI5WmAj2LQ6TN77aWHQ56/7xnUcEhW2MripVf8zyghxj7QFh84IGfZEwHx73mSUf1zRdcxIF/a2qElCUAwXbkcYfhPjv6wseNTuOaESWtknKjy6BeupSWYS8YpBCUC1taFVWFdaiPkw8nJYPbai1MvzW3nZnKr31JMgTHAGgG4VNArFCTMmCCdfO+78WTiIr/j+yul5Vl56vgSLd/o/dkB6KzE/YFaYqkEqR0bX8pSypGfYRksZGkJJQH4K6YMpVjaBLLGyiS6BOijbYMNKlx2Aw8UEF6tTqallVvH7sxlr44daN3d8QKBgQDbB2IA+/qrjhuy44pMqZThLJpgiIaH/YRE4YLZwiclnMDvFXRYYR1mcziaWkkK573btm0qTsjrmZiPKQCLHmnArs7OyXBMDbs/Mk+WKh12S5K8QmNVz7bzLkPD9C4X7u4z5tu56qZNC57z6ds1EQkLZtxFGsL/9nPc/DCWxExdcQKBgQDiJLKd+lRXk67eus4200O8gdEBEz4lh1TX9kTOjkiOT1uYiVpa7BmSbXcAmRUMY4H2RLp2IfMKJ5ssHwe4H3Y7Y1/H/rAtpK8ruOSkXlLq2DvnfjqyEc4P094R1pCh/viC3IKMzSbX0JZcEYJvHCc+mLgvPL/EqL5vF5ab/Ib9NQKBgGJ3nDijD5uGpK80ml1Cs9rTaYfSeOC0OX2aAHCTV3QSV65kb8y3xDblv+Gsiz/q1TDsf4FQsAUzJSHfJg5lGtfz/qd6ahDW74JGxP7Wai5fZVVbZzsRycbj2rVClmJOGSqeM9QOSLtEaS5wyQq/YNiOYqJymI3oJ0iG2/U7xLURAoGALdr1IRWGjq+SkPVeJT3XiVzlbYtiWafEa3ozX5L4YWr0Ds0jNjaTxN5PeB6SZw2yZ6ZuDNA7gP6g92RfY1V12vr+jAY34Tl4j6wRKMc7lwU7uGgfLMZxe0Ih0Ioqj76s05Q1IKnky3QvWQHv6enSh13eUy3FUPJKkyo8Tur40FUCgYEApwdwanLrnoTmNJnjXR1VZw7BAuAsPBwjo3kQrdKpmCIbAETRmMaclN3ChUGzaXo54jt9k9EQt2oC0UWSsr2o09Q976oJ1mmWiG0et1bVXhNpxcoHlug0qlb7PzUVh0ct6b8rkH+xvWM7QABzH7/aQpUQWPdntclzaeQjYm/NNsI=-----END PRIVATE KEY-----";

        this.publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwXvqYPJv45FkAoJE3SSQ4oVgFpnOUsiduU4CDu/davzmGwCS7zv75p/NLLxMMQGdO7OfJWZRZ+Ts6avhLqAXJSAiebbzy8iTHSKKeKbWuwZwwRDRCs7YieyyHt8JYpzx/U31e/2U1H1BqQ/xdDr5yqHV65fj1Sdq+gMxKWEtC0P2QJgs2XyuiCRKGWLy8Dy3D1X7+bPzAlRP7o+LUVJdCGOFdRUteknQDVKZ1PFPkbk41jjaJn/Hd8L5QDNllFtPZhaW1i1egRZMFUwh3hJWxb2aTe15azuxuTYQRgfZajcWT/f0L7seKpcKgMKy0nodZ7ADlVkSdsPRhMSVQU4FZQIDAQAB-----END PUBLIC KEY-----";

        try {
            // Clean PEM formatting
            privateKey = privateKey.replaceAll("-----.*?-----", "").replaceAll("\\s", "");
            publicKey = publicKey.replaceAll("-----.*?-----", "").replaceAll("\\s", "");

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            myprivateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            mypublicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.tokenValidity = TimeUnit.HOURS.toMillis(10);
        this.tokenValidityForRememberMe = TimeUnit.SECONDS.toMillis(REMEMBERME_VALIDITY_SECONDS);
    }

    public String createToken(String username, Set<String> authorities, Boolean rememberMe) {
        long now = System.currentTimeMillis();
        long validity = rememberMe ? tokenValidityForRememberMe : tokenValidity;

        System.out.println("TokenProvider - In createToken");

        return Jwts.builder()
                .setSubject(username)
                .setIssuer("localhost")
                .claim(AUTHORITIES_KEY, String.join(",", authorities))
                .signWith(myprivateKey, SignatureAlgorithm.RS256)
                .setExpiration(new Date(now + validity))
                .compact();
    }

    public JWTCredential getCredential(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(mypublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        System.out.println("TokenProvider - In getCredential");

        Set<String> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .collect(Collectors.toSet());

        return new JWTCredential(claims.getSubject(), authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            System.out.println("TokenProvider - validateToken");
            Jwts.parserBuilder()
                    .setSigningKey(mypublicKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid JWT token: {0}", e.getMessage());
            return false;
        }
    }
}
