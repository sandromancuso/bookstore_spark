package unit.core.com.codurance.infrastructure.repository;

import com.codurance.infrastructure.repositories.InMemoryOrders;
import com.codurance.model.order.Order;
import com.codurance.model.order.Orders;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static unit.core.com.codurance.model.order.OrderBuilder.anOrder;

public class InMemoryOrdersShould {

	public static final Order ORDER_1 = anOrder().withId(1).build();
	public static final Order ORDER_2 = anOrder().withId(2).build();

	private Orders orders;

	@Before
	public void initialise() {
		orders = new InMemoryOrders();
	}

	@Test public void
	return_no_orders_when_there_are_no_orders() {
		List<Order> result = orders.all();

		assertThat(result.isEmpty(), is(true));
	}

	@Test public void
	return_orders() {
			orders.add(ORDER_1);
		orders.add(ORDER_2);

		List<Order> result = orders.all();

		assertThat(result.size(), is(2));
		assertThat(result.contains(ORDER_1), is(true));
		assertThat(result.contains(ORDER_2), is(true));
	}

	@Test public void
	return_1_as_next_order_id_when_there_are_no_orders() {
		assertThat(orders.nextId(), is(1));
	}

	@Test public void
	return_the_max_id_plus_one_as_next_order_id() {
		orders.add(ORDER_1);
		orders.add(ORDER_2);

		assertThat(orders.nextId(), is(3));
	}


}
