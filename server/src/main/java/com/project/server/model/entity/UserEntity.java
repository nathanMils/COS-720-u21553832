package com.project.server.model.entity;

import com.project.server.converter.StringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME",unique = true,nullable = false)
    @Convert(converter = StringConverter.class)
    private String username;
    @Column(name = "PASSWORD",nullable = false)
    @Convert(converter = StringConverter.class)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(name = "STUDENT_MODULE", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<ModuleEntity> modules;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //Not doing override for non expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Not doing override for non expired
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Not doing override for non expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Not doing override for non expired
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
