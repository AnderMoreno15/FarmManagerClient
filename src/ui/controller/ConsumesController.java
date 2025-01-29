package ui.controller;

import DTO.ConsumesBean;
import DTO.ProductBean;
import DTO.AnimalGroupBean;
import businessLogic.animalGroup.AnimalGroupFactory;
import cellFactories.DatePickerTableCell;
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
import businessLogic.product.ProductManagerFactory;

/**
 * Controller for the Consumes window management.
 * @author YourName
 */
public class ConsumesController {
    
    @FXML
    private TableView<ConsumesBean> tableConsumes;
    @FXML
    private TableColumn<ConsumesBean, AnimalGroupBean>tcAnimalGroup;
    @FXML
    private TableColumn<ConsumesBean, ProductBean>tcProduct;
    @FXML
    private TableColumn<ConsumesBean, Float> tcConsumeAmount;
    @FXML
    private TableColumn<ConsumesBean, Date> tcDate;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnPrint;
    @FXML
    private MenuItem itemDelete;
    @FXML
    private DatePicker dpSearchFrom;
    @FXML
    private DatePicker dpSearchTo;
    @FXML
    private ComboBox<String> comboSearch;
    @FXML
    private HBox hboxDatePicker;
    
    private String managerId;
   // private static ManagerBean manager;
   // getters y setters

    private final String PROHIBITED_CHARACTERS = "'\"\\;*%()<>|&,-/!?=";
    private Stage stage;
    private ConsumesRestClient consumesClient;
    private static final Logger LOGGER = Logger.getLogger("ConsumesController");

    /**
     * Initializes the window.
     * @param root The FXML document graph.
     */
     public void initialize(URL url, ResourceBundle rb) { 
        try {
           //Initialize RestClient
            consumesClient = new ConsumesRestClient();
            managerId = "1";
            // Initialize UI components
            initializeComponents();
            
            // Initialize table
            initializeTable();

            // Set up event handlers
            setupEventHandlers();

            // Load initial data
            showAllConsumes();
            
            //Mostrar Todos los Consumos
            showAllConsumes();
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
        comboSearch.getItems().addAll(
           "Animal Group","Product", "Date");
        comboSearch.setValue("Product");
        comboSearch.setEditable(false);
        // Initialize search field
        searchField.setPromptText("Enter search text");
        comboSearch.setValue("Animal Group");
        // Initialize buttons
        btnSearch.setDisable(false);
        btnSearch.setDefaultButton(true);
        // Initialize menu items
        itemDelete.setDisable(true);
        // Hide the elements
        dpSearchFrom.setVisible(false);
        dpSearchTo.setVisible(false);
        
    }

    /**
     * Initializes the table and its columns.
     */
    
    // Faltaria importar las factorias de animal group y de product y el cellfactory
    private void initializeTable() {
        
        // Set up column AnimalGroup
        tcAnimalGroup.setCellValueFactory(new PropertyValueFactory<>("animalGroup"));
        List<AnimalGroupBean> animalGroupList = new ArrayList<AnimalGroupBean>();
        
        animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, managerId);  
        ObservableList<AnimalGroupBean> animalGroupData = FXCollections.observableArrayList(animalGroupList);
        tcAnimalGroup.setCellFactory(ComboBoxTableCell.forTableColumn(animalGroupData));
        
        tcAnimalGroup.setOnEditCommit(event -> handleEditCommit(event, "animalGroup"));
        
        //Set up column Products
        tcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        List<ProductBean> productList = new ArrayList<ProductBean>();
        
        productList = ProductManagerFactory.get().getAllProducts(new GenericType<List<ProductBean>>() {});  
        ObservableList<ProductBean> productData = FXCollections.observableArrayList(productList);
        tcProduct.setCellFactory(ComboBoxTableCell.forTableColumn(productData));
        
        tcAnimalGroup.setOnEditCommit(event -> handleEditCommit(event, "product"));
        
        //Initialize column consume amount
        tcConsumeAmount.setCellValueFactory(new PropertyValueFactory<>("consumeAmount"));

        tcConsumeAmount.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
   
         public String toString(Float value) {
         return value != null ? value.toString() : "";
         }

         public Float fromString(String string) {
          try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return 0.0f; // Default value in case of invalid input
        }
        }
        }));
       
      // Configurar la columna 'consumeAmount' para manejar commits de edición
        tcConsumeAmount.setOnEditCommit(event -> handleEditCommit(event, "consumeAmount"));

      // Configurar la columna 'date' con un CellValueFactory y un CellFactory personalizado
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));

      // Configurar el CellFactory para la columna 'date'
        tcDate.setCellFactory(new Callback<TableColumn<ConsumesBean, Date>, TableCell<ConsumesBean, Date>>() {
    @Override
    public TableCell<ConsumesBean, Date> call(TableColumn<ConsumesBean, Date> param) {
        // Crear una celda personalizada con DatePicker
        DatePickerTableCell<ConsumesBean> cell = new DatePickerTableCell<>(param);
        cell.updateDateCallback = (Date updatedDate) -> {
            try {
                updateConsumeDate(updatedDate);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ConsumesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return cell;
    }
     });

     // Aplicar un estilo centrado a la columna 'date'
       tcDate.setStyle("-fx-alignment: center;");

        

        // Set up selection listener
        tableConsumes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                boolean hasSelection = newVal != null;
                itemDelete.setDisable(!hasSelection);
            });
    }

    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        btnSearch.setOnAction(this::handleSearchAction);
        btnAdd.setOnAction(this::handleCreateAction);
        comboSearch.setOnAction(this::handleComboSearchAction);
        itemDelete.setDisable(true);
            tableConsumes.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ConsumesBean>) change -> {
                itemDelete.setDisable(tableConsumes.getSelectionModel().getSelectedItems().isEmpty());
            });
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
                dpSearchFrom.setVisible(true);
                dpSearchTo.setVisible(true);
                searchField.setVisible(false);   
                break;
        }
    }

    /**
     * Handles the search action.
     */
    private void handleSearchAction(ActionEvent event) {
        try {
            LOGGER.info("Handling search action.");
            String searchText = searchField.getText().trim();
            String searchType = comboSearch.getValue();
            List<ConsumesBean> consumesList;
                consumesList = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {});

            if(searchField.getText() != null && !searchField.getText().isEmpty()){
               
              } else {
                  switch (searchType) {
                    case "Product":
                      String nameProduct=searchField.getText();
                      consumesList = ConsumesManagerFactory.get().findConsumesByProduct(new GenericType<List<ConsumesBean>>() {},nameProduct);
;
                        break;
                    case "Animal Group": //Como el de abajo pues todooosssss
                        String nameAnimalGroup=searchField.getText();
                        consumesList = ConsumesManagerFactory.get().findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {},nameAnimalGroup);

                        break;
                    case "Date":
                        
                    String from = (dpSearchFrom.getValue() != null && !dpSearchFrom.getValue().toString().isEmpty())? dpSearchFrom.getValue().toString(): null;
                     String to = (dpSearchTo.getValue() != null && !dpSearchTo.getValue().toString().isEmpty())? dpSearchTo.getValue().toString(): null;

                      if (from != null && to != null) {
                      consumesList = ConsumesManagerFactory.get().getConsumesByDate(new GenericType<List<ConsumesBean>>() {},from, to);
                      } else if (from != null) {
                      consumesList = ConsumesManagerFactory.get().getConsumesByDateFrom(new GenericType<List<ConsumesBean>>() {},from);
                      } else if (to != null) {consumesList = ConsumesManagerFactory.get().getConsumesByDateTo(new GenericType<List<ConsumesBean>>() {}, to);
                       } else {
                         showAllConsumes(); // Método para mostrar todos los consumos, equivalente a showAllAnimals().
                       }
                     break;

                    default:
                        consumesList = consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {});
             
                }
                  
            }
             if (!consumesList.isEmpty()) {
                ObservableList<ConsumesBean> consumesData = FXCollections.observableArrayList(consumesList);
                tableConsumes.setItems(consumesData);
                btnAdd.setDisable(false);
             }

            tableConsumes.setItems(FXCollections.observableArrayList(consumesList));
            LOGGER.info("Search completed successfully.");

        } catch (WebApplicationException e) {
            String errorMsg = "Error performing search: " + e.getMessage();
//            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
        
        
               
    }
        
    
    
    private <T> void handleEditCommit(TableColumn.CellEditEvent<ConsumesBean, T> event, String fieldName) {
        try {
            TablePosition<ConsumesBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("El valor ingresado no es válido.");
            }

            int row = pos.getRow();
            ConsumesBean consume = event.getTableView().getItems().get(row);
             ConsumesBean consumeCopy =  (ConsumesBean) consume.clone();

            // actualiza en la capa lógica y también en el objeto original
            switch (fieldName) {
                case "consumeAmount":
                    if (newValue instanceof String) {
                        consumeCopy.setConsumeAmount((Float) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setConsumeAmount((Float) newValue);
                    }
                    break;
       
                case "animalGroup":
                    if (newValue instanceof ConsumesBean) {
                        consumeCopy.setAnimalGroup((AnimalGroupBean) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setAnimalGroup((AnimalGroupBean) newValue);
                    }
                case "product":
                    if (newValue instanceof ConsumesBean) {
                        consumeCopy.setProduct((ProductBean) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumeCopy);
                        consume.setProduct((ProductBean) newValue);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Campo desconocido: " + fieldName);
            }

            event.getTableView().refresh();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
        }
    }
    
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
     * Handles the create action.
     */
    private void handleCreateAction(ActionEvent event) {
        

        try {
        ConsumesBean newConsume = new ConsumesBean();
        //crear constructor por defecto
        newConsume.setAnimalGroup(null);
        newConsume.setProduct(null);
        newConsume.setDate(new Date());
        newConsume.setConsumeAmount(null);
        ConsumesManagerFactory.get().createConsume(newConsume);
        } catch (WebApplicationException e) {
            String errorMsg = "Error creating consume: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
         btnSearch.fire();
    } 
//        String filterType = comboSearch.getValue();
//        String filterValue = searchField.getText();
//
//        // Lógica para asignar AnimalGroup o Subespecie según el filtro
//        if ("Animal Group".equals(filterType)) {
//            if (filterValue != null && !filterValue.isEmpty()) {
//                AnimalGroupBean choiceAnimalGroup = AnimalGroupFactory.get().getAnimalGroupByName(new GenericType<AnimalGroupBean>() {}, filterValue, managerId);
//                // internal server error si no hay coincidencias, tratarlo y darle un defaultAnimalGroup
//
//                if (choiceAnimalGroup != null) {
//                    newAnimal.setAnimalGroup(choiceAnimalGroup);
//                } else {
//                    setDefaultAnimalGroup(newAnimal);
//                }
//            }
//        } else if ("Subespecies".equals(filterType)) {
//            if (filterValue != null && !filterValue.isEmpty()) {
//                // Asignar la subespecie basada en el filtro
//                newAnimal.setSubespecies(filterValue);
//                setDefaultAnimalGroup(newAnimal);
//            }
//        }

//        List<SpeciesBean> availableSpecies = new ArrayList<SpeciesBean>();
//        availableSpecies = SpeciesManagerFactory.get().getAllSpecies(new GenericType<List<SpeciesBean>>() {});
//       
//        if (availableSpecies != null && !availableSpecies.isEmpty()) {
//            newAnimal.setSpecies(availableSpecies.get(0));
//        } else {
//            System.out.println("No se encontraron especies");
        
       
       
        
        //lanzar accion btnSearch
       

        //poner en modo edicion la casilla que contenga en la columna name "New Animal"
//        final int NEW_ANIMAL_ROW;
//        for (int row = 0; row < tbAnimal.getItems().size(); row++) {
//            AnimalBean animal = tbAnimal.getItems().get(row);
//            if (animal.getName().equals("New Animal")) {                
////                tbAnimal.edit(row, tcName);
//                NEW_ANIMAL_ROW=row;
//                Platform.runLater(() -> tbAnimal.edit(NEW_ANIMAL_ROW, tcName));
//                tbAnimal.refresh();
//                break;
//            }
//        }
//            LOGGER.info("New consume created successfully.");
//         

    /**
     * Handles the delete action.
     */
    private void handleDeleteAction(ActionEvent event) {
           ObservableList<ConsumesBean> selectedConsumes = tableConsumes.getSelectionModel().getSelectedItems();
        List<ConsumesBean> successfullyDeleted = new ArrayList<>();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected consumes?", ButtonType.YES, ButtonType.NO);   
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                for (ConsumesBean selectedConsume : selectedConsumes) {
                    try {
                        System.out.println(selectedConsume.toString());
                        ConsumesManagerFactory.get().deleteConsume(String.valueOf(selectedConsume.getConsumesId()));
                        successfullyDeleted.add(selectedConsume);
                
                    } catch (WebApplicationException e) {
                        System.err.println("Error deleting animal: " + selectedConsume.getConsumesId() + " - " + e.getMessage());
                    }
                }

                if (!successfullyDeleted.isEmpty()) {
                    tableConsumes.getItems().removeAll(successfullyDeleted);
                    tableConsumes.getSelectionModel().clearSelection();
                    tableConsumes.refresh();
                }

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, 
                    "Unexpected error during deletion: " + e.getMessage(), 
                    ButtonType.OK);
                errorAlert.showAndWait();
            }
        }     
    }


    /**
     * Loads table data from server.
     */
    private void showAllConsumes() {
        try {
            List<ConsumesBean> allConsumes = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {});
                    
            if (allConsumes != null && !allConsumes.isEmpty()) {
                ObservableList<ConsumesBean> consumesData = FXCollections.observableArrayList(allConsumes);
                tableConsumes.setItems(consumesData);
                btnAdd.setDisable(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No tiene consumos asociados", ButtonType.OK);
                alert.showAndWait();
            }
                    
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los consumos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
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
