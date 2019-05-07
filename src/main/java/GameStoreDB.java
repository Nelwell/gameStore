import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

// database communications
public class GameStoreDB {
    //Categorical statement query strings
    private static final String GET_ALL_CATEGORIES = "SELECT * FROM inventory";
    private static final String GET_CONSOLES = "SELECT * FROM inventory WHERE category LIKE 'Consoles'";
    private static final String GET_ACCESSORIES = "SELECT * FROM inventory WHERE category LIKE 'Accessories'";
    private static final String GET_GAMES = "SELECT * FROM inventory WHERE category LIKE 'Games'";

    //PS4 Filter statement query strings
    private static final String ALL_PS4_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'PS4'";
    private static final String CONSOLE_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'PS4'";
    private static final String ACCESSORIES_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'PS4'";
    private static final String GAMES_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'PS4'";

    //Xbox Filter statement query strings
    private static final String ALL_XBOX_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'Xbox'";
    private static final String CONSOLE_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'Xbox'";
    private static final String ACCESSORIES_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'Xbox'";
    private static final String GAMES_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'Xbox'";

    // Nintendo
    private static final String ALL_NINTENDO_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'Nintendo Switch'";
    private static final String CONSOLE_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'Nintendo Switch'";
    private static final String ACCESSORIES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'Nintendo Switch'";
    private static final String GAMES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'Nintendo Switch'";

    public static ArrayList<String> getCategories() {

        ArrayList<String> allCategories = new ArrayList<>();
        ArrayList<String> duplicateCategoriesRemoved = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url)) { // Connect
            Statement statement = connection.createStatement(); // Statements are used to issue queries

            String getCategories = "SELECT CATEGORY FROM inventory"; // Query to fetch data
            ResultSet categories = statement.executeQuery(getCategories); // Use executeQuery. It returns a ResultSet

            while (categories.next()) {  // Have to loop over the ResultSet to read it. Loop reads one row at a time
                String productName = categories.getString("CATEGORY"); // Can get data from each column, by column name
                allCategories.add(productName);
            }
            categories.close(); // Close ResultSet when done using it

        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        // for loop to remove duplicate strings from allCategories list
        for (String cat : allCategories) {
            // If this element is not present in newList then add it
            if (!duplicateCategoriesRemoved.contains(cat)) {
                duplicateCategoriesRemoved.add(cat);
            }
        }
        return duplicateCategoriesRemoved;

    }

    public static Vector getColumnNames() {

        Vector<String> colNames = new Vector<String>();

        colNames.add("Product Image");
        colNames.add("Product");
        colNames.add("Platform");
        colNames.add("Price");

        return colNames;
    }


    public static Vector<Vector> getCategoriesResultSet(JComboBox<String> CategoriesOptionsBox, JRadioButton PS4RadioButton,
                                                        JRadioButton xboxRadioButton, JRadioButton nintendoRadioButton) {

        Vector<Vector> browserData = new Vector<>();

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement createStatement = connection.createStatement()) {

            String selectedCategory = (String) CategoriesOptionsBox.getSelectedItem();

            if (selectedCategory.equals("All")) {
                browserData = applySelectedFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement,
                        GET_ALL_CATEGORIES, ALL_PS4_FILTER, ALL_XBOX_FILTER, ALL_NINTENDO_FILTER);
            }
            if (selectedCategory.equals("Consoles")) {
                browserData = applySelectedFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement,
                        GET_CONSOLES, CONSOLE_PS4_FILTER, CONSOLE_XBOX_FILTER, CONSOLE_NINTENDO_FILTER);
            }
            if (selectedCategory.equals("Accessories")) {
                browserData = applySelectedFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement,
                        GET_ACCESSORIES, ACCESSORIES_PS4_FILTER, ACCESSORIES_XBOX_FILTER, ACCESSORIES_NINTENDO_FILTER);
            }
            if (selectedCategory.equals("Games")) {
                browserData = applySelectedFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement,
                        GET_GAMES, GAMES_PS4_FILTER, GAMES_XBOX_FILTER, GAMES_NINTENDO_FILTER);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return browserData;
    }


    private static Vector<Vector> applySelectedFilter(JRadioButton PS4RadioButton, JRadioButton xboxRadioButton,
                                                      JRadioButton nintendoRadioButton, Statement createStatement,
                                                      String getAllCategories, String allPs4Filter, String allXboxFilter,
                                                      String allNintendoFilter) throws SQLException {

        ResultSet selectedCategoryAndFilter ;
        Vector<Vector> productInfo;
        selectedCategoryAndFilter  = createStatement.executeQuery(getAllCategories);
        if (PS4RadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(allPs4Filter);
        } else if (xboxRadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(allXboxFilter);
        } else if (nintendoRadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(allNintendoFilter);
        }
        productInfo = getProductList(selectedCategoryAndFilter );
        return productInfo;
    }


    public static Vector<Vector> getProductList(ResultSet catAndFilter) throws SQLException {

        Vector<Vector> productInfo  = new Vector<>();

        String image;
        String name;
        String platform;
        double price;

        while (catAndFilter.next()) {

            image = catAndFilter.getString("PRODUCT_IMAGE");
            name = catAndFilter.getString("PRODUCT_NAME");
            platform = catAndFilter.getString("PLATFORM");
            price = catAndFilter.getDouble("PRICE");

            Vector columnData = new Vector();

            columnData.add(image);
            columnData.add(name);
            columnData.add(platform);
            columnData.add(price);

            productInfo .add(columnData);
        }
        return productInfo ;
    }
}