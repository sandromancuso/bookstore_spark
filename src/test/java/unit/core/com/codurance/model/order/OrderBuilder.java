package unit.core.com.codurance.model.order;

import com.codurance.model.book.Book;
import com.codurance.model.order.Order;

import java.util.*;

public class OrderBuilder {

	private int id = 1;
	private Date date = new Date();
	private Map<String, String> paymentDetails = new HashMap<>();
	private List<Book> books = new ArrayList<>();

	public static OrderBuilder anOrder() {
		return new OrderBuilder();
	}

	public OrderBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public Order build() {
		Order order = new Order(id, date, paymentDetails, books);
		return order;
	}
}
