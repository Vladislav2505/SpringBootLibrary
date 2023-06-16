package com.topchiev.springboot.springbootlibrary.services;

import com.topchiev.springboot.springbootlibrary.models.Employee;
import com.topchiev.springboot.springbootlibrary.repositories.EmployeeRepository;
import com.topchiev.springboot.springbootlibrary.security.EmployeeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByUsername(username);

        if(employee.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден");

        return new EmployeeDetails(employee.get());
    }
}
