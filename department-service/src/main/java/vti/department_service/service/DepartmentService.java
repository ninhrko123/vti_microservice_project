package vti.department_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vti.department_service.dto.DepartmentDTO;
import vti.department_service.entity.Department;
import vti.department_service.repository.IDepartmentRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class DepartmentService implements IDepartmentService {
    @Autowired
    private IDepartmentRepository repository;

    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments = repository.findAll();
        return repository.findAll();
    }
}
