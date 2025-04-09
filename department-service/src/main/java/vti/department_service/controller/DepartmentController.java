package vti.department_service.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vti.department_service.dto.DepartmentDTO;
import vti.department_service.entity.Department;
import vti.department_service.service.DepartmentService;
import vti.department_service.service.IDepartmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/departments")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<DepartmentDTO> getDepartments() {
        List<Department> departments = departmentService.getAllDepartments();

        return departments.stream()
                .map(dept -> modelMapper.map(dept, DepartmentDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/name/{id}")
    public String getDepartmentName(@PathVariable("id") Integer id) {
        return departmentService.getDepartmentName(id);
    }

}
