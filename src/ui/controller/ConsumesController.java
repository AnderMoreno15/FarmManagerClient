package ui.controller;

import DTO.ConsumesBean;
import DTO.ProductBean;
import DTO.AnimalGroupBean;
import DTO.ConsumesIdBean;
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
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.view.JasperViewer;

/**
 * Controller for the Consumes window management.
 * @author YourName
 */
public class ConsumesController implements Initializable{
   
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
//    @FXML
//    private Button btnPrint;
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
    @FXML
    private StackPane stack;
    
    private String managerId;
   // private static ManagerBean manager;
   // getters y setters
    private Stage stage;
    private ConsumesRestClient consumesClient;
    private static final Logger LOGGER = Logger.getLogger("ConsumesController");

    /**
     * Initializes the window.
     * @param root The FXML document graph.
     */
     public void initialize(URL url, ResourceBundle rb) { 
       
           //Initialize RestClient
           try{
            consumesClient = new ConsumesRestClient();
            managerId = "2";
             } catch (Exception e) {
            String errorMsg = "Error initializing Rest: " + e + consumesClient;
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
            
//            String css = getClass().getResource("/ui/view/styles.css").toExternalForm();
//            searchField.getScene().getStylesheets().add(css);
            try { 
           
            // Initialize UI components
            initializeComponents();
            
            // Initialize table
            initializeTable();

            // Set up event handlers
            setupEventHandlers();

            // Load initial data
            
            //Mostrar Todos los Consumos
            showAllConsumes();
            // Show window
            stage.show();
            
            LOGGER.info("Consumes window initialized.");
        } catch (Exception e) {
    // Obtener la traza de pila como un array de StackTraceElement
    StackTraceElement[] stackTraceElements = e.getStackTrace();

    // Convertir la traza de pila en una cadena de texto legible
    StringBuilder traceBuilder = new StringBuilder();
    for (StackTraceElement element : stackTraceElements) {
        traceBuilder.append(element.toString()).append("\n");
    }

    // Mostrar el error con la traza de pila
    String errorMsg = "Error initializing window: \n" + traceBuilder.toString();
    System.err.println(errorMsg); // O usar un logger si prefieres
}}

    /**
     * Initializes window components.
     */
  private void initializeComponents() {
    try {
        LOGGER.info("Initializing ComboBox...");
        comboSearch.getItems().addAll("Animal Group", "Product", "Date");
        comboSearch.setValue("Animal Group");
        comboSearch.setEditable(false);
        LOGGER.info("ComboBox initialized successfully.");

        // Initialize search field
        LOGGER.info("Initializing search field...");
        searchField.setPromptText("Enter search text");
        LOGGER.info("Search field initialized successfully.");

        // Set ComboBox value
        LOGGER.info("Setting ComboBox value...");
        comboSearch.setValue("Animal Group");
        LOGGER.info("ComboBox value set to 'Animal Group'.");

        // Initialize buttons
        LOGGER.info("Initializing buttons...");
        btnSearch.setDisable(false);
        btnSearch.setDefaultButton(true);
        LOGGER.info("Buttons initialized successfully.");

        // Initialize menu items
        LOGGER.info("Initializing menu items...");
        itemDelete.setDisable(true);
        LOGGER.info("Menu items initialized successfully.");

        // Hide the elements
        LOGGER.info("Hiding date pickers...");
        dpSearchFrom.setVisible(false);
        dpSearchTo.setVisible(false);
        LOGGER.info("Date pickers hidden successfully.");
        
    } catch (Exception e) {
        String errorMsg = "Error initializing components: " + e;
        showErrorAlert(errorMsg);
        LOGGER.log(Level.SEVERE, errorMsg);
    }
}


    /**
     * Initializes the table and its columns antes de chat gpt ponga los logs
     */
    

  private void initializeTable() {
    // Set up column AnimalGroupç
    tableConsumes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    try {
        LOGGER.info("Setting up AnimalGroup column...");
        tcAnimalGroup.setCellValueFactory(new PropertyValueFactory<>("animalGroup"));
        
        List<AnimalGroupBean> animalGroupList = new ArrayList<AnimalGroupBean>();
        LOGGER.info("Fetching animal groups for manager...");
        animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, managerId);  
        
        ObservableList<AnimalGroupBean> animalGroupData = FXCollections.observableArrayList(animalGroupList);
        LOGGER.info("Animal groups fetched, setting up ComboBox cell...");
        tcAnimalGroup.setCellFactory(ComboBoxTableCell.forTableColumn(animalGroupData));
        
        tcAnimalGroup.setOnEditCommit(event -> {
            LOGGER.info("AnimalGroup edit committed: " + event.getNewValue());
            handleEditCommit(event, "animalGroup");
        });
           } catch (Exception e) {

    String errorMsg = "Error initialing animal group column: \n";  handleException(e);;
    System.err.println(errorMsg); // O usar un logger si prefieres
   }

    // Set up column Products
    try {
        LOGGER.info("Setting up Product column...");
        tcProduct.setCellValueFactory(new PropertyValueFactory<>("product"));    
        tcProduct.setOnEditCommit(event -> {
            LOGGER.info("Product edit committed: " + event.getNewValue());
            handleEditCommit(event, "product");
        });
           } catch (Exception e) {

    // Mostrar el error con la traza de pila
    String errorMsg = "Error setting up product column: \n" ;
    handleException(e);
    System.err.println(errorMsg); // O usar un logger si prefieres
   }

    // Initialize column consume amount
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
  

    // Mostrar el error con la traza de pila
    String errorMsg = "Error Setting up consume column: \n" ;  handleException(e);
    System.err.println(errorMsg); // O usar un logger si prefieres
}

    // Configurar la columna 'date' con un CellValueFactory y un CellFactory personalizado
    try {
        LOGGER.info("Setting up Date column...");
        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Configurar el CellFactory para la columna 'date'
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
                        
                        Logger.getLogger(AnimalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };
                return cell;
            }
        });

        tcDate.setStyle("-fx-alignment: center;");
        LOGGER.info("Date column setup complete.");
           } catch (Exception e) {


    // Mostrar el error con la traza de pila
    String errorMsg = "Error setting up date column: \n";
    handleException(e);
    System.err.println(errorMsg); // O usar un logger si prefieres
}


  }
    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
  LOGGER.info("Setting up search button action...");
        btnSearch.setOnAction(this::handleSearchAction);
   LOGGER.info("Search button clicked");
   LOGGER.info("Setting up add button action...");
        btnAdd.setOnAction(this::handleCreateAction);
   LOGGER.info("Add button clicked");
   LOGGER.info("Setting up combo box action...");
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
        //     Set up selection listener
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
     * Handles the search action.
     */
private void handleSearchAction(ActionEvent event) {
    try {
        LOGGER.info("Handling search action.");

        // Obtienes el texto de búsqueda y el tipo de búsqueda seleccionado
        String searchText = searchField.getText().trim();
        String searchType = comboSearch.getValue();

        // Lista de resultados
        List<ConsumesBean> consumesList = null;

        // Si el campo de búsqueda no está vacío
        if (!searchText.isEmpty()) {
            switch (searchType) {
                case "Product":
                    // Buscar por producto
                    consumesList = ConsumesManagerFactory.get().findConsumesByProduct(new GenericType<List<ConsumesBean>>() {}, searchText);
                    break;

                case "Animal Group":
                    // Buscar por grupo animal
                    consumesList = ConsumesManagerFactory.get().findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {}, searchText);
                    break;

                case "Date":
                    // Buscar por fecha
                    String from = (dpSearchFrom.getValue() != null && !dpSearchFrom.getValue().toString().isEmpty()) ? dpSearchFrom.getValue().toString() : null;
                    String to = (dpSearchTo.getValue() != null && !dpSearchTo.getValue().toString().isEmpty()) ? dpSearchTo.getValue().toString() : null;

                    if (from != null && to != null) {
                        consumesList = ConsumesManagerFactory.get().getConsumesByDate(new GenericType<List<ConsumesBean>>() {}, from, to);
                    } else if (from != null) {
                        consumesList = ConsumesManagerFactory.get().getConsumesByDateFrom(new GenericType<List<ConsumesBean>>() {}, from);
                    } else if (to != null) {
                        consumesList = ConsumesManagerFactory.get().getConsumesByDateTo(new GenericType<List<ConsumesBean>>() {}, to);
                    } else {
                        showAllConsumes(); // Método para mostrar todos los consumos, equivalente a showAllAnimals().
                    }
                    break;

                default:
                    LOGGER.warning("Tipo de búsqueda no reconocido.");
                    consumesList = consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {});
                    break;
            }
        } else {
            // Si el campo de búsqueda está vacío, obtienes todos los consumos
            consumesList = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {});
        }

        // Si la lista no está vacía, muestra los consumos encontrados
        if (consumesList != null && !consumesList.isEmpty()) {
            ObservableList<ConsumesBean> consumesData = FXCollections.observableArrayList(consumesList);
            tableConsumes.setItems(consumesData);
            btnAdd.setDisable(false); // Habilita el botón si hay resultados
            LOGGER.info("Search completed successfully.");
        } else {
            System.out.println("No se encontraron consumos para la búsqueda.");
        }

    } catch (Exception e) {
        // Manejo de excepciones generales
        handleException(e);
    }
}

    private <T> void handleEditCommit(TableColumn.CellEditEvent<ConsumesBean, T> event, String fieldName) {
        try {
            
            //     Set up selection listener
    LOGGER.info("Setting up selection listener for table...");
    tableConsumes.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            itemDelete.setDisable(!hasSelection);
            LOGGER.info("Selection changed: " + (hasSelection ? "Item selected" : "No item selected"));
        });
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
                if (newValue instanceof Number) { // Asegura que sea un número
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
                    throw new IllegalArgumentException("Campo desconocido: " + fieldName);
            }

            event.getTableView().refresh();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
            handleException(e);
            
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
    // Crear un nuevo objeto ConsumesBean con valores iniciales
    ConsumesBean newConsume = new ConsumesBean();
    newConsume.setProduct(null);  // Producto nulo
    newConsume.setAnimalGroup(null);  // Grupo de animales nulo
    newConsume.setConsumeAmount(0f);  // Cantidad inicial en 0
    newConsume.setDate(new Date());  // Fecha actual

    // Crear el nuevo consumo en la base de datos
    try {
        ConsumesManagerFactory.get().createConsume(newConsume);
    } catch (WebApplicationException e) {
        System.err.println("Error creating consume: " + e.getMessage());
        handleException(e);
        return;
    }

    // Agregar el nuevo consumo a la tabla
    tableConsumes.getItems().add(newConsume);
    tableConsumes.refresh();

    // Poner en modo edición la casilla de la columna "Quantity" del nuevo consumo
    final int NEW_CONSUME_ROW = tableConsumes.getItems().size() - 1; // Última fila añadida
    Platform.runLater(() -> tableConsumes.edit(NEW_CONSUME_ROW, tcAnimalGroup));
}

private void handleDeleteAction(ActionEvent event) {
    ObservableList<ConsumesBean> selectedConsumes = tableConsumes.getSelectionModel().getSelectedItems();
    List<ConsumesBean> successfullyDeleted = new ArrayList<>();

    // Si no se seleccionan consumos, mostramos una advertencia.
    if (selectedConsumes.isEmpty()) {
        Platform.runLater(() -> {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING, "No consumes selected for deletion.", ButtonType.OK);
            warningAlert.showAndWait();
        });
        return;
    }

    // Mostramos una alerta de confirmación.
    Platform.runLater(() -> {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to delete the selected consumes?", 
            ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            for (ConsumesBean selectedConsume : selectedConsumes) {
                try {
                    // Se extraen los parámetros de la entidad ConsumesBean
                    String productId = selectedConsume.getProduct().getId().toString();  // ID del Producto
                    String animalGroupId = selectedConsume.getAnimalGroup().getId().toString();  // ID del Animal Group

                    
                    ConsumesManagerFactory.get().deleteConsume( productId, animalGroupId);

                    // Si la eliminación es exitosa, agregamos el consumo a la lista de eliminados
                    successfullyDeleted.add(selectedConsume);
                    
                } catch (WebApplicationException e) {
                    System.err.println("Error deleting consume ID " + selectedConsume.getConsumesId() + ": " + e.getMessage());
                    handleException(e);  // Manejo específico de la excepción (puedes agregar más detalles aquí)
                } catch (Exception e) {
                    // Si ocurre un error inesperado, mostramos una alerta en la interfaz
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, 
                            "Unexpected error during deletion: " + e.getMessage(), 
                            ButtonType.OK);
                        errorAlert.showAndWait();
                    });
                }
            }

            // Si al menos un consumo fue eliminado exitosamente, actualizamos la interfaz
            if (!successfullyDeleted.isEmpty()) {
                Platform.runLater(() -> {
                    // Actualizamos la tabla eliminando los elementos
                    tableConsumes.getItems().removeAll(successfullyDeleted);
                    tableConsumes.getSelectionModel().clearSelection();
                    tableConsumes.refresh();
                });
            }
        }
    });
}


private void showAllConsumes() {
    try {
        LOGGER.info("Fetching all consumes...");
        List<ConsumesBean> allConsumes = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>() {});
        
        // Comprobación de si se obtuvieron consumos
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
     * Shows error alert dialog.
     * @param message The error message to display.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
            message,
            ButtonType.OK);
        alert.showAndWait();
    }

// Método para manejar excepciones y mostrar el stack trace
private void handleException(Exception e) {
    // Obtener la traza de pila como un array de StackTraceElement
    StackTraceElement[] stackTraceElements = e.getStackTrace();

    // Convertir la traza de pila en una cadena de texto legible
    StringBuilder traceBuilder = new StringBuilder();
    for (StackTraceElement element : stackTraceElements) {
        traceBuilder.append(element.toString()).append("\n");
    }

    // Mostrar el error con la traza de pila
    String errorMsg = "Error in search Action Handler: \n" + traceBuilder.toString();
    System.err.println(errorMsg); // O usar un logger si prefieres
}

//    
//    private void handlePrintAction(ActionEvent event){
//        try {
//            LOGGER.info("Beginning printing action...");
//           
//            JasperReport report=
//                JasperCompileManager.compileReport(getClass()
//                    .getResourceAsStream("/ui/reports/ConsumesReport.jrxml"));
//            //Data for the report: a collection of UserBean passed as a JRDataSource 
//            //implementation 
//            JRBeanCollectionDataSource dataItems=
//                    new JRBeanCollectionDataSource((Collection<ConsumesBean>)this.tableConsumes.getItems());
//            //Map of parameter to be passed to the report
//            Map<String,Object> parameters=new HashMap<>();
//            //Fill report with data
//            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,dataItems);
//            //Create and show the report window. The second parameter false value makes 
//            //report window not to close app.
//            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
//            jasperViewer.setVisible(true);
//           // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//        } catch (JRException ex) {
//            If there is an error show message and
//            log it.
//            showErrorAlert("Error al imprimir:\n"+
//                            ex.getMessage());
//            
//        }
//    }
}
