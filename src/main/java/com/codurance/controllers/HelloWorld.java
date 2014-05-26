package com.codurance.controllers;

import com.codurance.infrastructure.template.jade.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codurance.infrastructure.template.jade.JadeRenderer.render;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.util.Comparator.reverseOrder;
import static spark.Spark.*;

public class HelloWorld {

	public static final int NO_CONTENT = 204;
	private static List<Book> bookList = new ArrayList<>();

	public static void main(String[] args) {

		createBooks();

		staticFileLocation("/public");

		get("/hello", (request, response) -> "Hello, World!");

		get("/books", (request, response) -> {
			ViewModel viewModel = new ViewModel();
			viewModel.put("pageName", "Books");
			viewModel.put("books", bookList);

			return render("index.jade", viewModel);
		});

		post("/books/search", (request, response) -> {
			try {
				String criteria = request.queryParams("criteria");
				List<Book> filteredBooks = bookList.stream().filter(book -> book.getName().contains(criteria)).collect(Collectors.toList());

				ViewModel viewModel = new ViewModel();
				viewModel.put("pageName", "Books");
				viewModel.put("books", filteredBooks);

				return render("index.jade", viewModel);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/books", (request, response) -> {
			try {
				int nextId = nextBookId();
				Book book = new Book(
					nextId,
					request.queryParams("name"),
					request.queryParams("available") != null,
					parseDouble(request.queryParams("price"))
				);
				bookList.add(book);

				response.redirect("/books");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
	}

	private static int nextBookId() {
		Stream<Integer> bookListDescending = bookList.stream().map(Book::getId).sorted(reverseOrder());
		Optional<Integer> lastId = bookListDescending.findFirst();
		return lastId.orElse(0) + 1;
	}

	private static void createBooks() {
		bookList.add(new Book(nextBookId(), "Book A", true, 10));
		bookList.add(new Book(nextBookId(), "Book B", false, 20));
		bookList.add(new Book(nextBookId(), "Book C", true, 30));
	}

	public static class Book {

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

}
