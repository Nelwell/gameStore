import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class GameStoreGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel ProductBrowserPanel, CheckOutPanel, ShoppingCartPanel;
    private JTextField customerNameTextField, shippingAddressTextField;
    protected JComboBox<String> CategoriesOptionsBox;
    private JComboBox<String> SortingOptionsBox;
    private JButton placeOrderButton, addToCartButton;
    private JTable ProductBrowserTable;
    private JRadioButton PS4RadioButton, xboxRadioButton, nintendoRadioButton;


    GameStoreGUI() {

        setContentPane(mainPanel); // opens entire GUI form when run
        pack();
        setTitle("Couch Potato Shopping Application");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // opens window to fullscreen
//        setUndecorated(false); // keeps top menu bar for closing/min/maximizing etc.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // ends program when window is closed
        setVisible(true); // makes window visible when program is run

        eventHandlers(); // configure event handlers for GUI interaction

        // calls method that will get list of category options from inventory table
        ArrayList<String> inventoryCategories = GameStoreDB.getCategories();

        // Categories assigned to individual variables from array
        String all = "All";
        String consoles = inventoryCategories.get(0);
        String accessories = inventoryCategories.get(1);
        String games = inventoryCategories.get(2);

        // adds category options to comboBox
        CategoriesOptionsBox.addItem(all);
        CategoriesOptionsBox.addItem(consoles);
        CategoriesOptionsBox.addItem(accessories);
        CategoriesOptionsBox.addItem(games);

    }


    private void configureProductTable() {

        Vector columnNames = GameStoreDB.getColumnNames();
        Vector<Vector> tableData = GameStoreDB.getCategoriesResultSet(CategoriesOptionsBox, PS4RadioButton, xboxRadioButton,
                nintendoRadioButton);

        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames);
        ProductBrowserTable.setModel(tableModel);

    }


    private void eventHandlers() {

        CategoriesOptionsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureProductTable();
            }
        });

        PS4RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // deselects other radio button filter options
                xboxRadioButton.setSelected(false);
                nintendoRadioButton.setSelected(false);
                configureProductTable();
            }
        });

        xboxRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // deselects other radio button filter options
                PS4RadioButton.setSelected(false);
                nintendoRadioButton.setSelected(false);
                configureProductTable();

            }
        });

        nintendoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // deselects other radio button filter options
                xboxRadioButton.setSelected(false);
                PS4RadioButton.setSelected(false);
                configureProductTable();

            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToCart();
            }
        });

        }

    private void addProductToCart() {

            //Get Movie title, make sure it's not blank
            String selectedProduct = (String) ProductBrowserTable.getValueAt(ProductBrowserTable.getSelectedRow(), 2);
//
//            if (titleData == null || titleData.trim().equals("")) {
//                JOptionPane.showMessageDialog(rootPane, "Please enter a title for the new movie");
//                return;
//            }
//
//            //Get movie year. Check it's a number between 1900 and present year
//            int yearData;
//
//            try {
//                yearData = Integer.parseInt(yearTextField.getText());
//                if (yearData < 1900 || yearData > Calendar.getInstance().get(Calendar.YEAR)) {
//                    //Calendar.getInstance() returns a Calendar object representing right now.
//                    //calenderObject.get(Calendar.MONTH) gets current month, calenderObject.get(Calendar.SECOND) gets current second
//                    //Can get and set other time/date fields- check Java documentation for others
//                    //http://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
//                    throw new NumberFormatException("Year needs to be between 1900 and present year");
//                }
//            } catch (NumberFormatException ne) {
//                JOptionPane.showMessageDialog(rootPane,
//                        "Year needs to be a number between 1900 and now");
//                return;
//            }
//
//            //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
//            int ratingData = (Integer) (ratingSpinner.getValue());
//
//            GameStoreDB.addToCart(selectedProduct);
//
            updateCartTable(); // ??

    }

    private void updateCartTable() {
    }
}

