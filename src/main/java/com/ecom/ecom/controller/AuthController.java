package com.ecom.ecom.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.ecom.dto.AuththenticationRequest;
import com.ecom.ecom.dto.SignupRequest;
import com.ecom.ecom.dto.UserDto;
import com.ecom.ecom.entity.User;
import com.ecom.ecom.repository.UserRepository;
import com.ecom.ecom.services.auth.AuthService;
import com.ecom.ecom.utils.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_STRING = "Bearer ";

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public void createAuthTenticaion(@RequestBody AuththenticationRequest auththenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        logger.info("Received signup request: {}", auththenticationRequest);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auththenticationRequest.getUsername(), auththenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorret Password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(auththenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            // response.getWriter().write(new JSONObject().put("userId", optionalUser.get().getId())
            // .put("role", optionalUser.get().getRole()).toString());

            JSONObject jsonResponse = new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .put("token", jwt);

// Enviar la respuesta
            response.getWriter().write(jsonResponse.toString());
        }
        response.addHeader(HEADER_STRING, TOKEN_STRING + jwt);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        logger.info("Received signup request: {}", signupRequest);

        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("User Already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);

        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

}
