package org.example.librarymanagementsystemgui.AdminClasses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.librarymanagementsystemgui.DatabaseClasses.Books;
import org.example.librarymanagementsystemgui.DatabaseClasses.User;
import org.example.librarymanagementsystemgui.Exceptions.InputException;
import org.example.librarymanagementsystemgui.LaunchClasses.SystemLaunch;
import org.example.librarymanagementsystemgui.MemberClasses.Members;

import java.io.IOException;
import java.util.Optional;

public class Admins extends User {
    @FXML
    public Button ViewAllAvailableBooks;
    @FXML
    public Button SearchForBookByTitle;
    @FXML
    public Button SearchForBookByAuthorName;
    @FXML
    public Button AddABook;
    @FXML
    public Button RemoveABook;
    @FXML
    public Button EditABook;
    @FXML
    public Button SearchForAMember;
    @FXML
    public Button AddAMember;
    @FXML
    public Button RemoveAMember;
    @FXML
    public Button EditAMember;
    @FXML
    public Button ViewAllMembers;
    @FXML
    public Button FineAMember;
    @FXML
    public Button LogOut;
    @FXML
    public Button Exit;

    protected static Admins adminToPass;

    public Admins(){}

    public Admins(String name, String phone, String email){
        super(name,phone,email);
    }


    public void receiveAdmin(ActionEvent event, Admins admin){
        adminToPass = admin;
    }
    public void passAdminToMethods(ActionEvent event) throws IOException {
        System.out.println(adminToPass);
        if(event.getSource()==ViewAllAvailableBooks){
            adminToPass.ViewAllAvailableBooks(event);
        } else if(event.getSource()==SearchForBookByTitle){
            adminToPass.SearchForBookByTitle(event);
        } else if(event.getSource()==SearchForBookByAuthorName){
            adminToPass.SearchForBookByAuthorName(event);
        } else if(event.getSource()==AddABook){
            adminToPass.AddABook(event);
        } else if(event.getSource()==RemoveABook){
            adminToPass.RemoveABook(event);
        } else if(event.getSource()==EditABook){
            adminToPass.EditABook(event);
        } else if(event.getSource()==SearchForAMember){
            adminToPass.SearchForAMember(event);
        } else if(event.getSource()==AddAMember){
            adminToPass.AddAMember(event);
        } else if(event.getSource()==RemoveAMember){
            adminToPass.RemoveAMember(event);
        } else if(event.getSource()==EditAMember){
            adminToPass.EditAMember(event);
        } else if(event.getSource()==ViewAllMembers){
            adminToPass.ViewAllMembers(event);
        } else if(event.getSource()==FineAMember){
            adminToPass.FineAMember(event);
        }
    }


    public void AddABook(ActionEvent event) throws IOException {
        Books bookToAdd = inputBookDetails(event);
        // Check if the book is already present
        if(isBookInDB(bookToAdd.getTitle())==null){
            books.add(bookToAdd);
            String title = "Book's Added";
            String content = "Books is added successfully!";
            alert(event, title, content);
        }else{
            String title = "Book exists";
            String content = "Book already exists";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
    }

    public void RemoveABook(ActionEvent event) throws IOException {
        String bookNameToRemove = inputBookName(event);
        // Check if the book is already present
        Books bookToRemove = isBookInDB(bookNameToRemove);
        if(bookToRemove!=null){
            books.remove(bookToRemove);
            String title = "Book is removed";
            String content = "Books is removed successfully!";
            alert(event, title, content);
        }else{
            String title = "Book not exists";
            String content = "Book not exists";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
    }

    public void EditABook(ActionEvent event) throws IOException {
        String bookNameToEdit = inputBookName(event);
        Books bookToEdit = isBookInDB(bookNameToEdit);


        // Create a TextInputDialog to input the book name
        TextInputDialog dialog = new TextInputDialog();
        Optional<String> choiceOptional = null;
        try {
            dialog.setTitle("Input your choice");
            dialog.setHeaderText("Enter your choice:");
            dialog.setContentText("What do you want to edit in the book?\n1)Book name\n2)Author's name\n" +
                    "\n3)number of books available to buy\n4)number of books available to borrow\n5)Price to buy\n6)Price to borrow");
            choiceOptional = dialog.showAndWait();
            if(choiceOptional.isEmpty()){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        int choice = Integer.parseInt(choiceOptional.get());
        switch(choice){
            case 1:
                bookToEdit.setTitle(inputBookName(event));
                break;

            case 2:
                bookToEdit.setAuthorName(inputAuthorName(event));
                break;

            case 3:
                // Create a numToBuyDialogue with a default value
                TextInputDialog numToBuyDialogue = new TextInputDialog();
                Optional<String> numToBuy = null;
                try {
                    // setting a numToBuyDialogue
                    numToBuyDialogue.setTitle("books to buy");
                    numToBuyDialogue.setHeaderText("Enter the number of books available to buy:");
                    numToBuyDialogue.setContentText("Please enter the number of books available to buy:");
                    numToBuy = numToBuyDialogue.showAndWait();

                    if(numToBuy.isEmpty() || Integer.parseInt(numToBuy.get())<0){
                        throw new InputException("Invalid inputs");
                    }
                }catch(Exception e){
                    String title = "Invalid input";
                    String content = "Invalid input, please try again!";
                    alert(event, title, content);

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }

                int numToBuyInt = Integer.parseInt(numToBuy.get());

                bookToEdit.setNumAvailableToBuy(numToBuyInt);
                break;

            case 4:
                // Create a numToBorrowDialogue with a default value
                TextInputDialog numToBorrowDialogue = new TextInputDialog();
                Optional<String> numToBorrow = null;
                try {
                    // setting a numToBorrowDialogue
                    numToBorrowDialogue.setTitle("books to borrow");
                    numToBorrowDialogue.setHeaderText("Enter the number of books available to borrow:");
                    numToBorrowDialogue.setContentText("Please enter the number of books available to borrow:");
                    numToBorrow = numToBorrowDialogue.showAndWait();

                    if(numToBorrow.isEmpty() || Integer.parseInt(numToBorrow.get())<0){
                        throw new InputException("Empty inputs");
                    }
                }catch(Exception e){
                    String title = "Invalid input";
                    String content = "Invalid input, please try again!";
                    alert(event, title, content);

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }

                int numToBorrowInt = Integer.parseInt(numToBorrow.get());

                bookToEdit.setNumAvailableToBorrow(numToBorrowInt);
                break;

            case 5:
                // Create a priceToBuyDialogue with a default value
                TextInputDialog priceToBuyDialogue = new TextInputDialog();
                Optional<String> priceToBuy = null;

                try {
                    // setting a numToBuyDialogue
                    priceToBuyDialogue.setTitle("Price to buy");
                    priceToBuyDialogue.setHeaderText("Enter the price to buy:");
                    priceToBuyDialogue.setContentText("Please enter the price to buy:");
                    priceToBuy = priceToBuyDialogue.showAndWait();

                    if(priceToBuy.isEmpty() || Double.parseDouble(priceToBuy.get())<0){
                        throw new InputException("Empty inputs");
                    }
                }catch(Exception e){
                    String title = "Invalid input";
                    String content = "Invalid input, please try again!";
                    alert(event, title, content);

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }

                double priceToBuyDouble = Double.parseDouble(priceToBuy.get());

                bookToEdit.setPriceToBuy(priceToBuyDouble);
                break;

            case 6:
                // Create a priceToBorrowDialogue with a default value
                TextInputDialog priceToBorrowDialogue = new TextInputDialog();
                Optional<String> priceToBorrow = null;

                try {
                    // setting a priceToBorrowDialogue
                    priceToBorrowDialogue.setTitle("Price to borrow");
                    priceToBorrowDialogue.setHeaderText("Enter the price to borrow:");
                    priceToBorrowDialogue.setContentText("Please enter the price to borrow:");
                    priceToBorrow = priceToBorrowDialogue.showAndWait();

                    if(priceToBorrow.isEmpty() || Double.parseDouble(priceToBorrow.get())<0){
                        throw new InputException("Empty inputs");
                    }
                }catch(Exception e){
                    String title = "Invalid input";
                    String content = "Invalid input, please try again!";
                    alert(event, title, content);

                    Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                    setScene(event, root, "Admin access");
                }

                double priceToBorrowDouble = Double.parseDouble(priceToBorrow.get());


                bookToEdit.setPriceToBorrow(priceToBorrowDouble);
                break;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book is edited successfully");
        alert.setHeaderText(null);
        alert.setContentText("Book is edited successfully!");
        alert.showAndWait();

    }

    public void SearchForAMember(ActionEvent event) throws IOException {
        TextInputDialog howToSearch = new TextInputDialog();
        Optional<String> howToSearchOptional = null;
        try {
            howToSearch.setTitle("\"Member search\"");
            howToSearch.setHeaderText("\"Member search\"");
            howToSearch.setContentText("How do you want to search for the member? \n1)By ID \n2)By Number \n3)By Email");
            howToSearchOptional = howToSearch.showAndWait();
            if(howToSearchOptional.isEmpty()){
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
        String howToSearchString = howToSearchOptional.get();

        int choice = Integer.parseInt(howToSearchString);

        switch (choice) {
            case 1:
                int id = inputMemberID(event);
                boolean memberFoundById = false;
                for(Members member : members){
                    if(member.getMemberId()==id){
                        memberFoundById = true;
                        printMemberDetails(event, member,1);
                        break;
                    }
                }
                if(!memberFoundById){
                    memberNotFoundAlert(event);
                }
                break;

            case 2:
                String number = inputMemberNumber(event);
                boolean memberFoundByNum = false;
                for(Members member : members){
                    if(member.getPhoneNumber().equals(number)){
                        memberFoundByNum = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByNum){
                    memberNotFoundAlert(event);
                }
                break;

            case 3:
                String email = inputMemberEmail(event);
                boolean memberFoundByEmail = false;
                for(Members member : members){
                    if(member.getEmail().equals(email)){
                        memberFoundByEmail = true;
                        printMemberDetails(event, member,1);
                    }
                }
                if(!memberFoundByEmail){
                    memberNotFoundAlert(event);
                }
                break;
        }
    }

    public void AddAMember(ActionEvent event) throws IOException {
        Members memberToAdd = inputMemberDetails(event);

        // Check if the member already exists
        if(checkIfMemberExists(memberToAdd.getName(), memberToAdd.getPhoneNumber(), memberToAdd.getEmail())){
            String title = "Member already exist";
            String content = "Member already exist!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else{
            members.add(memberToAdd);
            String title = "Member Added";
            String content = "Member is added successfully!";
            alert(event, title, content);
        }
    }

    public void RemoveAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);
        Members memberToRemove = getMemberById(memberId);

        // Check if the member already exists
        if(memberToRemove==null){
            memberNotFoundAlert(event);
        }else{
            members.remove(memberToRemove);
            String title = "Member removed";
            String content = "Member is removed successfully!";
            alert(event, title, content);
        }
    }

    public void EditAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);
        Members memberToEdit = getMemberById(memberId);

        // Check if the member already exists
        if(memberToEdit==null){
            memberNotFoundAlert(event);
        }else{
            // Create a TextInputDialog to input the book name
            TextInputDialog dialog = new TextInputDialog();
            Optional<String> choiceOptional = null;
            try {
                dialog.setTitle("Input your choice");
                dialog.setHeaderText("Enter your choice:");
                dialog.setContentText("What do you want to edit in the member?\n1)Name\n2)Number\n3)Email\n4)Fine amount");
                choiceOptional = dialog.showAndWait();
                if(choiceOptional.isEmpty()){
                    throw new InputException("Empty inputs");
                }
            }catch(Exception e){
                String title = "Invalid input";
                String content = "Invalid input, please try again!";
                alert(event, title, content);

                Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
                setScene(event, root, "Admin access");
            }

            int choice = Integer.parseInt(choiceOptional.get());
            switch(choice){
                case 1:
                    memberToEdit.setName(this.inputMemberName(event));
                    break;

                case 2:
                    memberToEdit.setPhoneNumber(this.inputMemberNumber(event));
                    break;

                case 3:
                    memberToEdit.setEmail(this.inputMemberEmail(event));
                    break;

                case 4:
                    double memberFine =  inputMemberFineAmount(event);
                    memberToEdit.setFineAmount(memberFine);
                    break;
            }
            String title = "Member edited";
            String content = "Member is edited successfully!";
            alert(event, title, content);
        }
    }

    public void ViewAllMembers(ActionEvent event) throws IOException {
        if(members.isEmpty()){
            String title = "No Books";
            String content = "There are no available members!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else {
            int memberCounter = 1;
            for (Members member : members) {
                printMemberDetails(event, member, memberCounter);
                memberCounter++;
            }
        }
    }

    public void FineAMember(ActionEvent event) throws IOException {
        int memberId = inputMemberID(event);
        Members memberIdToFine = getMemberById(memberId);

        if(memberIdToFine==null){
            memberNotFoundAlert(event);
        }else {
            double fineAmount = inputMemberFineAmount(event);
            memberIdToFine.setFineAmount(fineAmount);

            String title = "Successful";
            String content = "Member is fined successfully";
            alert(event,title,content);
        }
    }


    public Books inputBookDetails(ActionEvent event) throws IOException{
        String bookName = inputBookName(event);
        String authorName = inputAuthorName(event);

        // Create a numToBuyDialogue with a default value
        TextInputDialog numToBuyDialogue = new TextInputDialog();
        Optional<String> numToBuy = null;
        // Create a numToBorrowDialogue with a default value
        TextInputDialog numToBorrowDialogue = new TextInputDialog();
        Optional<String> numToBorrow = null;
        // Create a priceToBuyDialogue with a default value
        TextInputDialog priceToBuyDialogue = new TextInputDialog();
        Optional<String> priceToBuy = null;
        // Create a priceToBorrowDialogue with a default value
        TextInputDialog priceToBorrowDialogue = new TextInputDialog();
        Optional<String> priceToBorrow = null;

        try {
            // setting a numToBuyDialogue
            numToBuyDialogue.setTitle("books to buy");
            numToBuyDialogue.setHeaderText("Enter the number of books available to buy:");
            numToBuyDialogue.setContentText("Please enter the number of books available to buy:");
            numToBuy = numToBuyDialogue.showAndWait();

            // setting a numToBorrowDialogue
            numToBorrowDialogue.setTitle("books to borrow");
            numToBorrowDialogue.setHeaderText("Enter the number of books available to borrow:");
            numToBorrowDialogue.setContentText("Please enter the number of books available to borrow:");
            numToBorrow = numToBorrowDialogue.showAndWait();

            // setting a numToBuyDialogue
            priceToBuyDialogue.setTitle("Price to buy");
            priceToBuyDialogue.setHeaderText("Enter the price to buy:");
            priceToBuyDialogue.setContentText("Please enter the price to buy:");
            priceToBuy = priceToBuyDialogue.showAndWait();

            // setting a priceToBorrowDialogue
            priceToBorrowDialogue.setTitle("Price to borrow");
            priceToBorrowDialogue.setHeaderText("Enter the price to borrow:");
            priceToBorrowDialogue.setContentText("Please enter the price to borrow:");
            priceToBorrow = priceToBorrowDialogue.showAndWait();

            if(numToBuy.isEmpty() || Integer.parseInt(numToBuy.get())<0 || numToBorrow.isEmpty() || Integer.parseInt(numToBorrow.get())<0 || priceToBuy.isEmpty() || Integer.parseInt(priceToBuy.get())<0 || priceToBorrow.isEmpty() || Integer.parseInt(priceToBorrow.get())<0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e){
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        int numToBuyInt = Integer.parseInt(numToBuy.get());
        int numToBorrowInt = Integer.parseInt(numToBorrow.get());
        double priceToBuyDouble = Double.parseDouble(priceToBuy.get());
        double priceToBorrowDouble = Double.parseDouble(priceToBorrow.get());

        return new Books(bookName, authorName, numToBuyInt, numToBorrowInt, priceToBuyDouble, priceToBorrowDouble);
    }

    public int inputMemberID(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the member id
        TextInputDialog memberId = new TextInputDialog();
        Optional<String> memberIdOptional = null;
        try {
            memberId.setTitle("Input member id");
            memberId.setHeaderText("Enter the id of the member:");
            memberId.setContentText("Please enter the id of the member:");
            memberIdOptional = memberId.showAndWait();
            if(memberIdOptional.isEmpty() || Integer.parseInt(memberIdOptional.get())<0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }
        return Integer.parseInt(memberIdOptional.get());
    }

    public String inputMemberName(ActionEvent event) throws IOException{
        // Create a TextInputDialog to input the member name
        TextInputDialog memberName = new TextInputDialog();
        Optional<String> memberNameOptional = null;
        try {
            memberName.setTitle("Input member name");
            memberName.setHeaderText("Enter the name of the member:");
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
        Optional<String> memberPhoneOptional = null;
        try {
            memberPhone.setTitle("Input member name");
            memberPhone.setHeaderText("Enter the phone number of the member:");
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
        Optional<String> memberEmailOptional = null;
        try {
            memberEmail.setTitle("Input member name");
            memberEmail.setHeaderText("Enter the email of the member:");
            memberEmail.setContentText("Please enter the email of the member:");
            memberEmailOptional = memberEmail.showAndWait();
            if(!isValidEmail(memberEmailOptional.get())){
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
        return memberEmailOptional.get();
    }

    public double inputMemberFineAmount(ActionEvent event) throws IOException {
        // Create a TextInputDialog to input the member fine amount
        TextInputDialog memberFineAmount = new TextInputDialog();
        Optional<String> memberFineAmountOptional = null;
        try {
            memberFineAmount.setTitle("Input Fine amount");
            memberFineAmount.setHeaderText("Enter the fine amount:");
            memberFineAmount.setContentText("Please enter the fine amount:");
            memberFineAmountOptional = memberFineAmount.showAndWait();
            if(memberFineAmountOptional.isEmpty() || Integer.parseInt(memberFineAmountOptional.get())<0){
                throw new InputException("Empty inputs");
            }
        }catch(Exception e) {
            String title = "Invalid input";
            String content = "Invalid input, please try again!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }

        return Double.parseDouble(memberFineAmountOptional.get());
    }

    public Members inputMemberDetails(ActionEvent event) throws IOException{
        String memberName = inputMemberName(event);
        String memberPhone = inputMemberNumber(event);
        String memberEmail = inputMemberEmail(event);

        //making a new member object
        Members member = new Members(memberName, memberPhone, memberEmail);

        return member;
    }

    public Members getMemberById(int id){
        //looping on the members to check if the data is right
        for(Members member : members){
            if(member.getMemberId()==id){
                return member;
            }
        }
        return null;
    }

    public void printMemberDetails(ActionEvent event, Members member, int MemberCounter) throws IOException {
        if(members.isEmpty() || !checkIfMemberExists(member.getName(), member.getPhoneNumber(), member.getEmail())){
            String title = "No members";
            String content = "There are no available members!";
            alert(event, title, content);

            Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
            setScene(event, root, "Admin access");
        }else {
            String title = "Member details";
            String content = MemberCounter + ")\nId: " + member.getMemberId() + "\nName: " + member.getName() + "\nNumber: " + member.getPhoneNumber()
                    + "\nEmail: " + member.getEmail() + "\nFine Amount: " + member.getFineAmount() + "$";

            alert(event, title, content);
        }
    }

    public void memberNotFoundAlert(ActionEvent event) throws IOException{
        String title = "Member not found";
        String content = "Member isn't found!";
        alert(event, title, content);

        Parent root = FXMLLoader.load(SystemLaunch.class.getResource("/org/example/librarymanagementsystemgui/adminAccess.fxml"));
        setScene(event, root, "Admin access");

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
