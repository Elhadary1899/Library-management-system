package org.example.librarymanagementsystemgui.DatabaseClasses;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.AdminClasses.Admin;
import org.example.librarymanagementsystemgui.Books.Book;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;
import org.example.librarymanagementsystemgui.MemberClasses.Member;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemDataBase{
    protected static ArrayList<Member> members = new ArrayList<Member>();
    protected static ArrayList<Admin> admins = new ArrayList<Admin>();
    protected static ArrayList<Book> books = new ArrayList<Book>();

    public static int memberCounter = 0;


   public SystemDataBase(){}


    public static void incrementID(){
        memberCounter++;
    }

    //checking if the members already exists
    public boolean checkIfMemberExists(String name, String number, String email){
        boolean isMember = false;
        for(Member member : members){
            if(member.getName().equals(name) && member.getPhoneNumber().equals(number) && member.getEmail().equals(email)){
                isMember = true;
                break;
            }
        }
        return isMember;
    }

    public boolean checkIfAdminExists(String name, String number, String email){//looping on the members to check if the data is right
        boolean isAdmin = false;
        for(Admin admin : admins){
            if(admin.getName().equals(name) && admin.getPhoneNumber().equals(number) && admin.getEmail().equals(email)){
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    public Member returnMemberFromDB(String name, String number, String email){
        //looping on the members to check if the data is right
        for(Member member : members){
            if(member.getName().equals(name) && member.getPhoneNumber().equals(number) && member.getEmail().equals(email)){
                return member;
            }
        }
        return null;
    }

    public Book getBookFromDBbyISBN(String ISBN){
        Book Book = null;
        for(Book book : books){
            if(book.getISBN().equals(ISBN)){
                Book = book;
                break;
            }
        }
        return Book;
    }

    public Book getBookFromDBbyName(String name){
        Book Book = null;
        for(Book book : books){
            if(book.getName().equals(name)){
                Book = book;
                break;
            }
        }
        return Book;
    }

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
        alertStage.getIcons().add(new Image(SystemDataBase.class.getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));


        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
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
