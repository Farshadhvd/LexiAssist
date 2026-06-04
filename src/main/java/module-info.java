module com.farshad.lexiassist {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.farshad.lexiassist;
    exports com.farshad.lexiassist.io;
    opens com.farshad.lexiassist.ui to javafx.fxml;
}