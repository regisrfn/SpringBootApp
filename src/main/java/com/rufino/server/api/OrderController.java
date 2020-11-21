package com.rufino.server.api;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Order;
import com.rufino.server.services.OrderService;
import com.rufino.server.services.RabbitMqService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    RabbitMqService rabbitMqSender;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("{id}")
    public Order getOrder(@PathVariable String id) {
        try {
            UUID idOrder = UUID.fromString(id);
            return orderService.getOrder(idOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping
    public String saveOrder(@RequestBody Order order) {
        int op = orderService.addOrder(order);

        if (op > 0) {
            JSONObject deliveryObj = new JSONObject();
            deliveryObj.put("idClient", order.getIdClient());
            deliveryObj.put("orderAddress", order.getOrderAddress());
            rabbitMqSender.send(deliveryObj.toString());
        }

        return (op > 0) ? "order added successfully" : "error operation";
    }

    @DeleteMapping("{id}")
    public String deleteOrder(@PathVariable String id) {
        try {
            UUID idOrder = UUID.fromString(id);
            int op = orderService.delete(idOrder);
            return (op > 0) ? "successfully operation" : "error operation";
        } catch (Exception e) {
            e.printStackTrace();
            return "error operation";
        }

    }

    @PutMapping("{id}")
    public String updateOrder(@PathVariable String id, @RequestBody Order order) {
        try {
            UUID idOrder = UUID.fromString(id);
            int op = orderService.update(idOrder, order);
            return (op > 0) ? "successfully operation" : "error operation";
        } catch (Exception e) {
            e.printStackTrace();
            return "error operation";
        }

    }
}
