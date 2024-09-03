package org.example.librarymanagementsystemgui.DatabaseClasses;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.AdminClasses.Admins;
import org.example.librarymanagementsystemgui.MemberClasses.Members;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemDataBase{
    protected static ArrayList<Members> members = new ArrayList<Members>();
    protected static ArrayList<Admins> admins = new ArrayList<Admins>();
    protected static ArrayList<Books> books = new ArrayList<Books>();

    public static int memberCounter = 0;


   public SystemDataBase(){
    }


    public static void incrementID(){
        memberCounter++;
    }

    public boolean checkIfMemberExists(String name, String number, String email){
        //looping on the members to check if the data is right
        boolean isMember = false;
        for(Members member : members){
            if(member.getName().equals(name) && member.getPhoneNumber().equals(number) && member.getEmail().equals(email)){
                isMember = true;
                break;
            }
        }
        return isMember;
    }

    public boolean checkIfAdminExists(String name, String number, String email){//looping on the members to check if the data is right
        boolean isAdmin = false;
        for(Admins admin : admins){
            if(admin.getName().equals(name) && admin.getPhoneNumber().equals(number) && admin.getEmail().equals(email)){
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    public Members returnMemberFromDB(String name, String number, String email){
        //looping on the members to check if the data is right
        for(Members member : members){
            if(member.getName().equals(name) && member.getPhoneNumber().equals(number) && member.getEmail().equals(email)){
                return member;
            }
        }
        return null;
    }

    public Books isBookInDB(String bookName){
        Books Book = null;
        for(Books book : books){
            if(book.getTitle().equals(bookName)){
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

    public void alert(ActionEvent event, String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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
        stage.setTitle("Admin access");
        Scene scene = new Scene(root,900,700);
        stage.setScene(scene);
        stage.show();
    }

}
