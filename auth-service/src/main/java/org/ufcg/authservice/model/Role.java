package org.ufcg.authservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String roleName;

}
