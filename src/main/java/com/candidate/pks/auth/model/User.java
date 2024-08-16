package com.candidate.pks.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private boolean active = false;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.Employee;


    @OneToOne(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Transient
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean matchesPassword(String providedPassword) {
        return passwordEncoder.matches(providedPassword, this.password);
    }

    private static final String PASSWORD_CHARACTERS = "0123456789";
    private static final int PASSWORD_LENGTH = 4;

    public String getRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(PASSWORD_CHARACTERS.length());
            password.append(PASSWORD_CHARACTERS.charAt(randomIndex));
        }

        return password.toString();
    }

    public String getHashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}