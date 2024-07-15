package Controller;

import Model.Person;
import Model.Purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class to manage the cost sharing and purchases.
 */
public class CostSharingController {
    private List<Person> people;
    private List<Purchase> purchases;

    /**
     * Constructs a CostSharingController object.
     */
    public CostSharingController() {
        this.people = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    /**
     * Adds a person to the list of people.
     *
     * @param person the person to be added
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    /**
     * Adds a purchase to the list of purchases.
     *
     * @param purchase the purchase to be added
     */
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Person> getPeople() {
        return people;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * Calculates and returns the cost sharing details.
     *
     * @return a string with the cost sharing details
     */
    public String calculateCosts() {
        double totalCost = purchases.stream().mapToDouble(Purchase::getAmount).sum();
        double perPersonCost = totalCost / people.size();

        Map<Person, Double> balances = new HashMap<>();
        for (Person person : people) {
            balances.put(person, 0.0);
        }

        for (Purchase purchase : purchases) {
            Person person = purchase.getPerson();
            double amount = purchase.getAmount();
            balances.put(person, balances.get(person) + amount); // Add amount to the purchaser's balance
        }

        StringBuilder result = new StringBuilder();
        result.append("Total cost: $").append(totalCost).append("\n");
        result.append("Cost per person: $").append(perPersonCost).append("\n\n");

        List<Person> creditors = new ArrayList<>();
        List<Person> debtors = new ArrayList<>();

        for (Map.Entry<Person, Double> entry : balances.entrySet()) {
            Person person = entry.getKey();
            double balance = entry.getValue() - perPersonCost; // Subtract per person cost to get net balance
            balances.put(person, balance);

            if (balance > 0) {
                creditors.add(person);
            } else if (balance < 0) {
                debtors.add(person);
            }
        }

        result.append("Payment instructions:\n");

        int i = 0, j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            Person debtor = debtors.get(i);
            Person creditor = creditors.get(j);
            double debtAmount = -balances.get(debtor);
            double creditAmount = balances.get(creditor);

            double payment = Math.min(debtAmount, creditAmount);

            result.append(debtor.getName()).append(" should pay ").append(creditor.getName()).append(" $").append(payment).append("\n");

            balances.put(debtor, balances.get(debtor) + payment);
            balances.put(creditor, balances.get(creditor) - payment);

            if (balances.get(debtor) >= 0) {
                i++;
            }
            if (balances.get(creditor) <= 0) {
                j++;
            }
        }

        return result.toString();
    }
}
