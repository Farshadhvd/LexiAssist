module com.farshad.lexiassist {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.farshad.lexiassist;
    exports com.farshad.lexiassist.io;
    exports com.farshad.lexiassist.dictionary.autocompletelookup;
    exports com.farshad.lexiassist.autocomplete;
    opens com.farshad.lexiassist.ui to javafx.fxml;
    exports com.farshad.lexiassist.dictionary.exactwordlookup;
}