package org.example.librarymanagementsystemgui.UtilityClasses;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class User extends UtilityMethods{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;

//For DB Connection
    private final static String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=Library;" +
            "user=sa;password=12345678;encrypt=false;trustServerCertificate=true";


//Constructors
    public User(){

    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password){
        this.setfirstName(firstName);
        this.setlastName(lastName);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.setPassword(password);
    }


//Setters and Getters
    public void setfirstName(String firstName) {
        if(!firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setlastName(String lastName) {
        if(!lastName.isEmpty()) {
            this.lastName = lastName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public String getName(){return this.firstName + ' ' + this.lastName;}

    public void setPhoneNumber(String phoneNumber) {
        if(!phoneNumber.isEmpty()) {
            this.phoneNumber = phoneNumber;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        if(!email.isEmpty()) {
            this.email = email;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password){
        if(!password.isEmpty()){
            this.password = password;
        }
    }

    public String getPassword() { return password; }


//Common methods between members and admins
    public void ViewAvailableBooks(ActionEvent event) {
        String sql = "SELECT * FROM AvailableBooks";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String bookName = rs.getString("BookName");
                String bookPrice = rs.getString("Price");
                String borrowPrice = rs.getString("BorrowPrice");
                String bookCategory = rs.getString("Category");
                String authorName = rs.getString("AuthorName");
                String publisherName = rs.getString("PublisherName");

                String content = "Book ISBN: " + isbn + "\nBook Name: " + bookName + "\nCategory: " + bookCategory +
                        "\nAuthor: " + authorName + "\nPublisher: " + publisherName + "\nBuy Price: " + bookPrice +
                        "\nBorrow Price: " + borrowPrice;
                alert(event, "Book Details",content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ViewAllBooks(ActionEvent event) {
        String query = "SELECT * FROM AllBooks";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String bookName = rs.getString("BookName");
                String bookPrice = rs.getString("Price");
                String borrowPrice = rs.getString("BorrowPrice");
                String bookCategory = rs.getString("CategoryName");
                String authorName = rs.getString("AuthorName");
                String publisherName = rs.getString("PublisherName");

                String content = "Book ISBN: " + isbn + "\nBook Name: " + bookName + "\nCategory: " + bookCategory +
                        "\nAuthor: " + authorName + "\nPublisher: " + publisherName + "\nBuy Price: " + bookPrice +
                        "\nBorrow Price: " + borrowPrice;
                alert(event, "Book Details",content);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//SearchForBook let the user choose how to search for the book
    public void SearchForBook(ActionEvent event) throws IOException{
        TextInputDialog howToSearch = new TextInputDialog();

        //Visual customizations for the inputDialogue
        howToSearch.setGraphic(null);  // Removes the default icon
        // Set custom icon for the dialog
        Stage dialogStage = (Stage) howToSearch.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        howToSearch.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        howToSearch.getDialogPane().getStyleClass().add("custom-dialog");

        //To store the value of the string coming from the inputDialogue
        Optional<String> howToSearchOptional = null;

        try {
            howToSearch.setTitle("\"Book search\"");
            howToSearch.setHeaderText(null); // Remove the default header
            howToSearch.setContentText("How do you want to search for the Book? \n1)By Name \n2)By Author Name \n3)By Category");
            howToSearchOptional = howToSearch.showAndWait();

            if(howToSearchOptional.isEmpty()){
                throw new InputException("Empty inputs");
            }

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        String howToSearchString = howToSearchOptional.get();
        int choice = Integer.parseInt(howToSearchString);

        switch (choice) {
            case 1:
                searchForBookByName(event);
                break;
            case 2:
                searchForBookByAuthor(event);
                break;
            case 3:
                searchForBookByCategory(event);
                break;
        }
    }

    public void searchForBookByName(ActionEvent event) throws IOException{
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC SearchBookByName ?")
        ) {
            String bookName = inputBookName(event);
            stmt.setString(1, bookName);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;

            ResultSetMetaData rsmd = rs.getMetaData();
            boolean hasISBNColumn = false;

            // Check if ISBN column exists
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                if (rsmd.getColumnName(i).equalsIgnoreCase("ISBN")) {
                    hasISBNColumn = true;
                    break;
                }
            }

            while (rs.next()) {
                if (!hasISBNColumn) {
                    alert(event, "No available books", rs.getString("message"));
                    return;
                }

                hasResults = true;
                String content = "ISBN: " + rs.getString("ISBN") +
                        "\nName: " + rs.getString("BookName") +
                        "\nAuthor: " + rs.getString("AuthorName") +
                        "\nCategory: " + rs.getString("CategoryName") +
                        "\nPublisher: " + rs.getString("PublisherName") +
                        "\nPublish Date: " + rs.getString("PublishDate") +
                        "\nPrice: $" + rs.getDouble("Price") +
                        "\nBorrow Price: $" + rs.getDouble("BorrowPrice");

                alert(event, "Book Details", content);
            }

            if (!hasResults) {
                alert(event, "No available books", "No Books Found!");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error:", e.getMessage());
        }
    }

    public void searchForBookByCategory(ActionEvent event) throws IOException{
        String category = inputCategory(event);

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC SearchBookByCategory ?")
        ) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            boolean hasResults = false;
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                if (metaData.getColumnCount() == 1 && metaData.getColumnLabel(1).equalsIgnoreCase("message")) {
                    alert(event, "No available books", rs.getString("message"));
                    return;
                }

                hasResults = true;

                alert(event, "Book Details", "Book: " + rs.getString("BookName") + "\nAuthor: " + rs.getString("AuthorName") +
                        "\nDate: " + rs.getString("PublishDate"));
            }

            if (!hasResults) {
                alert(event, "No available books", "No Books Found!");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error", e.getMessage());
        }
    }

    public void searchForBookByAuthor(ActionEvent event) throws IOException {
        String authorName = inputAuthorName(event);

        String sql = "EXEC SearchBookByAuthorName ?";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, authorName);
            ResultSet rs = pstmt.executeQuery();

            boolean hasRes = false;

            while (rs.next()) {
                StringBuilder result = new StringBuilder();
                hasRes = true;

                result.append("Book: ").append(rs.getString("BookName"))
                        .append("\nCategory: ").append(rs.getString("CategoryName"))
                        .append("\nPublish Date: ").append(rs.getString("PublishDate"));

                alert(event, "Book Details", result.toString());
            }
            if (!hasRes){
                alert(event, "Not Found", "No books found for this author!");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error", e.getMessage());
        }
    }


//Some input utility functions
    public String inputBookISBN(ActionEvent event) throws IOException {
        String bookISBN = inputDialogue(event, "Input book ISBN", "Please enter the ISBN of the book:");

        try {
            if(bookISBN.isEmpty())
                throw new InputException("Empty input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }
        return bookISBN;
    }

    public String inputBookName(ActionEvent event) throws IOException {
        String bookName = inputDialogue(event, "Input book name", "Please enter the name of the book:");

        try {
            if(bookName.isEmpty())
                throw new InputException("Empty input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return bookName;
    }

    public String inputAuthorName(ActionEvent event) throws IOException{
        String authorName = inputDialogue(event, "Input author name", "Please enter the name of the author:");

        try {
            if(authorName.isEmpty())
                throw new InputException("Empty input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return authorName;
    }

    public String inputPublisherName(ActionEvent event) throws IOException{
        String publisherName = inputDialogue(event, "Input publisher name", "Please enter the name of the publisher:");

        try {
            if(publisherName.isEmpty())
                throw new InputException("Empty input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return publisherName;
    }

    public String inputCategory(ActionEvent event) throws IOException{
        String category = inputDialogue(event, "Input category", "Please enter the category of the book:");

        try {
            if(category.isEmpty())
                throw new InputException("Empty input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return category;
    }
}
