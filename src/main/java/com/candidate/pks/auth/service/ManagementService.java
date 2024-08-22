package com.candidate.pks.auth.service;

import com.candidate.pks.auth.dto.AddEmployeeRequest;
import com.candidate.pks.auth.dto.EmployeeData;
import com.candidate.pks.auth.dto.EmployeeResponseDTO;
import com.candidate.pks.auth.dto.UserEmployeeResponseList;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.auth.repository.UserRepository;
import com.candidate.pks.exception.BadDateAndTimeFormatException;
import com.candidate.pks.exception.BadRequestException;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagementService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final MailService mailService;

    public Response addEmployee(AddEmployeeRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            log.info("Adding employee with ID: {}", request.getEmpId());
            LocalDate joiningDate;
            try {
                joiningDate = LocalDate.parse(request.getJoiningDate(), formatter);
            } catch (DateTimeParseException e) {
                log.error("Invalid date format for joiningDate: {}", request.getJoiningDate());
                throw new BadDateAndTimeFormatException("Invalid date format for joiningDate. Please use dd/MM/yyyy.");
            }

            Employee employee = Employee.builder()
                    .empId(request.getEmpId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .designation(request.getDesignation())
                    .joiningDate(joiningDate)
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

            try {
                log.info("Sending email to: {}", request.getUserRequest().getEmail());
                mailService.sendUserPassword(request.getUserRequest().getEmail(), request.getFirstName(), request.getLastName(), request.getEmpId(), password);
            } catch (MessagingException | IOException e) {
                log.error("Error sending email to {}: {}", request.getUserRequest().getEmail(), e.getMessage());
                return new Response("Employee created, but email sending failed.");
            }

            log.info("Employee and user created successfully with ID: {}", request.getEmpId());
            return new Response("Employee and user created successfully.");

        } catch (Exception e) {
            log.error("Error adding employee: {}", e.getMessage());
            throw new BadRequestException("Error adding employee: " + e.getMessage());
        }
    }

    public Page<EmployeeResponseDTO> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(employee -> EmployeeResponseDTO.builder()
                .empId(employee.getId())
                .name(employee.getFirstName() + " " + employee.getLastName())
                .designation(employee.getDesignation().name())
                .email(employee.getUser() != null ? employee.getUser().getUsername() : null)
                .joiningDate(String.valueOf(employee.getJoiningDate()))
                .build());
    }

    public UserEmployeeResponseList getUserEmployeeData(String search) {
        try {
            List<EmployeeData> employeeDataList = employeeRepository.searchByName(search);

            UserEmployeeResponseList userEmployeeResponse = new UserEmployeeResponseList();
            userEmployeeResponse.setData(true);
            userEmployeeResponse.setEmployeeData(employeeDataList);

            return userEmployeeResponse;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
