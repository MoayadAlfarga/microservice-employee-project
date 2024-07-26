package com.security_service.security_service_employee.dto;

import com.security_service.security_service_employee.model.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String phoneNumber;
    private String address;
    private String jopTitle;
    private String department;
    private String departmentCode;
    private String status;
    private String email;
    private String password;
    private Role role;

}
