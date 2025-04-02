package org.example.librarymanagementsystemgui.MemberClasses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.UtilityClasses.UtilityMethods;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.sql.*;

public class MemberSignInController extends UtilityMethods {
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button signIn;
    @FXML
    public Button back;

//Establishing DB Connection
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Library;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";
    private static int CurrentuserId=0;


    public void signIn(ActionEvent event) throws IOException{
        String sql = "EXEC MemberSignIn ?, ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, emailField.getText());
            pstmt.setString(2, passwordField.getText());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID"); // Retrieve UserID
                CurrentuserId = (userId == -1) ? -1 : userId; // Store or reset UserID

                // Return the message
                if(rs.getString("Message").equals("Successful sign-in")){
                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    setScene(event, root, "Library Management System");
                    Member.receiveMember(event, CurrentuserId);

                }else if(rs.getString("Message").equals("Invalid Email Or Password")){
                    String title = "Invalid data";
                    String content = "Invalid Email Or Password!";
                    alert(event,title,content);

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui//org/example/librarymanagementsystemgui/memberSignIn.fxml"));
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    setScene(event, root, "Library Management System");
                }

            } else {
                CurrentuserId=-1;// Reset if sign-in fails
                String title = "Invalid data";
                String content = "Invalid Email Or Password!";
                alert(event,title,content);
            }

        } catch (SQLException e) {
            alert(event, "SQL Error: ", e.getMessage());
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        setScene(event, root, "Library Management System");
    }
}
