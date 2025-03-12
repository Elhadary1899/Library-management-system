package org.example.librarymanagementsystemgui.LaunchClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;

import java.io.IOException;

public class StartController extends SystemDataBase {
    @FXML
    public Button signInMember;
    @FXML
    public Button signUpMember;
    @FXML
    public Button signInAdmin;


    public void memberSignIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignIn.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Member sign in");
    }


    public void memberSignUp(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Member sign up");
    }


    public void adminSignIn(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminSignIn.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Admin sign in");
    }


}
