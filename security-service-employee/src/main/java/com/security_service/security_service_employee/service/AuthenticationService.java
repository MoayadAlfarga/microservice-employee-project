package com.security_service.security_service_employee.service;

import com.security_service.security_service_employee.dto.AuthenticationResponse;
import com.security_service.security_service_employee.dto.LoginRequestDto;
import com.security_service.security_service_employee.dto.RegistrationRequestDto;
import com.security_service.security_service_employee.model.Role;
import com.security_service.security_service_employee.model.User;
import com.security_service.security_service_employee.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {
    private JwtGenerateService jwtGenerateService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;


    public AuthenticationResponse registrationNewUser(RegistrationRequestDto
                                                                registrationRequestDto) {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RuntimeException("User Already Exists ");
        }
        User user = User.builder()
                  .firstName(registrationRequestDto.getFirstName())
                  .lastName(registrationRequestDto.getLastName())
                  .age(registrationRequestDto.getAge())
                  .phoneNumber(registrationRequestDto.getPhoneNumber())
                  .address(registrationRequestDto.getAddress())
                  .jopTitle(registrationRequestDto.getJopTitle())
                  .department(registrationRequestDto.getDepartment())
                  .departmentCode(registrationRequestDto.getDepartmentCode())
                  .status(registrationRequestDto.getStatus())
                  .email(registrationRequestDto.getEmail())
                  .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                  .role(Role.ADMIN_HR_MANAGER)
                  .build();
        log.info("User save Successfully ");
        userRepository.save(user);
        String jwtToken = jwtGenerateService.generateToken(user);
        log.info("Successfully Generate Token ");
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse loginUser(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                  (loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow();
        String jwtToken = jwtGenerateService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void validateToken(String token) {
        jwtGenerateService.validateToken(token);
    }
}



