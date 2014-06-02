package unit.core.com.codurance.model.book;

import com.codurance.model.book.Book;

public class BookBuilder {

	private int id = 1;
	private String name = "Book A";
	private String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu.";
	private double price = 10;
	private boolean available = true;

	public static BookBuilder aBook() {
		return new BookBuilder();
	}

	public BookBuilder withId(int id) {
		this.id = id;
		return this;
	}

	public BookBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public BookBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public BookBuilder withPrice(double price) {
		this.price = price;
		return this;
	}

	public BookBuilder available() {
		this.available = true;
		return this;
	}

	public Book build() {
		return new Book(id, name, description, available, price);
	}
}
