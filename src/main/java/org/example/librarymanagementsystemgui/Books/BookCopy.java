package org.example.librarymanagementsystemgui.Books;

public class BookCopy{
    private Book parent;
    private int copyNumber;
    private boolean available;
    private boolean sold;

    BookCopy(int copyNumber){
        this.copyNumber = copyNumber;
        this.available = true;
        this.sold = false;
    }

    public void setParent(Book book) {this.parent = book;}

    public Book getParent() {return this.parent;}

    public int getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(int copyNumber) {
        this.copyNumber = copyNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }
}
