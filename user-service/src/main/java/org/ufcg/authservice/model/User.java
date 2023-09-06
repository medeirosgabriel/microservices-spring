package org.ufcg.authservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@EqualsAndHashCode
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, // load info with the user
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "tb_user_role", // third table name
            joinColumns = @JoinColumn(name = "user_id"), // user column name
            inverseJoinColumns = @JoinColumn(name = "role_id") // role column name
    )
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<>();

}
