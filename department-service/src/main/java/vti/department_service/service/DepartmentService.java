package vti.department_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vti.department_service.dto.DepartmentDTO;
import vti.department_service.entity.Department;
import vti.department_service.repository.IDepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service

public class DepartmentService implements IDepartmentService {
    @Autowired
    private IDepartmentRepository repository;
    @Override
    public List<Department> getAllDepartments() {
        return repository.findAll();

    }

    @Override
    public String getDepartmentName(Integer id) {
        Optional<Department> departmentOpt = repository.findById(id);
        if(departmentOpt.isEmpty()) {
            return null;
        }
        Department department = departmentOpt.get();
        return department.getName();
    }
}
