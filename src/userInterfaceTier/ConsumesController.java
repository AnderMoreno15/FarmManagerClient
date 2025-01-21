package userInterfaceTier;

import DTO.ConsumesBean;
import DTO.ProductBean;
import DTO.AnimalGroupBean;
import userLogicTier.ConsumesRestClient;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Optional;
import java.util.List;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.WebApplicationException;

/**
 * Controller for the Consumes window management.
 * @author YourName
 */
public class ConsumesController {
    
    @FXML
    private TableView<ConsumesBean> tableConsumes;
    @FXML
    private TableColumn<ConsumesBean, String> columnName;
    @FXML
    private TableColumn<ConsumesBean, String> columnDescription;
    @FXML
    private TableColumn<ConsumesBean, Float> columnConsume;
    @FXML
    private TableColumn<ConsumesBean, Date> columnDate;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnCreate;
    @FXML
    private MenuItem itemOpen;
    @FXML
    private MenuItem itemDelete;
    @FXML
    private ComboBox<String> comboSearch;

    private final String PROHIBITED_CHARACTERS = "'\"\\;*%()<>|&,-/!?=";
    private Stage stage;
    private ConsumesRestClient consumesClient;
    private static final Logger LOGGER = Logger.getLogger("userInterfaceTier.ConsumesController");

    /**
     * Initializes the window.
     * @param root The FXML document graph.
     */
    public void initStage(Parent root) {
        try {
            LOGGER.info("Initializing Consumes window.");
            
            // Create scene and set stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Consumes Management");
            stage.setResizable(false);

            // Initialize REST client
            consumesClient = new ConsumesRestClient();

            // Initialize UI components
            initializeComponents();
            
            // Initialize table
            initializeTable();

            // Set up event handlers
            setupEventHandlers();

            // Load initial data
            loadTableData();

            // Show window
            stage.show();
            
            LOGGER.info("Consumes window initialized.");
        } catch (Exception e) {
            String errorMsg = "Error initializing window: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Initializes window components.
     */
    private void initializeComponents() {
        // Initialize ComboBox
        comboSearch.setItems(FXCollections.observableArrayList(
            "Product", "Animal Group", "Date"
        ));
        comboSearch.setValue("Product");
        comboSearch.setEditable(false);

        // Initialize search field
        searchField.setPromptText("Enter search text");
        searchField.setText("");

        // Initialize buttons
        btnSearch.setDisable(false);
        btnCreate.setDisable(false);

        // Initialize menu items
        itemDelete.setDisable(true);
        itemOpen.setDisable(true);
    }

    /**
     * Initializes the table and its columns.
     */
    private void initializeTable() {
        // Set up column cell value factories
        columnName.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        
        columnDescription.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAnimalGroup().getName()));
        
        columnConsume.setCellValueFactory(
            new PropertyValueFactory<>("consumeAmount"));
        
        columnDate.setCellValueFactory(
            new PropertyValueFactory<>("date"));

        // Set up selection listener
        tableConsumes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                boolean hasSelection = newVal != null;
                itemDelete.setDisable(!hasSelection);
                itemOpen.setDisable(!hasSelection);
            });
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        btnSearch.setOnAction(this::handleSearchAction);
        btnCreate.setOnAction(this::handleCreateAction);
        itemDelete.setOnAction(this::handleDeleteAction);
        itemOpen.setOnAction(this::handleOpenAction);
        stage.setOnCloseRequest(this::handleExitAction);
        
        comboSearch.setOnAction(this::handleComboSearchAction);
    }

    /**
     * Handles changes in the search combo box selection.
     */
    private void handleComboSearchAction(Event event) {
        String searchType = comboSearch.getValue();
        updateSearchFieldPrompt(searchType);
    }

    /**
     * Updates search field prompt based on search type selection.
     */
    private void updateSearchFieldPrompt(String searchType) {
        switch (searchType) {
            case "Product":
                searchField.setPromptText("Enter product name");
                break;
            case "Animal Group":
                searchField.setPromptText("Enter animal group name");
                break;
            case "Date":
                searchField.setPromptText("Enter date (dd/MM/yyyy)");
                break;
        }
    }

    /**
     * Handles the search action.
     */
    private void handleSearchAction(Event event) {
        try {
            LOGGER.info("Handling search action.");
            String searchText = searchField.getText().trim();
            String searchType = comboSearch.getValue();
            List<ConsumesBean> results;

            if (searchText.isEmpty()) {
                results = consumesClient.getAllConsumes(List.class);
            } else {
                switch (searchType) {
                    case "Product":
                        results = consumesClient.findConsumesByProduct(List.class, searchText);
                        break;
                    case "Animal Group":
                        results = consumesClient.findConsumesByAnimalGroup(List.class, searchText);
                        break;
                    case "Date":
                        results = consumesClient.getConsumesByDateFrom(List.class, searchText);
                        break;
                    default:
                        results = consumesClient.getAllConsumes(List.class);
                }
            }

            tableConsumes.setItems(FXCollections.observableArrayList(results));
            LOGGER.info("Search completed successfully.");

        } catch (WebApplicationException e) {
            String errorMsg = "Error performing search: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Handles the create action.
     */
    private void handleCreateAction(Event event) {
        try {
            LOGGER.info("Creating new consume.");
            ConsumesBean newConsume = new ConsumesBean();
            consumesClient.createConsume(newConsume);
            loadTableData();
            LOGGER.info("New consume created successfully.");
        } catch (WebApplicationException e) {
            String errorMsg = "Error creating consume: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Handles the delete action.
     */
    private void handleDeleteAction(Event event) {
        try {
            ConsumesBean selectedConsume = tableConsumes.getSelectionModel().getSelectedItem();
            if (selectedConsume != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this consume?",
                    ButtonType.OK, ButtonType.CANCEL);
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    consumesClient.deleteConsume(
                        selectedConsume.getConsumesId().getProductId().toString(),
                        selectedConsume.getConsumesId().getAnimalGroupId().toString()
                    );
                    loadTableData();
                    LOGGER.info("Consume deleted successfully.");
                }
            }
        } catch (WebApplicationException e) {
            String errorMsg = "Error deleting consume: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Handles the open action.
     */
    private void handleOpenAction(Event event) {
        // Implement if needed
        LOGGER.info("Open action not implemented yet.");
    }

    /**
     * Handles the exit action.
     */
    private void handleExitAction(Event event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?",
                ButtonType.OK, ButtonType.CANCEL);
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage.close();
            } else {
                event.consume();
            }
        } catch (Exception e) {
            String errorMsg = "Error closing window: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Loads table data from server.
     */
    private void loadTableData() {
        try {
            List<ConsumesBean> consumes = consumesClient.getAllConsumes(List.class);
            tableConsumes.setItems(FXCollections.observableArrayList(consumes));
            LOGGER.info("Table data loaded successfully.");
        } catch (WebApplicationException e) {
            String errorMsg = "Error loading data: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Shows error alert dialog.
     * @param message The error message to display.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
            message,
            ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Sets the stage for this controller.
     * @param stage The stage to set.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
