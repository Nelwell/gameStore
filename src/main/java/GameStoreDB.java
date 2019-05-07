import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

// database communications
class GameStoreDB {
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
    private static final String CONSOLE_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'Nintendo'";
    private static final String ACCESSORIES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'Nintendo'";
    private static final String GAMES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'Nintendo'";

    static ArrayList<String> getCategories() {

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

    static Vector getColumnNames() {

        Vector<String> colNames = new Vector<String>();

        colNames.add("Product Image");
        colNames.add("Product");
        colNames.add("Platform");
        colNames.add("Price");

        return colNames;
    }


    static Vector<Vector> getCategoriesResultSet(JComboBox<String> CategoriesOptionsBox, JRadioButton PS4RadioButton,
                                                 JRadioButton xboxRadioButton, JRadioButton nintendoRadioButton) {

        Vector<Vector> browserData = new Vector<>();

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement createStatement = connection.createStatement()) {

            String selectedCategory = (String) CategoriesOptionsBox.getSelectedItem();

            if (selectedCategory.equals("All")) {

                browserData = getFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement, GET_ALL_CATEGORIES);
            }
            if (selectedCategory.equals("Consoles")) {
                browserData = getFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement, GET_CONSOLES);
            }
            if (selectedCategory.equals("Accessories")) {
                browserData = getFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement, GET_ACCESSORIES);
            }
            if (selectedCategory.equals("Games")) {
                browserData = getFilter(PS4RadioButton, xboxRadioButton, nintendoRadioButton, createStatement, GET_GAMES);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return browserData;
    }

    private static Vector<Vector> getFilter(JRadioButton PS4RadioButton, JRadioButton xboxRadioButton, JRadioButton nintendoRadioButton,
                                            Statement createStatement, String categoryQuery) throws SQLException {
        ResultSet selectedCategoryAndFilter;
        Vector<Vector> productInfo;
        selectedCategoryAndFilter = createStatement.executeQuery(categoryQuery);
        if (PS4RadioButton.isSelected()) {
            selectedCategoryAndFilter = createStatement.executeQuery(ALL_PS4_FILTER);
        } else if (xboxRadioButton.isSelected()) {
            selectedCategoryAndFilter = createStatement.executeQuery(ALL_XBOX_FILTER);
        } else if (nintendoRadioButton.isSelected()) {
            selectedCategoryAndFilter = createStatement.executeQuery(ALL_NINTENDO_FILTER);
        }
        productInfo = getProductList(selectedCategoryAndFilter);
        return productInfo;
    }

    private static Vector<Vector> getProductList(ResultSet catAndFilter) throws SQLException {

        Vector<Vector> productInfo = new Vector<>();

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

            productInfo.add(columnData);
        }
        return productInfo;

    }
}