package user_journey.books;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertNotNull;


public class ListAllBooksSpec {

	private WebDriver driver = new HtmlUnitDriver();

	@Given("^there are some books available$")
	public void there_are_some_books_available() throws Throwable {
	}

	@When("^I go to the books page$")
	public void I_go_to_the_books_page() throws Throwable {
		driver.get("http://localhost:4567");
	}

	@Then("^I should see all the books$")
	public void I_should_see_all_the_books() throws Throwable {
	    assertNotNull(driver.findElement(By.linkText("Book A")));

	}

}
