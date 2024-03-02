module com.example.lab3guiworking {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab3guiworking to javafx.fxml;
    exports com.example.lab3guiworking;
}