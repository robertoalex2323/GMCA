package GMCA.gmca_api.controller;

import GMCA.gmca_api.config.JwtAuthenticationFilter;
import GMCA.gmca_api.model.Role;
import GMCA.gmca_api.repository.UserRepository;
import GMCA.gmca_api.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository users;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository users, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = users.findByUsername(request.username()).orElseThrow();
        var roles = user.getRoles().stream().map(Role::name).sorted().toList();
        var token = jwtService.issue(user.getUsername(), roles);
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from(JwtAuthenticationFilter.TOKEN_COOKIE, token)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(8))
                .build()
                .toString());
        return new LoginResponse(token, user.getUsername(), roles);
    }

    @PostMapping("/logout")
    void logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from(JwtAuthenticationFilter.TOKEN_COOKIE, "")
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build()
                .toString());
    }

    record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    record LoginResponse(String token, String username, List<String> roles) {}
}