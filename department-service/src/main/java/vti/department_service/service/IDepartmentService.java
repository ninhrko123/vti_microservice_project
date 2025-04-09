package vti.department_service.service;

import vti.department_service.dto.DepartmentDTO;
import vti.department_service.entity.Department;

import java.util.List;

public interface IDepartmentService {

    List<Department> getAllDepartments();
    String getDepartmentName(Integer id);
}
