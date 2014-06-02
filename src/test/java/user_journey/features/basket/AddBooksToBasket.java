package user_journey.features.basket;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static user_journey.dsl.BookstoreTestDSL.*;

public class AddBooksToBasket {

	public static final String BOOK_A_ID = "1";
	public static final String BOOK_B_ID = "2";

	@Given("^I add a few books to the basket$")
	public void I_add_a_few_books_to_the_basket() throws Throwable {
		goToHomePage();
		addBookToBasket(BOOK_A_ID);
		addBookToBasket(BOOK_B_ID);
	}

	@When("^I check the basket$")
	public void I_check_the_basket() throws Throwable {
		navigateTo("/basket");
	}

	@Then("^I should see all the books I've added$")
	public void I_should_see_all_the_books_I_ve_added() throws Throwable {
		waitUntil(linkWithTextExists("Book A"));
		assertLinkWithTextExists("Book B");
	}

	@Then("^the total price$")
	public void the_total_price() throws Throwable {
		assertBasketTotalIs("Total: 30.0");
		clearBasket();
	}
}
