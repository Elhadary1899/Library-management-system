package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.Books;
import org.example.librarymanagementsystemgui.DatabaseClasses.BorrowedBook;
import org.example.librarymanagementsystemgui.DatabaseClasses.User;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


public class Members extends User {
    @FXML
    public Button ViewAllAvailableBooks;
    @FXML
    public Button SearchForBookByTitle;
    @FXML
    public Button SearchForBookByAuthorName;
    @FXML
    public Button PlaceAnOrder;
    @FXML
    public Button BorrowABook;
    @FXML
    public Button ReturnABook;
    @FXML
    public Button CheckForBorrowStatus;
    @FXML
    public Button ViewYourBorrowHistory;
    @FXML
    public Button CalculateYourFine;
    @FXML
    public Button PayYourFine;
    @FXML
    public Button ProceedInDays;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

    private int memberId;
    private ArrayList<BorrowedBook> borrowedBooks;
    private ArrayList<BorrowedBook> borrowHistory;
    private double fineAmount;

    protected static Members memberToPass;

    public Members(){
        this.setMemberId(memberCounter);
        this.setFineAmount(0);
        borrowedBooks = new ArrayList<>();
        borrowHistory = new ArrayList<>();
    }

    public Members(String name, String phoneNumber, String email) {
        super(name, phoneNumber, email);
        incrementID();
        this.setMemberId(memberCounter);
        this.setFineAmount(0);
        borrowedBooks = new ArrayList<>();
        borrowHistory = new ArrayList<>();
    }


    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public ArrayList<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void receiveMember(ActionEvent event, Members member){
        memberToPass = member;
    }
    public void passMemberToMethods(ActionEvent event) throws IOException {
        System.out.println(memberToPass);
        if(event.getSource()==ViewAllAvailableBooks){
            memberToPass.ViewAllAvailableBooks(event);
        } else if(event.getSource()==SearchForBookByTitle){
            memberToPass.SearchForBookByTitle(event);
        } else if(event.getSource()==SearchForBookByAuthorName){
            memberToPass.SearchForBookByAuthorName(event);
        } else if(event.getSource()==PlaceAnOrder){
            memberToPass.PlaceAnOrder(event);
        } else if(event.getSource()==BorrowABook){
            memberToPass.BorrowABook(event);
        } else if(event.getSource()==ReturnABook){
            memberToPass.ReturnABook(event);
        } else if(event.getSource()==CheckForBorrowStatus){
            memberToPass.CheckForBorrowStatus(event);
        } else if(event.getSource()==ViewYourBorrowHistory){
            memberToPass.ViewYourBorrowHistory(event);
        } else if(event.getSource()==CalculateYourFine){
            memberToPass.CalculateYourFine(event);
        } else if(event.getSource()==PayYourFine){
            memberToPass.PayYourFine(event);
        } else if(event.getSource()==ProceedInDays){
            memberToPass.ProceedInDays(event);
        }
    }


    public void PlaceAnOrder(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Books book = isBookInDB(bookName);
        if(book==null){
            String title = "Books isn't found";
            String content = "Books isn't found";
            alert(event,title,content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Admin access");
        }else{
            if(book.isAvailableToBuy()){
                String title = "successful order";
                String content = "Order to buy the book: \"" + book.getTitle() + "\" is confirmed for: " + book.getPriceToBuy() + "$";
                alert(event,title,content);

                book.setNumAvailableToBuy(book.getNumAvailableToBuy()-1);
            }else{
                String title = "Out of stock";
                String content = "Book is out of buying stock";
                alert(event,title,content);
            }
        }
    }

    public void BorrowABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Books book = isBookInDB(bookName);
        if(book==null){
            String title = "Books isn't found";
            String content = "Books isn't found";
            alert(event,title,content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Admin access");

        }else{
            if(book.isAvailableToBorrow()){
                BorrowedBook borrowedBook = new BorrowedBook(book);
                book.setNumAvailableToBorrow(book.getNumAvailableToBorrow()-1);
                borrowedBooks.add(borrowedBook);
                borrowHistory.add(borrowedBook);
                borrowedBook.setBorrowDate(LocalDate.now());
                borrowedBook.setDueDate(borrowedBook.getBorrowDate().plusDays(7));

                String title = "successful order";
                String content = "\nOrder to borrow the book: \"" + book.getTitle() + "\" is confirmed for: " + book.getPriceToBorrow() + "$ on: " + borrowedBook.getBorrowDate() +
                        "\nYou have 7 days to return the book or you will be fined with " + borrowedBook.getFineAmount() + "$";
                alert(event,title,content);
            }else{
                String title = "Out of stock";
                String content = "Book is out of buying stock";
                alert(event,title,content);
            }
        }
    }

    public void ReturnABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Books bookToReturn = isBookInDB(bookName);

        if(bookToReturn == null){
            String title = "Books isn't found";
            String content = "Books isn't found";
            alert(event,title,content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Admin access");
        }else{
            boolean bookFound = false;
            for(BorrowedBook borrowedBook : borrowedBooks){
                if(borrowedBook.getBook().getTitle().equals(bookToReturn.getTitle())){
                    bookFound = true;
                    borrowedBook.setReturnDate(BorrowedBook.getCurrentDate());
                    borrowedBook.getBook().setNumAvailableToBorrow(bookToReturn.getNumAvailableToBorrow()+1);
                    if(borrowedBook.calculateFineAmount()==0){
                        String title = "Books returned";
                        String content = "Thanks for returning the book on time, looking forward to seeing you again :)";
                        alert(event,title,content);
                    }else{
                        String title = "Books returned";
                        String content = "You have returned the book after the due date, a delay fine of" + borrowedBook.calculateFineAmount() + "$ is added to your account";
                        alert(event,title,content);
                        this.setFineAmount(this.getFineAmount() + borrowedBook.calculateFineAmount());
                    }
                    borrowedBooks.remove(borrowedBook);
                    break;
                }
            }
            if(!bookFound){
                String title = "Books not borrowed";
                String content = "You didn't borrow a book of that name, please try again!";
                alert(event,title,content);
            }
        }
    }

    public void CheckForBorrowStatus(ActionEvent event) {
        if(borrowedBooks.isEmpty()){
            String title = "No borrows lately";
            String content = "You didn't borrow any books lately";
            alert(event,title,content);
        }else{
            int bookNumber = 1;
            for(BorrowedBook borrowedBook : borrowedBooks){
                String title = "Borrows status";
                String content = bookNumber + ") Book name: " + borrowedBook.getBook().getTitle() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                        + "\nDue date: " + borrowedBook.getDueDate() + "\nCurrent date: " + BorrowedBook.getCurrentDate() + "\nDays Left: " + borrowedBook.calculateDaysLeft()
                        + "\nBook's fine amount: " + borrowedBook.getFineAmount() + "$\n";
                alert(event,title,content);
                bookNumber++;
            }
        }
    }

    public void ViewYourBorrowHistory(ActionEvent event) {
        if(borrowHistory.isEmpty()){
            String title = "No borrow history";
            String content = "You didn't borrow any books before!";
            alert(event,title,content);
        }else{
            int bookNumber = 1;
            for(BorrowedBook borrowedBook : borrowHistory){
                String title = "Borrows status";
                String content = "";
                if(borrowedBook.getReturnDate()==null){
                    content = bookNumber + ") Book name: " + borrowedBook.getBook().getTitle() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                            + "\nDue date: " + borrowedBook.getDueDate() + "\nReturning date: still not returned";
                }else {
                    content = bookNumber + ") Book name: " + borrowedBook.getBook().getTitle() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                            + "\nDue date: " + borrowedBook.getDueDate() + "\nReturning date: " + borrowedBook.getReturnDate();
                }
                alert(event,title,content);
                bookNumber++;
            }
        }
    }

    public void CalculateYourFine(ActionEvent event) {
        String title = "Fine Amount";
        String content = "Your total fine amount is : " + this.getFineAmount() + "$";
        alert(event,title,content);
        System.out.println(this.getName());
    }

    public void PayYourFine(ActionEvent event) {
        if(this.getFineAmount()!=0) {
            this.setFineAmount(0);
            String title = "Fine paid";
            String content = "Fine is paid successfully :) your fine amount now is: " + this.getFineAmount() + "$";
            alert(event,title,content);
        }else{
            String title = "No fine";
            String content = "You have no fine to pay :)";
            alert(event,title,content);
        }
    }

    public void ProceedInDays(ActionEvent event) throws IOException {
        // Create a TextInputDialog to input the member id
        TextInputDialog daysToProceedDialogue = new TextInputDialog();
        Optional<String> daysToProceedOptional = null;
        try {
            daysToProceedDialogue.setTitle("Input days to proceed");
            daysToProceedDialogue.setHeaderText("How many days do you want to proceed: ");
            daysToProceedDialogue.setContentText("How many days do you want to proceed: ");
            daysToProceedOptional = daysToProceedDialogue.showAndWait();
            if(daysToProceedOptional.isEmpty() || Integer.parseInt(daysToProceedOptional.get())<0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        int daysToProceed = Integer.parseInt(daysToProceedOptional.get());

        BorrowedBook.setCurrentDate(BorrowedBook.getCurrentDate().plusDays(daysToProceed));
    }

    public void LogOut(ActionEvent event) throws IOException {
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

    public void Exit(ActionEvent event) {
        System.exit(0);
    }
}
