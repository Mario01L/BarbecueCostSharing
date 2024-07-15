package View;

import Controller.CostSharingController;
import Model.Person;
import Model.Purchase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main application window for the cost sharing application.
 */
public class CostSharingApp extends JFrame {
    private CostSharingController controller;
    private JPanel inputPanel;
    private JTextArea resultArea;
    private JComboBox<Person> personComboBox;
    private JTextField purchaseAmountField;
    private JTextArea purchasesListArea;

    /**
     * Constructs the CostSharingApp window.
     */
    public CostSharingApp() {
        controller = new CostSharingController();

        setTitle("Cost Sharing Application");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JButton addPersonButton = new JButton("Add Person");
        addPersonButton.addActionListener(new AddPersonButtonListener());
        inputPanel.add(addPersonButton);

        JPanel purchasePanel = new JPanel();
        purchasePanel.setLayout(new GridLayout(1, 3));

        personComboBox = new JComboBox<>();
        purchasePanel.add(personComboBox);

        purchasePanel.add(new JLabel("Purchase Amount:"));
        purchaseAmountField = new JTextField();
        purchasePanel.add(purchaseAmountField);

        JButton addPurchaseButton = new JButton("Add Purchase");
        addPurchaseButton.addActionListener(new AddPurchaseButtonListener());
        purchasePanel.add(addPurchaseButton);

        inputPanel.add(purchasePanel);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new CalculateButtonListener());
        inputPanel.add(calculateButton);

        purchasesListArea = new JTextArea(10, 30);
        purchasesListArea.setEditable(false);
        inputPanel.add(new JScrollPane(purchasesListArea));

        add(new JScrollPane(inputPanel), BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    /**
     * Action listener for the add person button.
     */
    private class AddPersonButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter person's name:");
            if (name != null && !name.trim().isEmpty()) {
                Person person = new Person(controller.getPeople().size() + 1, name);
                controller.addPerson(person);
                personComboBox.addItem(person);
            }
        }
    }

    /**
     * Action listener for the add purchase button.
     */
    private class AddPurchaseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Person person = (Person) personComboBox.getSelectedItem();
            if (person != null && !purchaseAmountField.getText().trim().isEmpty()) {
                try {
                    double amount = Double.parseDouble(purchaseAmountField.getText().trim());
                    Purchase purchase = new Purchase(person, amount);
                    controller.addPurchase(purchase);
                    purchaseAmountField.setText("");
                    updatePurchasesList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a person and enter an amount.");
            }
        }
    }

    /**
     * Updates the text area showing the list of purchases.
     */
    private void updatePurchasesList() {
        StringBuilder purchasesList = new StringBuilder();
        for (Purchase purchase : controller.getPurchases()) {
            purchasesList.append(purchase.toString()).append("\n");
        }
        purchasesListArea.setText(purchasesList.toString());
    }

    /**
     * Action listener for the calculate button.
     */
    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String result = controller.calculateCosts();
            resultArea.setText(result);
        }
    }

    /**
     * Shows the application window.
     */
    public void showApp() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
