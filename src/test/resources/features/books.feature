Feature: Display books

	Scenario: Book details
		Given there are some books available
		When I click on a book title
		Then I should see the book details

	Scenario: Add book to library
		Given the book details are informed
		When the book is added
		Then the book should be displayed in the library

