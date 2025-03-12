module org.example.librarymanagementsystemgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jfr;
    requires javafx.graphics;


    opens org.example.librarymanagementsystemgui to javafx.fxml;
    opens org.example.librarymanagementsystemgui.LaunchClasses to javafx.fxml;

    exports org.example.librarymanagementsystemgui.LaunchClasses;
    exports org.example.librarymanagementsystemgui.DatabaseClasses;
    exports org.example.librarymanagementsystemgui.MemberClasses;
    exports org.example.librarymanagementsystemgui.AdminClasses;
    exports org.example.librarymanagementsystemgui.Exceptions;
    opens org.example.librarymanagementsystemgui.Exceptions to javafx.fxml;
    exports org.example.librarymanagementsystemgui.Books;
}