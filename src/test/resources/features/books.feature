Feature: Display books

    Scenario: Book details
        Given there are some books available
	    When I click on a book title
	    Then I should see the book details