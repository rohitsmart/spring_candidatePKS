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


    public Page<EmployeeResponseDTO> getEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(employee -> EmployeeResponseDTO.builder()
                .empId(employee.getId())
                .name(employee.getFirstName() + " " + employee.getLastName())
                .designation(employee.getDesignation())
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
