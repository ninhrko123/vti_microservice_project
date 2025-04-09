package vti.department_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vti.department_service.entity.Department;

public interface IDepartmentRepository extends JpaRepository<Department, Integer> {
}
