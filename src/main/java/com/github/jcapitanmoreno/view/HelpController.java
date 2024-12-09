package com.github.jcapitanmoreno.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class HelpController {
    @FXML
    private TextField asuntoTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Button enviarButton;

    @FXML
    private Button volver;
    @FXML
    public void initialize() {

    }


    /**
     * Handles the process of sending an email.
     * Configures Gmail SMTP properties, creates a message, and sends it.
     */
    @FXML
    private void enviarCorreo() {
        String asunto = asuntoTextField.getText();
        String descripcion = descripcionTextArea.getText();


        String to = "jcapitanmoreno@gmail.com";
        String from = "ayudagamehub@gmail.com";
        String host = "smtp.gmail.com";


        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("ayudagamehub@gmail.com", "ffnt odps fuqc tnbj");
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(asunto);
            message.setText(descripcion);


            Transport.send(message);
            System.out.println("Mensaje enviado exitosamente....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }


    /**
     * Navigates to the "Inicio" view by loading the corresponding FXML file.
     * Displays an alert if an error occurs while loading the view.
     */
    @FXML
    private void navigateToInicio(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicioV.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.show();
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista");
        }
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) volver.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert with the given parameters (type, title, and content).
     *
     * @param alertType the type of the alert (ERROR, INFORMATION, etc.)
     * @param title the title of the alert
     * @param content the content message of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}