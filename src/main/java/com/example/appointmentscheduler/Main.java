package com.example.appointmentscheduler;

import com.example.appointmentscheduler.controller.LoginController;
import com.example.appointmentscheduler.controller.MainSchedulerController;
import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.JDBCConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {




    @Override
    public void start(Stage stage) throws IOException {

        ToggleGroup toggleGroup = new ToggleGroup();


        // Load the default resource bundle (English)



        ZoneId userTimeZone = ZoneId.systemDefault();


        // Load the FXML file and get the root node
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("loginform.fxml"));
        Parent root = loader.load();

        RadioButton englishBtn = (RadioButton) loader.getNamespace().get("englishBtn");
        RadioButton frenchBtn = (RadioButton) loader.getNamespace().get("frenchBtn");
        englishBtn.setToggleGroup(toggleGroup);
        frenchBtn.setToggleGroup(toggleGroup);
        englishBtn.setSelected(true);




        Label timeZoneLabel = (Label) loader.getNamespace().get("timeZoneLabel");
        timeZoneLabel.setText(userTimeZone.getId());
        TextField usernameTextField = (TextField) loader.getNamespace().get("usernameTextField");
        usernameTextField.setPromptText("username");
        PasswordField passwordTextField = (PasswordField) loader.getNamespace().get("passwordTextField");
        passwordTextField.setPromptText("password");

                // Create the scene and set it to the stage
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Scheduler");
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }


}