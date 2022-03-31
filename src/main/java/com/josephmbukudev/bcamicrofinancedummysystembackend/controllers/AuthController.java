package com.josephmbukudev.bcamicrofinancedummysystembackend.controllers;

import com.josephmbukudev.bcamicrofinancedummysystembackend.models.ERole;
import com.josephmbukudev.bcamicrofinancedummysystembackend.models.Role;
import com.josephmbukudev.bcamicrofinancedummysystembackend.models.User;
import com.josephmbukudev.bcamicrofinancedummysystembackend.payload.request.LoginRequest;
import com.josephmbukudev.bcamicrofinancedummysystembackend.payload.request.SignUpRequest;
import com.josephmbukudev.bcamicrofinancedummysystembackend.payload.response.JwtResponse;
import com.josephmbukudev.bcamicrofinancedummysystembackend.payload.response.MessageResponse;
import com.josephmbukudev.bcamicrofinancedummysystembackend.repository.RoleRepository;
import com.josephmbukudev.bcamicrofinancedummysystembackend.repository.UserRepository;
import com.josephmbukudev.bcamicrofinancedummysystembackend.security.jwt.JwtUtils;
import com.josephmbukudev.bcamicrofinancedummysystembackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
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

//  Login API
    //  - Authenticate the username, password
    //  - update SecurityContext using Authentication object
    //  - generate JWT
    //  - get UserDetails from Authentication object
    //  - response contains JWT and UserDetails data
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

//  Register API
    //  - Check existing username/email.
    //  - create new User (with ROLE_LOAN_APPLICANT if not specifying the role)
    //  - save User to database using UserRepository
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already on use!"));
        }

        //  Create New user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        // Constant error message if role not found.
        String roleErrorMessage = "Error: Role is not found.";

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_LOAN_APPLICANT)
                    .orElseThrow(() -> new RuntimeException(roleErrorMessage));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "supervisor":
                        Role supervisorRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                                .orElseThrow(() -> new RuntimeException(roleErrorMessage));
                        roles.add(supervisorRole);
                        break;

                    case "credit_officer":
                        Role creditOfficerRole = roleRepository.findByName(ERole.ROLE_CREDIT_OFFICER)
                                .orElseThrow(() -> new RuntimeException(roleErrorMessage));
                        roles.add(creditOfficerRole);
                        break;

                    case "office_admin":
                        Role officeAdminRole = roleRepository.findByName(ERole.ROLE_OFFICE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(roleErrorMessage));
                        roles.add(officeAdminRole);
                        break;

                    default:
                        Role loanApplicantRole = roleRepository.findByName(ERole.ROLE_LOAN_APPLICANT)
                                .orElseThrow(() -> new RuntimeException(roleErrorMessage));
                        roles.add(loanApplicantRole);

                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
    }

}

