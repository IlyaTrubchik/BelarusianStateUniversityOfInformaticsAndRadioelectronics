module com.example.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;


    exports com.example.lab1;
    opens com.example.lab1 to javafx.fxml;
}