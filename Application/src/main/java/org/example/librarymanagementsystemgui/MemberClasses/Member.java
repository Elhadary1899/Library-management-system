package org.example.librarymanagementsystemgui.MemberClasses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.UtilityClasses.User;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;
import java.io.IOException;
import java.sql.*;

public class Member extends User {
    @FXML
    public Button ViewAvailableBooks;
    @FXML
    public Button SearchForBook;
    @FXML
    public Button PlaceAnOrder;
    @FXML
    public Button BorrowABook;
    @FXML
    public Button ReturnABook;
    @FXML
    public Button CheckForBorrowStatus;
    @FXML
    public Button ViewYourBuyingHistory;
    @FXML
    public Button ViewYourBorrowHistory;
    @FXML
    public Button CalculateYourFine;
    @FXML
    public Button PayYourFine;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

//Establishing DB Connection
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Library;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";
    private static int currentMemberID = 0;


//Constructors
    public Member(){
    }

    public Member(String firstName, String lastName, String phoneNumber, String email, String password) {
        super(firstName, lastName, phoneNumber, email, password);
    }


//method that indicates which specific member has signed in
    public static void receiveMember(ActionEvent event, int currentMemberID){
        Member.currentMemberID = currentMemberID;
    }

//method that invokes other methods based on the button pressed
    public void passMemberToMethods(ActionEvent event) throws IOException {
        if(event.getSource()==ViewAvailableBooks){
            ViewAvailableBooks(event);
        } else if(event.getSource()==SearchForBook){
            SearchForBook(event);
        } else if(event.getSource()==PlaceAnOrder){
            PlaceAnOrder(event);
        } else if(event.getSource()==BorrowABook){
            BorrowABook(event);
        } else if(event.getSource()==ReturnABook){
            ReturnABook(event);
        } else if(event.getSource()==CheckForBorrowStatus){
            CheckForBorrowStatus(event);
        } else if(event.getSource()==ViewYourBuyingHistory){
            viewYourBuyingHistory(event);
        } else if(event.getSource()==ViewYourBorrowHistory){
            ViewYourBorrowHistory(event);
        } else if(event.getSource()==CalculateYourFine){
            CalculateYourFine(event);
        } else if(event.getSource()==PayYourFine){
            PayYourFine(event);
        }
    }


//Members' methods
    public void PlaceAnOrder(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);

        String sql = "EXEC BuyBook ?, ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookName);
            pstmt.setInt(2, currentMemberID);
            ResultSet rs = pstmt.executeQuery();

            alert(event, "Status", rs.next() ? rs.getString("Message") : "Book Not Available");
        } catch (SQLException e) {
            alert(event, "Status", e.getMessage());
        }
    }

    public void BorrowABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);

        String sql = "EXEC BorrowBook ?, ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, currentMemberID);
            pstmt.setString(2, bookName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                alert(event,"Borrowed Successfully!", rs.getString("Message"));
            }else{
                alert(event,"Can't proceed!", "Book is not available!");
            }

        } catch (SQLException e) {
            alert(event,"SQL Error!", e.getMessage());
        }
    }

    public void ReturnABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);

        String sql = "EXEC ReturnBook ?, ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentMemberID);
            pstmt.setString(2, bookName);
            ResultSet rs = pstmt.executeQuery();
            alert(event, "Status", rs.next() ? rs.getString("Message") : "No records found");
        } catch (SQLException e) {
            alert(event, "Status", e.getMessage());
        }
    }

    public void CheckForBorrowStatus(ActionEvent event) {
        String sql = "EXEC CurrentBorrowRecords ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, currentMemberID);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasResults = false;

            while (rs.next()) {
                hasResults = true;
                String content = "";
                for (int i = 1; i <= columnCount; i++) {
                    content += metaData.getColumnLabel(i) + ": " + rs.getString(i) + "\n";
                }
                alert(event, "Borrows status", content);
            }

            if (!hasResults) {
                alert(event,"No borrows lately","You didn't borrow any books lately");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error", "Error: " + e.getMessage());
        }
    }

    public void viewYourBuyingHistory(ActionEvent event){
        String sql = "EXEC BuyRecordeHistory ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, currentMemberID);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasResults = false;

            while (rs.next()) {
                String content = "";
                hasResults = true;
                for (int i = 1; i <= columnCount; i++) {
                    content += metaData.getColumnLabel(i) + ": " + rs.getString(i) + "\n";
                }
                alert(event,"Borrow status",content);
            }

            if (!hasResults) {
                alert(event,"No borrow history", "You didn't borrow any books before!");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error", "Error: " + e.getMessage());
        }
    }

    public void ViewYourBorrowHistory(ActionEvent event) {
        String sql = "EXEC BorrowRecordeHistory ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, currentMemberID);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            boolean hasResults = false;

            while (rs.next()) {
                String content = "";
                hasResults = true;
                for (int i = 1; i <= columnCount; i++) {
                    content += metaData.getColumnLabel(i) + ": " + rs.getString(i) + "\n";
                }
                alert(event,"Borrow status",content);
            }

            if (!hasResults) {
                alert(event,"No borrow history", "You didn't borrow any books before!");
            }

        } catch (SQLException e) {
            alert(event, "SQL Error", "Error: " + e.getMessage());
        }
    }

    public void CalculateYourFine(ActionEvent event) {
        String sql = "EXEC CalculateFine ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, currentMemberID);
            ResultSet rs = pstmt.executeQuery();
            alert(event,"Fine Amount", "Your total fine amount is : " + (rs.next()? rs.getInt("TotalFine") : 0) + "$");

        } catch (SQLException e) {
            alert(event,"SQL Error", e.getMessage());
        }
    }

    public void PayYourFine(ActionEvent event) {
        String sql = "EXEC PayFine ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentMemberID);
            ResultSet rs = pstmt.executeQuery();
            alert(event, "Fine paid", rs.next() ? rs.getString("Message") : "No Fine to Pay");
        } catch (SQLException e) {
            alert(event, "Fine paid", e.getMessage());
        }
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
