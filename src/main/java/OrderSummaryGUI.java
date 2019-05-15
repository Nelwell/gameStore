import org.sqlite.util.StringUtils;

import javax.swing.*;

public class OrderSummaryGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel orderSummaryPanel;
    private JTextArea orderSummaryTextArea;

    OrderSummaryGUI(JTextField customerNameTextField, JTextField shippingAddressTextField, JLabel shippingFeeLabel,
                    JLabel subtotalLabel, JLabel taxesLabel, JLabel totalLabel) {

        setContentPane(mainPanel); // opens entire GUI form when run
        setTitle("Order Summary");
        setSize(500, 750);
//        setExtendedState(JFrame.MAXIMIZED_BOTH); // opens window to fullscreen
        setVisible(true); // makes window visible when program is run
//        pack();


//        String ORDER_HEADER = "ORDER_DETAILS";
//        String NAME = "NAME";
//        String ADDRESS = "ADDRESS";
//        String DATE = "DATE";
//        String SHIPPING_FEE = shippingFeeLabel.getText();
//        String SUBTOTAL = "SUBTOTAL";
//        String TAXES = "TAXES";
//        String TOTAL = "TOTAL";


        displayOrderInfo(customerNameTextField, shippingAddressTextField, shippingFeeLabel,
                subtotalLabel, taxesLabel, totalLabel);

    }

    private void displayOrderInfo(JTextField customerNameTextField, JTextField shippingAddressTextField,
                                  JLabel shippingFeeLabel, JLabel subtotalLabel, JLabel taxesLabel, JLabel totalLabel) {


        orderSummaryTextArea.setText("Customer Name: " + customerNameTextField.getText() + "\n\n"
        + "Shipping Address: " + shippingAddressTextField.getText() + "\n\n"
        + "Shipping Fee: " + shippingFeeLabel.getText() + "\n\n"
        + "Subtotal: $" + subtotalLabel.getText() + "\n\n"
        + "Taxes: $" + taxesLabel.getText() + "\n\n"
        + "Total: $" + totalLabel.getText());


//        int width = 90;
//
//        String lines[] = {
//            "************ Couch Potato Gaming Order Summary ************", width),
//            "",
//            "&{ORDER_HEADER}",
//            "",
//            "Customer Name: &{NAME}",
//            "Address of garden: &{ADDRESS}",
//            "",
//            "Date of service: &{DATE}",
//            "Size of garden: &{SHIPPING_FEE}",
//            "",
//            "Lawn mowing service charge: $ &{SUBTOTAL}",
//            "Leaf raking service charge: $ &{TAXES}",
//            "",
//            "Total: $ &{TOTAL}",
//            "",
//            "Please send payment to the address above.",
//            "Thank you for your business.";
//    } ;
    }


}
