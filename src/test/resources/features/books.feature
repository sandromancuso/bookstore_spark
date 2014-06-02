Feature: Display books

	Scenario: Book details
		Given there are some books available
		When I click on a book title
		Then I should see the book details

	Scenario: Add book to library
		Given the book details are informed
		When the book is added
		Then the book should be displayed in the library

	Scenario: Search book
		Given I'm at the home page
		And a few books are listed
		When I search for a specific book
		Then only this book is displayed

