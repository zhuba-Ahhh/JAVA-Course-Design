module com.example.talk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.talk to javafx.fxml;
    exports com.example.talk;
    exports music;
}