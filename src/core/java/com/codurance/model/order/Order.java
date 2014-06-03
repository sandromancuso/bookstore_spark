package com.codurance.model.order;

import com.codurance.model.book.Book;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public class Order {

	private int id;
	private Date date;
	private Map<String, String> paymentDetails;
	private List<Book> booksBought;

	public Order(int id, Date date, Map<String, String> paymentDetails, List<Book> booksBought) {
		this.id = id;
		this.date = date;
		this.paymentDetails = paymentDetails;
		this.booksBought = booksBought;
	}

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Map<String, String> getPaymentDetails() {
		return unmodifiableMap(paymentDetails);
	}

	public List<Book> getBooksBought() {
		return unmodifiableList(booksBought);
	}
}
