package com.codurance.controllers;

import com.codurance.infrastructure.template.jade.ViewModel;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codurance.infrastructure.template.jade.JadeRenderer.render;
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

		get("/books", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				ViewModel viewModel = new ViewModel();
				viewModel.put("pageName", "Books");
				viewModel.put("books", bookList);

				return render("index.jade", viewModel);
			}
		});

		get("/books/search", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				try {
					String criteria = request.queryParams("criteria");
					List<Book> filteredBooks = bookList.stream().filter(book -> book.getName().contains(criteria)).collect(Collectors.toList());

					ViewModel viewModel = new ViewModel();
					viewModel.put("pageName", "Books");
					viewModel.put("criteria", criteria);
					viewModel.put("books", filteredBooks);

					return render("index.jade", viewModel);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});

		post("/books", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				try {
					int nextId = nextBookId();
					Book book = new Book(
							nextId,
							request.queryParams("name"),
							request.queryParams("description"),
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
			}
		});

		get("/books/:id", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				try {
					int id = Integer.valueOf(request.params(":id"));
					Optional<Book> book = bookList.stream().collect(Collectors.groupingBy(Book::getId)).get(id).stream().findAny();
					if (!book.isPresent()) {
						return notFound(response);
					}
					ViewModel model = new ViewModel();
					model.put("book", book.get());
					return render("bookdetails.jade", model);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	private static String notFound(Response response) {
		response.status(404);
		return "Not Found";
	}

	private static int nextBookId() {
		Stream<Integer> bookListDescending = bookList.stream().map(Book::getId).sorted(reverseOrder());
		Optional<Integer> lastId = bookListDescending.findFirst();
		return lastId.orElse(0) + 1;
	}

	private static void createBooks() {
		bookList.add(new Book(nextBookId(), "Book A", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu.", true, 10));
		bookList.add(new Book(nextBookId(), "Book B", "Ut sollicitudin mi et felis laoreet tempor. Sed tincidunt, nisl.", false, 20));
		bookList.add(new Book(nextBookId(), "Book C", "Vivamus id sem magna. Phasellus non elit vel tortor adipiscing.", true, 30));
	}

	public static class Book {

		private final boolean available;
		private final String name;
		private final int id;
		private final double price;
		private final String description;

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

		public String getDescription() {
			return description;
		}

		public boolean isAvailable() {
			return this.available;
		}

		public double getPrice() {
			return this.price;
		}

	}

}
