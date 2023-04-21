# Our project
Class ATM reproduces the simplified processing of a cash withdrawal operation at an ATM:
1. The client selects the amount for the withdrawal
2. We check that the amount is available in the machine
3. We execute the payment for the amount with the user's payment card
4. We deliver the banknotes

Class ATM relies on three components in order to achieve its work:
* AmountSelector: method selectAmount() corresponds to step 1
* CashManager: methods canDeliver(int) for step 2 and deliver(int) for step 4
* PaymentProcessor: method pay(int) for step 3

The code includes an interface pour each of the three components but no implementations.

# Goal of the exercise
Fill test class ATMTest with all unit test cases you consider useful for class ATM.
Caution: there is a bug in ATM's implementation. Will you find it?