package org.example.librarymanagementsystemgui.AdminClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.UtilityClasses.SessionManager;
import org.example.librarymanagementsystemgui.UtilityClasses.UtilityMethods;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.sql.*;

public class adminSignInController extends UtilityMethods {
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button signIn;
    @FXML
    public Button back;

    private final static String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=Library;" +
            "user=sa;password=12345678;encrypt=false;trustServerCertificate=true";

    public void signIn(ActionEvent event) throws IOException{
        String sql = "EXEC AdminSignIn ?, ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, emailField.getText());
            pstmt.setString(2, passwordField.getText());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("message").equals("Sign-in Successful")) {
                SessionManager.CurrentuserId = rs.getInt("UserID");
                alert(event, "Welcome", "Welcome " + rs.getString("FirstName") + "!");

                Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                setScene(event, root, "Admin Access");
            } else {
                SessionManager.CurrentuserId = -1;
                alert(event, "Invalid data", "Admin doesn't exist, please Sign In again!");
                Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminSignIn.fxml"));
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                setScene(event, root, "Admin Sign in");
            }
        } catch (SQLException e) {
            alert(event, "SQL Error", e.getMessage());
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        setScene(event, root, "Library Management System");
    }


}
