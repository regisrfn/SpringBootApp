package com.rufino.server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.Order;
import com.rufino.server.services.OrderService;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

class ServerApplicationTests {

	@Autowired
	private OrderService orderService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM ORDERS");
	}

	// ---------------------TEST HOME PAGE-------------------
	@Test
	void testHomeHttp() throws Exception {
		MvcResult result = mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();

		assertEquals("Hello World from Spring Boot", result.getResponse().getContentAsString());

	}

	// ----------------- TESTING CREATED ORDER
	@Test
	public void createNewOrder() {
		Order order = new Order();
		createAndAssert(order);
	}

	@Test
	void addOrderTestHttp() throws Exception {

		JSONObject my_obj = new JSONObject();
		my_obj.put("idClient", 1111);
		my_obj.put("idParcel", 2222);
		my_obj.put("totalValue", 15.99);
		my_obj.put("orderAddress", "Rua do meio");

		MvcResult result = mockMvc
				.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
				.andExpect(status().isOk()).andReturn();

		assertEquals("order added successfully", result.getResponse().getContentAsString());

	}

	@Test
	void addOrderTestHttp_ExpectedError() throws Exception {

		JSONObject my_obj = new JSONObject();
		my_obj.put("idCliente", 1111);
		MvcResult result = mockMvc
				.perform(post("/api/v1/order").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
				.andExpect(status().isOk()).andReturn();

		assertEquals("error operation", result.getResponse().getContentAsString());

	}

	// -----------------TEST DELETING ORDER
	@Test
	public void deleteOrderDAO() {
		Order order = new Order();
		createAndAssert(order);

		UUID id = order.getIdOrder();
		orderService.delete(id);
		long countAfterDelete = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(0, countAfterDelete);
	}

	@Test
	public void deleteOrderHttp() throws Exception {
		Order order = new Order();
		createAndAssert(order);

		MvcResult result = mockMvc.perform(delete(String.format("/api/v1/order/%s", order.getIdOrder())))
				.andExpect(status().isOk()).andReturn();

		assertEquals("successfully operation", result.getResponse().getContentAsString());
		long countAfterDelete = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(0, countAfterDelete);
	}

	@Test
	public void deleteOrderHttp_ErrorExpected() throws Exception {
		Order order = new Order();
		createAndAssert(order);

		MvcResult result = mockMvc.perform(delete(String.format("/api/v1/order/1"))).andExpect(status().isOk())
				.andReturn();

		assertEquals("error operation", result.getResponse().getContentAsString());
		long countAfterDelete = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(1, countAfterDelete);
	}

	// -----------------TEST SELECT ALL
	@Test
	public void selectAllOrderDAO() {
		Order order = new Order();
		createAndAssert(order);
		List<Order> Db = orderService.getAll();
		assertEquals(order.getIdOrder(), Db.get(0).getIdOrder());
		assertEquals(order.getIdClient(), Db.get(0).getIdClient());
		assertEquals(order.getIdParcel(), Db.get(0).getIdParcel());
		assertEquals(order.getTotalValue(), Db.get(0).getTotalValue());
		assertEquals(order.getOrderAddress(), Db.get(0).getOrderAddress());
	}

	@Test
	void selectAllOrderHttp() throws Exception {
		Order order = new Order();
		createAndAssert(order);
		MvcResult result = mockMvc.perform(get("/api/v1/order").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper om = new ObjectMapper();
		String jsonString = om.writeValueAsString(order);
		assertEquals("[" + jsonString + "]", result.getResponse().getContentAsString());

	}

	// -----------------TEST UPDATE ORDER
	@Test
	public void updateOrderDAO() {
		Order order = new Order();
		createAndAssert(order);

		Order updateOrder = new Order();
		updateOrder.setIdClient("abc123 updated");
		updateOrder.setTotalValue(20.5f);
		int result = orderService.update(order.getIdOrder(), updateOrder);
		assertEquals(1, result);

		List<Order> Db = orderService.getAll();
		assertEquals(order.getIdOrder(), Db.get(0).getIdOrder());
		assertEquals("abc123 updated", Db.get(0).getIdClient());
		assertEquals(order.getIdParcel(), Db.get(0).getIdParcel());
		assertEquals(20.5f, Db.get(0).getTotalValue());
		assertEquals(order.getOrderAddress(), Db.get(0).getOrderAddress());

	}

	@Test
	public void updateOrderHttp() throws Exception {
		Order order = new Order();
		createAndAssert(order);

		JSONObject my_obj = new JSONObject();
		my_obj.put("idClient", "abc123 updated");
		my_obj.put("totalValue", 20.5f);

		MvcResult result = mockMvc
				.perform(put(String.format("/api/v1/order/%s", order.getIdOrder()))
						.contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
				.andExpect(status().isOk()).andReturn();
		assertEquals("successfully operation", result.getResponse().getContentAsString());

		List<Order> Db = orderService.getAll();
		assertEquals(order.getIdOrder(), Db.get(0).getIdOrder());
		assertEquals("abc123 updated", Db.get(0).getIdClient());
		assertEquals(order.getIdParcel(), Db.get(0).getIdParcel());
		assertEquals(20.5f, Db.get(0).getTotalValue());
		assertEquals(order.getOrderAddress(), Db.get(0).getOrderAddress());

	}

	// -----------------TEST GET ORDER BY ID
	@Test
	public void getOrderDAO() {
		Order order = new Order();
		createAndAssert(order);
		Order orderDb = orderService.getOrder(order.getIdOrder());

		assertEquals(order.getIdOrder(), orderDb.getIdOrder());
		assertEquals(order.getIdClient(), orderDb.getIdClient());
		assertEquals(order.getIdParcel(), orderDb.getIdParcel());
		assertEquals(order.getTotalValue(), orderDb.getTotalValue());
		assertEquals(order.getOrderAddress(), orderDb.getOrderAddress());

	}

	@Test
	public void getOrderHttp() throws Exception {
		Order order = new Order();
		createAndAssert(order);

		MvcResult result = mockMvc.perform(
				get(String.format("/api/v1/order/%s", order.getIdOrder())).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper om = new ObjectMapper();
		String jsonString = om.writeValueAsString(order);
		assertEquals(jsonString,result.getResponse().getContentAsString());

	}

	@Test
	public void getOrderHttp_NotFoundOrder() throws Exception {
		UUID id = UUID.fromString("0332d486-2855-11eb-adc1-0242ac120002");

		MvcResult result = mockMvc.perform(
				get(String.format("/api/v1/order/%s",id)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		assertEquals("",result.getResponse().getContentAsString());

	}

	// -----------------------------------------------------
	private void createAndAssert(Order order) {
		order.setIdClient("abc123");
		order.setIdParcel("abc456");
		order.setTotalValue(0.50f);
		order.setOrderAddress("Rua de cima");
		long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(0, countBeforeInsert);
		orderService.addOrder(order);
		long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from orders", Long.class);
		assertEquals(1, countAfterInsert);
	}

}
