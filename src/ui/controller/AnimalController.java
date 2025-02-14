
package ui.controller;

import DTO.AnimalBean;
import DTO.AnimalGroupBean;
import DTO.ManagerBean;
import DTO.SpeciesBean;
import ui.cellFactories.DatePickerTableCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import businessLogic.animalGroup.AnimalGroupFactory;
import businessLogic.animal.AnimalManagerFactory;
import businessLogic.species.SpeciesManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.util.Collection;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import net.sf.jasperreports.engine.JasperPrint;
import ui.help.AnimalHelpController;


/**
 * AnimalController manages the interactions between the user interface and the backend logic for managing animals.
 * It allows for searching, editing, deleting and adding animals. It also handles the printing of animal data reports.
 *
 * @author Aitziber
 */
public class AnimalController implements Initializable {
    
    /**
     * Logger instance for logging events and errors.
     */
    private static final Logger logger = Logger.getLogger(AnimalController.class.getName());
    
    /**
     * The stage associated with the current window.
     */
    private Stage stage;
    
     /**
     * ComboBox for selecting the search criteria (Subespecies, Animal Group, Birthdate).
     */
    @FXML
    private ComboBox<String> comboSearch;
    
     /**
     * TextField for entering search queries.
     */
    @FXML
    private TextField tfSearch;
    
     /**
     * Button to initiate the search action.
     */
    @FXML
    private Button btnSearch;

    /**
     * Button to add a new animal.
     */
    @FXML
    private Button btnAdd;
    
    /**
     * Button to print animal data.
     */
    @FXML
    private Button btnPrint;
    
    /**
     * Button to open the help view.
     */
    @FXML
    private Button btnHelp;
    /**
     * Text for the "From" label when searching by date range.
     */
    @FXML
    private Text txtFrom;
    
    /**
     * Text for the "To" label when searching by date range.
     */
    @FXML
    private Text txtTo;
    
     /**
     * DatePicker for selecting the start date of the search.
     */
    @FXML
    private DatePicker dpSearchFrom;

     /**
     * DatePicker for selecting the end date of the search.
     */
    @FXML
    private DatePicker dpSearchTo;
    
    /**
     * TableView for displaying a list of animals.
     */
    @FXML
    private TableView<AnimalBean> tbAnimal;
    
    /**
     * TableColumn for the animal name.
     */
    @FXML
    private TableColumn<AnimalBean,String> tcName;
    
    /**
     * TableColumn for the animal birthdate.
     */
    @FXML
    private TableColumn<AnimalBean,Date> tcBirthdate;
    
    /**
     * TableColumn for the animal group.
     */
    @FXML
    private TableColumn<AnimalBean, AnimalGroupBean> tcAnimalGroup;
    
    /**
     * TableColumn for the animal subespecies.
     */
    @FXML
    private TableColumn<AnimalBean,String> tcSubespecies;
    
    /**
     * TableColumn for the animal species.
     */
    @FXML
    private TableColumn<AnimalBean, SpeciesBean> tcSpecies;
    
    /**
     * MenuItem for deleting selected animals.
     */
    @FXML
    private MenuItem miDelete;
    
    /**
     * HBox container for the date picker components.
     */
    @FXML
    private HBox hboxDatePicker;
    
     /**
     * ManagerBean representing the current user managing the animals.
     */
    private static ManagerBean manager;
    
    /**
     * Sets the manager for this controller.
     *
     * @param manager The ManagerBean to set.
     */
    public static void setManager(ManagerBean manager) {
        AnimalController.manager = manager;
    }

    /**
     * Gets the current manager.
     *
     * @return The ManagerBean instance.
     */
    public static ManagerBean getManager() {
        return manager;
    }
    
    /**
     * AnimalGroupBean representing the group for filtering animals.
     */
    private static AnimalGroupBean conditionalAnimalGroup;

    /**
     * Sets the conditional animal group for filtering.
     *
     * @param conditionalAnimalGroup The AnimalGroupBean used for filtering.
     */
    public static void setConditionalAnimalGroup(AnimalGroupBean conditionalAnimalGroup) {
        AnimalController.conditionalAnimalGroup = conditionalAnimalGroup;
    }
    
    /**
     * Gets the conditional animal group.
     *
     * @return The AnimalGroupBean instance used for conditional filtering.
     */
    public static AnimalGroupBean getConditionalAnimalGroup() {
        return conditionalAnimalGroup;
    }
    
    /**
     * Initializes the controller class, setting up the interface components and event handlers.
     * 
     * @param url The location of the FXML file that is being loaded.
     * @param rb  The resource bundle used to localize the controller's text.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        logger.info("Initializing AnimalController...");
        // Initialize table selection model and other UI components
        
        // Allow multiple row selection
        tbAnimal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // Load items into the search combo box
        comboSearch.getItems().addAll("Subespecies", "Animal Group", "Birthdate");

        // Select "Animal Group" by default
        comboSearch.setValue("Animal Group");
        
       // Listen for changes in ComboBox
        comboSearch.valueProperty().addListener(this::handleComboBoxChange);

        // Clear tfSearch
        tfSearch.setText("");
        tfSearch.toFront();

        showDateFields(false);
        
        // Enable the search button
        btnSearch.setDisable(false);

        // Set "Search" as the default button
        btnSearch.setDefaultButton(true);
        btnSearch.setOnAction(this::onSearchButtonClicked);  

        // Initialize table columns and their cell factories
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcName.setCellFactory(TextFieldTableCell.<AnimalBean>forTableColumn());
        tcName.setOnEditCommit(event -> handleEditCommit(event, "name"));

        tcBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        tcBirthdate.setCellFactory(new Callback<TableColumn<AnimalBean, Date>, TableCell<AnimalBean, Date>>() {
            @Override
            public TableCell<AnimalBean, Date> call(TableColumn<AnimalBean, Date> param) {
                DatePickerTableCell<AnimalBean> cell = new DatePickerTableCell<>(param);
                cell.updateDateCallback = (Date updatedDate) -> {
                    try {
                        updateAnimalBirthdate(updatedDate);
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(AnimalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
                return cell;
            }
        });
        tcBirthdate.setStyle("-fx-alignment: center;");

        tcAnimalGroup.setCellValueFactory(new PropertyValueFactory<>("animalGroup"));
        List<AnimalGroupBean> animalGroupList = new ArrayList<AnimalGroupBean>();
        animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, String.valueOf(manager.getId())); 

        ObservableList<AnimalGroupBean> animalGroupData = FXCollections.observableArrayList(animalGroupList);
        tcAnimalGroup.setCellFactory(ComboBoxTableCell.forTableColumn(animalGroupData));
        tcAnimalGroup.setOnEditCommit(event -> handleEditCommit(event, "animalGroup"));

        tcSubespecies.setCellValueFactory(new PropertyValueFactory<>("subespecies"));
        tcSubespecies.setCellFactory(TextFieldTableCell.<AnimalBean>forTableColumn());
        tcSubespecies.setOnEditCommit(event -> handleEditCommit(event, "subespecies"));

        tcSpecies.setCellValueFactory(new PropertyValueFactory<>("species"));
        List<SpeciesBean> speciesList = new ArrayList<SpeciesBean>();

        speciesList = SpeciesManagerFactory.get().getAllSpecies(new GenericType<List<SpeciesBean>>() {});     
        ObservableList<SpeciesBean> speciesData = FXCollections.observableArrayList(speciesList);
        tcSpecies.setCellFactory(ComboBoxTableCell.forTableColumn(speciesData));
        tcSpecies.setOnEditCommit(event -> handleEditCommit(event, "species"));

        miDelete.setDisable(true);
        tbAnimal.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalBean>) change -> {
            miDelete.setDisable(tbAnimal.getSelectionModel().getSelectedItems().isEmpty());
        });
        miDelete.setOnAction(this::onDeleteMenuItemClicked);

        btnAdd.setOnAction(this::onAddButtonClicked);

        tbAnimal.setEditable(true);

        if (this.conditionalAnimalGroup != null) {
            tfSearch.setText(conditionalAnimalGroup.getName());
            btnSearch.fire();
        } else{
            showAllAnimals();
        }

        btnPrint.setOnAction(this::handlePrintAction); 
        btnHelp.setOnAction(this::handleHelpAction);
    }
    
    /**
    * Handles ComboBox value change to adjust the UI components accordingly.
    * 
    * @param observable The observable value of the ComboBox.
    * @param oldValue The previous value of the ComboBox.
    * @param newValue The new value of the ComboBox.
    */
    private void handleComboBoxChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if ("Birthdate".equals(newValue)) {
            // Hide the text search field
            tfSearch.toBack();
            tfSearch.setVisible(false);
            // Show the date fields
            showDateFields(true);
        } else if ("Subespecies".equals(newValue) || "Animal Group".equals(newValue)) {
            // Hide the date fields
            showDateFields(false);
            // Show the text search field
            tfSearch.clear();
            tfSearch.toFront();
            tfSearch.setVisible(true);
        }
    }
    
    /**
    * Shows or hides the date fields in the UI based on the given boolean flag.
    * 
    * @param show Indicates whether the date fields should be shown.
    */
    private void showDateFields(boolean show){
        if (show) {
            hboxDatePicker.toFront();
        } else {
            hboxDatePicker.toBack();
        }
        txtFrom.setVisible(show);
        txtTo.setVisible(show);
        dpSearchFrom.setVisible(show);
        dpSearchTo.setVisible(show);
    }
    
    /**
    * Handles the action of the search button.
    * 
    * @param event The event triggered when the search button is clicked.
    */
    private void onSearchButtonClicked(ActionEvent event) {
        try {
            String searchType = comboSearch.getValue();
            List<AnimalBean> animalList = null;

            switch (searchType) {
                case "Animal Group":
                    if(tfSearch.getText() != null && !tfSearch.getText().isEmpty()){
                        logger.log(Level.INFO, "Searching animals by animal group: {0}", tfSearch.getText());
                        animalList = AnimalManagerFactory.get().getAnimalsByAnimalGroup(new GenericType<List<AnimalBean>>() {}, tfSearch.getText(), String.valueOf(manager.getId()));
                    }
                    else{
                        logger.info("No animal group specified. Fetching all animals.");
                        showAllAnimals();
                    }
                    break;
                case "Subespecies":
                    if(tfSearch.getText() != null && !tfSearch.getText().isEmpty()){
                        logger.log(Level.INFO, "Searching animals by subspecies: {0}", tfSearch.getText());
                        animalList = AnimalManagerFactory.get().getAnimalsBySubespecies(new GenericType<List<AnimalBean>>() {}, tfSearch.getText(), String.valueOf(manager.getId()));
                    }
                    else{
                        logger.info("No subspecies specified. Fetching all animals.");
                        showAllAnimals();
                    }
                    break;
                case "Birthdate":
                    LocalDate fromDate = dpSearchFrom.getValue();
                    LocalDate toDate = dpSearchTo.getValue();

                    if (fromDate == null && toDate == null) {
                        logger.info("No date range specified. Fetching all animals.");
                        showAllAnimals();
                    } else {
                        boolean fromDateValid = isValidDate(fromDate);
                        boolean toDateValid = isValidDate(toDate);

                        if (fromDateValid && toDateValid) {
                            logger.log(Level.INFO, "Searching animals born between {0} and {1}", new Object[]{fromDate, toDate});
                            animalList = AnimalManagerFactory.get().getAnimalsByBirthdate(new GenericType<List<AnimalBean>>() {}, fromDate.toString(), toDate.toString(), String.valueOf(manager.getId()));
                        } else if (fromDateValid) {
                            logger.log(Level.INFO, "Searching animals born from {0} onwards.", fromDate);
                            animalList = AnimalManagerFactory.get().getAnimalsByBirthdateFrom(new GenericType<List<AnimalBean>>() {}, fromDate.toString(), String.valueOf(manager.getId()));
                        } else if (toDateValid) {
                            logger.log(Level.INFO, "Searching animals born before {0}", toDate);
                            animalList = AnimalManagerFactory.get().getAnimalsByBirthdateTo(new GenericType<List<AnimalBean>>() {}, toDate.toString(), String.valueOf(manager.getId()));
                        } else {
                            logger.log(Level.WARNING, "Invalid date range provided. Fetching all animals instead.");
                            showAllAnimals();
                        }
                    }   
                    break;
                default:
                    logger.log(Level.WARNING, "Unknown search type: {0}", searchType);
            }
            if (animalList != null && !animalList.isEmpty()) {
                logger.log(Level.INFO, "Search returned {0} results.", animalList.size());
                ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(animalList);
                tbAnimal.setItems(animalData);
                btnAdd.setDisable(false);
            } else {
                logger.info("No animals found for the given search criteria.");
            }
        } catch (WebApplicationException e) {
            logger.log(Level.WARNING, "Error fetching animals: " + e.getMessage(), e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching animals: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();    
        } catch (Exception e) {
            logger.log(Level.WARNING, "An unexpected error occurred: {0}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }   
    }
    
    /**
    * Validates if a given date is valid based on a specific format (dd/MM/yyyy).
    * 
    * @param date The LocalDate to be validated.
    * @return true if the date is valid, false otherwise.
    */
    private boolean isValidDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");     
        if (date == null) {
            return false;
        }
        try {
            String dateStr = date.format(formatter);
            formatter.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
    * Handles the commit of an edit event for a table cell. This method updates the 
    * value of the specified field in the corresponding animal record and synchronizes 
    * the changes in the database.
    * 
    * @param <T> The type of the edited field (e.g., String, AnimalGroupBean).
    * @param event The edit event containing details about the table and the new value.
    * @param fieldName The name of the field being edited (e.g., "name", "subespecies").
    */
    private <T> void handleEditCommit(CellEditEvent<AnimalBean, T> event, String fieldName) {
        try {
            TablePosition<AnimalBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                logger.log(Level.WARNING, "Invalid value entered for field: {0}", fieldName);
                throw new IllegalArgumentException("The entered value is not valid.");
            }
            
            int row = pos.getRow();
            AnimalBean animal = event.getTableView().getItems().get(row);
            AnimalBean animalCopy = animal.clone();
            
            logger.log(Level.INFO, "Updating field: {0} for animal ID: {1}", new Object[]{fieldName, animal.getId()});

            // Updates the animal object and synchronize with the database
            switch (fieldName) {
                case "name":
                    if (newValue instanceof String) {
                        animalCopy.setName((String) newValue);
                        AnimalManagerFactory.get().updateAnimal(animalCopy);
                        animal.setName((String) newValue);
                    }
                    break;
                case "subespecies":
                    if (newValue instanceof String) {
                        animalCopy.setSubespecies((String) newValue);
                        AnimalManagerFactory.get().updateAnimal(animalCopy);
                        animal.setSubespecies((String) newValue);
                    }
                    break;
                case "species":
                    if (newValue instanceof SpeciesBean) {
                        animalCopy.setSpecies((SpeciesBean) newValue);
                        AnimalManagerFactory.get().updateAnimal(animalCopy);
                        animal.setSpecies((SpeciesBean) newValue);
                    }
                    break;
                case "animalGroup":
                    if (newValue instanceof AnimalGroupBean) {
                        animalCopy.setAnimalGroup((AnimalGroupBean) newValue);
                        AnimalManagerFactory.get().updateAnimal(animalCopy);
                        animal.setAnimalGroup((AnimalGroupBean) newValue);
                    }
                    break;
                default:
                    logger.log(Level.WARNING, "Unknown field: {0}", fieldName);
            }          
            event.getTableView().refresh();
            logger.log(Level.INFO, "Successfully updated field: {0} for animal ID: {1}", new Object[]{fieldName, animal.getId()});
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error updating field: {0} - {1}", new Object[]{fieldName, e.getMessage()});
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
        }
    }
    
    /**
    * Updates the birthdate of the selected animal in the table.
    * 
    * @param updatedDate The new birthdate to set for the animal.
    * @throws CloneNotSupportedException If the cloning of the animal object fails.
    */
    private void updateAnimalBirthdate(Date updatedDate) throws CloneNotSupportedException {
        AnimalBean animal = tbAnimal.getSelectionModel().getSelectedItem();
        //comprobar formato de fecha
        if (animal != null && updatedDate != null) {
            AnimalBean animalCopy = animal.clone();
            animalCopy.setBirthdate(updatedDate);
            try {
                logger.log(Level.INFO, "Updating birthdate for animal: {0} to {1}", new Object[]{animal.getName(), updatedDate});
                AnimalManagerFactory.get().updateAnimal(animalCopy);
                animal.setBirthdate(updatedDate);
                logger.log(Level.INFO, "Successfully updated birthdate for animal: {0}", animal.getName());
            } catch (WebApplicationException e) {
                logger.log(Level.WARNING, "Error updating birthdate for animal: " + animal.getName() + " - " + e.getMessage(), e);
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    
    /**
    * Handles the event triggered when the delete menu item is clicked. This method 
    * confirms the deletion of selected animals and removes them from the table and database.
    * 
    * @param event The action event triggered by the user clicking the delete menu item.
    */
    private void onDeleteMenuItemClicked(ActionEvent event) {
        ObservableList<AnimalBean> selectedAnimals = tbAnimal.getSelectionModel().getSelectedItems();
        List<AnimalBean> successfullyDeleted = new ArrayList<>();

        // User is asked for confirmation to preceed with the deletion of selected animals
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected animals?", ButtonType.YES, ButtonType.NO);   
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                logger.log(Level.INFO, "Number of animals selected for deletion: {0}", selectedAnimals.size());
                for (AnimalBean selectedAnimal : selectedAnimals) {
                    try {
                        AnimalManagerFactory.get().deleteAnimalById(String.valueOf(selectedAnimal.getId()));
                        successfullyDeleted.add(selectedAnimal);
                    } catch (WebApplicationException e) {
                         logger.log(Level.WARNING, "Error deleting animal: " + selectedAnimal.getName() + " - " + e.getMessage(), e);
                    }
                }
                if (!successfullyDeleted.isEmpty()) {
                    tbAnimal.getItems().removeAll(successfullyDeleted);
                    tbAnimal.getSelectionModel().clearSelection();
                    tbAnimal.refresh();
                }
                logger.info("Successfully removed deleted animals from table.");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unexpected error during deletion: " + e.getMessage(), e);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, 
                    "Unexpected error during deletion: " + e.getMessage(), 
                    ButtonType.OK);
                errorAlert.showAndWait();
            }
        }     
    }

    /**
    * Handles the event triggered when the add button is clicked. This method creates a 
    * new animal record with default values and triggers the search button to refresh the table.
    * 
    * @param event The action event triggered by the user clicking the add button.
    */
    private void onAddButtonClicked(ActionEvent event){
        try{
            logger.info("Creating a new animal instance.");
            AnimalBean newAnimal = new AnimalBean();
            newAnimal.setName("New Animal");
            newAnimal.setBirthdate(new Date());
            setDefaultAnimalGroup(newAnimal);
            newAnimal.setSubespecies("Unknown");
            setDefaultSpecies(newAnimal);
            newAnimal.setMonthlyConsume(0);
            logger.info("New animal initialized with default values.");
        
            String filterType = comboSearch.getValue();
            // Set default values based on search filter
            if (filterType.equals("Animal Group")  || filterType.equals("Subespecies")){
                String filterValue = tfSearch.getText();
                if (filterValue != null && !filterValue.isEmpty()){
                    if (filterType.equals("Subespecies")){
                        newAnimal.setSubespecies(filterValue); 
                    }
                    else if (filterType.equals("Animal Group")){
                         try {
                            AnimalGroupBean choiceAnimalGroup = AnimalGroupFactory.get().getAnimalGroupByName(new GenericType<AnimalGroupBean>() {}, filterValue, String.valueOf(manager.getId()));
                            newAnimal.setAnimalGroup(choiceAnimalGroup);
                        } catch (NotFoundException | BadRequestException e) {
                            logger.log(Level.WARNING, "Animal group not found. Assigning default group.", e.getMessage());
                            tfSearch.setText("");
                        }
                    }
                }
            }
            logger.info("Saving new animal to database.");
            AnimalManagerFactory.get().createAnimal(newAnimal);
            logger.log(Level.INFO, "New animal successfully created with name: {0}", newAnimal.getName());
            // Refresh table
            btnSearch.fire();

            // Put the newly created animal in edit mode
            focusNewAnimal();
            logger.info("New animal is now in edit mode.");
        } catch (Exception e){
            logger.log(Level.WARNING, "Connection to server refused");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection error");
            alert.setHeaderText(null);
            alert.setContentText("Please try again later");
            alert.showAndWait();
        }
    }
    
    /**
    * Sets the default animal group for a new animal. If no animal groups are available,
    * an information alert is displayed.
    * 
    * @param newAnimal The new animal object to set the default animal group for.
    */
    private void setDefaultAnimalGroup(AnimalBean newAnimal){
        logger.log(Level.INFO, "Fetching available animal groups for manager wit email: {0}", manager.getEmail());
        List<AnimalGroupBean> availableAnimalGroups = new ArrayList<AnimalGroupBean>();
        availableAnimalGroups = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, String.valueOf(manager.getId()));
        if (availableAnimalGroups != null && !availableAnimalGroups.isEmpty()) {
            logger.info("Animal groups found: " + availableAnimalGroups.size() + ". Assigning default group to the new animal.");
            newAnimal.setAnimalGroup(availableAnimalGroups.get(0));
        } else {
            logger.log(Level.WARNING, "No animal groups found. User needs to create one before assigning.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "First you need to create an animal group", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    /**
    * Sets the default species for a new animal. If no species are available, a message is logged.
    * 
    * @param newAnimal The new animal object to set the default species for.
    */
    private void setDefaultSpecies(AnimalBean newAnimal){
        logger.info("Fetching available species...");
        List<SpeciesBean> availableSpecies = new ArrayList<SpeciesBean>();
        availableSpecies = SpeciesManagerFactory.get().getAllSpecies(new GenericType<List<SpeciesBean>>() {});
        if (availableSpecies != null && !availableSpecies.isEmpty()) {
            logger.log(Level.INFO, "Species found: {0}. Assigning default species to the new animal.", availableSpecies.size());
            newAnimal.setSpecies(availableSpecies.get(0));
        } else {
            logger.log(Level.WARNING, "No species found. Cannot assign a default species.");
        }
    }
    
    /**
    * Focuses the table cell of the newly added animal, making it editable immediately.
    */
    private void focusNewAnimal(){
        final int NEW_ANIMAL_ROW;
        for (int row = 0; row < tbAnimal.getItems().size(); row++) {
            AnimalBean animal = tbAnimal.getItems().get(row);
            if (animal.getName().equals("New Animal")) {                
                NEW_ANIMAL_ROW=row;
                Platform.runLater(() -> tbAnimal.edit(NEW_ANIMAL_ROW, tcName));
                tbAnimal.refresh();
                break;
            }
        }
    }
    
    /**
    * Displays all animals in the table. If no animals are available, an informational alert is shown.
    */
    private void showAllAnimals() {
        try {
            logger.log(Level.INFO, "Fetching all animals for manager with email: {0}", manager.getEmail());
            List<AnimalBean> allAnimals = AnimalManagerFactory.get().getAllAnimals(new GenericType<List<AnimalBean>>() {}, String.valueOf(manager.getId()));
                    
            if (allAnimals != null && !allAnimals.isEmpty()) {
                logger.log(Level.INFO, "Found {0} animals.", allAnimals.size());
                ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(allAnimals);
                tbAnimal.setItems(animalData);
                btnAdd.setDisable(false);
            } else {
                logger.info("No animals associated with the manager.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Currently, you have no animals associated", ButtonType.OK);
                alert.showAndWait();
            }
                    
        } catch (WebApplicationException e) {
            logger.log(Level.WARNING, "Error occurred while fetching animals: " + e.getMessage(), e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching animals: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    /**
    * Handles the action triggered by the print button. This method generates a report 
    * of the animals in the table and displays it using JasperReports. If an error occurs, 
    * an alert is shown and the error is logged.
    * 
    * @param event The action event triggered by the user clicking the print button.
    */
    private void handlePrintAction(ActionEvent event){
        try {
            logger.info("Beginning printing action...");
           
            // Compile the report from the .jrxml file
            JasperReport report=
                JasperCompileManager.compileReport(getClass()
                    .getResourceAsStream("/ui/reports/animalReport.jrxml"));
            logger.info("Report compiled successfully.");
            
            // Data for the report: a collection of AnimalBean passed as a JRDataSource
            JRBeanCollectionDataSource dataItems=
                    new JRBeanCollectionDataSource((Collection<AnimalBean>)this.tbAnimal.getItems());
            logger.info("Data for the report retrieved successfully.");
            
            // Parameters for the report
            Map<String,Object> parameters=new HashMap<>();
            
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,dataItems);
            logger.info("Report filled with data successfully.");
            
            // Create and show the report window
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);
            logger.info("Report viewer displayed successfully.");
        } catch (JRException ex) {
            logger.log(Level.WARNING, "Error occurred during print action: " + ex.getMessage(), ex);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Printing could not be completed. Please try again later.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
     private void handleHelpAction(ActionEvent event){
          try{
            logger.info("Loading help view...");

            FXMLLoader loader=
                new FXMLLoader(getClass().getResource("/ui/help/AnimalHelp.fxml"));
                Parent root = (Parent)loader.load();
                AnimalHelpController helpController=
                        ((AnimalHelpController)loader.getController());
                helpController.initAndShowStage(root);
        }catch(Exception ex){
            logger.log(Level.SEVERE,
                        "Error loading help window: {0}",
                        ex.getMessage());
        }
     }
}
