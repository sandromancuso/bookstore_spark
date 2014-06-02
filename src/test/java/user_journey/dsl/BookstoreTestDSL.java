package user_journey.dsl;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BookstoreTestDSL {

	public static final String HOME_PAGE_ADDRESS = "http://localhost:4567";

	public static WebDriver driver = new HtmlUnitDriver();

	public static void goToHomePage() {
		driver.get(HOME_PAGE_ADDRESS);
	}

	public static void navigateTo(String pagePath) {
		driver.get(HOME_PAGE_ADDRESS + pagePath);
	}

	public static InputFiller fill(String inputId) {
		return new InputFiller(inputId);
	}

	public static void check(String checkboxId) {
		if (isUnchecked(checkboxId)) click(checkboxId);
	}

	public static boolean isUnchecked(String checkboxId) {
		return !elementById(checkboxId).isSelected();
	}

	public static void click(String elementId) {
		elementById(elementId).click();
	}

	public static void waitUntil(ExpectedCondition<WebElement> expectedCondition) {
		(new WebDriverWait(driver, 5)).until(expectedCondition);
	}

	public static ExpectedCondition<WebElement> linkWithTextExists(String text) {
		return ExpectedConditions.elementToBeClickable(By.linkText(text));
	}

	public static void searchBookWithTitle(String title) {
		WebElement searchBox = elementById("book_criteria");
		searchBox.sendKeys(title);
		click("search_submit");
	}

	public static void addBookToBasket(String bookId) {
		elementById("submit_to_basket_" + bookId).click();
	}

	public static void assertBasketTotalIs(String total) {
		assertThat(elementById("total").getText(), is(total));
	}

	public static void clearBasket() {
		click("clear_basket");
	}

	public static void assertLinkWithTextExists(String text) {
		assertThat(driver.findElement(By.linkText(text)), is(notNullValue()));
	}

	public static void assertLinkWithTextDoesNotExist(String text) {
		try {
			driver.findElement(By.linkText(text));
		    fail("Link with text [" + text + "] was found.");
		} catch (NoSuchElementException e) {
			// Expected exception to be thrown.
		}
	}

	public static void assertBookDetailsAreNotEmpty() {
		assertElementIsNotEmpty("book_id");
		assertElementIsNotEmpty("book_name");
		assertElementIsNotEmpty("book_description");
		assertElementIsNotEmpty("book_price");
	}

	public static void clickOnFirstBook() {
		List<WebElement> bookLinks = elementsByCSS(".bookline a");
		bookLinks.get(0).click();
	}

	public static WebElement elementById(String id) {
		return driver.findElement(By.id(id));
	}

	public static List<WebElement> elementsByCSS(String css) {
		return driver.findElements(By.cssSelector(css));
	}

	public static void assertPageTitleIs(String title) {
		assertThat(driver.getTitle(), is(title));
	}

	public static void assertElementIsNotEmpty(String book_id) {
		assertThat(driver.findElement(By.id(book_id)).getText(), is(not("")));
	}

	public static class InputFiller {
		private String inputId;

		public InputFiller(String inputId) {
			this.inputId = inputId;
		}

		public void with(String text) {
			driver.findElement(By.id(inputId)).sendKeys(text);
		}
	}

}
