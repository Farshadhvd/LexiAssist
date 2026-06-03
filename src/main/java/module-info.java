module com.farshad.lexiassist {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.farshad.lexiassist;
    opens com.farshad.lexiassist.ui to javafx.fxml;
}