import org.sqlite.util.StringUtils;

/**
 * class that handles all order summary updates.  Class takes in data from checkOutPanel on GameStore GUI and uses it
 * to create a simple order summary page base on user's shopping cart input
 */

import javax.swing.*;

public class OrderSummaryGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel orderSummaryPanel;
    private JTextArea orderSummaryTextArea;
    // handles order summary GUI functions
    OrderSummaryGUI(JTextField customerNameTextField, JTextField shippingAddressTextField, JLabel shippingFeeLabel,
                    JLabel subtotalLabel, JLabel taxesLabel, JLabel totalLabel) {
        // sets GUI settings
        setContentPane(mainPanel); // opens entire GUI form when run
        setTitle("Order Summary");
        setSize(500, 750);
        setVisible(true); // makes window visible when program is run


        displayOrderInfo(customerNameTextField, shippingAddressTextField, shippingFeeLabel,
                subtotalLabel, taxesLabel, totalLabel);

    }
    // sets text from checkOutPanel in gameStoreGUI to Order Summary text area
    private void displayOrderInfo(JTextField customerNameTextField, JTextField shippingAddressTextField,
                                  JLabel shippingFeeLabel, JLabel subtotalLabel, JLabel taxesLabel, JLabel totalLabel) {

            orderSummaryTextArea.setText("Customer Name: " + customerNameTextField.getText() + "\n\n"
                    + "Shipping Address: " + shippingAddressTextField.getText() + "\n\n"
                    + "Shipping Fee: " + shippingFeeLabel.getText() + "\n\n"
                    + "Subtotal: $" + subtotalLabel.getText() + "\n\n"
                    + "Taxes: $" + taxesLabel.getText() + "\n\n"
                    + "Total: $" + totalLabel.getText());

        }


}
