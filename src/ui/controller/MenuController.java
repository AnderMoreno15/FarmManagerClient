/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import DTO.AnimalGroupBean;
import DTO.ManagerBean;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.utilities.WindowManager;

/**
 * FXML Controller class
 *
 * @author Aitziber
 */
public class MenuController implements Initializable {

    private static final Logger logger = Logger.getLogger(MenuController.class.getName());
    
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miAnimal;
    @FXML
    private MenuItem miAnimalGroup;
    @FXML
    private MenuItem miConsume;
    @FXML
    private MenuItem miProduct;
    
    private static ManagerBean manager;
    
    /**
     * Sets the manager instance for this session.
     * 
     * @param manager the manager to be set
     */
    public static void setManager(ManagerBean manager) {
        MenuController.manager = manager;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        miAnimalGroup.setOnAction(event -> openView("/ui/view/AnimalGroup.fxml", "Animal Group"));
        miAnimal.setOnAction(event -> openView("/ui/view/Animal.fxml", "Animal"));
        miConsume.setOnAction(event -> openView("/ui/view/Consumes.fxml", "Consumes"));
        miProduct.setOnAction(event -> openView("/ui/view/Product.fxml", "Product"));
    }

    /**
     * Handles the window transition and hides the current stage.
     * 
     * @param fxmlPath the FXML file to load
     * @param title the title of the new window
     */
    private void openView(String fxmlPath, String title) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.hide();
            WindowManager.openWindowWithManager(fxmlPath, title, manager);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error opening {0} window: {1}", new Object[]{title, e.getMessage()});
        }
    }
}