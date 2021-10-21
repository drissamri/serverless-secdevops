package com.nike.emea;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.nike.emea.config.AppConfig;
import com.nike.emea.model.Order;
import com.nike.emea.model.dto.AddOrderRequest;
import com.nike.emea.service.OrderService;
import com.fasterxml.jackson.jr.ob.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.HttpStatusCode;

public class AddOrderLambda implements RequestHandler<APIGatewayProxyRequestEvent, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(AddOrderLambda.class);
    private OrderService orderService;
    private JSON jsonMapper;

    public AddOrderLambda() {
        this(AppConfig.orderService(), JSON.std);
    }

    public AddOrderLambda(OrderService orderService, JSON jsonMapper) {
        this.orderService = orderService;
        this.jsonMapper = jsonMapper;
    }

    public Object handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent response;
        try {
            AddOrderRequest order = new AddOrderRequest(input.getHeaders().get("X-Amzn-Trace-Id"));
            Order savedOrder = orderService.add(order);
            LOG.info("Order created: {}", savedOrder);

            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(jsonMapper.asString(savedOrder));
        } catch (Exception ex) {
            LOG.error("Exception: {}", ex.getMessage());
            response = createErrorResponse();
        }
        return response;
    }

    private APIGatewayProxyResponseEvent createErrorResponse() {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
}