Feature: Display books

    Scenario: List all books
        Given there are some books available
	    When I go to the books page
	    Then I should see all the books