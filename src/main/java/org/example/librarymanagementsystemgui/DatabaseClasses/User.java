package org.example.librarymanagementsystemgui.DatabaseClasses;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import org.example.librarymanagementsystemgui.Exceptions.InputException;

import java.io.IOException;
import java.util.Optional;

public class User extends SystemDataBase{
    private String name;
    private String phoneNumber;
    private String email;

    public User(){

    }

    public User(String name, String phoneNumber, String email){
        this.setName(name);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
    }


    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        if(!name.isEmpty()) {
            this.name = name;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if(!phoneNumber.isEmpty()) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void setEmail(String email) {
        if(!email.isEmpty()) {
            this.email = email;
        }
    }

    public void ViewAllAvailableBooks(ActionEvent event) {
        if(books.isEmpty()){
            String title = "No Books";
            String content = "There are no available books!";
            alert(event, title, content);
        }else {
            int counter = 1;
            for (Books book : books) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book" + counter);
                alert.setHeaderText(null);
                alert.setContentText(counter + ")\nTitle: " + book.getTitle() + "\nAuthor name: " + book.getAuthorName() + "\nAvailable to buy: " +
                        book.getNumAvailableToBuy() + "\nAvailable to borrow: " + book.getNumAvailableToBorrow() + "\nPrice to buy: " +
                        book.getPriceToBuy() + "\nPrice to borrow: " + book.getPriceToBorrow());
                alert.showAndWait();
                counter++;
            }
        }
    }

    public void SearchForBookByTitle(ActionEvent event) throws IOException{
        String bookName = inputBookName(event);

        // Check if the book is present
        Books book = isBookInDB(bookName);

        if(book==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No book found");
            alert.setHeaderText(null);
            alert.setContentText("There's no any book with that name!");
            alert.showAndWait();
        }else{
            book.printBookDetails(1);
        }
    }

    public void SearchForBookByAuthorName(ActionEvent event) throws IOException{
        String authorName = inputAuthorName(event);

        boolean bookFound = false;
        int counter = 1;
        for(Books book : books){
            if(book.getAuthorName().equals(authorName)){
                book.printBookDetails(counter);
                counter++;
                bookFound = true;
            }
        }
        if(!bookFound){
            String title =  "No book found";
            String content = "There's no any book with that name!";
            alert(event,title,content);
        }
    }

    public String inputBookName(ActionEvent event) throws IOException {
        // Create a bookName dialogue
        TextInputDialog bookNameDialogue = new TextInputDialog();
        Optional<String> bookName = null;
        try {
            // setting a bookName dialogue
            bookNameDialogue.setTitle("Input book name");
            bookNameDialogue.setHeaderText("Enter the name of the book:");
            bookNameDialogue.setContentText("Please enter the name of the book:");
            bookName = bookNameDialogue.showAndWait();

            if(bookName.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        return bookName.get();
    }

    public String inputAuthorName(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the author name
        TextInputDialog dialog = new TextInputDialog();
        Optional<String> authorName = null;
        try {
            dialog.setTitle("Input author's name");
            dialog.setHeaderText("Enter the name of the author:");
            dialog.setContentText("Please enter the name of the author:");
            authorName = dialog.showAndWait();
            if(authorName.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        // Capturing the input
        return authorName.get();
    }
}
