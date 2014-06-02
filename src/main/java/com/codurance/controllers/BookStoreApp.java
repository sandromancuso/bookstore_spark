package com.codurance.controllers;

import com.codurance.infrastructure.repositories.InMemoryLibrary;
import com.codurance.infrastructure.template.jade.ViewModel;
import com.codurance.model.book.Book;
import com.codurance.model.book.Library;
import spark.Response;

import java.util.*;
import java.util.stream.Stream;

import static com.codurance.infrastructure.template.jade.JadeRenderer.render;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.valueOf;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Comparator.reverseOrder;
import static spark.Spark.*;

public class BookstoreApp {

	public static final int NO_CONTENT = 204;
	private static Library library = new InMemoryLibrary();
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

		get("/books", (request, response) -> {
			ViewModel viewModel = new ViewModel();
			viewModel.put("pageName", "Books");
			viewModel.put("books", library.allBooks());
			viewModel.put("basket_count", basket.size());

			return render("index.jade", viewModel);
		});

		get("/books/search", (request, response) -> {
			try {
				String criteria = request.queryParams("criteria");
				List<Book> filteredBooks = library.booksWhichNameContains(criteria);

				ViewModel viewModel = new ViewModel();
				viewModel.put("pageName", "Books");
				viewModel.put("criteria", criteria);
				viewModel.put("books", filteredBooks);

				return render("index.jade", viewModel);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
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

		get("/books/edit/:id", (request, response) -> {
			try {
				Optional<Book> book = getBookBy(valueOf(request.params(":id")));
				if (!book.isPresent()) {
					return notFound(response);
				}
				ViewModel model = new ViewModel();
				model.put("book", book.get());

				return render("editbook.jade", model);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/books/edit", (request, response) -> {
			try {
				Optional<Book> book = getBookBy(valueOf(request.queryParams("id")));
				if (!book.isPresent()) {
					return notFound(response);
				}
			    book.get().setName(request.queryParams("name"));
				book.get().setDescription(request.queryParams("description"));
				book.get().setAvailable(request.queryParams("available") != null);
				book.get().setPrice(parseDouble(request.queryParams("price")));

				response.redirect("/books");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		post("/books", (request, response) -> {
			try {
				int nextId = library.nextBookId();
				Book book = new Book(
						nextId,
						request.queryParams("name"),
						request.queryParams("description"),
						request.queryParams("available") != null,
						parseDouble(request.queryParams("price"))
				);
				library.add(book);

				response.redirect("/books");
				response.status(NO_CONTENT);
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});

		get("/books/:id", (request, response) -> {
		try {
			int id = valueOf(request.params(":id"));
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
		});

		post("/basket", (request, response) -> {
			try {
				int id = valueOf(request.queryParams("bookId"));
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
				int id = valueOf(request.queryParams("bookId"));
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
				List<Book> booksBought = new ArrayList<>();
				basket.stream().forEach(book -> booksBought.add(book.clone()));

				orders.add(new Order(nextOrderId(), new Date(), paymentDetails, booksBought));
				basket.clear();

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
				ViewModel model = new ViewModel();
				model.put("orders", orders);
				return render("orders.jade", model);
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
		return library.bookById(id);
	}

	private static String notFound(Response response) {
		response.status(404);
		return "Not Found";
	}

	private static int nextOrderId() {
		Stream<Integer> olderListDescending = orders.stream().map(Order::getId).sorted(reverseOrder());
		Optional<Integer> lastId = olderListDescending.findFirst();
		return lastId.orElse(0) + 1;
	}

	private static void createBooks() {
		library.add(new Book(library.nextBookId(), "Book A", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eu.", true, 10));
		library.add(new Book(library.nextBookId(), "Book B", "Ut sollicitudin mi et felis laoreet tempor. Sed tincidunt, nisl.", true, 20));
		library.add(new Book(library.nextBookId(), "Book C", "Vivamus id sem magna. Phasellus non elit vel tortor adipiscing.", true, 30));
		library.add(new Book(library.nextBookId(), "Book D", "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet.", true, 40));
	}

	public static class Order {

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

}
