package vti.auth_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 10, nullable = true)
    private String username;

    @Column(name = "firstname", length = 50, nullable = true)
    private String firstName;

    @Column(name = "lastname", length = 50, nullable = true)
    private String lastName;

    @Column(name = "email", length = 50, nullable = true)
    private String email;

    @Column(name = "password", length = 150, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "access_token", length = 150, nullable = true)
    private String accessToken;

    @Column(name = "refresh_token", length = 150, nullable = true)
    private String refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
