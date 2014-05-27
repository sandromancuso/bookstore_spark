package com.codurance.controllers;

import com.codurance.infrastructure.template.jade.ViewModel;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codurance.infrastructure.template.jade.JadeRenderer.render;
import static java.lang.Double.parseDouble;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Comparator.reverseOrder;
import static spark.Spark.*;

public class BookStoreApp {

	public static final int NO_CONTENT = 204;
	private static List<Book> bookList = new ArrayList<>();
	private static List<Book> basket = new ArrayList<>();
	private static List<Order> orders = new ArrayList<>();

	public static void main(String[] args) {

		createBooks();

		staticFileLocation("/public");

		get("/", (request, response) -> {
			response.redirect("/books");
			response.status(NO_CONTENT);
			return "";
		});

		get("/books", new Route() {
			@Override
			public Object handle(Request request, Response response) {
				ViewModel viewModel = new ViewModel();
				viewModel.put("pageName", "Books");
				viewModel.put("books", bookList);
				viewModel.put("basket_count", basket.size());

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

		get("/books/add", (request, response) -> {
			try {
				return render("createbook.jade");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
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
				Optional<Book> book = getBookBy(id);
				if (!book.isPresent()) {
					return notFound(response);
				}
				ViewModel model = new ViewModel();
				model.put("book", book.get());
				model.put("basket_count", basket.size());

				return render("bookdetails.jade", model);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			}
		});

		post("/basket", (request, response) -> {
			try {
				int id = Integer.valueOf(request.queryParams("bookId"));
				Optional<Book> book = getBookBy(id);
				if (!book.isPresent()) {
					return notFound(response);
				}

				basket.add(book.get());

				response.redirect("/books");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		get("/basket", (request, response) -> {
			try {
				ViewModel viewModel = new ViewModel();
				viewModel.put("basket_count", basket.size());
				viewModel.put("basket", basket);
				viewModel.put("total", basketTotal());
				return render("basket.jade", viewModel);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/basket/delete", (request, response) -> {
			try {
				int id = Integer.valueOf(request.queryParams("bookId"));
				Optional<Book> book = getBookBy(id);
				if (!book.isPresent()) {
					return notFound(response);
				}
				basket.remove(book.get());

				response.redirect("/basket");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/basket/clear", (request, response) -> {
			try {
				basket.clear();

				response.redirect("/basket");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		get("/checkout", (request, response) -> {
			try {
				ViewModel model = new ViewModel();
				model.put("basket", basket);
				model.put("total", basketTotal());
				return render("checkout.jade", model);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/payment", (request, response) -> {
			try {
				Map<String, String> paymentDetails = new HashMap<>();
				request.queryMap().toMap().entrySet().stream().forEach(e -> paymentDetails.put(e.getKey(), e.getValue()[0]));
				List<Book> booksBought = new ArrayList<>(basket);

				orders.add(new Order(paymentDetails, booksBought));

				response.redirect("/orderconfirmation");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		get("/orderconfirmation", (request, response) -> {
			try {
				return render("orderconfirmation.jade");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		get("/orders", (request, response) -> {
			try {
				return render("orders.jade");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

	}

	private static Double basketTotal() {
		return basket.stream().map(b -> b.getPrice()).reduce(0.0, (a, b) -> a + b);
	}

	private static Optional<Book> getBookBy(int id) {
		return bookList.stream().collect(Collectors.groupingBy(Book::getId)).get(id).stream().findAny();
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
		bookList.add(new Book(nextBookId(), "Book B", "Ut sollicitudin mi et felis laoreet tempor. Sed tincidunt, nisl.", true, 20));
		bookList.add(new Book(nextBookId(), "Book C", "Vivamus id sem magna. Phasellus non elit vel tortor adipiscing.", true, 30));
		bookList.add(new Book(nextBookId(), "Book D", "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet.", true, 40));
	}

	public static class Order {

		private Map<String, String> paymentDetails;
		private List<Book> booksBought;

		public Order(Map<String, String> paymentDetails, List<Book> booksBought) {
			this.paymentDetails = paymentDetails;
			this.booksBought = booksBought;
		}

		public Map<String, String> getPaymentDetails() {
			return unmodifiableMap(paymentDetails);
		}

		public List<Book> getBooksBought() {
			return unmodifiableList(booksBought);
		}
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
