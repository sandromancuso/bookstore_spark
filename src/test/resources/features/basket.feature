Feature: Basket

	Scenario: Add books to basket

		Given I add a few books to the basket
		When I check the basket
		Then I should see all the books I've added
		And the total price
