package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static com.ndirituedwin.Utils.AppConstants.USERAVATARS;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@ConfigurationProperties(prefix = "file")
public class User extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 30)
    private String email;
    private String avatar;


    @NotBlank
    private String password;
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name="role_id")
             )
    private Set<Role> roles=new HashSet<>(0);
    @Transient
    public String getPhotosImagePath() {
//                final Path fileStorageLocation= Path.of(USERAVATARS.concat("/").concat(String.valueOf(currentUser.getId())));

        if (avatar == null || id == null) return null;
          return USERAVATARS+"/"+id+"/"+avatar;
//        return "/"+USERAVATARS+"/" + id + "/" + avatar;
    }

}
