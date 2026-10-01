package GMCA.gmca_api.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final byte[] secret;
    private final long expirationSeconds;

    public JwtService(@Value("${gmca.jwt.secret}") String secret,
                      @Value("${gmca.jwt.expiration-minutes}") long expirationMinutes) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationMinutes * 60;
    }

    public String issue(String username, List<String> roles) {
        try {
            var now = Instant.now().getEpochSecond();
            var unsigned = encodeJson("{\"alg\":\"HS256\",\"typ\":\"JWT\"}") + "." +
                    encodeJson("{\"sub\":\"" + escape(username) + "\",\"roles\":[" + roleJson(roles) + "],\"iat\":" + now + ",\"exp\":" + (now + expirationSeconds) + "}");
            return unsigned + "." + sign(unsigned);
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el token JWT", ex);
        }
    }

    public Map<String, Object> verify(String token) {
        try {
            var parts = token.split("\\.");
            if (parts.length != 3 || !MessageDigest.isEqual(sign(parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
                throw new IllegalArgumentException("Firma JWT invalida");
            }
            var payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            var exp = Long.parseLong(extractNumber(payload, "exp"));
            if (Instant.now().getEpochSecond() >= exp) {
                throw new IllegalArgumentException("Token expirado");
            }
            return Map.of("sub", extractString(payload, "sub"), "roles", extractRoles(payload));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Token JWT invalido", ex);
        }
    }

    private String encodeJson(String json) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) throws Exception {
        var mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }

    private String roleJson(List<String> roles) {
        return roles.stream().map(role -> "\"" + escape(role) + "\"").reduce((a, b) -> a + "," + b).orElse("");
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extractString(String json, String field) {
        var marker = "\"" + field + "\":\"";
        var start = json.indexOf(marker) + marker.length();
        var end = json.indexOf('"', start);
        return json.substring(start, end);
    }

    private String extractNumber(String json, String field) {
        var marker = "\"" + field + "\":";
        var start = json.indexOf(marker) + marker.length();
        var end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return json.substring(start, end);
    }

    private List<String> extractRoles(String json) {
        var marker = "\"roles\":[";
        var start = json.indexOf(marker) + marker.length();
        var end = json.indexOf(']', start);
        var raw = json.substring(start, end).trim();
        var roles = new ArrayList<String>();
        if (raw.isEmpty()) return roles;
        for (var part : raw.split(",")) roles.add(part.trim().replace("\"", ""));
        return roles;
    }
}