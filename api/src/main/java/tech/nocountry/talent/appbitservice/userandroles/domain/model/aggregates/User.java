package tech.nocountry.talent.appbitservice.userandroles.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.CreateUserCommand;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.entities.Role;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Password;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.UserName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "User")
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditableAbstractAggregateRoot<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Embedded
    private UserName username;

    @Embedded
    private Password password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive;

    public User(CreateUserCommand command) {
        this.username = new UserName(command.username());
        this.password = new Password(command.password());
        this.roles = new ArrayList<>();
        Role roleToAssign = command.role() != null ? command.role() : Role.create(Roles.GENERAL_USER);
        this.roles.add(roleToAssign);
        this.isActive = command.isActive();
    }

    public User(UUID userId, UserName username, Password password, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.roles = new ArrayList<>();
    }

    public Role getRole() {
        if (this.roles.isEmpty()) {
            throw new IllegalStateException("El usuario debe tener al menos un rol");
        }
        return this.roles.getFirst();
    }

    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("El rol no puede ser null");
        }
        boolean exists = this.roles.stream()
                .anyMatch(r -> r.getStringName().equals(role.getStringName()));
        if (!exists) {
            this.roles.add(role);
        }
    }

    public void updateSecondaryRole(Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("El rol no puede ser null");
        }

        if (this.roles.size() == 1) {
            this.roles.add(newRole);
        }
        else {
            this.roles.set(this.roles.size() - 1, newRole);
        }
    }

    public List<String> getRolesAsStrings() {
        return this.roles.stream()
                .map(Role::getStringName)
                .toList();
    }

    public void updateUser(String username, String password, Boolean isActive) {
        if (username != null && !username.isEmpty()) {
            this.username = new UserName(username);
        }
        if (password != null && !password.isEmpty()) {
            this.password = new Password(password);
        }
        if (isActive != null) {
            this.isActive = isActive;
        }
    }

    public void updateIdentifierAndStatus(String username, Boolean isActive) {
        if (username != null && !username.isEmpty()) {
            this.username = new UserName(username);
        }
        if (isActive != null && isActive != this.isActive) {
            this.isActive = isActive;
        }
    }

    public void replaceRoles(List<Role> newRoles) {
        if (newRoles == null || newRoles.isEmpty()) {
            throw new IllegalArgumentException("La lista de roles no puede ser null o estar vacia");
        }
        this.roles.clear();
        this.roles.addAll(newRoles);
        
        if (this.username != null) {
            this.username = new UserName(this.username.value());
        }
    }
}
