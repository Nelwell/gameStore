import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

// database communications
class GameStoreDB {

    // gets current stock for product
    private static final String GET_PRODUCT_STOCK = "SELECT stock FROM inventory WHERE product_name LIKE 'PS4 System'";

//    private static final String CREATE_CART_TABLE = "CREATE TABLE shopping_cart (ID integer primary key, QUANTITY integer, PRODUCT text, PRICE double)";
    private static final String CLEAR_CART_TABLE = "DELETE FROM shopping_cart";

    // Categorical statement query strings
    private static final String GET_ALL_CATEGORIES = "SELECT * FROM inventory";
    private static final String GET_CONSOLES = "SELECT * FROM inventory WHERE category LIKE 'Consoles'";
    private static final String GET_ACCESSORIES = "SELECT * FROM inventory WHERE category LIKE 'Accessories'";
    private static final String GET_GAMES = "SELECT * FROM inventory WHERE category LIKE 'Games'";

    // PS4 Filter statement query strings
    private static final String ALL_PS4_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'PS4'";
    private static final String CONSOLE_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'PS4'";
    private static final String ACCESSORIES_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'PS4'";
    private static final String GAMES_PS4_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'PS4'";

    // Xbox Filter statement query strings
    private static final String ALL_XBOX_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'Xbox'";
    private static final String CONSOLE_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'Xbox'";
    private static final String ACCESSORIES_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'Xbox'";
    private static final String GAMES_XBOX_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'Xbox'";

    // Nintendo Filter statement query strings
    private static final String ALL_NINTENDO_FILTER = "SELECT * FROM inventory WHERE platform LIKE 'Nintendo Switch'";
    private static final String CONSOLE_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Consoles' AND platform LIKE 'Nintendo Switch'";
    private static final String ACCESSORIES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Accessories' AND platform LIKE 'Nintendo Switch'";
    private static final String GAMES_NINTENDO_FILTER = "SELECT * FROM inventory WHERE category LIKE 'Games' AND platform LIKE 'Nintendo Switch'";

    // Shopping Cart queries
    private static final String ADD_TO_CART = "INSERT INTO shopping_cart VALUES ( ? , ? , ? , ? )";
    private static final String DELETE_FROM_CART = "DELETE FROM shopping_cart WHERE ID = ( ? )";
    private static final String GET_ALL_CART_ITEMS = "SELECT * FROM shopping_cart";


//    public void insertImage() {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        FileInputStream inputStream = null;
//
//        try {
//            File image = new File("C:/honda.jpg");
//            inputStream = new FileInputStream(image);
//
//            connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url); // Connect
//            statement = connection.prepareStatement("insert into trn_imgs(img_title, img_data) " + "values(?,?)");
//            statement.setString(1, "Honda Car");
//            statement.setBinaryStream(2, (InputStream) inputStream, (int)(image.length()));
//
//            statement.executeUpdate();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("FileNotFoundException: - " + e);
//        } catch (SQLException e) {
//            System.out.println("SQLException: - " + e);
//        } finally {
//
//            try {
//                connection.close();
//                statement.close();
//            } catch (SQLException e) {
//                System.out.println("SQLException Finally: - " + e);
//            }
//        }
//    }


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

        Vector<String> colNames = new Vector<>();

        colNames.add("ID #");
        colNames.add("Product Image");
        colNames.add("Product");
        colNames.add("Platform");
        colNames.add("Price ($)");

        return colNames;
    }

    static Vector getCartColumnNames() {

        Vector<String> cartColNames = new Vector<>();

//        colNames.add("Product Image");
        cartColNames.add("ID #");
        cartColNames.add("Quantity");
        cartColNames.add("Product");
        cartColNames.add("Price ($)");

        return cartColNames;
    }

    static Vector<Vector> getCartItems() {

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_ALL_CART_ITEMS);

            Vector<Vector> vectors = new Vector<>();

            int id;
            int quantity;
            String product;
            double price;

            while (rs.next()) {

                id = rs.getInt("ID");
                quantity = rs.getInt("QUANTITY");
                product = rs.getString("PRODUCT");
                price = rs.getDouble("PRICE");

                Vector v = new Vector();
                v.add(id); v.add(quantity); v.add(product); v.add(price);

                vectors.add(v);
            }
            return vectors;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static void addToCart(int productId, int quantity, String selectedProduct, double price) {

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_CART)) {

            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setString(3, selectedProduct);
            preparedStatement.setDouble(4, price);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static void clearShoppingCart() {

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             PreparedStatement preparedStatement = connection.prepareStatement(CLEAR_CART_TABLE)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static void deleteFromCart(int selectedProduct) {

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FROM_CART)) {

            preparedStatement.setInt(1, selectedProduct);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static Vector<Vector> getCategoriesResultSet(JComboBox<String> CategoriesOptionsBox, JRadioButton PS4RadioButton,
                                                 JRadioButton xboxRadioButton, JRadioButton nintendoRadioButton) {
        // initialize vector
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
                                                      String getCategory, String ps4Filter, String xboxFilter,
                                                      String nintendoFilter) throws SQLException {

        ResultSet selectedCategoryAndFilter;
        Vector<Vector> productInfo;

        selectedCategoryAndFilter  = createStatement.executeQuery(getCategory);
        if (PS4RadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(ps4Filter);
        } else if (xboxRadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(xboxFilter);
        } else if (nintendoRadioButton.isSelected()) {
            selectedCategoryAndFilter  = createStatement.executeQuery(nintendoFilter);
        }
        productInfo = getProductList(selectedCategoryAndFilter);
        return productInfo;
    }


    private static Vector<Vector> getProductList(ResultSet catAndFilter) throws SQLException {

        Vector<Vector> productInfo  = new Vector<>();

        int id;
        Blob image;
        String name, platform;
        double price;

        while (catAndFilter.next()) {

            id = catAndFilter.getInt("ID");
            image = catAndFilter.getBlob("PRODUCT_IMAGE");
            name = catAndFilter.getString("PRODUCT_NAME");
            platform = catAndFilter.getString("PLATFORM");
            price = catAndFilter.getDouble("PRICE");

            Vector columnData = new Vector();

            columnData.add(id);
            columnData.add(image);
            columnData.add(name);
            columnData.add(platform);
            columnData.add(price);

            productInfo.add(columnData);
        }
        return productInfo ;
    }

    static int getAvailableStock(){

        int stockToInt = 0;

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement statement = connection.createStatement()) {

            ResultSet stockResultSet = statement.executeQuery(GET_PRODUCT_STOCK); // Use executeQuery. It returns a ResultSet

            while (stockResultSet.next()) {  // Have to loop over the ResultSet to read it. Loop reads one row at a time
                String stock = stockResultSet.getString("STOCK"); // Can get data from each column, by column name
                stockToInt = Integer.parseInt(stock);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stockToInt;
    }
}