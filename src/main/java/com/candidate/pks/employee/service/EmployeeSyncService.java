package com.candidate.pks.employee.service;

import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeSyncService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void syncEmployees() {
        log.info("Starting employee synchronization process...");

        String url = apiUrl + "/api/employee/all";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            log.info("Received response from external API with status: {}", response.getStatusCode());

            List<Map<String, Object>> employeesFromApi = (List<Map<String, Object>>) response.getBody().get("employee");
            log.info("Fetched {} employees from the API", employeesFromApi.size());

            Set<String> employeeIdsFromApi = employeesFromApi.stream()
                    .map(emp -> (String) emp.get("employee_id"))
                    .collect(Collectors.toSet());

            List<Employee> existingEmployees = employeeRepository.findAll();
            Set<String> existingEmployeeIds = existingEmployees.stream()
                    .map(Employee::getEmpId)
                    .collect(Collectors.toSet());

            log.info("Found {} existing employees in the database", existingEmployees.size());
            existingEmployees.stream()
                    .filter(emp -> !employeeIdsFromApi.contains(emp.getEmpId()))
                    .forEach(emp -> {
                        log.info("Deleting employee with ID: {} and their associated user", emp.getEmpId());
                        userRepository.delete(emp.getUser()); // Delete the associated user
                        employeeRepository.delete(emp); // Delete the employee
                    });

            for (Map<String, Object> emp : employeesFromApi) {
                String email = (String) emp.get("email");
                String empId = (String) emp.get("employee_id");
                String firstName = (String) emp.get("first_name");
                String lastName = (String) emp.get("last_name");
                String designationStr = (String) emp.get("designation");
                LocalDate joiningDate = parseDate((String) emp.get("date_of_joining"));
                List<String> roles = (List<String>) emp.get("roles");

                Optional<Employee> existingEmployeeOpt = employeeRepository.findByEmpId(empId);

                if (existingEmployeeOpt.isPresent()) {
                    Employee employee = existingEmployeeOpt.get();
                    employee.setFirstName(firstName);
                    employee.setLastName(lastName);
                    employee.setDesignation(designationStr);
                    employee.setJoiningDate(joiningDate);
                    User user = employee.getUser();
                    updateUserRoles(user, roles);
                    userRepository.save(user); // Save the user first
                    employeeRepository.save(employee);
                    log.info("Updated employee with ID: {}", empId);
                } else {
                    String password = generatePassword(firstName, empId);


                    Employee newEmployee = Employee.builder()
                            .empId(empId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .designation(designationStr)
                            .joiningDate(joiningDate)
                            .build();
                    User newUser = User.builder()
                            .username(email)
                            .roles(roles.stream().map(role -> UserRole.valueOf(role.toUpperCase())).collect(Collectors.toSet()))
                            .active(true)
                            .password(new User().getHashPassword(password))
                            .employee(newEmployee)
                            .build();

                    User savedUser = userRepository.save(newUser);
                    log.info("Created new employee with ID: {}", empId);
                }
            }

            log.info("Employee synchronization process completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during employee synchronization: {}", e.getMessage(), e);
        }
    }

    private void updateUserRoles(User user, List<String> roles) {
        if (!roles.isEmpty()) {
            Set<UserRole> userRoles = roles.stream()
                    .map(role -> UserRole.valueOf(role.toUpperCase()))
                    .collect(Collectors.toSet());
            user.setRoles(userRoles);
            log.info("Updated roles for user: {}", user.getUsername());
        }
    }

    private String generatePassword(String firstName, String empId) {
        // Ensure the first letter of the first name is capitalized
        String capitalizedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        return capitalizedFirstName + "@" + empId;
    }

    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final DateTimeFormatter STORAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parseDate(String date) {
        if ("N/A".equals(date) || date == null || date.isEmpty()) {
            return null;
        }
        try {
            // Parse date from API format (M/d/yyyy)
            LocalDate parsedDate = LocalDate.parse(date, API_DATE_FORMATTER);
            // Optionally, format it to the desired format for storage or display
            String formattedDate = parsedDate.format(STORAGE_DATE_FORMATTER);
            return LocalDate.parse(formattedDate, STORAGE_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.info("Unable to parse date: {}", date);
            return null; // or handle as needed
        }
    }
}
