package vti.department_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class DepartmentDTO {
    private String name;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cretedDate;

    private List<AccountDTO> accounts;


}
