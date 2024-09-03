package org.example.librarymanagementsystemgui.DatabaseClasses;

import java.time.LocalDate;
import java.time.Period;

public class BorrowedBook {
    private Books book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private static LocalDate currentDate;
    private double fineAmountForThisBook;

    BorrowedBook(){
        setCurrentDate(LocalDate.now());
    }
    public BorrowedBook(Books book){
        setCurrentDate(LocalDate.now());
        this.setBook(book);
        this.setFineAmount();
    }

    //Setters and getters
    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public static LocalDate getCurrentDate() {
        return currentDate;
    }

    public static void setCurrentDate(LocalDate currentDate) {
        BorrowedBook.currentDate = currentDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFineAmount() {
        return fineAmountForThisBook;
    }

    public void setFineAmount() {
        fineAmountForThisBook = this.book.getPriceToBorrow()*0.5;
    }



    //Other methods
    public long calculateDaysLeft(){
        if(this.dueDate.isAfter(getCurrentDate())) {
            Period period = Period.between(getCurrentDate(), this.getDueDate());
            return period.getDays();
        }else {
            return 0;
        }
    }

    private boolean isOverDue(){
        return this.getReturnDate().isAfter(this.getDueDate());
    }

    public double calculateFineAmount(){
        if(isOverDue()){
            return this.getFineAmount();
        }
        return 0;
    }

}
