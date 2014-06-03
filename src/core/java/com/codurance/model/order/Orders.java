package com.codurance.model.order;

import java.util.List;

public interface Orders {
	List<Order> all();

	void add(Order order);

	int nextId();
}
