package com.nike.emea.model.dto;

public class AddOrderRequest {
    public AddOrderRequest() {
    }

    public AddOrderRequest(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
