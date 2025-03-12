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
import org.example.librarymanagementsystemgui.AdminClasses.Admin;
import org.example.librarymanagementsystemgui.Books.Book;
import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;
import org.example.librarymanagementsystemgui.MemberClasses.Member;

import java.io.IOException;

public class WelcomeController extends SystemDataBase {
    @FXML
    public Button startSystem;

    public void startSystem(ActionEvent event) throws IOException {
        setDB();
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Pane glassPane = (Pane) root.lookup(".glass");
        GaussianBlur blur = new GaussianBlur(8);
        glassPane.setEffect(blur);

        Image icon = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png"));
        stage.getIcons().add(icon);

        stage.setTitle("Member sign in");
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    //initializing the DB with some data
    public void setDB(){
        Book book1 = new Book("12345", "To Kill a Mockingbird", "Harper Lee", "Arts publishing", "2014-05-29", "Drama", 50, 5);
        Book book2 = new Book("123456", "Crime and Punishment", "Fyodor Dostoevsky", "Arts publishing", "2014-05-29", "Drama", 50, 5);
        Book book3 = new Book("1234567","Pride and Prejudice", "Jane Austen", "Arts publishing", "2014-05-29", "Drama",   50, 5);

        books.add(book1);
        books.add(book2);
        books.add(book3);

        Member member1 = new Member("Adham", "Elhadary", "01226327795", "adham@gmail.com", "abcdef123");
        Member member2 = new Member("Ahmed", "Amir", "01202155424", "amir@gmail.com","abdcdef1234");
        members.add(member1);
        members.add(member2);

        Admin admin1 = new Admin("Ahmed","Elhadary","01204553222","elhadary@gmail.com", "admin123");
        admins.add(admin1);
    }

}
