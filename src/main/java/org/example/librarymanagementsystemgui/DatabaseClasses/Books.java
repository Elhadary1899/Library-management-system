package org.example.librarymanagementsystemgui.DatabaseClasses;

import javafx.scene.control.Alert;

public class Books{
    private String title;
    private String authorName;
    private int numAvailableToBuy;
    private int numAvailableToBorrow;
    private double priceToBuy;
    private double priceToBorrow;

    Books(){

    }

    public Books(String title, String authorName, int numAvailableToBuy, int numAvailableToBorrow, double priceToBuy, double priceToBorrow) {
        this.setTitle(title);
        this.setAuthorName(authorName);
        this.setNumAvailableToBuy(numAvailableToBuy);
        this.setNumAvailableToBorrow(numAvailableToBorrow);
        this.setPriceToBuy(priceToBuy);
        this.setPriceToBorrow(priceToBorrow);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getNumAvailableToBuy() {
        return numAvailableToBuy;
    }

    public void setNumAvailableToBuy(int numAvailableToBuy) {
        this.numAvailableToBuy = numAvailableToBuy;
    }

    public int getNumAvailableToBorrow() {
        return numAvailableToBorrow;
    }

    public void setNumAvailableToBorrow(int numAvailableToBorrow) {
        this.numAvailableToBorrow = numAvailableToBorrow;
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



    public void printBookDetails(int BookCounter){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book details");
            alert.setHeaderText(null);
            alert.setContentText(BookCounter + ")\nTitle: " + this.getTitle() + "\nAuthor name: " + this.getAuthorName() + "\nAvailable to buy: " +
                    this.getNumAvailableToBuy() + "\nAvailable to borrow: " + this.getNumAvailableToBorrow() + "\nPrice to buy: " +
                    this.getPriceToBuy() + "\nPrice to borrow: " + this.getPriceToBorrow());
            alert.showAndWait();
    }


    public boolean isAvailableToBuy(){
        return this.getNumAvailableToBuy()>0;
    }


    public boolean isAvailableToBorrow(){
        return this.getNumAvailableToBorrow()>0;
    }

}

