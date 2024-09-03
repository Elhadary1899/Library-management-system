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
import org.example.librarymanagementsystemgui.LaunchClasses.StartController;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;

public class adminSignUpController extends StartController {
    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField emailField;
    @FXML
    public Button signUp;
    @FXML
    public Button back;


    public void signUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystemgui/adminConfirmSignUpDetails.fxml"));
        Parent root = loader.load();
        adminConfirmSignUpDetailsController controller = loader.getController();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        controller.name.setText(nameField.getText());
        controller.phone.setText(phoneField.getText());
        controller.email.setText(emailField.getText());

        if(SystemDataBase.isValidName(nameField.getText()) && SystemDataBase.isValidNumber(phoneField.getText()) && SystemDataBase.isValidEmail(emailField.getText())) {
            controller.name.setText(nameField.getText());
            controller.phone.setText(phoneField.getText());
            controller.email.setText(emailField.getText());

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
        }else{
            String title = "Invalid data";
            String content = "Invalid data, please Sign Up again!";
            alert(event, title, content);
            adminSignUp(event);
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
