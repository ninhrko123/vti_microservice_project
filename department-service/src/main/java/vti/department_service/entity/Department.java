package vti.department_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "department")
@NoArgsConstructor
public class Department {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name",length = 100,nullable = false,unique = true)
    private String name;

    @Column(name="total_member")
    private int totalMember;

    @Column(name = "created_at",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name ="type", columnDefinition = "ENUM('DEV', 'TEST', 'SCRUM_MASTER', 'PM')")
    private DepartmentType type;

    @OneToMany(mappedBy = "department")
    private List<Account> accounts;

    public enum DepartmentType {
        DEV, TEST, SCRUM_MASTER,PM;

        public static DepartmentType toEnum(String type) {
            for (DepartmentType item: values()){
                if (item.toString().equals(type)){
                    return item;
                }
            }
            return null;
        }
    }

}
