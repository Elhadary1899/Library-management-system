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
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;

public class MemberSignInController extends SystemDataBase {
    @FXML
    public TextField emailField;
    @FXML
    public TextField passwordField;
    @FXML
    public Button signIn;
    @FXML
    public Button back;

    public void signIn(ActionEvent event) throws IOException{
        //looping on the members to check if the member truly exists
        boolean isMember = false;
        for(Member member : members){
            if(member.getEmail().equals(emailField.getText()) && member.getPassword().equals(passwordField.getText())){
                //passing member's info to the system to be able to access his functionalities
                member.receiveMember(event,member);
                isMember = true;
                break;
            }
        }

        if(!isMember){
            String title = "Invalid data";
            String content = "Member doesn't exist!";
            alert(event,title,content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui//org/example/librarymanagementsystemgui/memberSignIn.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            setScene(event, root, "Library Management System");
        }else{
            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            setScene(event, root, "Library Management System");
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Library Management System");
    }

}
