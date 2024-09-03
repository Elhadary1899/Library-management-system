package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;
import org.example.librarymanagementsystemgui.MemberClasses.Members;

import java.io.IOException;

public class MemberSignInController extends SystemDataBase {
    @FXML
    public TextField idField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField emailField;
    @FXML
    public Button signIn;
    @FXML
    public Button back;

    public void signIn(ActionEvent event) throws IOException{
        //looping on the members to check if the data is right
        boolean isMember = false;
        for(Members member : members){
            if(member.getMemberId()==Integer.parseInt(idField.getText()) && member.getName().equals(nameField.getText()) && member.getPhoneNumber().equals(phoneField.getText()) && member.getEmail().equals(emailField.getText())){
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

            //to add the bg blur
            Pane glassPane = (Pane) root.lookup(".glass");
            GaussianBlur blur = new GaussianBlur(8);
            glassPane.setEffect(blur);

            //changing the icon of the stage
            Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
            stage.getIcons().add(icon);

            stage.setTitle("Library Management System");
            Scene scene = new Scene(root,900,700);
            stage.setScene(scene);
            stage.show();
        }else{
            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            //to add the bg blur
            Pane glassPane = (Pane) root.lookup(".glass");
            GaussianBlur blur = new GaussianBlur(8);
            glassPane.setEffect(blur);

            //changing the icon of the stage
            Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
            stage.getIcons().add(icon);

            stage.setTitle("Library Management System");
            Scene scene = new Scene(root,900,700);
            stage.setScene(scene);
            stage.show();

        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        //to add the bg blur
        Pane glassPane = (Pane) root.lookup(".glass");
        GaussianBlur blur = new GaussianBlur(8);
        glassPane.setEffect(blur);

        //changing the icon of the stage
        Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Library Management System");
        Scene scene = new Scene(root,900,700);
        stage.setScene(scene);
        stage.show();
    }

}
