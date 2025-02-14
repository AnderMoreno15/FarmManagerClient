/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.help;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Aitziber
 */
public class AnimalHelpController {
    @FXML
    private WebView webView;

    public void initAndShowStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestion de Animales");
        stage.setResizable(false);
        stage.setMinWidth(1024);
        stage.setMinHeight(720);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }
    private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        //Load help page.
        webEngine.load(getClass()
                .getResource("/ui/help/animalHelp.html").toExternalForm());
    }
}
    