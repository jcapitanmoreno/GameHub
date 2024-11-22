module com.github.jcapitanmoreno {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.jcapitanmoreno to javafx.fxml;
    exports com.github.jcapitanmoreno;
}
