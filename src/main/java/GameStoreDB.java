import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

// database communications
public class GameStoreDB {

    private static final String GET_CONSOLES = "SELECT * FROM inventory WHERE category LIKE 'Consoles'";

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
        colNames.add("Price");

        return colNames;
    }


    public static Vector<Vector> getConsoles() {

        try (Connection connection = DriverManager.getConnection(GameStoreConfigDB.gameStoreDb_url);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(GET_CONSOLES);

            Vector<Vector> vectors = new Vector<>();

            String image;
            String name;
            double price;

            while (rs.next()) {

                image = rs.getString("PRODUCT_IMAGE");
                name = rs.getString("PRODUCT_NAME");
                price = rs.getDouble("PRICE");

                Vector v = new Vector();
                v.add(image);
                v.add(name);
                v.add(price);

                vectors.add(v);
            }
            return vectors;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
