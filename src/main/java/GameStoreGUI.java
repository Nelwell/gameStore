import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class GameStoreGUI extends JFrame {

    private JPanel mainPanel;
    public JPanel productBrowserPanel, checkOutPanel, shoppingCartPanel;
    public JTextField customerNameTextField, shippingAddressTextField;
    protected JComboBox<String> categoriesOptionsBox;
    private JButton placeOrderButton, addToCartButton, removeButton, clearCartButton;
    public JTable productBrowserTable, shoppingCartTable;
    private JRadioButton ps4RadioButton, xboxRadioButton, nintendoRadioButton;
    private JLabel shippingFeeLabel, subtotalLabel, taxesLabel, totalLabel;


    Vector columnNames;


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

//        GameStoreDB.createShoppingCartTable();

        // Categories assigned to individual variables from array
        String all = "All";
        String consoles = inventoryCategories.get(0);
        String accessories = inventoryCategories.get(1);
        String games = inventoryCategories.get(2);

        // adds category options to comboBox
        categoriesOptionsBox.addItem(all);
        categoriesOptionsBox.addItem(consoles);
        categoriesOptionsBox.addItem(accessories);
        categoriesOptionsBox.addItem(games);

        productBrowserTable.setAutoCreateRowSorter(true);

    }


    private void configureProductTable() {

        // allows for only one row to be selected at a time
        productBrowserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Vector<Vector> tableData = GameStoreDB.getCategoriesResultSet(categoriesOptionsBox, ps4RadioButton, xboxRadioButton,
                nintendoRadioButton);
        columnNames = GameStoreDB.getColumnNames();

        productBrowserTable.setModel(new DefaultTableModel(tableData, columnNames) {
            Class[] types = { Integer.class, String.class, String.class, String.class, Double.class };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });
//        tableModel.fireTableDataChanged();

    }


    private void configureShoppingCartTable() {

        // allows for only one row to be selected at a time
        shoppingCartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Vector<Vector> cartTableData = GameStoreDB.getCartItems();
        Vector cartColumnNames = GameStoreDB.getCartColumnNames();

        // sets table model and assigns object types to each column in JTable
        shoppingCartTable.setModel(new DefaultTableModel(cartTableData, cartColumnNames) {
            Class[] types = { Integer.class, Integer.class, String.class, Double.class };
            // assigns which columns are editable in shopping cart to variable
            boolean[] canEdit = { false, true, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
            // allows only quantity column in shopping cart to be edited
            public boolean isCellEditable(int row, int col) {
                return this.canEdit[col];
            }

//            @Override
//            public void setValueAt(Object val, int row, int col) {
//
//                // Get row and send new value to DB for update
////                int id = (int) getValueAt(row, 0);
//
//                int stock = GameStoreDB.getAvailableStock();
//
//                System.out.println(stock);
//
//                try {
//                    int cartQuantity = Integer.parseInt(val.toString());
//                    if (cartQuantity > stock) {
//                        throw new NumberFormatException();
//                    }
////                    updateTable();
//                } catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(GameStoreGUI.this, "Not enough stock");
//                }
//            }
        });

        if (shoppingCartTable.getRowCount() > 0) { //&& removeButton.isSelected()) {
            shoppingCartTable.setRowSelectionInterval(0, 0);
        }

    }


    private void eventHandlers() {

        categoriesOptionsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureProductTable();
            }
        });

        ps4RadioButton.addActionListener(new ActionListener() {
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
                ps4RadioButton.setSelected(false);
                nintendoRadioButton.setSelected(false);
                configureProductTable();

            }
        });

        nintendoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // deselects other radio button filter options
                xboxRadioButton.setSelected(false);
                ps4RadioButton.setSelected(false);
                configureProductTable();


            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductToCart();
                calculateAndDisplayCosts();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProductFromCart();
                calculateAndDisplayCosts();
            }
        });

        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStoreDB.clearCart();
            }
        });

        }

    private void deleteAllFromCart() {

    }

    private void addProductToCart() {

        // default quantity added to cart
        int productQuantity = 1;

        // Get selected product info
        int productId = (int) productBrowserTable.getValueAt(productBrowserTable.getSelectedRow(), 0);
        String selectedProduct = (String) productBrowserTable.getValueAt(productBrowserTable.getSelectedRow(), 2);
        double productPrice = (double) productBrowserTable.getValueAt(productBrowserTable.getSelectedRow(), 4);

//        // If no selected product blank
//        if (selectedProduct == null || selectedProduct.trim().equals("")) {
//            JOptionPane.showMessageDialog(rootPane, "Please select a product");
//            return;
//        }

        GameStoreDB.addToCart(productId, productQuantity, selectedProduct, productPrice);

//            //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
//            int ratingData = (Integer) (ratingSpinner.getValue());

        configureShoppingCartTable();

    }

    private void deleteProductFromCart() {

        // Get selected product
        int selectedProduct = (int) shoppingCartTable.getValueAt(shoppingCartTable.getSelectedRow(), 0);

//        // If no selected product blank
//        if (selectedProduct == null || selectedProduct.trim().equals("")) {
//            JOptionPane.showMessageDialog(rootPane, "Please select a product");
//            return;
//        }

        GameStoreDB.deleteFromCart(selectedProduct);

//            //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
//            int ratingData = (Integer) (ratingSpinner.getValue());

        configureShoppingCartTable();

    }

//    private void updateTable() {
//
//        Vector data = GameStoreDB.getCategoriesResultSet(categoriesOptionsBox, ps4RadioButton, xboxRadioButton,
//                nintendoRadioButton);
//        DefaultTableModel.setDataVector(data, columnNames);
//
//    }



//            String size = (String) gardenSizeComboBox.getSelectedItem();
//            double notSelected = 0.00;
//            double mowingCost = GardenServiceData.MOWING;

    // calculate amounts in checkOutPanel
    private void calculateAndDisplayCosts() {

        double subtotal = 0;
        for (int i = 0; i < shoppingCartTable.getRowCount(); i++){
            double amount = (double) shoppingCartTable.getValueAt(i, 3);
            subtotal += amount;
        }

        double emptyCart = 0.00;
        double shipFee = Fees.SHIPPING_FEE;
        double taxRate = Fees.TAX_RATE;
        double taxes = taxRate * subtotal;
        double total = shipFee + subtotal + taxes;

        if (shoppingCartTable.getRowCount() > 0) {
            shippingFeeLabel.setText(String.format("%.2f", shipFee));
            subtotalLabel.setText(String.format("%.2f", subtotal));
            taxesLabel.setText(String.format("%.2f", taxes));
            totalLabel.setText(String.format("%.2f", total));
        } else {
            shippingFeeLabel.setText(String.format("%.2f", emptyCart));
            subtotalLabel.setText(String.format("%.2f", emptyCart));
            taxesLabel.setText(String.format("%.2f", emptyCart));
            totalLabel.setText(String.format("%.2f", emptyCart));
        }

        updateTotal();

    }


    private void updateTotal() {

//        double taxes = Fees.TAXES;

        double doubleShippingFee = Double.valueOf(this.shippingFeeLabel.getText());
//        double doubleSubtotal = Double.valueOf(this.leafRakingCost.getText());
//        double serviceTotal = doubleShippingFee + doubleRakingCost;

//        invoiceTotal.setText(String.format("%.2f", serviceTotal));
    }



}

