package ui.controller;

import DTO.ConsumesBean;
import DTO.ProductBean;
import DTO.AnimalGroupBean;
import DTO.ConsumesIdBean;
import DTO.ManagerBean;
import businessLogic.animalGroup.AnimalGroupFactory;
import ui.cellFactories.DatePickerTableCell;
import java.net.URL;
import java.util.ArrayList;
import businessLogic.consumes.ConsumesRestClient;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Optional;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import ui.controller.AnimalController;
import businessLogic.consumes.ConsumesManagerFactory;
import businessLogic.consumes.IConsumesManager;
import businessLogic.product.ProductManagerFactory;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javax.ws.rs.ClientErrorException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Controller class for managing the "Consumes" window in the application. Handles user interactions such as searching, adding, and deleting consume records. Provides methods for managing the view and controlling business logic interactions related to consume operations. Implements {@link Initializable} to initialize the controller and its elements.
 *
 * @author YourName
 */
public class ConsumesController implements Initializable {

    /**
     * Table view displaying the consume records.
     */
    @FXML
    private TableView<ConsumesBean> tableConsumes;

    /**
     * Table column displaying the associated animal group for each consume record.
     */
    @FXML
    private TableColumn<ConsumesBean, AnimalGroupBean> tcAnimalGroup;

    /**
     * Table column displaying the associated product for each consume record.
     */
    @FXML
    private TableColumn<ConsumesBean, ProductBean> tcProduct;

    /**
     * Table column displaying the amount consumed for each consume record.
     */
    @FXML
    private TableColumn<ConsumesBean, Float> tcConsumeAmount;

    /**
     * Table column displaying the date of consumption for each consume record.
     */
    @FXML
    private TableColumn<ConsumesBean, Date> tcDate;

    /**
     * Text field for the user to input a search query.
     */
    @FXML
    private TextField searchField;

    /**
     * Button that triggers the search operation.
     */
    @FXML
    private Button btnSearch;

    /**
     * Button that allows the user to add a new consume record.
     */
    @FXML
    private Button btnAdd;

    /**
     * Menu item to delete a selected consume record.
     */
    @FXML
    private MenuItem itemDelete;

    /**
     * DatePicker for selecting the "From" date in search queries.
     */
    @FXML
    private DatePicker dpSearchFrom;

    /**
     * DatePicker for selecting the "To" date in search queries.
     */
    @FXML
    private DatePicker dpSearchTo;

    /**
     * ComboBox for selecting a search criterion.
     */
    @FXML
    private ComboBox<String> comboSearch;

    /**
     * HBox containing the DatePicker components.
     */
    @FXML
    private HBox hboxDatePicker;

    /**
     * StackPane for managing the layout of the consume records table.
     */
    @FXML
    private StackPane stack;

    /**
     * The ID of the manager associated with the current session.
     */
    private String managerId;

    /**
     * The current stage for the consumes view window.
     */
    private Stage stage;

    /**
     * The client used to interact with the backend consume-related services.
     */
    private ConsumesRestClient consumesClient;

    /**
     * Logger used for logging any actions or errors within the controller.
     */
    private static final Logger LOGGER = Logger.getLogger("ConsumesController");

    /**
     * Initializes the window and prepares the necessary components and data for the "Consumes" window. 
     * This method is invoked automatically when the FXML is loaded, setting up the RestClient, 
     * UI components, event handlers, and initial data.
     *
     * <p>
     * It performs the following actions:</p>
     * <ul>
     * <li>Initializes the RestClient and sets up the Manager's ID.</li>
     * <li>Initializes UI components such as buttons, tables, and other controls.</li>
     * <li>Sets up event handlers for user interactions with the window.</li>
     * <li>Loads and displays all consume records from the backend system.</li>
     * <li>Shows the stage (window) to the user.</li>
     * </ul>
     *
     * <p>
     * If any error occurs during the initialization, an error message is displayed and logged.</p>
     *
     * @param url The location used to resolve relative paths for the root object (usually <code>null</code>).
     * @param rb The resource bundle used to localize the root object (usually <code>null</code>).
     */
    public void initialize(URL url, ResourceBundle rb) {

        //Initialize RestClient
        try {
            consumesClient = new ConsumesRestClient();
            ManagerBean manager = new ManagerBean();
            managerId = String.valueOf(manager.getId());
        } catch (Exception e) {
            String errorMsg = "Error initializing Rest: " + e + consumesClient;
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }

        try {

            // Initialize UI components
            initializeComponents();

            // Initialize table
            initializeTable();

        } catch (Exception e) {
            // Handle and log any errors that occur during the initialization of the RestClient
            String errorMsg = "Error initializing Rest: " + e + consumesClient;
            showErrorAlert(errorMsg); // Display error message to the user
            LOGGER.log(Level.SEVERE, errorMsg); // Log the error details for debugging
        }

        try {
            // Initialize UI components (e.g., buttons, fields, etc.)
            initializeComponents();

            // Initialize the table to display the consume records
            initializeTable();

            // Set up event handlers (e.g., button clicks, table actions)
            setupEventHandlers();

            // Load and display all consume records from the backend system
            showAllConsumes();

            // Show the window to the user
            stage.show();

            // Log the successful initialization of the window
            LOGGER.info("Consumes window initialized.");

        } catch (Exception e) {
            // In case of any errors during window initialization, capture and log the stack trace
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            StringBuilder traceBuilder = new StringBuilder();
            for (StackTraceElement element : stackTraceElements) {
                traceBuilder.append(element.toString()).append("\n");
            }
            String errorMsg = "Error initializing window: \n" + traceBuilder.toString();

            // Output the error message for debugging
            System.err.println(errorMsg); // Or use a logger if preferred
        }
    }

    /**
     * Initializes the window components including the ComboBox, search field, buttons, and menu items. This method configures the basic UI elements and sets up their default states. It also handles the visibility of certain UI components based on the initial setup.
     *
     * <p>
     * This method performs the following actions:</p>
     * <ul>
     * <li>Initializes and populates the ComboBox for search criteria with values like "Animal Group", "Product", and "Date".</li>
     * <li>Sets a default value for the ComboBox and ensures it is not editable.</li>
     * <li>Configures the search field with a prompt text to guide the user on its expected input.</li>
     * <li>Sets up the "Search" button as enabled and the default action button for the UI.</li>
     * <li>Disables the "Delete" menu item, ensuring it is inactive initially.</li>
     * <li>Hides the date pickers used for filtering the records by date.</li>
     * </ul>
     *
     * <p>
     * If an error occurs during the initialization, it is logged and displayed to the user.</p>
     *
     * @throws Exception if any error occurs while initializing components.
     */
    private void initializeComponents() {
        try {
            // Initialize ComboBox for search criteria
            LOGGER.info("Initializing ComboBox...");
            comboSearch.getItems().addAll("Animal Group", "Product", "Date");
            comboSearch.setValue("Animal Group");
            comboSearch.setEditable(false);
            LOGGER.info("ComboBox initialized successfully.");

            // Initialize search field with placeholder text
            LOGGER.info("Initializing search field...");
            searchField.setPromptText("Enter search text");
            LOGGER.info("Search field initialized successfully.");

            // Set default value for ComboBox
            LOGGER.info("Setting ComboBox value...");
            comboSearch.setValue("Animal Group");
            LOGGER.info("ComboBox value set to 'Animal Group'.");

            // Initialize the search button
            LOGGER.info("Initializing buttons...");
            btnSearch.setDisable(false);
            btnSearch.setDefaultButton(true);
            LOGGER.info("Buttons initialized successfully.");

            // Initialize menu items and set the delete item as disabled initially
            LOGGER.info("Initializing menu items...");
            itemDelete.setDisable(true);
            LOGGER.info("Menu items initialized successfully.");

            // Hide the date pickers initially
            LOGGER.info("Hiding date pickers...");
            dpSearchFrom.setVisible(false);
            dpSearchTo.setVisible(false);
            LOGGER.info("Date pickers hidden successfully.");

        } catch (Exception e) {
            // Log and display error message in case of an exception
            String errorMsg = "Error initializing components: " + e;
            showErrorAlert(errorMsg); // Display error to user
            LOGGER.log(Level.SEVERE, errorMsg); // Log the error for debugging
        }
    }

    /**
     * Initializes the table and sets up each column with appropriate cell factories, data bindings, and edit commit handlers. This method configures the table for displaying consume records, with each column bound to a specific property of the `ConsumesBean` model. It also loads data for the columns (such as animal groups, products, and consume amounts) and sets up event handlers to allow editing of the table's contents.
     *
     * <p>
     * This method performs the following actions:</p>
     * <ul>
     * <li>Configures the "Animal Group" column by fetching data for animal groups and setting up a ComboBox for selection.</li>
     * <li>Sets up the "Product" column by fetching available products and using a ComboBox for product selection.</li>
     * <li>Configures the "Consume Amount" column to allow numeric input and binds it to the corresponding property of `ConsumesBean`.</li>
     * <li>Sets up the "Date" column with a custom DatePicker cell that allows users to pick and update the date for each consume record.</li>
     * </ul>
     *
     * <p>
     * If an error occurs while setting up any of the columns, it is logged and handled appropriately.</p>
     *
     * @throws Exception if any error occurs while initializing the table or setting up the columns.
     */
    private void initializeTable() {
        // Initialize the Animal Group column
        tableConsumes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            LOGGER.info("Setting up AnimalGroup column...");
            tcAnimalGroup.setCellValueFactory(new PropertyValueFactory<>("animalGroup"));

            List<AnimalGroupBean> animalGroupList = new ArrayList<AnimalGroupBean>();
            LOGGER.info("Fetching animal groups for manager...");
            animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {
            }, managerId);

            ObservableList<AnimalGroupBean> animalGroupData = FXCollections.observableArrayList(animalGroupList);
            LOGGER.info("Animal groups fetched, setting up ComboBox cell...");
            tcAnimalGroup.setCellFactory(ComboBoxTableCell.forTableColumn(animalGroupData));

            tcAnimalGroup.setOnEditCommit(event -> {
                LOGGER.info("AnimalGroup edit committed: " + event.getNewValue());
                handleEditCommit(event, "animalGroup");
            });
        } catch (Exception e) {
            String errorMsg = "Error initializing animal group column: \n";
            handleException(e); // Handle error
            LOGGER.log(Level.SEVERE, errorMsg);
        }

        // Initialize the Product column
        try {
            LOGGER.info("Setting up Product column...");
            tcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));

            List<ProductBean> productList = new ArrayList<ProductBean>();
            LOGGER.info("Fetching products...");
            productList = ProductManagerFactory.get().getAllProducts(new GenericType<List<ProductBean>>() {
            });

            ObservableList<ProductBean> productData = FXCollections.observableArrayList(productList);
            LOGGER.info("Products fetched, setting up ComboBox cell...");
            tcProduct.setCellFactory(ComboBoxTableCell.forTableColumn(productData));

            tcProduct.setOnEditCommit(event -> {
                LOGGER.info("Product edit committed: " + event.getNewValue());
                handleEditCommit(event, "product");
            });
        } catch (Exception e) {
            String errorMsg = "Error setting up product column: \n";
            handleException(e); // Handle error
            LOGGER.log(Level.SEVERE, errorMsg);
        }

        // Initialize the Consume Amount column
        try {
            LOGGER.info("Setting up ConsumeAmount column...");
            tcConsumeAmount.setCellValueFactory(new PropertyValueFactory<>("consumeAmount"));

            tcConsumeAmount.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
                public String toString(Float value) {
                    return value != null ? value.toString() : "";
                }

                public Float fromString(String string) {
                    try {
                        return Float.parseFloat(string);
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid input for consume amount: " + string, e);
                        return 0.0f; // Default value in case of invalid input
                    }
                }
            }));

            tcConsumeAmount.setOnEditCommit(event -> {
                LOGGER.info("ConsumeAmount edit committed: " + event.getNewValue());
                handleEditCommit(event, "consumeAmount");
            });
        } catch (Exception e) {
            String errorMsg = "Error setting up consume column: \n";
            handleException(e); // Handle error
            LOGGER.log(Level.SEVERE, errorMsg);
        }

        // Initialize the Date column with a custom DatePicker cell
        try {
            LOGGER.info("Setting up Date column...");
            tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));

            // Set up the custom DatePicker cell for the date column
            tcDate.setCellFactory(new Callback<TableColumn<ConsumesBean, Date>, TableCell<ConsumesBean, Date>>() {
                @Override
                public TableCell<ConsumesBean, Date> call(TableColumn<ConsumesBean, Date> param) {
                    LOGGER.info("Creating custom DatePicker table cell...");
                    DatePickerTableCell<ConsumesBean> cell = new DatePickerTableCell<>(param);
                    cell.updateDateCallback = (Date updatedDate) -> {
                        try {
                            LOGGER.info("Updating consume date: " + updatedDate);
                            updateConsumeDate(updatedDate);
                        } catch (CloneNotSupportedException ex) {
                            LOGGER.log(Level.SEVERE, "Error updating consume date", ex);
                        }
                    };
                    return cell;
                }
            });

            tcDate.setStyle("-fx-alignment: center;");
            LOGGER.info("Date column setup complete.");
        } catch (Exception e) {
            String errorMsg = "Error setting up date column: \n";
            handleException(e); // Handle error
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    /**
     * Sets up event handlers for all the UI components on the window. This method configures actions for buttons, combo boxes, and table selections.
     */
    private void setupEventHandlers() {
        LOGGER.info("Setting up search button action...");
        btnSearch.setOnAction(this::handleSearchAction);
        LOGGER.info("Search button clicked");

        LOGGER.info("Setting up add button action...");
        btnAdd.setOnAction(this::handleCreateAction);
        LOGGER.info("Add button clicked");

        LOGGER.info("Setting up combo box action...");
        comboSearch.setOnAction(this::handleComboSearchAction);
        LOGGER.info("Search combo box selection changed to: " + comboSearch.getValue());

        LOGGER.info("Setting up Delete item.");
        itemDelete.setDisable(false);
        itemDelete.setOnAction(this::handleDeleteAction);

        tableConsumes.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ConsumesBean>) change -> {
            itemDelete.setDisable(tableConsumes.getSelectionModel().getSelectedItems().isEmpty());
            LOGGER.info("Item delete launched.");
        });

        LOGGER.info("Setting up selection listener for table...");
        tableConsumes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    boolean hasSelection = newVal != null;
                    itemDelete.setDisable(!hasSelection);
                    LOGGER.info("Selection changed: " + (hasSelection ? "Item selected" : "No item selected"));
                });

        tableConsumes.setEditable(true);
    }

    /**
     * Handles changes in the combo box selection for the search criteria. Updates the prompt text in the search field based on the selected search type.
     *
     * @param event The action event triggered by the combo box selection change.
     */
    private void handleComboSearchAction(Event event) {
        String searchType = comboSearch.getValue();
        updateSearchFieldPrompt(searchType);
    }

    /**
     * Updates the prompt text for the search field based on the selected search type. The prompt will change based on whether the search type is "Product", "Animal Group", or "Date".
     *
     * @param searchType The type of search selected (Product, Animal Group, or Date).
     */
    private void updateSearchFieldPrompt(String searchType) {
        switch (searchType) {
            case "Product":
                dpSearchFrom.setVisible(false);
                dpSearchTo.setVisible(false);
                searchField.setVisible(true);
                searchField.setPromptText("Enter product name");
                break;
            case "Animal Group":
                dpSearchFrom.setVisible(false);
                dpSearchTo.setVisible(false);
                searchField.setVisible(true);
                searchField.setPromptText("Enter animal group name");
                break;
            case "Date":
                dpSearchFrom.setVisible(true);
                dpSearchTo.setVisible(true);
                searchField.setVisible(false);
                break;
        }
    }

    /**
     * Handles the search action when the search button is clicked. It performs a search based on the selected criteria (Product, Animal Group, Date) and displays the results in the table. If no search criteria is provided, it retrieves and displays all consumption records.
     *
     * @param event The action event triggered by the search button click.
     */
    private void handleSearchAction(ActionEvent event) {
        try {
            LOGGER.info("Handling search action.");

            String searchText = searchField.getText().trim();
            String searchType = comboSearch.getValue();

            List<ConsumesBean> consumesList = null;

            if (!searchText.isEmpty()) {
                switch (searchType) {
                    case "Product":
                        consumesList = ConsumesManagerFactory.get().findConsumesByProduct(new GenericType<List<ConsumesBean>>() {
                        }, searchText);
                        break;

                    case "Animal Group":
                        consumesList = ConsumesManagerFactory.get().findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {
                        }, searchText);
                        break;

                    case "Date":
                        String from = (dpSearchFrom.getValue() != null && !dpSearchFrom.getValue().toString().isEmpty()) ? dpSearchFrom.getValue().toString() : null;
                        String to = (dpSearchTo.getValue() != null && !dpSearchTo.getValue().toString().isEmpty()) ? dpSearchTo.getValue().toString() : null;

                        if (from != null && to != null) {
                            consumesList = ConsumesManagerFactory.get().getConsumesByDate(new GenericType<List<ConsumesBean>>() {
                            }, from, to);
                        } else if (from != null) {
                            consumesList = ConsumesManagerFactory.get().getConsumesByDateFrom(new GenericType<List<ConsumesBean>>() {
                            }, from);
                        } else if (to != null) {
                            consumesList = ConsumesManagerFactory.get().getConsumesByDateTo(new GenericType<List<ConsumesBean>>() {
                            }, to);
                        } else {
                            showAllConsumes(); // Method to display all consumption records.
                        }
                        break;

                    default:
                        LOGGER.warning("Unrecognized search type.");
                        consumesList = consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {
                        });
                        break;
                }
            } else {
                consumesList = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {
                });
            }

            if (consumesList != null && !consumesList.isEmpty()) {
                ObservableList<ConsumesBean> consumesData = FXCollections.observableArrayList(consumesList);
                tableConsumes.setItems(consumesData);
                btnAdd.setDisable(false);
                LOGGER.info("Search completed successfully.");
            } else {
                System.out.println("No consumptions found for the search.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Handles the commit of an edit in the table's columns (e.g., when a user changes a value in a table cell). Validates the new value and updates the underlying data model accordingly.
     *
     * @param event The cell edit event triggered by editing a table cell.
     * @param fieldName The field name to which the edited value should be applied (e.g., "consumeAmount", "animalGroup", "product").
     */
    private <T> void handleEditCommit(TableColumn.CellEditEvent<ConsumesBean, T> event, String fieldName) {
        try {
            TablePosition<ConsumesBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("The entered value is invalid.");
            }

            int row = pos.getRow();
            ConsumesBean consume = event.getTableView().getItems().get(row);
            ConsumesBean consumeCopy = (ConsumesBean) consume.clone();

            switch (fieldName) {
                case "consumeAmount":
                    if (newValue instanceof Number) {
                        consumeCopy.setConsumeAmount(((Number) newValue).floatValue());
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setConsumeAmount(((Number) newValue).floatValue());
                    }
                    break;

                case "animalGroup":
                    if (newValue instanceof AnimalGroupBean) {
                        consumeCopy.setAnimalGroup((AnimalGroupBean) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setAnimalGroup((AnimalGroupBean) newValue);
                    }
                    break;

                case "product":
                    if (newValue instanceof ProductBean) {
                        consumeCopy.setProduct((ProductBean) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setProduct((ProductBean) newValue);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unknown field: " + fieldName);
            }

            event.getTableView().refresh();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
            handleException(e);
        }
    }

    /**
     * Updates the consumption date for the currently selected item in the table. The method creates a clone of the selected `ConsumesBean` item, sets the updated date on the clone, and then updates the original item in the backend and the table.
     *
     * @param updatedDate The updated date to set for the consumption.
     * @throws CloneNotSupportedException If cloning the `ConsumesBean` object fails.
     */
    private void updateConsumeDate(Date updatedDate) throws CloneNotSupportedException {
        ConsumesBean consume = tableConsumes.getSelectionModel().getSelectedItem();
        if (consume != null && updatedDate != null) {
            ConsumesBean consumeCopy = (ConsumesBean) consume.clone();
            consumeCopy.setDate(updatedDate);
            try {
                ConsumesManagerFactory.get().updateConsume(consumeCopy);
                consume.setDate(updatedDate);
            } catch (WebApplicationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the create action. This method is invoked when a user clicks the "Add" button. It creates a new `ConsumesBean` object with initial values, persists it via the backend, and adds it to the table for display. The newly added item is also edited in the column "Quantity".
     *
     * @param event The action event triggered by clicking the "Add" button.
     */
    private void handleCreateAction(ActionEvent event) {
        // Create a new ConsumesBean with initial values
        ConsumesBean newConsume = new ConsumesBean();
        newConsume.setProduct(null);  // Null product
        newConsume.setAnimalGroup(null);  // Null animal group
        newConsume.setConsumeAmount(0f);  // Initial amount set to 0
        newConsume.setDate(new Date());  // Set the current date

        // Create the new consumption in the database
        try {
            ConsumesManagerFactory.get().createConsume(newConsume);
        } catch (WebApplicationException e) {
            System.err.println("Error creating consume: " + e.getMessage());
            handleException(e);
            return;
        }

        // Add the new consumption to the table
        tableConsumes.getItems().add(newConsume);
        tableConsumes.refresh();

        // Set the "Quantity" column to edit mode for the newly added item
        final int NEW_CONSUME_ROW = tableConsumes.getItems().size() - 1; // Last added row
        Platform.runLater(() -> tableConsumes.edit(NEW_CONSUME_ROW, tcAnimalGroup));
    }

    /**
     * Handles the delete action. This method is invoked when the user clicks the "Delete" button. It confirms the deletion of selected consumption items and removes them from both the backend and the table.
     *
     * @param event The action event triggered by clicking the "Delete" button.
     */
    private void handleDeleteAction(ActionEvent event) {
        ObservableList<ConsumesBean> selectedConsumes = tableConsumes.getSelectionModel().getSelectedItems();
        List<ConsumesBean> successfullyDeleted = new ArrayList<>();

        // If no items are selected, show a warning
        if (selectedConsumes.isEmpty()) {
            Platform.runLater(() -> {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING, "No consumes selected for deletion.", ButtonType.OK);
                warningAlert.showAndWait();
            });
            return;
        }

        // Show a confirmation alert
        Platform.runLater(() -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected consumes?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                for (ConsumesBean selectedConsume : selectedConsumes) {
                    try {
                        String productId = selectedConsume.getProduct().getId().toString();  // Product ID
                        String animalGroupId = selectedConsume.getAnimalGroup().getId().toString();  // Animal Group ID

                        ConsumesManagerFactory.get().deleteConsume(productId, animalGroupId);
                        successfullyDeleted.add(selectedConsume);
                    } catch (WebApplicationException e) {
                        System.err.println("Error deleting consume ID " + selectedConsume.getConsumesId() + ": " + e.getMessage());
                        handleException(e);
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                                    "Unexpected error during deletion: " + e.getMessage(),
                                    ButtonType.OK);
                            errorAlert.showAndWait();
                        });
                    }
                }

                if (!successfullyDeleted.isEmpty()) {
                    Platform.runLater(() -> {
                        // Remove successfully deleted items from the table
                        tableConsumes.getItems().removeAll(successfullyDeleted);
                        tableConsumes.getSelectionModel().clearSelection();
                        tableConsumes.refresh();
                    });
                }
            }
        });
    }

    /**
     * Fetches and displays all consumes from the backend. If no consumes are found, it shows an informational alert. If any error occurs while fetching the data, it shows an error alert.
     */
    private void showAllConsumes() {
        try {
            LOGGER.info("Fetching all consumes...");
            List<ConsumesBean> allConsumes = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {
            });

            if (allConsumes != null && !allConsumes.isEmpty()) {
                LOGGER.info("Consumes fetched successfully. Total items: " + allConsumes.size());
                ObservableList<ConsumesBean> consumesData = FXCollections.observableArrayList(allConsumes);
                tableConsumes.setItems(consumesData);
                btnAdd.setDisable(false);
                LOGGER.info("Table updated with consumes data. Add button enabled.");
            } else {
                LOGGER.info("No consumes found.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No tiene consumos asociados", ButtonType.OK);
                alert.showAndWait();
            }

        } catch (WebApplicationException e) {
            LOGGER.severe("Error occurred while fetching consumes: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los consumos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Shows an error alert with the specified message.
     *
     * @param message The error message to display in the alert.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Handles exceptions by displaying the stack trace of the exception and logging the error details.
     *
     * @param e The exception to handle.
     */
    private void handleException(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder traceBuilder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements) {
            traceBuilder.append(element.toString()).append("\n");
        }

        String errorMsg = "Error in search Action Handler: \n" + traceBuilder.toString();
        System.err.println(errorMsg);
    }

    /**
     * Handles the print action. It generates a report based on the current table data and displays it in a JasperViewer window.
     *
     * @param event The action event triggered by clicking the "Print" button.
     */
    private void handlePrintAction(ActionEvent event) {
        try {
            LOGGER.info("Beginning printing action...");

            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/ui/reports/ConsumesReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<ConsumesBean>) this.tableConsumes.getItems());
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            showErrorAlert("Error al imprimir:\n" + ex.getMessage());
        }
    }
}
