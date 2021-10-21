package com.nike.emea.config;

import com.nike.emea.model.Order;
import com.nike.emea.service.OrderService;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

public class AppConfig {
    public static OrderService orderService() {
        DynamoDbAsyncClient dynamoDbClient = DynamoDbAsyncClient.builder()
                .httpClient(AwsCrtAsyncHttpClient.builder().build())
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        DynamoDbEnhancedAsyncClient client = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        DynamoDbAsyncTable<Order> dynamoDbTable = client.table(
                System.getenv("TABLE"),
                TableSchema.fromBean(Order.class));
        try {

            return new OrderService(dynamoDbTable);
        } catch (Exception e) {
            throw new RuntimeException("Init dynamodb failed");
        }
    }
}
