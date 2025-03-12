package org.example.librarymanagementsystemgui.AdminClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;

public class adminSignInController extends SystemDataBase {
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button signIn;
    @FXML
    public Button back;

    public void signIn(ActionEvent event) throws IOException{
        //looping on the admins to check if the admin exists
        boolean isAdmin = false;
        for(Admin admin : admins){
            if(admin.getEmail().equals(emailField.getText()) && admin.getPassword().equals(passwordField.getText())){
                admin.receiveAdmin(event,admin);
                isAdmin = true;
                break;
            }
        }

        if(!isAdmin){
            alert(event, "Invalid data", "Admin doesn't exist, please Sign In again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminSignIn.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            setScene(event, root, "Admin Sign in");

        }else{
            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            setScene(event, root, "Admin Access");
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Library Management System");
    }


}
