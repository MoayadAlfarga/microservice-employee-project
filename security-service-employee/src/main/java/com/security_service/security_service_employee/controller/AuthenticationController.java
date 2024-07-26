package com.security_service.security_service_employee.controller;

import com.security_service.security_service_employee.dto.AuthenticationResponse;
import com.security_service.security_service_employee.dto.LoginRequestDto;
import com.security_service.security_service_employee.dto.RegistrationRequestDto;
import com.security_service.security_service_employee.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @PostMapping("/add")
    public ResponseEntity<AuthenticationResponse> registrationNewUser(@RequestBody RegistrationRequestDto
                                                                                registrationRequestDto) {
        return new ResponseEntity<>(authenticationService.registrationNewUser(registrationRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginRequestDto requestDto) {
        return new ResponseEntity<>(authenticationService.loginUser(requestDto), HttpStatus.OK);
    }
    @GetMapping("/validate/{token}")
    public String validateToken(@PathVariable("token") String token) {
        authenticationService.validateToken(token);
        return token+"This token is valid ";
    }


}
