package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
    public Button confirm;
    @FXML
    public Button again;


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
            Members member = new Members();
            member.setMemberId(SystemDataBase.memberCounter);
            member.setName(name.getText());
            member.setPhoneNumber(phone.getText());
            member.setEmail(email.getText());
            //adding him to the DB
            SystemDataBase.members.add(member);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignIn.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            //to add the bg blur
            Pane glassPane = (Pane) root.lookup(".glass");
            GaussianBlur blur = new GaussianBlur(8);
            glassPane.setEffect(blur);

            //changing the icon of the stage
            Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
            stage.getIcons().add(icon);

            stage.setTitle("Member sign in");
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void signUpAgain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberSignUp.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        //to add the bg blur
        Pane glassPane = (Pane) root.lookup(".glass");
        GaussianBlur blur = new GaussianBlur(8);
        glassPane.setEffect(blur);

        //changing the icon of the stage
        Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Member sign in");
        Scene scene = new Scene(root,900,700);
        stage.setScene(scene);
        stage.show();
    }



}
