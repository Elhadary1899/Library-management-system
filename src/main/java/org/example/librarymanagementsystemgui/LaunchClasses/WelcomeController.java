package org.example.librarymanagementsystemgui.LaunchClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.AdminClasses.Admins;
import org.example.librarymanagementsystemgui.DatabaseClasses.Books;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.MemberClasses.Members;

import java.io.IOException;

public class WelcomeController extends SystemDataBase {
    @FXML
    public Button startSystem;

    public void startSystem(ActionEvent event) throws IOException {
        setDB();
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
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

    public void setDB(){
        Books book1 = new Books("To Kill a Mockingbird", "Harper Lee", 5, 5, 50, 20);
        Books book2 = new Books("Pride and Prejudice", "Jane Austen", 5, 5, 50, 20);
        Books book3 = new Books("Crime and Punishment", "Fyodor Dostoevsky", 5, 5, 50, 20);
        books.add(book1);
        books.add(book2);
        books.add(book3);

        Members member1 = new Members("Adham", "01226327795", "adham@gmail.com");
        Members member2 = new Members("Amir", "01202155424", "amir@gmail.com");
        members.add(member1);
        members.add(member2);

        Admins admin1 = new Admins("Ahmed","01204553222","ahmed@gmail.com");
        admins.add(admin1);
    }

}
