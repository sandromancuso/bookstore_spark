package user_journey.features.basket;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static user_journey.dsl.BookstoreTestDSL.*;

public class BuyingBooksSpec {

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

	@When("^I proceed to the checkout$")
	public void I_proceed_to_the_checkout() throws Throwable {
		navigateTo("/basket");
		waitUntilPageTitleIs("Basket");
		proceedToCheckout();
	}

	@When("^I pay using my payment details$")
	public void I_pay_using_my_payment_details() throws Throwable {
		waitUntilPageTitleIs("Checkout");
		fill("user_name").with("Sandro Mancuso");
		fill("address").with("15 Churchill Close");
		fill("card_details").with("1234567890");
		submitPayment();
	}

	@Then("^I should see the order confirmation$")
	public void I_should_see_the_order_confirmation() throws Throwable {
		waitUntilPageTitleIs("Order Confirmation");
	}

	@Then("^see the order in my order history$")
	public void see_the_order_in_my_order_history() throws Throwable {
		click("orders");
		waitUntilPageTitleIs("Order History");
		assertLinkWithTextExists("Book A");
		assertLinkWithTextExists("Book B");
	}


}
