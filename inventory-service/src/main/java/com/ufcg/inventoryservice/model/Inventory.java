package com.ufcg.inventoryservice.model;

import javax.persistence.*;
import lombok.*;

@Data
@Table(name = "t_inventory")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
