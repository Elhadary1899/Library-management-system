package org.example.librarymanagementsystemgui.AdminClasses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.UtilityClasses.User;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class Admin extends User {
    @FXML
    public Button ViewAllBooks;
    @FXML
    public Button ViewAvailableBooks;
    @FXML
    public Button SearchForBook;
    @FXML
    public Button AddABook;
    @FXML
    public Button RemoveABook;
    @FXML
    public Button EditABook;
    @FXML
    public Button SearchForAMember;
    @FXML
    public Button RemoveAMember;
    @FXML
    public Button ViewAllMembers;
    @FXML
    public Button ListOverdueBooks;
    @FXML
    public Button AddAdmin;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

//Establishing DB Connection
    private final static String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=Library;" +
            "user=sa;password=12345678;encrypt=false;trustServerCertificate=true";

//Constructors
    public Admin(){}

    public Admin(String firstName, String lastName, String phone, String email, String password){
        super(firstName, lastName, phone, email, password);
    }


//method that invokes other methods based on the button pressed
    public void passAdminToMethods(ActionEvent event) throws IOException {
        if(event.getSource()==ViewAllBooks){
            ViewAllBooks(event);
        }else if(event.getSource()==ViewAvailableBooks){
            ViewAvailableBooks(event);
        } else if(event.getSource()==SearchForBook){
            SearchForBook(event);
        } else if(event.getSource()==AddABook){
            AddABook(event);
        } else if(event.getSource()==RemoveABook){
            RemoveABook(event);
        } else if(event.getSource()== EditABook){
            EditABook(event);
        } else if(event.getSource()==SearchForAMember){
            howToSearchForAMember(event);
        } else if(event.getSource()==RemoveAMember){
            RemoveAMember(event);
        } else if(event.getSource()==ViewAllMembers){
            ViewAllMembers(event);
        } else if(event.getSource()==AddAdmin){
            addNewAdmin(event);
        } else if(event.getSource()==ListOverdueBooks){
            ListOverdueBooks(event);
        }
    }

//Admin methods
    public void AddABook(ActionEvent event) throws IOException {
        String ISBN = inputBookISBN(event);
        String bookName = inputBookName(event);
        String authorName = inputAuthorName(event);
        String publisherName = inputPublisherName(event);
        String category = inputCategory(event);
        String publishDate = inputPublishDate(event);
        double priceToBuy = inputPriceToBuy(event);
        double priceToBorrow = priceToBuy * 0.25;
        int numOfCopies = inputNumOfCopies(event);

        String sql = "EXEC AddBook ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ISBN);
            pstmt.setString(2, bookName);
            pstmt.setString(3, authorName);
            pstmt.setString(4, publisherName);
            pstmt.setString(5, category);
            pstmt.setDouble(6, priceToBorrow);
            pstmt.setDouble(7, priceToBuy);
            pstmt.setString(8, publishDate);
            pstmt.setInt(9, numOfCopies);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                alert(event, "Book Added Successfully", rs.getString("message"));
            }else{
                alert(event, "Error", rs.getString("Error adding book!"));
            }

        } catch (SQLException e) {
            alert(event, "SQL Error",e.getMessage());
        }
    }

    public void RemoveABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);

        String sql = "EXEC RemoveBook ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
                alert(event, "Book Removed", rs.getString("Message"));
            else
                alert(event, "Book Removed","Book Not Found.");

        } catch (SQLException e) {
            alert(event, "Book Removed",e.getMessage());
        }
    }

    public void EditABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);

        String whatToEdit = inputDialogue(event, "Edit a Book",
                "What do you want to edit in the book? \n1)Book Name \n2)Author Name \n3)Price \n4)Borrow Price");

        try {
            if (whatToEdit.isEmpty() || !(Integer.parseInt(whatToEdit) > 0 &&  Integer.parseInt(whatToEdit) < 5)) {
                throw new InputException("Invalid input");
            }

        } catch (Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        int choice = Integer.parseInt(whatToEdit);


        String newValue = inputDialogue(event, "Edit a Book", "Enter the new value!");

        try {
            if (newValue.isEmpty()) {
                throw new InputException("Invalid input");
            }

        } catch (Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC EditBookInfo ?, ?, ?")) {
            stmt.setString(1, bookName);
            stmt.setInt(2, choice);
            stmt.setString(3, newValue);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alert(event, "Book Edited Successfully", rs.getString("Message"));
            } else {
                alert(event, "Book Edited Successfully", "No response from database.");
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public void searchForAMember(ActionEvent event, String searchKeyword) throws IOException {
        String sql = "EXEC SearchForMember ?";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchKeyword);
            ResultSet rs = pstmt.executeQuery();
            boolean hasResults = false;
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                if (metaData.getColumnCount() == 1 && metaData.getColumnLabel(1).equalsIgnoreCase("message")) {
                    System.out.println(rs.getString("message"));
                    return;
                }
                hasResults = true;

                alert(event, "Member Details", "User ID: " + rs.getInt("UserID") +
                        "\nName: " + rs.getString("FirstName") + " " + rs.getString("LastName") +
                        "\nEmail: " + rs.getString("Email") +
                        "\nPhone: " + rs.getString("Phone"));
            }

            if (!hasResults) {
                alert(event, "Error","No Memebers found.");
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public void howToSearchForAMember(ActionEvent event) throws IOException {
        TextInputDialog howToSearch = new TextInputDialog();

        //Visual customizations for the inputDialogue
        Stage dialogStage = (Stage) howToSearch.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        howToSearch.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        howToSearch.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        //To store the value of the string coming from the inputDialogue
        Optional<String> howToSearchOptional = null;

        try {
            howToSearch.setTitle("\"Member search\"");
            howToSearch.setHeaderText(null);
            howToSearch.setContentText("How do you want to search for the member? \n1)By ID \n2)By First Name \n3)By Last Name \n4)By Number \n5)By Email");
            howToSearchOptional = howToSearch.showAndWait();

            if (howToSearchOptional.isEmpty()) {
                throw new InputException("Empty inputs");
            }

        } catch (Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        String howToSearchString = howToSearchOptional.get();
        int choice = Integer.parseInt(howToSearchString);

        switch (choice) {
            case 1:
                int id = inputMemberID(event);
                searchForAMember(event, String.valueOf(id));
                break;
            case 2:
                String firstName = inputMemberName(event);
                searchForAMember(event, firstName);
                break;
            case 3:
                String lastName = inputMemberName(event);
                searchForAMember(event, lastName);
                break;
            case 4:
                String number = inputMemberNumber(event);
                searchForAMember(event, number);
                break;
            case 5:
                String email = inputMemberEmail(event);
                searchForAMember(event, email);
                break;
        }
    }

    public void RemoveAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC RemoveMember ?")) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alert(event,"SQL Error", rs.getString("Message"));
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public void ViewAllMembers(ActionEvent event) throws IOException {
        String query = "SELECT * FROM Members";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userID = rs.getInt("UserID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                Date registrationDate = rs.getDate("RegistrationDate");

                String content = "UserID: " + userID + "\nName: " + firstName + " " + lastName +
                        "\nEmail: " + email + "\nPhone: " + phone +
                        "\nAddress: " + address + "\nRegistered On: " + registrationDate;

                alert(event, "Member Details",content);
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public static void ListOverdueBooks(ActionEvent event) {
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC ListOverdueBooks")) {
            ResultSet rs = stmt.executeQuery();
            boolean exists = false;

            while (rs.next()) {
                exists = true;
                alert(event, "Book","Overdue Book: " + rs.getString("BookID") + " Due Date: " + rs.getDate("DueDate"));
            }

            if (!exists){
                alert(event, "Book","There is no any overdue books");
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public void addNewAdmin(ActionEvent event) throws IOException {
        String firstName = inputMemberName(event);
        String lastName = inputMemberName(event);
        String email = inputMemberEmail(event);
        String phone = inputMemberNumber(event);
        String address = inputMemberAddress(event);
        String password = inputMemberPassword(event);

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement("EXEC AddNewAdmins ?, ?, ?, ?, ?, ?")) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                alert(event, "Admin Added Successfully","message");
            }

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }



//Some input utility functions
    public String inputPublishDate(ActionEvent event) throws IOException {
        String PublishDate = inputDialogue(event, "Input book publish date", "Please enter the publish date of the book in the form of \"YYYY-MM-DD\":");

        try {
            if(PublishDate.isEmpty())
                throw new InputException("Empty inputs");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }
        return PublishDate;
    }

    public double inputPriceToBuy(ActionEvent event) throws IOException {
        String priceToBuyString = inputDialogue(event, "Price to buy", "Please enter the price of the that book:");

        try {
            if(priceToBuyString.isEmpty() || Integer.parseInt(priceToBuyString)<0)
                throw new InputException("Empty inputs");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return Double.parseDouble(priceToBuyString);
    }

    public int inputNumOfCopies(ActionEvent event) throws IOException {
        String numOfCopiesString = inputDialogue(event, "Number of Copies", "Please enter the Number of Copies of the that book:");

        try {
            if(numOfCopiesString.isEmpty() || Integer.parseInt(numOfCopiesString)<0)
                throw new InputException("Empty inputs");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return Integer.parseInt(numOfCopiesString);
    }

    public int inputMemberID(ActionEvent event) throws IOException{
        String stringMemberID = inputDialogue(event, "Input member id", "Please enter the id of the member:");

        try {
            if(stringMemberID.isEmpty() || Integer.parseInt(stringMemberID)<0)
                throw new InputException("Empty inputs");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return Integer.parseInt(stringMemberID);
    }

    public String inputMemberName(ActionEvent event) throws IOException{
        String memberName = inputDialogue(event, "Input member name", "Please enter the name of the member:");

        try {
            if(memberName.isEmpty())
                throw new InputException("Empty inputs");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }
        return memberName;
    }

    public String inputMemberNumber(ActionEvent event) throws IOException {
        String memberPhone = inputDialogue(event, "Input member number", "Please enter the phone number of the member:");

        try {
            if(!isValidNumber(memberPhone))
                throw new InputException("Invalid input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return memberPhone;
    }

    public String inputMemberEmail(ActionEvent event) throws IOException{
        String memberEmail = inputDialogue(event, "Input member email", "Please enter the email of the member:");

        try {
            if(!isValidEmail(memberEmail))
                throw new InputException("Invalid input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return memberEmail;
    }

    public String inputMemberAddress(ActionEvent event) throws IOException {
        String memberAddress = inputDialogue(event, "Input member address", "Please enter the address of the member:");

        try {
            if (memberAddress.isEmpty())
                throw new InputException("Empty inputs");

        } catch (Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return memberAddress;
    }

    public String inputMemberPassword(ActionEvent event) throws IOException{
        String memberPassword = inputDialogue(event, "Input member password", "Please enter the password of the member:");

        try {
            if(isValidPassword(memberPassword))
                throw new InputException("Invalid input");

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return memberPassword;
    }


    public void LogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Library Management System");
    }

    public void Exit(ActionEvent event) {
        System.exit(0);
    }

}
