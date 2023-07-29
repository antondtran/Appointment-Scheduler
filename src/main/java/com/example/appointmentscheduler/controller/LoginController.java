package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.Main;
import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.JDBCConnection;
import com.example.appointmentscheduler.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {


    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;


    @FXML
    RadioButton englishBtn;

    @FXML
    RadioButton frenchBtn;

    @FXML
    Label loginLabel;

    @FXML
    Button loginBtn;

    @FXML
    Button cancelBtn;

    @FXML
    Label timeZoneOneLabel;



    User user = new User();




    public void loginBtn(ActionEvent event){
        user.setUserName(usernameTextField.getText());
        user.setPassword(passwordTextField.getText());
        try {


            boolean loginSuccessful = JDBCConnection.verifyLogin(user.getUserName(), user.getPassword());
            if (loginSuccessful) {
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.close();


                // Load the FXML file and get the root node
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainscheduler.fxml"));
                Parent root = loader.load();


                // Create the scene and set it to the stage
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Appointment Scheduler");
                stage.setScene(scene);
                stage.show();
            } else {

                if (englishBtn.isSelected()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password. Please try again.");
                    alert.showAndWait();
                } else {
                    // Show an error message if login fails
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Échec de la connexion");
                    alert.setHeaderText(null);
                    alert.setContentText("Nom d'utilisateur ou mot de passe incorrect. Veuillez réessayer.");
                    alert.showAndWait();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void englishBtn(ActionEvent event) {

        Locale userLocale = Locale.getDefault();
        ResourceBundle bundle;
        userLocale.getLanguage().equals("en");
        bundle = ResourceBundle.getBundle("com.example.appointmentscheduler.messages", new Locale("en", "US"));

        loginLabel.setText(bundle.getString("login.title"));
        loginBtn.setText(bundle.getString("login.loginBtn"));
        cancelBtn.setText(bundle.getString("login.cancelBtn"));
        englishBtn.setText(bundle.getString("login.englishBtn"));
        frenchBtn.setText(bundle.getString("login.frenchBtn"));
        timeZoneOneLabel.setText(bundle.getString("login.timeZoneOneLabel"));

        // ...

        // Optionally, you can set the prompt text for text fields and other elements using translations
        usernameTextField.setPromptText(bundle.getString("login.username"));
        passwordTextField.setPromptText(bundle.getString("login.password"));

    }

    public void frenchBtn(ActionEvent event) {
        // Detect the user's locale
        Locale userLocale = Locale.getDefault();

        // Load the appropriate resource bundle based on the user's locale
        ResourceBundle bundle;
        userLocale.getLanguage().equals("fr");
        bundle = ResourceBundle.getBundle("com.example.appointmentscheduler.messages", new Locale("fr", "FR"));



        // Set the text for labels, buttons, etc., using the translations from the resource bundle
        loginLabel.setText(bundle.getString("login.title"));
        loginBtn.setText(bundle.getString("login.loginBtn"));
        cancelBtn.setText(bundle.getString("login.cancelBtn"));
        englishBtn.setText(bundle.getString("login.englishBtn"));
        frenchBtn.setText(bundle.getString("login.frenchBtn"));
        timeZoneOneLabel.setText(bundle.getString("login.timeZoneOneLabel"));
        // ...

        // Optionally, you can set the prompt text for text fields and other elements using translations
        usernameTextField.setPromptText(bundle.getString("login.username"));
        passwordTextField.setPromptText(bundle.getString("login.password"));
        // ...
    }

    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.close();

    }
}
