package user_journey.features.books;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static user_journey.dsl.BookstoreTestDSL.*;

public class AddBookToLibrarySpec {

	@Given("^the book details are informed$")
	public void the_book_details_are_informed() throws Throwable {
		navigateTo("/books/add");
		fill("book_name").with("New book");
		fill("book_description").with("Some description");
		fill("book_price").with("100.00");
		check("book_availability");
	}

	@When("^the book is added$")
	public void the_book_is_added() throws Throwable {
		click("submit");
	}

	@Then("^the book should be displayed in the library$")
	public void the_book_should_be_displayed_in_the_library() throws Throwable {
		waitUnitl(linkWithTextExists("New book"));
		assertPageTitleIs("Books");
	}

}
