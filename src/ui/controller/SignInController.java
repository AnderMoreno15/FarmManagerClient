/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import businessLogic.manager.ManagerFactory;
import DTO.ManagerBean;
import ui.utilities.WindowManager;
import ui.controller.MenuController;
import exceptions.InactiveUserException;
import exceptions.UserCredentialException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * Controller class for the Sign In screen. Manages user authentication, navigation actions, and input validation. This class is responsible for handling user interactions on the sign-in screen. It includes: - Field validation methods - Password visibility toggle - Sign-in logic with error handling for various exceptions
 *
 * @see DTO.ManagerBean
 * @see javafx.beans.value.ObservableValue
 * @see javafx.event.ActionEvent
 * @see java.util.logging.Logger
 *
 * @author Ander
 * @author Aitziber
 */
public class SignInController {

    /**
     * Logger to track the class activity and handle debugging information.
     */
    private static final Logger logger = Logger.getLogger(SignInController.class.getName());

    /**
     * TextField for entering the username, expected to be an email.
     */
    @FXML
    private TextField tfUsername;

    /**
     * PasswordField for entering the password in a concealed form.
     */
    @FXML
    private PasswordField pfPasswd;

    /**
     * TextField for showing the password in plain text when visibility is toggled.
     */
    @FXML
    private TextField tfPasswd;

    /**
     * Button to toggle the visibility of the password.
     */
    @FXML
    private Button btnShowPassword;

    /**
     * Button to initiate the sign-in process.
     */
    @FXML
    private Button btnSignIn;

    /**
     * Label to display error messages, such as input validation issues or login failures.
     */
    @FXML
    private Label lblError;

    /**
     * Hyperlink to navigate to the Sign Up screen.
     */
    @FXML
    private Hyperlink hlSignUp;
    
    @FXML
    private Hyperlink hlReset;

    /**
     * Initializes the controller by setting up event listeners and button properties. This method is automatically called after the FXML file is loaded.
     */
    public void initialize() {
        logger.log(Level.INFO, "Initilizing sign in controller");
        tfUsername.focusedProperty().addListener(this::handleTfUsernameFocusProperyLost);
        btnSignIn.setOnAction(this::handleSignInButtonAction);
        hlSignUp.setOnAction(this::handleSignUpHyperlinkAction);
        hlReset.setOnAction(this::handleResetHyperlinkAction);

        // Set the "Sign In" button as the default
        btnSignIn.setDefaultButton(true);
        // Button for showing password
        btnShowPassword.armedProperty().addListener(this::handleButtonPasswordVisibility);
    }

    /**
     * Validates the email format in the username field when it loses focus. Disables the Sign In button if the email format is incorrect.
     *
     * @param observable the observable property of the TextField's focus.
     * @param oldValue the previous focus state.
     * @param newValue the new focus state.
     */
    private void handleTfUsernameFocusProperyLost(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        if (oldValue) {
            String email = tfUsername.getText();
            Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher matcher = pattern.matcher(email);

            if (!matcher.matches()) {
                lblError.setText("Incorrect email format");
                btnSignIn.setDisable(true);
            } else {
                lblError.setText("");
                btnSignIn.setDisable(false);
            }
        }
    }

    /**
     * Toggles the visibility of the password between a concealed PasswordField and a plain text TextField. Called when the "Show Password" button is pressed or released.
     *
     * @param observable the observable property of the button's armed status.
     * @param oldValue the previous armed status.
     * @param newValue the new armed status.
     */
    private void handleButtonPasswordVisibility(ObservableValue observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            // Show the password when pressed
            String password = pfPasswd.getText();
            pfPasswd.setVisible(false);
            tfPasswd.setText(password);
            tfPasswd.setVisible(true);
        } else {
            // Hide the password when released
            tfPasswd.setVisible(false);
            pfPasswd.setVisible(true);
        }
    }

    /**
     * Handles the Sign In button action, validating the fields and performing user authentication. Displays appropriate error messages or navigates to the home screen upon successful authentication.
     *
     * @param actionEvent Action event triggered by the Sign In button.
     */
    private void handleSignInButtonAction(ActionEvent actionEvent) {
        String username = tfUsername.getText();
        String password = pfPasswd.getText();
        
        ManagerBean m = new ManagerBean();
        m.setEmail(username);
        m.setPassword(password);

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please fill out all fields.");
        } else {
            try {
                lblError.setText("");

                ManagerBean manager = ManagerFactory.get().getManagerByEmail(new GenericType<ManagerBean>() {}, username);
                ManagerBean signedManager = ManagerFactory.get().signIn(m, new GenericType<ManagerBean>(){})
                        
//          ManagerBean signedManager = ManagerFactory.get().signIn(new GenericType<ManagerBean>() m);
;

                
//                if (!UserAuthService.verifyPassword(password, manager.getPassword())) {
//                    actionEvent.consume();
//                    throw new UserCredentialException("Incorrect password");
//                }

                if (!signedManager.isIsActive()) {
                    throw new InactiveUserException("User is not active");
                }

            // Autenticación exitosa, proceder a la siguiente pantalla
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            MenuController.setManager(signedManager);
            AnimalGroupController.setManager(signedManager);
            ConsumesController.setManager(signedManager);
            WindowManager.openWindowWithManager("/ui/view/AnimalGroup.fxml", "Animal Group", signedManager);
                

            } catch (WebApplicationException ex) {
                showErrorAlert("Server error", "There was an error on the server, please contact support.");
                logger.log(Level.SEVERE, "Server error", ex);
            } catch (InactiveUserException ex) {
                showErrorAlert("User error", "Your account is deactivated, please contact support.");
                logger.log(Level.SEVERE, "Inactive user", ex);
            }
        }
    }

    /**
     * Opens the Sign Up screen when the Sign Up hyperlink is clicked, prompting for confirmation.
     *
     * @param actionEvent action event triggered by the Sign Up hyperlink.
     */
    public void handleSignUpHyperlinkAction(ActionEvent actionEvent) {
        WindowManager.openWindow("/ui/view/SignUp.fxml", "Sign Up");
    }
    
    public void handleResetHyperlinkAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Recovery");
        dialog.setHeaderText("Forgot your password?");
        dialog.setContentText("Please enter your email address:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String email = result.get();

            if (!email.contains("@")) {
                showErrorAlert("Invalid Email", "Please enter a valid email address.");
                return;
            }

            try {
                ManagerBean manager = ManagerFactory.get().getManagerByEmail(new GenericType<ManagerBean>() {}, email);
                if (manager ==null){
                    //aviso?
                    return;
                }

                ManagerFactory.get().resetPassword(manager);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Reset");
                alert.setHeaderText(null);
                alert.setContentText("A new password has been sent to your email.");
                alert.showAndWait();                

            } catch (WebApplicationException e) {
                showErrorAlert("Server Error", "An error occurred while processing your request.");
                logger.log(Level.SEVERE, "Server error during password reset", e);
            } catch (Exception e) {
                showErrorAlert("Error", "No account found with this email.");
                logger.log(Level.SEVERE, "Error finding user", e);
            }
        }
    }
    
    /**
     * Helper method to display an error alert with a custom title and message.
     *
     * @param title title of the alert dialog.
     * @param message message content of the alert dialog.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
