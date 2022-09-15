module simple.music {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires rxcontrols;
    exports bigHomeWork;
    opens bigHomeWork to javafx.fxml;
}