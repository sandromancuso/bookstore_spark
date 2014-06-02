package user_journey.features.books;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static user_journey.dsl.BookstoreTestDSL.*;


public class BookDetailsSpec {

	@Given("^there are some books available$")
	public void there_are_some_books_available() throws Throwable {
		goToHomePage();
	}

	@When("^I click on a book title$")
	public void I_click_on_a_book_title() throws Throwable {
		clickOnFirstBook();
	}

	@Then("^I should see the book details$")
	public void I_should_see_the_book_details() throws Throwable {
		assertPageTitleIs("Book Details");
		assertBookDetailsAreNotEmpty();
	}

}
