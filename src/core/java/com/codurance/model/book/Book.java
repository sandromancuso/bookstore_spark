package com.codurance.model.book;

public class Book {

	private int id;

	private boolean available;
	private String name;
	private double price;
	private String description;

	public Book(int id, String name, String description, boolean available, double price) {
		this.id = id;
		this.name = name;
		this.available = available;
		this.price = price;
		this.description = description;
	}


	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAvailable() {
		return this.available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Book clone() {
		return new Book(id, name, description, available, price);
	}

}
