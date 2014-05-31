package user_journey.books;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;


public class ListAllBooksSpec {

	private WebDriver driver = new HtmlUnitDriver();

	@Given("^there are some books available$")
	public void there_are_some_books_available() throws Throwable {
		driver.get("http://localhost:4567");
	}

	@When("^I click on a book title$")
	public void I_click_on_a_book_title() throws Throwable {
		List<WebElement> bookLinks = driver.findElements(By.cssSelector(".bookline a"));
		bookLinks.get(0).click();
	}

	@Then("^I should see the book details$")
	public void I_should_see_the_book_details() throws Throwable {
		assertThat(driver.getTitle(), is("Book Details"));
		assertThat(driver.findElement(By.id("book_id")).getText(), is(not("")));
		assertThat(driver.findElement(By.id("book_name")).getText(), is(not("")));
		assertThat(driver.findElement(By.id("book_description")).getText(), is(not("")));
		assertThat(driver.findElement(By.id("book_price")).getText(), is(not("")));
	}

}
