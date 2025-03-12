package org.example.librarymanagementsystemgui.Books;

import java.time.LocalDate;

public class PurchasedBook {
    private BookCopy book;
    private LocalDate purchaseDate;
    private double purchasedPrice;

    public PurchasedBook(BookCopy book, LocalDate purchaseDate, double purchasedPrice) {
        this.setBook(book);
        this.setPurchaseDate(purchaseDate);
        this.setPurchasedPrice(purchasedPrice);
    }

    public BookCopy getBook() {
        return book;
    }

    public void setBook(BookCopy book) {
        this.book = book;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(double purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }
}
