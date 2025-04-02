package org.example.librarymanagementsystemgui.UtilityClasses;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityMethods{

   public UtilityMethods(){}


//Some utility methods
    public static boolean isValidName(String name){
        return !name.isEmpty();
    }

    public static boolean isValidNumber(String phoneNumber){
        String regex = "^01[0125]\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password){
       if(password.length() < 8 ) return false;
       int digitCounts = 0;
       for(char c : password.toCharArray()){
           if(c >= '0' && c <= '9') digitCounts++;
       }
        return digitCounts >= 2;
    }

    public static void alert(ActionEvent event, String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setGraphic(null);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(UtilityMethods.class.getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));


        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }

    public String inputDialogue(ActionEvent event, String title, String contentText) throws IOException {
        TextInputDialog dialog = new TextInputDialog();

        //Visual customizations for the inputDialogue
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        dialog.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        //To store the value of the string coming from the inputDialogue
        Optional<String> OptionalString = null;

        try {
            dialog.setTitle(title);
            dialog.setHeaderText(null);
            dialog.setContentText(contentText);
            OptionalString = dialog.showAndWait();

            if(OptionalString.isEmpty()){
                throw new InputException("Empty inputs");
            }

        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        return OptionalString.get();
    }

    public void setScene(ActionEvent event, Parent root, String title){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        //to add the bg blur
        Pane glassPane = (Pane) root.lookup(".glass");
        GaussianBlur blur = new GaussianBlur(8);
        glassPane.setEffect(blur);

        //changing the icon of the stage
        Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
        stage.getIcons().add(icon);
        stage.setTitle(title);
        Scene scene = new Scene(root,900,700);
        stage.setScene(scene);
        stage.show();
    }
}
