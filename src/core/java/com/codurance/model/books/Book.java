package com.codurance.model.books;

public class Book {

	private final boolean available;
	private final String name;
	private final int id;
	private final double price;

	public Book(int id, String name, boolean available, double price) {
		this.id = id;
		this.name = name;
		this.available = available;
		this.price = price;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isAvailable() {
		return this.available;
	}

	public double getPrice() {
		return this.price;
	}

}
