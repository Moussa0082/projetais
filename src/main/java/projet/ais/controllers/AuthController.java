package projet.ais.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projet.ais.CodeGenerator;
import projet.ais.repository.ActeurRepository;
import projet.ais.services.ActeurService;
import projet.ais.models.Acteur;
import projet.ais.util.JwtUtil;

@RestController
@RequestMapping("/api-koumi/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private  ActeurRepository acteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private ActeurService acteurService;

    @Autowired
    CodeGenerator codeGenerator;
    
    @PostMapping("/login")
    @Operation(summary = "Authentification avec jwt")
    public ResponseEntity<?> login(@RequestBody Acteur acteur) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(acteur.getUsername(), acteur.getPassword()));
            if (authentication.isAuthenticated()) {
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtil.generateToken(acteur));
                authData.put("type", "Bearer");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username ou mot de passe incorrect");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username ou mot de passe incorrect");
        }
    }
   
    @PostMapping("/pinLogin")
    @Operation(summary = "Connexion d'un Acteur ")
    public Acteur connexionActeur(@RequestBody Map<String, String> loginData) {
        String codeActeur = loginData.get("codeActeur");
        String password = loginData.get("password");
        return acteurService.connexionActeurWithPin(codeActeur, password);
    }
    
}
