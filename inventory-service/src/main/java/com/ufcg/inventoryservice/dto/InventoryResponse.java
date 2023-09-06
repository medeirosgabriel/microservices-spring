package com.ufcg.inventoryservice.dto;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
@Data
public class InventoryResponse {

    private String skuCode;
    private boolean isInStock;
}
