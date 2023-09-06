package com.ufcg.orderservice.service;

import com.ufcg.orderservice.dto.InventoryResponse;
import com.ufcg.orderservice.dto.OrderLineItemsDTO;
import com.ufcg.orderservice.dto.OrderRequest;
import com.ufcg.orderservice.model.Order;
import com.ufcg.orderservice.repository.OrderRepository;
import com.ufcg.orderservice.model.OrderLineItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> list = orderRequest.getOrderLineItemsList().stream().map(this::mapToDTO).toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(list)
                .build();

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(
                OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean result = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (result) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock");
        }
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .price(orderLineItemsDTO.getPrice())
                .quantity(orderLineItemsDTO.getQuantity())
                .skuCode(orderLineItemsDTO.getSkuCode()).build();
        return orderLineItems;

    }
}
