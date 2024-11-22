module com.github.jcapitanmoreno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    opens com.github.jcapitanmoreno to javafx.fxml;
    exports com.github.jcapitanmoreno;
}
