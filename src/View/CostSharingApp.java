package View;

import Controller.CostSharingController;
import Model.Person;
import Model.Purchase;
import Util.FileWriteHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

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

        JButton finallyButton = new JButton("Finally");
        finallyButton.addActionListener(new FinallyButtonListener());
        inputPanel.add(finallyButton);

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
     * Action listener for the finally button.
     */
    private class FinallyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = "cost_sharing_data.txt";

            try {
                StringBuilder content = new StringBuilder();
                content.append("List of Purchases:\n");
                List<Purchase> purchases = controller.getPurchases();
                for (Purchase purchase : purchases) {
                    content.append(purchase.toString()).append("\n");
                }
                content.append("\nCost Sharing Result:\n");
                content.append(controller.calculateCosts());

                FileWriteHelper.writeToFile(filePath, content.toString());

                JOptionPane.showMessageDialog(null, "Data saved to file: " + filePath);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error occurred while saving data: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * Shows the application window.
     */
    public void showApp() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    /**
     * Main method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CostSharingApp app = new CostSharingApp();
        app.showApp();
    }
}
