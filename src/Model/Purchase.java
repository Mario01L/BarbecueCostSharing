package Model;

/**
 * Represents a purchase made by a person.
 */
public class Purchase {
    private Person person;
    private double amount;

    /**
     * Constructs a Purchase object.
     *
     * @param person the person who made the purchase
     * @param amount the amount of the purchase
     */
    public Purchase(Person person, double amount) {
        this.person = person;
        this.amount = amount;
    }

    public Person getPerson() {
        return person;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return person.getName() + ": $" + amount;
    }
}
