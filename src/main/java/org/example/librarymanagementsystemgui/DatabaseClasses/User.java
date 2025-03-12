package org.example.librarymanagementsystemgui.DatabaseClasses;

import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.Books.Book;
import org.example.librarymanagementsystemgui.Exceptions.InputException;

import java.io.IOException;
import java.util.Optional;

public class User extends SystemDataBase{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;

    public User(){

    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password){
        this.setfirstName(firstName);
        this.setlastName(lastName);
        this.setPhoneNumber(phoneNumber);
        this.setEmail(email);
        this.setPassword(password);
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName(){return this.firstName + ' ' + this.lastName;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public void setfirstName(String firstName) {
        if(!firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }

    public void setlastName(String lastName) {
        if(!lastName.isEmpty()) {
            this.lastName = lastName;
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

    public void setPassword(String password){
        if(!password.isEmpty()){
            this.password = password;
        }
    }

    public void ViewAllAvailableBooks(ActionEvent event) {
        if(books.isEmpty()){
            String title = "No Books";
            String content = "There are no available books!";
            alert(event, title, content);
        }else {
            int counter = 1;
            for (Book book : books) {
                alert(event,
                        "Book" + counter,
                        counter + ")\nISBN: " + book.getISBN() + "\nTitle: " + book.getName() + "\nAuthor name: " + book.getAuthorName() + "\nPublisher name: " + book.getPublisherName() +
                                "\nPrice to buy: " + book.getPriceToBuy() + "\nPrice to borrow: " + book.getPriceToBorrow() + "\nNumber of Copies Available: " + book.getNumOfCopies()
                );
                counter++;
            }
        }
    }

    public void SearchForBook(ActionEvent event) throws IOException{
        TextInputDialog howToSearch = new TextInputDialog();
        howToSearch.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) howToSearch.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        howToSearch.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        howToSearch.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> howToSearchOptional = null;

        try {
            howToSearch.setTitle("\"Book search\"");
            howToSearch.setHeaderText(null); // Remove the default header
            howToSearch.setContentText("How do you want to search for the Book? \n1)By Name \n2)By Author Name \n3)By Category");
            howToSearchOptional = howToSearch.showAndWait();
            if(howToSearchOptional.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");
        }

        String howToSearchString = howToSearchOptional.get();
        int choice = Integer.parseInt(howToSearchString);

        switch (choice) {
            case 1:
                String bookName = inputBookName(event);
                Book Book = getBookFromDBbyName(bookName);

                if(Book==null){
                    alert(event, "No book found", "There's no any book with that name!");
                }else{
                    Book.printBookDetails(1);
                }

                break;
            case 2:
                String authorName = inputAuthorName(event);

                boolean bookFound = false;
                int counter = 1;
                for(Book book : books){
                    if(book.getAuthorName().equals(authorName)){
                        book.printBookDetails(counter);
                        counter++;
                        bookFound = true;
                    }
                }
                if(!bookFound){
                    alert(event, "No book found", "There's no any book with that name!");
                }

                break;
            case 3:
                TextInputDialog categoryDialogue = new TextInputDialog();
                Optional<String> category = null;
                try {
                    // setting a bookName dialogue
                    categoryDialogue.setTitle("Input book name");
                    categoryDialogue.setHeaderText("Enter the category of the book:");
                    categoryDialogue.setContentText("Please enter the category of the book:");
                    category = categoryDialogue.showAndWait();

                    if(category.isEmpty()){
                        throw new InputException("Empty inputs");
                    }
                }catch(Exception e){
                    String title = "Invalid input";
                    String content = "Invalid input, please try again!";
                    alert(event, title, content);
                }

                bookFound = false;
                counter = 1;
                for(Book book : books){
                    if(book.getCategory().equals(category.get())){
                        book.printBookDetails(counter);
                        counter++;
                        bookFound = true;
                    }
                }
                if(!bookFound){
                    alert(event, "No book found", "There's no any book with that name!");
                }

                break;
        }
    }

    public String inputBookISBN(ActionEvent event) throws IOException {
        // Create a bookName dialogue
        TextInputDialog bookISBNDialogue = new TextInputDialog();

        bookISBNDialogue.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) bookISBNDialogue.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        bookISBNDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        bookISBNDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> ISBN = null;
        try {
            // setting a bookName dialogue
            bookISBNDialogue.setTitle("Input book ISBN");
            bookISBNDialogue.setHeaderText(null);
            bookISBNDialogue.setContentText("Please enter the ISBN of the book:");
            ISBN = bookISBNDialogue.showAndWait();

            if(ISBN.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        return ISBN.get();
    }

    public String inputBookName(ActionEvent event) throws IOException {
        // Create a bookName dialogue
        TextInputDialog bookNameDialogue = new TextInputDialog();

        bookNameDialogue.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) bookNameDialogue.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        bookNameDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        bookNameDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> bookName = null;
        try {
            // setting a bookName dialogue
            bookNameDialogue.setTitle("Input book name");
            bookNameDialogue.setHeaderText(null);
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

        dialog.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        dialog.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> authorName = null;
        try {
            dialog.setTitle("Input author's name");
            dialog.setHeaderText(null);
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

    public String inputPublisherName(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the author name
        TextInputDialog dialog = new TextInputDialog();

        dialog.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        dialog.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> publisherName = null;
        try {
            dialog.setTitle("Input publisher's name");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the name of the publisher:");
            publisherName = dialog.showAndWait();
            if(publisherName.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        // Capturing the input
        return publisherName.get();
    }

    public String inputCategory(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the author name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setGraphic(null);  // Removes the default icon
        // Set custom icon for the dialog
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        dialog.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
        Optional<String> category = null;

        try {
            dialog.setTitle("Input a category");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the category of the book:");
            category = dialog.showAndWait();
            if(category.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        // Capturing the input
        return category.get();
    }
}
