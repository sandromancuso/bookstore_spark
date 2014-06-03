package com.codurance.infrastructure.repositories;

import com.codurance.model.order.Order;
import com.codurance.model.order.Orders;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class InMemoryOrders implements Orders {

	private List<Order> orders = new ArrayList<>();

	@Override
	public List<Order> all() {
		return unmodifiableList(orders);
	}

	@Override
	public void add(Order order) {
		this.orders.add(order);
	}

	@Override
	public int nextId() {
		return orders.stream().mapToInt(o -> o.getId()).max().orElse(0) + 1;
	}

}
