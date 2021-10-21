package com.nike.emea.service;

import com.nike.emea.model.dto.AddOrderRequest;
import com.nike.emea.model.Order;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OrderService {
    private DynamoDbAsyncTable<Order> favoritesTable;

    public OrderService(DynamoDbAsyncTable<Order> favoritesTable) {
        this.favoritesTable = favoritesTable;
    }

    public Order add(AddOrderRequest request) throws ExecutionException, InterruptedException {
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID().toString());
        newOrder.setName(request.getName());

        favoritesTable.putItem(newOrder).get();
        return newOrder;
    }
}
