package org.example.librarymanagementsystemgui.AdminClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.Books.Book;
import org.example.librarymanagementsystemgui.DatabaseClasses.User;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;
import org.example.librarymanagementsystemgui.MemberClasses.Member;

import java.io.IOException;
import java.util.Optional;

public class Admin extends User {
    @FXML
    public Button ViewAllAvailableBooks;
    @FXML
    public Button SearchForBook;
    @FXML
    public Button AddABook;
    @FXML
    public Button RemoveABook;
    @FXML
    public Button EditABook;
    @FXML
    public Button SearchForAMember;
    @FXML
    public Button RemoveAMember;
    @FXML
    public Button EditAMember;
    @FXML
    public Button ViewAllMembers;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

    protected static Admin adminToPass;

    public Admin(){}

    public Admin(String firstName, String lastName, String phone, String email, String password){
        super(firstName, lastName, phone, email, password);
    }

    //Receiving a specific admin from login
    public void receiveAdmin(ActionEvent event, Admin admin){
        adminToPass = admin;
    }

    public void passAdminToMethods(ActionEvent event) throws IOException {
        System.out.println(adminToPass);
        if(event.getSource()==ViewAllAvailableBooks){
            adminToPass.ViewAllAvailableBooks(event);
        } else if(event.getSource()==SearchForBook){
            adminToPass.SearchForBook(event);
        } else if(event.getSource()==AddABook){
            adminToPass.AddABook(event);
        } else if(event.getSource()==RemoveABook){
            adminToPass.RemoveABook(event);
        } else if(event.getSource()==SearchForAMember){
            adminToPass.SearchForAMember(event);
        } else if(event.getSource()==RemoveAMember){
            adminToPass.RemoveAMember(event);
        } else if(event.getSource()==EditAMember){
            adminToPass.EditAMember(event);
        } else if(event.getSource()==ViewAllMembers){
            adminToPass.ViewAllMembers(event);
        }
    }


    //Admin methods
    public void AddABook(ActionEvent event) throws IOException {
        Book bookToAdd = inputBookDetails(event);

        // Check if the book is already present
        if(getBookFromDBbyISBN(bookToAdd.getISBN())==null){
            books.add(bookToAdd);
            alert(event, "Book's Added", "Books is added successfully!");
        }else{
            alert(event, "Book exists", "Book already exists");

            // Create a numOfCopies
            TextInputDialog numOfCopiesDialogue = new TextInputDialog();
            // Set custom icon for the dialog
            Stage dialogStage = (Stage) numOfCopiesDialogue.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
            // Apply CSS to the dialog
            numOfCopiesDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
            numOfCopiesDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
            Optional<String> numOfCopiesString = null;

            try {
                numOfCopiesDialogue.setTitle("Number of Copies");
                numOfCopiesDialogue.setHeaderText("Enter the Number of Copies you want to add:");
                numOfCopiesDialogue.setContentText("Please enter the Number of Copies you want to add:");
                numOfCopiesString = numOfCopiesDialogue.showAndWait();

                if(numOfCopiesString.isEmpty() || Integer.parseInt(numOfCopiesString.get()) < 0){
                    throw new InputException("Empty inputs");
                }
            }catch(Exception e){
                alert(event, "Invalid input", "Invalid input, please try again!");

                Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                setScene(event, root, "Admin access");
            }

            int numOfCopies = Integer.parseInt(numOfCopiesString.get());

            if(numOfCopies > 0){
                bookToAdd.addCopies(numOfCopies);
            }
        }
    }

    public void RemoveABook(ActionEvent event) throws IOException {
        String bookNameToRemove = inputBookISBN(event);

        // Check if the book is already present
        Book bookToRemove = getBookFromDBbyISBN(bookNameToRemove);

        if(bookToRemove!=null){
            books.remove(bookToRemove);
            alert(event, "Book is removed", "Books is removed successfully!");
        }else{
            alert(event, "Book not exists", "Book not exists");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
    }

    public void SearchForAMember(ActionEvent event) throws IOException {
        TextInputDialog howToSearch = new TextInputDialog();
        // Set custom icon for the dialog
        Stage dialogStage = (Stage) howToSearch.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        howToSearch.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        howToSearch.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
        Optional<String> howToSearchOptional = null;

        try {
            howToSearch.setTitle("\"Member search\"");
            howToSearch.setHeaderText(null);
            howToSearch.setContentText("How do you want to search for the member? \n1)By ID \n2)By First Name \n3)By Last Name \n4)By Number \n5)By Email");
            howToSearchOptional = howToSearch.showAndWait();
            if(howToSearchOptional.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        String howToSearchString = howToSearchOptional.get();
        int choice = Integer.parseInt(howToSearchString);

        switch (choice) {
            case 1:
                int id = inputMemberID(event);
                //parse it to string when connecting to DB
                boolean memberFoundById = false;
                for(Member member : members){
                    if(member.getMemberId()==id){
                        memberFoundById = true;
                        printMemberDetails(event, member,1);
                        break;
                    }
                }
                if(!memberFoundById){
                    alert(event, "Member not found", "Member isn't found!");

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }
                break;

            case 2:
                String firstName = inputMemberName(event);
                boolean memberFoundByFN = false;
                for(Member member : members){
                    if(member.getFirstName().equals(firstName)){
                        memberFoundByFN = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByFN){
                    alert(event, "Member not found", "Member isn't found!");

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }
                break;


            case 3:
                String lastName = inputMemberName(event);
                boolean memberFoundByLN = false;
                for(Member member : members){
                    if(member.getFirstName().equals(lastName)){
                        memberFoundByFN = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByLN){
                    alert(event, "Member not found", "Member isn't found!");

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }
                break;

            case 4:
                String number = inputMemberNumber(event);
                boolean memberFoundByNum = false;
                for(Member member : members){
                    if(member.getPhoneNumber().equals(number)){
                        memberFoundByNum = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByNum){
                    alert(event, "Member not found", "Member isn't found!");

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }
                break;

            case 5:
                String email = inputMemberEmail(event);
                boolean memberFoundByEmail = false;
                for(Member member : members){
                    if(member.getEmail().equals(email)){
                        memberFoundByEmail = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByEmail){
                    alert(event, "Member not found", "Member isn't found!");

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }
                break;
        }
    }

    public void RemoveAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);
        Member memberToRemove = getMemberById(memberId);

        // Check if the member exists
        if(memberToRemove==null){
            alert(event, "Member not found", "Member isn't found!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else{
            //parse the id before connecting to DB
            members.remove(memberToRemove);
            alert(event, "Member removed", "Member is removed successfully!");
        }
    }

    public void EditAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);
        Member memberToEdit = getMemberById(memberId);

        // Check if the member exists
        if(memberToEdit==null){
            alert(event, "Member not found", "Member isn't found!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else{

            TextInputDialog dialog = new TextInputDialog();
            // Set custom icon for the dialog
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
            // Apply CSS to the dialog
            dialog.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
            dialog.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
            Optional<String> choiceOptional = null;

            try {
                dialog.setTitle("Input your choice");
                dialog.setHeaderText(null);
                dialog.setContentText("What do you want to edit in the member?\n1)First Name \n2)Last Name \n3)Number\n4)Email");
                choiceOptional = dialog.showAndWait();
                if(choiceOptional.isEmpty()){
                    throw new InputException("Empty inputs");
                }
            }catch(Exception e){
                alert(event, "Invalid input", "Invalid input, please try again!");

                Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                setScene(event, root, "Admin access");
            }

            int choice = Integer.parseInt(choiceOptional.get());
            switch(choice){
                case 1:
                    memberToEdit.setfirstName(this.inputMemberName(event));
                    break;

                case 2:
                    memberToEdit.setlastName(this.inputMemberName(event));
                    break;

                case 3:
                    memberToEdit.setPhoneNumber(this.inputMemberNumber(event));
                    break;

                case 4:
                    memberToEdit.setEmail(this.inputMemberEmail(event));
                    break;
            }
            alert(event, "Member edited", "Member is edited successfully!");
        }
    }

    public void ViewAllMembers(ActionEvent event) throws IOException {
        if(members.isEmpty()){
            alert(event, "No Books", "There are no available members!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else {
            int memberCounter = 1;
            for (Member member : members) {
                printMemberDetails(event, member, memberCounter);
                memberCounter++;
            }
        }
    }

    public void printMemberDetails(ActionEvent event, Member member, int MemberCounter) throws IOException {
        if(members.isEmpty() || !checkIfMemberExists(member.getName(), member.getPhoneNumber(), member.getEmail())){
            alert(event, "No member", "There is no such a member!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else
            alert(event,
                    "Member details",
                    MemberCounter + ")\nId: " + member.getMemberId() + "\nName: " + member.getName() + "\nNumber: " + member.getPhoneNumber()
                            + "\nEmail: " + member.getEmail() + "\nFine Amount: " + member.getFineAmount() + "$"
            );
    }


    //input methods
    public Book inputBookDetails(ActionEvent event) throws IOException{
        String ISBN = inputBookISBN(event);
        String bookName = inputBookName(event);
        String authorName = inputAuthorName(event);
        String publisherName = inputPublisherName(event);
        String category = inputCategory(event);


        // Create a publishDate dialogue
        TextInputDialog publishDateDialogue = new TextInputDialog();

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) publishDateDialogue.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        publishDateDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        publishDateDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> publishDate = null;
        try {
            // setting a publishDate dialogue
            publishDateDialogue.setTitle("Input book publish date");
            publishDateDialogue.setHeaderText(null);
            publishDateDialogue.setContentText("Please enter the publish date of the book in the form of \"YYYY-MM-DD\":");
            publishDate = publishDateDialogue.showAndWait();

            if(publishDate.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);
        }

        // Create a priceToBuyDialogue
        TextInputDialog priceToBuyDialogue = new TextInputDialog();
        // Set custom icon for the dialog
        dialogStage = (Stage) priceToBuyDialogue.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        priceToBuyDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        priceToBuyDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
        Optional<String> priceToBuyString = null;

        // Create a numOfCopies
        TextInputDialog numOfCopiesDialogue = new TextInputDialog();
        // Set custom icon for the dialog
        dialogStage = (Stage) numOfCopiesDialogue.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));
        // Apply CSS to the dialog
        numOfCopiesDialogue.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        numOfCopiesDialogue.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class
        Optional<String> numOfCopiesString = null;

        try {
            priceToBuyDialogue.setTitle("Price to buy");
            priceToBuyDialogue.setHeaderText(null);
            priceToBuyDialogue.setContentText("Please enter the price of the that book:");
            priceToBuyString = priceToBuyDialogue.showAndWait();

            numOfCopiesDialogue.setTitle("Number of Copies");
            numOfCopiesDialogue.setHeaderText(null);
            numOfCopiesDialogue.setContentText("Please enter the Number of Copies of the that book:");
            numOfCopiesString = numOfCopiesDialogue.showAndWait();

            if(priceToBuyString.isEmpty() || Integer.parseInt(priceToBuyString.get())<0 || numOfCopiesString.isEmpty() || Integer.parseInt(numOfCopiesString.get()) < 0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            alert(event, "Invalid input", "Invalid input, please try again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        double priceToBuy = Double.parseDouble(priceToBuyString.get());

        int numOfCopies = Integer.parseInt(numOfCopiesString.get());

        return new Book(ISBN, bookName, authorName, publisherName, publishDate.get(), category, priceToBuy, numOfCopies);
    }

    public int inputMemberID(ActionEvent event) throws IOException{
        TextInputDialog memberId = new TextInputDialog();

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) memberId.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        memberId.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        memberId.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> memberIdOptional = null;
        try {
            memberId.setTitle("Input member id");
            memberId.setHeaderText(null);
            memberId.setContentText("Please enter the id of the member:");
            memberIdOptional = memberId.showAndWait();
            if(memberIdOptional.isEmpty() || Integer.parseInt(memberIdOptional.get())<0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
        return Integer.parseInt(memberIdOptional.get());
    }

    public String inputMemberName(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the member name
        TextInputDialog memberName = new TextInputDialog();

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) memberName.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        memberName.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        memberName.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> memberNameOptional = null;
        try {
            memberName.setTitle("Input member name");
            memberName.setHeaderText(null);
            memberName.setContentText("Please enter the name of the member:");
            memberNameOptional = memberName.showAndWait();
            if(memberNameOptional.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
        return memberNameOptional.get();
    }

    public String inputMemberNumber(ActionEvent event) throws IOException {
        // Create a TextInputDialog to input the member phone
        TextInputDialog memberPhone = new TextInputDialog();

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) memberPhone.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        memberPhone.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());
        memberPhone.getDialogPane().getStyleClass().add("custom-dialog");  // Add custom class

        Optional<String> memberPhoneOptional = null;
        try {
            memberPhone.setTitle("Input member number");
            memberPhone.setHeaderText(null);
            memberPhone.setContentText("Please enter the phone number of the member:");
            memberPhoneOptional = memberPhone.showAndWait();
            if(!isValidNumber(memberPhoneOptional.get())){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
        // Capturing the input
        return memberPhoneOptional.get();
    }

    public String inputMemberEmail(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the member email
        TextInputDialog memberEmail = new TextInputDialog();


        memberEmail.setGraphic(null);  // Removes the default icon

        // Set custom icon for the dialog
        Stage dialogStage = (Stage) memberEmail.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/librarymanagementsystemgui/book.png")));

        // Apply CSS to the dialog
        memberEmail.getDialogPane().getStylesheets().add(User.class.getResource("/org/example/librarymanagementsystemgui/style.css").toExternalForm());

        Optional<String> memberEmailOptional = null;
        try {
            memberEmail.setTitle("Input member email");
            memberEmail.setHeaderText(null);
            memberEmail.setContentText("Please enter the email of the member:");
            memberEmailOptional = memberEmail.showAndWait();
            if(!isValidEmail(memberEmailOptional.get())){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            alert(event, "Invalid input", "Invalid input, please try again!");

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
        // Capturing the input
        return memberEmailOptional.get();
    }

    public Member getMemberById(int id){
        for(Member member : members){
            if(member.getMemberId()==id){
                return member;
            }
        }
        return null;
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
