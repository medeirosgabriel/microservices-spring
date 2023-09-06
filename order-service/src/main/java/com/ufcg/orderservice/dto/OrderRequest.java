package com.ufcg.orderservice.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderLineItemsDTO> orderLineItemsList;
}
