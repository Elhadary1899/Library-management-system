package org.example.librarymanagementsystemgui.MemberClasses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.LaunchClasses.StartController;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.sql.*;

public class memberConfirmSignUpDetailsController extends StartController {
    @FXML
    public Label  name;
    @FXML
    public Label phone;
    @FXML
    public Label email;
    @FXML
    public Label address;
    @FXML
    public Label password;
    @FXML
    public Button confirm;
    @FXML
    public Button again;

    public String firstName;
    public String lastName;

//Establishing DB Connection
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Library;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";
    private static int CurrentuserId=0;


    public void confirmSignUp(ActionEvent event) throws IOException {
        String sql = "EXEC SignUp ?, ?, ?, ?, ?, ?";

        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement prepStatement = connection.prepareStatement(sql)
        )
        {
            prepStatement.setString(1, firstName);
            prepStatement.setString(2, lastName);
            prepStatement.setString(3, email.getText());
            prepStatement.setString(4, phone.getText());
            prepStatement.setString(5, address.getText());
            prepStatement.setString(6, password.getText());
            ResultSet resSet = prepStatement.executeQuery();

            if (resSet.next()) {
                String message = resSet.getString("Message");
                int userId = resSet.getInt("UserID");

                // Store UserID if the user was successfully created
                CurrentuserId = (userId > 0) ? userId : -1;

                if(message.equals("User Already Exists")){
                    String title = "Exists";
                    alert(event,title,message);
                    memberSignIn(event);
                }else if(message.equals("User Registered Successfully")){
                    //Displaying his id
                    String title = "Id";
                    String content = "Your Id is: " + CurrentuserId;
                    alert(event,title,content);

                    //signing in again
                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignIn.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    setScene(event, root, "Member sign in");
                }

                return;
            }

            alert(event,"Error","Error: No response from database.");
            return;

        } catch (SQLException e) {
            alert(event,"SQL Error","SQL Error: " + e.getMessage());
            return;
        }
    }

    public void signUpAgain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Member sign in");
    }
}
