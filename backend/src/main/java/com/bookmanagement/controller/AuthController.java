//
//package com.bookmanagement.controller;
//
//import com.bookmanagement.dto.JwtResponse;
//import com.bookmanagement.dto.LoginRequest;
//import com.bookmanagement.dto.MessageResponse;
//import com.bookmanagement.dto.SignupRequest;
//import com.bookmanagement.model.User;
//import com.bookmanagement.repository.UserRepository;
//import com.bookmanagement.security.jwt.JwtUtils;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping("/auth")
//@Tag(name = "Authentification", description = "Endpoints pour l'authentification des utilisateurs")
//public class AuthController {
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Autowired
//    JwtUtils jwtUtils;
//
//    @PostMapping("/signin")
//    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
//            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
//    })
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
//
//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getEmail(),
//                userDetails.getFirstName(),
//                userDetails.getLastName()));
//    }
//
//    @PostMapping("/signup")
//    @Operation(summary = "Inscription utilisateur", description = "Crée un nouvel utilisateur")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Inscription réussie"),
//            @ApiResponse(responseCode = "400", description = "Email déjà utilisé")
//    })
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already taken!"));
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()),
//                signUpRequest.getFirstName(),
//                signUpRequest.getLastName());
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
//
//    @GetMapping("/verify")
//    @Operation(summary = "Vérifier le token", description = "Vérifie la validité du token JWT")
//    @SecurityRequirement(name = "bearerAuth")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Token valide"),
//            @ApiResponse(responseCode = "401", description = "Token invalide")
//    })
//    public ResponseEntity<?> verifyToken() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
//            return ResponseEntity.ok(userDetails);
//        }
//        return ResponseEntity.status(401).body(new MessageResponse("Token invalid"));
//    }
//}
