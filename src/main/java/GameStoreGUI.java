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
    private JButton placeOrderButton;
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

        // Variables to hold sorting options
        String lowPrice = "Price: Low to high";
        String highPrice = "Price: High to low";
        String alphabetical = "Alphabetical";

        // adds sorting options to comboBox
        SortingOptionsBox.addItem(lowPrice);
        SortingOptionsBox.addItem(highPrice);
        SortingOptionsBox.addItem(alphabetical);

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
                xboxRadioButton.setSelected(false);
                nintendoRadioButton.setSelected(false);
                configureProductTable();
            }
        });

        xboxRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PS4RadioButton.setSelected(false);
                nintendoRadioButton.setSelected(false);
                configureProductTable();

            }
        });

        nintendoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xboxRadioButton.setSelected(false);
                PS4RadioButton.setSelected(false);
                configureProductTable();

            }
        });
    }
}

