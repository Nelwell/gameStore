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
    private JButton placeOrderButton, addToCartButton, clearCartButton;
    private JTable ProductBrowserTable, ShoppingCartTable;
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

//        GameStoreDB.createShoppingCartTable();

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

        ProductBrowserTable.setAutoCreateRowSorter(true);

    }


    private void configureProductTable() {

        Vector columnNames = GameStoreDB.getColumnNames();
        Vector<Vector> tableData = GameStoreDB.getCategoriesResultSet(CategoriesOptionsBox, PS4RadioButton, xboxRadioButton,
                nintendoRadioButton);

//        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames);
        ProductBrowserTable.setModel(new DefaultTableModel(columnNames, tableData) {
            Class[] types = { Integer.class, String.class, String.class,
                    String.class, Double.class };
            boolean[] canEdit = { false, false, false, false, false };

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int columnIndex) {
                return this.canEdit[columnIndex];
            }
        });
//        tableModel.fireTableDataChanged();

    }


    private void configureShoppingCartTable() {

        Vector cartColumnNames = GameStoreDB.getCartColumnNames();
        Vector<Vector> cartTableData = GameStoreDB.getCartItems();

        DefaultTableModel cartTableModel = new DefaultTableModel(cartTableData, cartColumnNames);
        ShoppingCartTable.setModel(cartTableModel);

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

        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStoreDB.clearCart();
            }
        });

        }

    private void addProductToCart() {

        int productQuantity = 1;

        //Get selected product
        String selectedProduct = (String) ProductBrowserTable.getValueAt(ProductBrowserTable.getSelectedRow(), 2);
        double productPrice = (double) ProductBrowserTable.getValueAt(ProductBrowserTable.getSelectedRow(), 4);

//        //If no selected product blank
//        if (selectedProduct == null || selectedProduct.trim().equals("")) {
//            JOptionPane.showMessageDialog(rootPane, "Please select a product");
//            return;
//        }

        GameStoreDB.addToCart(productQuantity, selectedProduct, productPrice);

//            //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
//            int ratingData = (Integer) (ratingSpinner.getValue());

        configureShoppingCartTable();

//        updateCartTable(); // ??
//
//    }
//
//    private void updateCartTable() {
    }
}

