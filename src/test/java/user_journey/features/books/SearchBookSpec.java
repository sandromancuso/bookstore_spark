package user_journey.features.books;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static user_journey.dsl.BookstoreTestDSL.*;

public class SearchBookSpec {

	@Given("^I'm at the home page$")
	public void I_m_at_the_home_page() throws Throwable {
		goToHomePage();
	}

	@Given("^a few books are listed$")
	public void a_few_books_are_listed() throws Throwable {
		assertLinkWithTextExists("Book A");
		assertLinkWithTextExists("Book B");
	}

	@When("^I search for a specific book$")
	public void I_search_for_a_specific_book() throws Throwable {
		searchBookWithTitle("Book A");
	}

	@Then("^only this book is displayed$")
	public void only_this_book_is_displayed() throws Throwable {
		waitUntil(linkWithTextExists("Book A"));
		assertLinkWithTextDoesNotExist("Book B");
	}


}
