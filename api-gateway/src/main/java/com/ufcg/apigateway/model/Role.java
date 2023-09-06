package com.ufcg.apigateway.model;

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
