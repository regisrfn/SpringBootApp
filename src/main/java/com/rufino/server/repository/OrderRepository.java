package com.rufino.server.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.dao.OrderDAO;
import com.rufino.server.model.Order;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("DB_H2")
public class OrderRepository implements OrderDAO {

    private JdbcTemplate jdbcTemplate;
    private List<Order> orderDb;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderDb = new ArrayList<>();
    }

    @Override
    public int insertOrder(UUID id, Order order) {
        try {
            int result = jdbcTemplate.update(
                    "INSERT INTO orders " + "(id_order, id_client, id_parcel, total_value, order_address)"
                            + "VALUES (?, ?, ?, ?, ?)",
                    id, order.getIdClient(), order.getIdParcel(), order.getTotalValue(), order.getOrderAddress());
            order.setIdOrder((result > 0 ? id : null));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteOrder(UUID id) {
        try {
            return jdbcTemplate.update("delete from orders where id_order = ?", id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public List<Order> getAllOrder() {
        try {
            String sql = "SELECT * FROM ORDERS";
            orderDb = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Order>(Order.class));
            return orderDb;
        } catch (Exception e) {
            e.printStackTrace();
            return orderDb;
        }
    }

    @Override
    public Order getOrder(UUID id) {
        try {
            String sql = "SELECT * FROM ORDERS WHERE id_order = ?";
            orderDb = jdbcTemplate.query(sql, new Object[] { id }, new BeanPropertyRowMapper<Order>(Order.class));
            if (orderDb.isEmpty()) {
                return null;
            }
            return orderDb.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateOrder(UUID id, Order order) {
        try {
            String sql = "UPDATE Orders SET ";
            ObjectMapper om = new ObjectMapper();
            om.setSerializationInclusion(Include.NON_NULL);

            String orderString = om.writeValueAsString(order);
            JSONObject jsonObject = new JSONObject(orderString);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals("totalValue")) {
                    sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + jsonObject.get(key) + "' ";
                } else {
                    sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "=" + jsonObject.get(key) + " ";
                }
                if (keys.hasNext()) {
                    sql = sql + ", ";
                }
            }
            return jdbcTemplate.update(sql + "where id_order = ?", id);
        } catch (Exception e) {
            return 0;
        }

    }
}
