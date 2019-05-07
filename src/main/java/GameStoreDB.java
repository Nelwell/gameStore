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

        Vector<Vector> vectors = new Vector<>();

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement statement = connection.createStatement()) {

            String selectedCategory = (String) CategoriesOptionsBox.getSelectedItem();

            ResultSet category;

            if (selectedCategory.equals("All")) {
                category = statement.executeQuery(GET_ALL_CATEGORIES);
                if (PS4RadioButton.isSelected()) {
                    xboxRadioButton.setSelected(false);
                    nintendoRadioButton.setSelected(false);
                    category = statement.executeQuery(ALL_PS4_FILTER);
//                } else{
//                    PS4RadioButton.setSelected(false);
//                    category = statement.executeQuery(ALL_XBOX_FILTER);
                }
                vectors = getProductList(category);
            }
            if (selectedCategory.equals("Consoles")) {
                category = statement.executeQuery(GET_CONSOLES);
                if (PS4RadioButton.isSelected()) {
                    category = statement.executeQuery(CONSOLE_PS4_FILTER);
                }
                vectors = getProductList(category);
            }
            if (selectedCategory.equals("Accessories")) {
                category = statement.executeQuery(GET_ACCESSORIES);
                if (PS4RadioButton.isSelected()) {
                    category = statement.executeQuery(ACCESSORIES_PS4_FILTER);
                }
                vectors = getProductList(category);
            }
            if (selectedCategory.equals("Games")) {
                category = statement.executeQuery(GET_GAMES);
                if (PS4RadioButton.isSelected()) {
                    category = statement.executeQuery(GAMES_PS4_FILTER);
                }
                vectors = getProductList(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vectors;
    }

    public static Vector<Vector> getProductList(ResultSet category) throws SQLException {

        Vector<Vector> vectors = new Vector<>();

        String image;
        String name;
        String platform;
        double price;

        while (category.next()) {

            image = category.getString("PRODUCT_IMAGE");
            name = category.getString("PRODUCT_NAME");
            platform = category.getString("PLATFORM");
            price = category.getDouble("PRICE");

            Vector v = new Vector();

            v.add(image);
            v.add(name);
            v.add(platform);
            v.add(price);

            vectors.add(v);
        }
        return vectors;

    }
}