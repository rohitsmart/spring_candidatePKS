package com.candidate.pks.auth.service;

import com.candidate.pks.auth.dto.AddEmployeeRequest;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.auth.repository.UserRepository;
import com.candidate.pks.repeat.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final UserRepository userRepository;


    public Response addEmployee(AddEmployeeRequest request) {
        Employee employee = Employee.builder()
                .empId(request.getEmpId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .designation(request.getDesignation())
                .build();

        String password = request.getFirstName() + "@" + request.getEmpId();
        User user = User.builder()
                .username(request.getUserRequest().getEmail())
                .password(new User().getHashPassword(password))
                .role(UserRole.Employee)
                .active(true)
                .employee(employee)
                .build();
        userRepository.save(user);
        return new Response("Employee and user created successfully.");
    }
}
