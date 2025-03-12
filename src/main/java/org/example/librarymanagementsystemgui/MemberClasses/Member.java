package org.example.librarymanagementsystemgui.MemberClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.Books.Book;
import org.example.librarymanagementsystemgui.Books.BookCopy;
import org.example.librarymanagementsystemgui.Books.BorrowedBook;
import org.example.librarymanagementsystemgui.Books.PurchasedBook;
import org.example.librarymanagementsystemgui.DatabaseClasses.User;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class Member extends User {
    @FXML
    public Button ViewAllAvailableBooks;
    @FXML
    public Button SearchForBook;
    @FXML
    public Button PlaceAnOrder;
    @FXML
    public Button BorrowABook;
    @FXML
    public Button ReturnABook;
    @FXML
    public Button CheckForBorrowStatus;
    @FXML
    public Button ViewYourBuyingHistory;
    @FXML
    public Button ViewYourBorrowHistory;
    @FXML
    public Button CalculateYourFine;
    @FXML
    public Button PayYourFine;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

    private int memberId;
    private ArrayList<BorrowedBook> borrowedBooks;
    private ArrayList<BorrowedBook> borrowHistory;
    private ArrayList<PurchasedBook> buyingHistory;
    private double fineAmount;

    protected static Member memberToPass;

    public Member(){
        this.setMemberId(memberCounter);
        this.setFineAmount(0);
        borrowedBooks = new ArrayList<>();
        borrowHistory = new ArrayList<>();
        buyingHistory = new ArrayList<>();
    }

    public Member(String firstName, String lastName, String phoneNumber, String email, String password) {
        super(firstName, lastName, phoneNumber, email, password);
        incrementID();
        this.setMemberId(memberCounter);
        this.setFineAmount(0);
        borrowedBooks = new ArrayList<>();
        borrowHistory = new ArrayList<>();
        buyingHistory = new ArrayList<>();
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

    //method that indicates which specific member has signed in
    public void receiveMember(ActionEvent event, Member member){
        memberToPass = member;
    }

    //method that selects other method based on the button pressed
    public void passMemberToMethods(ActionEvent event) throws IOException {
        if(event.getSource()==ViewAllAvailableBooks){
            memberToPass.ViewAllAvailableBooks(event);
        } else if(event.getSource()==SearchForBook){
            memberToPass.SearchForBook(event);
        } else if(event.getSource()==PlaceAnOrder){
            memberToPass.PlaceAnOrder(event);
        } else if(event.getSource()==BorrowABook){
            memberToPass.BorrowABook(event);
        } else if(event.getSource()==ReturnABook){
            memberToPass.ReturnABook(event);
        } else if(event.getSource()==CheckForBorrowStatus){
            memberToPass.CheckForBorrowStatus(event);
        } else if(event.getSource()==ViewYourBuyingHistory){
            memberToPass.viewYourBuyingHistory(event);
        } else if(event.getSource()==ViewYourBorrowHistory){
            memberToPass.ViewYourBorrowHistory(event);
        } else if(event.getSource()==CalculateYourFine){
            memberToPass.CalculateYourFine(event);
        } else if(event.getSource()==PayYourFine){
            memberToPass.PayYourFine(event);
        }
    }

    public void PlaceAnOrder(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Book book = getBookFromDBbyName(bookName);

        if(book==null){
            alert(event,"Books isn't found","Books isn't found");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Member access");
        }else{
            BookCopy toBeSold = book.isAvailableToBuy();
            if(toBeSold != null){
                alert(event,
                        "successful order",
                        "Order to buy the book: \"" + book.getName() + "\" is confirmed for: " + book.getPriceToBuy() + "$");

                toBeSold.setSold(true);
                toBeSold.setAvailable(false);

                PurchasedBook pb = new PurchasedBook(toBeSold, LocalDate.now(), toBeSold.getParent().getPriceToBuy());
                buyingHistory.add(pb);
            }else{
                alert(event,"Out of stock","Book is out of buying stock");
            }
        }
    }

    public void BorrowABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Book book = getBookFromDBbyName(bookName);
        if(book==null){
            alert(event,"Books isn't found", "Books isn't found");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Member access");

        }else{
            BookCopy toBeBorrowed = book.isAvailableToBorrow();
            if(toBeBorrowed != null){
                BorrowedBook borrowedBook = new BorrowedBook(toBeBorrowed);
                borrowedBook.getBook().setAvailable(false);
                borrowedBooks.add(borrowedBook);
                borrowHistory.add(borrowedBook);
                borrowedBook.setBorrowDate(LocalDate.now());
                borrowedBook.setDueDate(borrowedBook.getBorrowDate().plusDays(7));

                alert(event,
                        "successful order",
                        "\nOrder to borrow the book: \"" + book.getName() + "\" is confirmed for: " + book.getPriceToBorrow() + "$ on: " + borrowedBook.getBorrowDate() +
                                "\nYou have 7 days to return the book or you will be fined with " + borrowedBook.getFineAmount() + "$"
                );

            }else{
                alert(event,"Out of stock","Book is out of borrowing stock");
            }
        }
    }

    public void ReturnABook(ActionEvent event) throws IOException {
        String bookName = inputBookName(event);
        Book bookToReturn = getBookFromDBbyName(bookName);

        if(bookToReturn == null){
            alert(event,"Books isn't found","Books isn't found");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/memberAccess.fxml"));
            setScene(event, root, "Member access");
        }else{
            boolean bookFound = false;
            for(BorrowedBook borrowedBook : borrowedBooks){
                if(borrowedBook.getBook().getParent().getName().equals(bookToReturn.getName())){
                    bookFound = true;

                    borrowedBook.setReturnDate(BorrowedBook.getCurrentDate());
                    borrowedBook.getBook().setAvailable(true);

                    if(borrowedBook.calculateFineAmount()==0){
                        alert(event,"Books returned",
                                "Thanks for returning the book on time, looking forward to seeing you again :)"
                        );
                    }else{
                        alert(event,"Books returned",
                                "You have returned the book after the due date, a delay fine of" + borrowedBook.calculateFineAmount() + "$ is added to your account"
                        );
                        this.setFineAmount(this.getFineAmount() + borrowedBook.calculateFineAmount());
                    }
                    borrowedBooks.remove(borrowedBook);
                    break;
                }
            }
            if(!bookFound){
                alert(event,"Books not borrowed","You didn't borrow a book of that name, please try again!");
            }
        }
    }

    public void CheckForBorrowStatus(ActionEvent event) {
        if(borrowedBooks.isEmpty()){
            alert(event,"No borrows lately","You didn't borrow any books lately");
        }else{
            int bookNumber = 1;
            for(BorrowedBook borrowedBook : borrowedBooks){
                System.out.println(borrowedBook.getBook().getParent().getName());
                alert(event,
                        "Borrows status",
                        bookNumber + ") Book name: " + borrowedBook.getBook().getParent().getName() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                                + "\nDue date: " + borrowedBook.getDueDate() + "\nCurrent date: " + BorrowedBook.getCurrentDate() + "\nDays Left: " + borrowedBook.calculateDaysLeft()
                                + "\nBook's fine amount: " + borrowedBook.getFineAmount() + "$\n"
                );
                bookNumber++;
            }
        }
    }

    public void viewYourBuyingHistory(ActionEvent event){
        if(buyingHistory.isEmpty()){
            alert(event,"No buying history",
                    "You didn't buy any book before!"
            );
        }else{
            int bookNumber = 1;
            for(PurchasedBook pk : buyingHistory){
                String content = bookNumber + ") Book name: " + pk.getBook().getParent().getName() + "\nPurchase date: " + pk.getPurchaseDate()
                        + "\nPrice: " + pk.getPurchasedPrice();

                alert(event,"Borrows status",content);
                bookNumber++;
            }
        }
    }

    public void ViewYourBorrowHistory(ActionEvent event) {
        if(borrowHistory.isEmpty()){
            alert(event,"No borrow history",
                    "You didn't borrow any books before!"
            );
        }else{
            int bookNumber = 1;
            for(BorrowedBook borrowedBook : borrowHistory){
                String content = "";
                if(borrowedBook.getReturnDate()==null){
                    content = bookNumber + ") Book name: " + borrowedBook.getBook().getParent().getName() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                            + "\nDue date: " + borrowedBook.getDueDate() + "\nReturning date: still not returned";
                }else {
                    content = bookNumber + ") Book name: " + borrowedBook.getBook().getParent().getName() + "\nBorrow date: " + borrowedBook.getBorrowDate()
                            + "\nDue date: " + borrowedBook.getDueDate() + "\nReturning date: " + borrowedBook.getReturnDate();
                }
                alert(event,"Borrows status",content);
                bookNumber++;
            }
        }
    }

    public void CalculateYourFine(ActionEvent event) {
        alert(event,"Fine Amount", "Your total fine amount is : " + this.getFineAmount() + "$");
        System.out.println(this.getName());
    }

    public void PayYourFine(ActionEvent event) {
        if(this.getFineAmount()!=0) {
            this.setFineAmount(0);
            alert(event,
                    "Fine paid",
                    "Fine is paid successfully :) your fine amount now is: " + this.getFineAmount() + "$"
            );
        }else{
            alert(event,"No fine","You have no fine to pay :)");
        }
    }

    public void LogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/start.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        setScene(event, root, "Library Management System");
    }

    public void Exit(ActionEvent event) {
        System.exit(0);
    }
}
