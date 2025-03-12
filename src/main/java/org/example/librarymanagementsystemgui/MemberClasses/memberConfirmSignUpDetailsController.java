package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.LaunchClasses.StartController;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;


public class memberConfirmSignUpDetailsController extends StartController {
    @FXML
    public Label  name;
    @FXML
    public Label phone;
    @FXML
    public Label email;
    @FXML
    public Label password;
    @FXML
    public Button confirm;
    @FXML
    public Button again;

    public String firstName;
    public String lastName;


    public void confirmSignUp(ActionEvent event) throws IOException {
        if(checkIfMemberExists(name.getText(), phone.getText(), email.getText())){
            String title = "Exists";
            String content = "You are already a member, please sign in instead!";
            alert(event,title,content);
            memberSignIn(event);
        }else {
            SystemDataBase.memberCounter++;

            //Displaying his id
            String title = "Id";
            String content = "Your Id is: " + SystemDataBase.memberCounter;
            alert(event,title,content);

            //storing his info
            Member member = new Member();
            member.setMemberId(SystemDataBase.memberCounter);
            member.setfirstName(firstName);
            member.setlastName(lastName);
            member.setPhoneNumber(phone.getText());
            member.setEmail(email.getText());
            member.setPassword(password.getText());

            //adding him to the DB
            SystemDataBase.members.add(member);

            //signing in again
            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignIn.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            setScene(event, root, "Member sign in");
        }
    }

    public void signUpAgain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Member sign in");
    }

}
