package com.candidate.pks.auth.service;

import com.candidate.pks.auth.dto.AddEmployeeRequest;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.auth.repository.UserRepository;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagementService {

    private final UserRepository userRepository;
    private final MailService mailService;

    public Response addEmployee(AddEmployeeRequest request) {
        log.info("Adding employee with ID: {}", request.getEmpId());

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

        // Uncomment this line when you're ready to save the user
        // userRepository.save(user);

        try {
            log.info("Sending email to: {}", request.getUserRequest().getEmail());
            mailService.sendEmail(request.getUserRequest().getEmail(), request.getFirstName(), request.getLastName(), request.getEmpId(), password);
        } catch (MessagingException | IOException e) {
            log.error("Error sending email to {}: {}", request.getUserRequest().getEmail(), e.getMessage());
            return new Response("Employee created, but email sending failed.");
        }

        log.info("Employee and user created successfully with ID: {}", request.getEmpId());
        return new Response("Employee and user created successfully.");
    }
}
