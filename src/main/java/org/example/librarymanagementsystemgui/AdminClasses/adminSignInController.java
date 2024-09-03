package org.example.librarymanagementsystemgui.AdminClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;

public class adminSignInController extends SystemDataBase {
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
        //looping on the admins to check if the data is right
        boolean isAdmin = false;
        for(Admins admin : admins){
            if(admin.getName().equals(nameField.getText()) && admin.getPhoneNumber().equals(phoneField.getText()) && admin.getEmail().equals(emailField.getText())){
                admin.receiveAdmin(event,admin);
                isAdmin = true;
                break;
            }
        }
        if(!isAdmin){
            String title = "Invalid data";
            String content = "Admin doesn't exist, please Sign In again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminSignIn.fxml"));
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
            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
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
