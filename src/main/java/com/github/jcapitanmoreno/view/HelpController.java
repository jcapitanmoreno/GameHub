package com.github.jcapitanmoreno.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class HelpController {
    @FXML
    private TextField asuntoTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Button enviarButton;

    @FXML
    public void initialize() {

    }

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

}