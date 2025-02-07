/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import businessLogic.manager.ManagerFactory;
import exceptions.ExistingUserException;
import DTO.ManagerBean;
import encryption.SymmetricEncryptor;
import java.io.FileInputStream;
import ui.utilities.WindowManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import ui.utilities.ConfigReader;

/**
 * Controller class for the Sign Up screen. Manages user registration, input validation, and navigation actions. This class handles user interactions on the sign-up screen, including: - Field validation methods - Password visibility toggle - Sign-up logic with error handling for various exceptions
 *
 * @see DTO.ManagerBean
 * @see javafx.beans.value.ObservableValue
 * @see javafx.event.ActionEvent
 * @see java.util.logging.Logger
 *
 * @author Ander
 * @author Aitziber
 */
public class SignUpController {

    /**
     * Logger to track the class activity and handle debugging information.
     */
    private static final Logger logger = Logger.getLogger(SignUpController.class.getName());

    /**
     * TextField for user name input.
     */
    @FXML
    private TextField tfName;

    /**
     * TextField for user email input.
     */
    @FXML
    private TextField tfEmail;

    /**
     * TextField for displaying the plain text password.
     */
    @FXML
    private TextField tfPassword;

    /**
     * TextField for user address input.
     */
    @FXML
    private TextField tfAddress;

    /**
     * TextField for user city input.
     */
    @FXML
    private TextField tfCity;

    /**
     * TextField for user ZIP code input.
     */
    @FXML
    private TextField tfZip;

    /**
     * Button to submit registration data.
     */
    @FXML
    private Button btnSignUp;

    /**
     * Hyperlink to navigate to the Sign In screen.
     */
    @FXML
    private Hyperlink hlSignIn;

    /**
     * Button to toggle password visibility.
     */
    @FXML
    private Button btnShowPassword;

    /**
     * Label to display error messages.
     */
    @FXML
    private Label lblErrorSignUp;

    /**
     * CheckBox to set the user's active status.
     */
    @FXML
    private CheckBox cbActive;

    /**
     * PasswordField for password input (hidden by default).
     */
    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller by setting up event listeners and button properties. This method is automatically called after the FXML file is loaded. It configures the default state of the sign-up fields and assigns listeners for validation and actions.
     */
    public void initialize() {
        logger.log(Level.INFO, "Initializing SignUpController...");

        btnSignUp.setDisable(true);
        lblErrorSignUp.setText("");

        // Add listeners for input field focus loss to validate data before enabling the SignUp button
        tfName.focusedProperty().addListener(this::handleFocusLost);
        tfEmail.focusedProperty().addListener(this::handleFocusLost);
        tfPassword.focusedProperty().addListener(this::handleFocusLost);
        tfAddress.focusedProperty().addListener(this::handleFocusLost);
        tfCity.focusedProperty().addListener(this::handleFocusLost);
        tfZip.focusedProperty().addListener(this::handleFocusLost);

        btnSignUp.setOnAction(this::handleSignUpButtonAction);
        hlSignIn.setOnAction(this::handleSignInHyperLinkAction);

        // Set the "Sign Up" button as the default button
        btnSignUp.setDefaultButton(true);
        // Toggle password visibility button listener
        btnShowPassword.armedProperty().addListener(this::handleButtonPasswordVisibility);
    }

    /**
     * Validates input fields when they lose focus. Enables the Sign Up button if all required fields are valid.
     *
     * @param observable the observable property of the input field's focus.
     * @param oldValue the previous focus state.
     * @param newValue the new focus state.
     */
    private void handleFocusLost(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        if (oldValue) {
            // Validate email format
            if (!tfEmail.getText().isEmpty()) {
                if (!validateEmail()) {
                    lblErrorSignUp.setText("Incorrect email format");
                    btnSignUp.setDisable(true);
                    return;
                } else {
                    lblErrorSignUp.setText("");
                }
            }

            // Validate zip code format
            if (!tfZip.getText().isEmpty()) {
                if (!validateZip()) {
                    lblErrorSignUp.setText("Write a valid 5 digit ZIP");
                    btnSignUp.setDisable(true);
                    return;
                } else {
                    lblErrorSignUp.setText("");
                }
            }

            // Validate password length
            if (!pfPassword.getText().isEmpty()) {
                if (!validatePassword()) {
                    lblErrorSignUp.setText("Password must contain at least 8 characters");
                    btnSignUp.setDisable(true);
                    return;
                } else {
                    lblErrorSignUp.setText("");
                }
            }

            // Check that all fields are completed
            if (tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || pfPassword.getText().isEmpty() || tfAddress.getText().isEmpty() || tfCity.getText().isEmpty() || tfZip.getText().isEmpty()) {
                btnSignUp.setDisable(true);
            } else {
                btnSignUp.setDisable(false);
            }
        }
    }

    /**
     * Validates the ZIP code format (must be exactly 5 digits).
     *
     * @return true if the ZIP code format is valid, false otherwise.
     */
    public boolean validateZip() {
        return tfZip.getText().matches("^\\d{5}$");
    }

    /**
     * Validates the email format.
     *
     * @return true if the email format is valid, false otherwise.
     */
    public boolean validateEmail() {
        boolean correct = false;
        String email = tfEmail.getText();
        Pattern model = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        Matcher matcher = model.matcher(email);
        if (matcher.matches()) {
            correct = true;
        }
        return correct;
    }

    /**
     * Validates the password length (must be at least 8 characters).
     *
     * @return true if the password meets the minimum length requirement, false otherwise.
     */
    public boolean validatePassword() {
        return pfPassword.getText().matches("^.{8,}$");
    }

    /**
     * Toggles the visibility of the password between plain text and hidden.
     *
     * @param observable the observable property of the button.
     * @param oldValue the previous state of the button.
     * @param newValue the new state of the button.
     */
    private void handleButtonPasswordVisibility(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            String password = pfPassword.getText();
            pfPassword.setVisible(false);
            tfPassword.setText(password);
            tfPassword.setVisible(true);
            logger.log(Level.INFO, "Password visibility toggled to show");
        } else {
            tfPassword.setVisible(false);
            pfPassword.setVisible(true);
            logger.log(Level.INFO, "Password visibility toggled to hide");
        }
    }

    /**
     * Handles the action of the Sign In hyperlink. Prompts for confirmation to leave the registration screen and navigates to the Sign In screen if confirmed.
     *
     * @param actionEvent the action event triggered by clicking the hyperlink.
     */
    private void handleSignInHyperLinkAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are about to exit");
        alert.setContentText("Are you sure you want to leave the registration window and return to the Sign In window?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                logger.log(Level.INFO, "Closing SignUp window");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/SignIn.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.getIcons().add(new Image("resources/logo.png"));
                stage.setTitle("SignIn");
                stage.setScene(new Scene(root));
                stage.show();
                logger.log(Level.INFO, "Opened SignIn window");
            } catch (IOException ex) {
                lblErrorSignUp.setText("Error opening Sign In window");
            }
        }
    }

    /**
     * Handles the action of the Sign Up button. Creates a user, attempts registration, and navigates to the Sign In screen on success.
     *
     * @param actionEvent the action event triggered by clicking the Sign Up button.
     * @throws ExistingUserException if the user already exists.
     * @throws ServerException if a server error occurs during registration.
     */
//    private void handleSignUpButtonAction(ActionEvent actionEvent) {
//        tfPassword.setText(pfPassword.getText());
//         
//        ManagerBean manager = new ManagerBean(cbActive.isSelected(), tfPassword.getText().trim(), tfName.getText().trim(), tfEmail.getText().trim(), "000000000", 
//                tfCity.getText().trim(), tfZip.getText().trim(), tfAddress.getText().trim());
//        logger.log(Level.INFO, "Manager signed up successfully");
//        
//        try {
//            /////
//            ManagerBean existingManager = ManagerFactory.get().getManagerByEmail(new GenericType<ManagerBean>() {
//            }, tfEmail.getText().trim());
//            if (existingManager != null ) {
//                throw new ExistingUserException("User already exists, please try with another email");
//            }
//            
//            logger.log(Level.INFO, "Creating manager: {0}", manager.toString());
//            ManagerFactory.get().signUp(manager);
//            logger.log(Level.INFO, "Manager signed up successfully");
//
//            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
//            WindowManager.openWindow("/ui/view/SignIn.fxml", "SignIn");
//        } catch (ExistingUserException ex) {
//            lblErrorSignUp.setText("User already exists");
//            logger.log(Level.INFO, "User already exists: ", ex);
//
//        } catch (WebApplicationException ex) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("ERROR");
//            alert.setHeaderText("Server error");
//            alert.setContentText("There was an error in the server, please contact the responsible technician"+ ex.getMessage());
//            alert.showAndWait();
//            logger.log(Level.SEVERE, "Server error", ex);
//        }
//    }
       private void handleSignUpButtonAction(ActionEvent actionEvent) {
        try {
            
        String email = tfEmail.getText().trim();
        String password = tfPassword.getText().trim();
        if (password.isEmpty()) {
            password = pfPassword.getText().trim();
        }
        
        if (email.isEmpty() || password.isEmpty()) {
            logger.severe("Error: Email o Password están vacíos");
            lblErrorSignUp.setText("Email o contraseña vacíos.");
            return; 
        }

        // Log antes de encriptar
        logger.log(Level.INFO, "Email antes de encriptar: {0}", email);
        logger.log(Level.INFO, "Password antes de encriptar: {0}", password);

        // Encriptar email y contraseña
        String encryptedEmail = SymmetricEncryptor.encrypt(email);
        String encryptedPassword = SymmetricEncryptor.encrypt(password);

        // Log después de encriptar
        logger.log(Level.INFO, "Encrypted email: {0}", encryptedEmail);
        logger.log(Level.INFO, "Encrypted password: {0}", encryptedPassword);

            
//            String encryptedEmail = SymmetricEncryptor.encrypt(tfEmail.getText().trim());
//            String encryptedPassword = SymmetricEncryptor.encrypt(tfPassword.getText().trim());
           
            ManagerBean manager = new ManagerBean(
                cbActive.isSelected(),
                encryptedPassword,
                tfName.getText().trim(),
                encryptedEmail,
                "000000000",
                tfCity.getText().trim(),
                tfZip.getText().trim(),
                tfAddress.getText().trim()
            );

            logger.log(Level.INFO, "Verificando si el usuario ya existe...");
            ManagerBean existingManager = ManagerFactory.get().getManagerByEmail(new GenericType<ManagerBean>() {}, encryptedEmail);

            if (existingManager != null) {
                throw new ExistingUserException("User already exists, please try with another email");
            }

            logger.log(Level.INFO, "Registrando manager: {0}", manager.toString());
            ManagerFactory.get().signUp(manager);
            logger.log(Level.INFO, "Manager registrado con éxito");

            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            WindowManager.openWindow("/ui/view/SignIn.fxml", "SignIn");

        } catch (ExistingUserException ex) {
            lblErrorSignUp.setText("User already exists");
            logger.log(Level.INFO, "User already exists: ", ex);

        } catch (WebApplicationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Server error");
            alert.setContentText("There was an error in the server, please contact the responsible technician" + ex.getMessage());
            alert.showAndWait();
            logger.log(Level.SEVERE, "Server error", ex);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en el proceso de registro", e);
        }
    }

   
}
