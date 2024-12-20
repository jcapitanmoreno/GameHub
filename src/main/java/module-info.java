module com.github.jcapitanmoreno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;
    requires java.desktop;
    requires org.checkerframework.checker.qual;
    requires java.mail;

    opens com.github.jcapitanmoreno.view to javafx.fxml;
    exports com.github.jcapitanmoreno.view;

    opens com.github.jcapitanmoreno.test to javafx.fxml;
    exports com.github.jcapitanmoreno.test;


    opens com.github.jcapitanmoreno to javafx.fxml;
    exports com.github.jcapitanmoreno;
    opens com.github.jcapitanmoreno.model.connection to java.xml.bind;

}
