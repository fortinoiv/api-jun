package com.fortinoiv.apijun.application;

import com.fortinoiv.apijun.domain.constans.ERole;
import com.fortinoiv.apijun.domain.dto.request.LoginRequestDTO;
import com.fortinoiv.apijun.domain.dto.request.SignupRequestDTO;
import com.fortinoiv.apijun.domain.dto.response.JwtResponseDTO;
import com.fortinoiv.apijun.domain.dto.response.MessageResponseDTO;
import com.fortinoiv.apijun.domain.entities.Role;
import com.fortinoiv.apijun.domain.entities.User;
import com.fortinoiv.apijun.repository.RoleRepository;
import com.fortinoiv.apijun.repository.UserRepository;
import com.fortinoiv.apijun.configuration.jwt.JwtUtils;
import com.fortinoiv.apijun.domain.dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthApplication {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<JwtResponseDTO> authenticateUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<MessageResponseDTO> registerUser(SignupRequestDTO signUpRequestDTO) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequestDTO.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDTO("Error: El Usuario ya existe!"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequestDTO.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDTO("Error: El Correo ya existe!"));
        }

        // Create new user's account
        User user = new User(signUpRequestDTO.getUsername(),
                signUpRequestDTO.getEmail(),
                encoder.encode(signUpRequestDTO.getPassword()));

        Set<String> strRoles = signUpRequestDTO.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no se encuentra."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no se encuentra."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no se encuentra."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no se encuentra."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponseDTO("Usuario registrado con éxito!"));
    }
}
