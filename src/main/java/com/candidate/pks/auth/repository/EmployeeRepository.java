package com.candidate.pks.auth.repository;

import com.candidate.pks.auth.dto.EmployeeData;
import com.candidate.pks.auth.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    @Query("SELECT new com.candidate.pks.auth.dto.EmployeeData(e.empId, CONCAT(e.firstName, ' ', e.lastName)) " +
            "FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<EmployeeData> searchByName(@Param("search") String search);

    @Query("select e from Employee e where e.empId = ?1")
    Optional<Employee> findByEmpId(String empId);
}
