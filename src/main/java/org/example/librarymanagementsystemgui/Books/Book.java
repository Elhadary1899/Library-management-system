package org.example.librarymanagementsystemgui.Books;

import org.example.librarymanagementsystemgui.DatabaseClasses.SystemDataBase;

import java.util.ArrayList;

public class Book{
    private String ISBN;
    private String name;
    private String authorName;
    private String publisherName;
    String publishDate;
    String category;
    private double priceToBuy;
    private double priceToBorrow;
    private static int numOfCopies = 0;
    private ArrayList<BookCopy> bookCopies = new ArrayList<>();

    Book(){}

    public Book(String ISBN, String name, String authorName, String publisherName, String publishDate, String category, double priceToBuy, int numOfCopies) {
        this.setISBN(ISBN);
        this.setName(name);
        this.setAuthorName(authorName);
        this.setPublisherName(publisherName);
        this.setPublishDate(publishDate);
        this.setCategory(category);
        this.setPriceToBuy(priceToBuy);
        this.setPriceToBorrow(this.getPriceToBuy() * 0.25);
        this.addCopies(numOfCopies);
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPriceToBuy() {
        return priceToBuy;
    }

    public void setPriceToBuy(double priceToBuy) {
        this.priceToBuy = priceToBuy;
    }

    public double getPriceToBorrow() {
        return priceToBorrow;
    }

    public void setPriceToBorrow(double priceToBorrow) {
        this.priceToBorrow = priceToBorrow;
    }

    public int getNumOfCopies() {
        int counter = 0;
        for(BookCopy bc : bookCopies){
            if(bc.isSold() || !bc.isAvailable()) continue;
            counter++;
        }
        return counter;
    }

    public void setNumOfCopies(int numOfCopies) {
        Book.numOfCopies = numOfCopies;
    }

    public void addCopies(int numOfCopies){
        while(numOfCopies > 0) {
            BookCopy bc = new BookCopy(++Book.numOfCopies);
            bc.setParent(this);
            bookCopies.add(bc);
            numOfCopies--;
        }
    }


    public void printBookDetails(int BookCounter){
        SystemDataBase.alert(null,
                "Book details",
                BookCounter + ")\nISBN: " + this.getISBN() + "\nTitle: " + this.getName() + "\nAuthor name: " + this.getAuthorName() + "\nPublisher name: " + this.getPublisherName() +
                        "\nCategory: " + this.getCategory() + "\nPrice to buy: " + this.getPriceToBuy() + "\nPrice to borrow: " + this.getPriceToBorrow() + "\nNumber of Copies Available: " + getNumOfCopies()
        );
    }


    public BookCopy isAvailableToBuy(){
        for(BookCopy bk : bookCopies){
            if(!bk.isSold() && bk.isAvailable()){
                return bk;
            }
        }
        return null;
    }

    public BookCopy isAvailableToBorrow(){
        for(BookCopy bk : bookCopies){
            if(bk.isAvailable() && !bk.isSold()){
                return bk;
            }
        }
        return null;
    }

}

