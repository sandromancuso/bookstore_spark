Feature: Basket and Checkout

	Scenario: Add books to basket

		Given I add a few books to the basket
		When I check the basket
		Then I should see all the books I've added
		And the total price

	Scenario: Buying books

		Given I add a few books to the basket
		When I proceed to the checkout
		And I pay using my payment details
        Then I should see the order confirmation
		And see the order in my order history