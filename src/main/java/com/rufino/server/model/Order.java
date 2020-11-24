package com.rufino.server.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"idOrder", "idClient","idParcel", "totalValue", "orderAddress" })
public class Order {

    private UUID idOrder;
    private String idClient;
    private String idParcel;
    private Float totalValue;
    private String orderAddress;

    public void setIdOrder(UUID idOrder) {
        this.idOrder = idOrder;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public void setIdParcel(String idParcel) {
        this.idParcel = idParcel;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public UUID getIdOrder() {
        return idOrder;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getIdParcel() {
        return idParcel;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public String getOrderAddress() {
        return orderAddress;
    }
}
