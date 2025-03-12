package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.LaunchClasses.StartController;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;

public class mebmerSignUpController extends StartController {
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button signUp;
    @FXML
    public Button back;


    public void signUp(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystemgui/memberConfirmSignUpDetails.fxml"));
        Parent root = loader.load();
        memberConfirmSignUpDetailsController controller = loader.getController();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        if(SystemDataBase.isValidName(firstNameField.getText()) && SystemDataBase.isValidName(lastNameField.getText()) && SystemDataBase.isValidNumber(phoneField.getText()) && SystemDataBase.isValidEmail(emailField.getText()) && SystemDataBase.isValidPassword(passwordField.getText())) {
            controller.name.setText(firstNameField.getText() + ' ' + lastNameField.getText());
            controller.firstName = firstNameField.getText();
            controller.lastName = lastNameField.getText();
            controller.phone.setText(phoneField.getText());
            controller.email.setText(emailField.getText());
            controller.password.setText(passwordField.getText());

            //setting the scene for sign in
            setScene(event, root, "Confirm sign up");
        }else{
            String title = "Data guide";
            String content = "Invalid data\nMake sure that the password is at least 8 characters and \ncontains at least 2 digits.";
            alert(event, title, content);
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();


        //setting the scene for go back to the start screen
        setScene(event, root, "Library Management System");
    }
}
