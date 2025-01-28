package userInterfaceTier;

import DTO.ConsumesBean;
import static antlr.build.ANTLR.root;
import java.net.URL;
import java.util.ArrayList;
import userLogicTier.ConsumesRestClient1;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Optional;
import java.util.List;
import java.util.Date;
import java.util.ResourceBundle;
import DTO.AnimalGroupBean;
import DTO.ProductBean;
import cellFactories.DatePickerTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import userLogicTier.ConsumesManagerFactory;

/**
 * Controller for the Consumes window management.
 * @author YourName
 */
public class ConsumesController implements Initializable {
    
    @FXML
    private TableView<ConsumesBean> tableConsumes;
    
    @FXML
    private TableColumn<ConsumesBean, String> columnAnimalGroup; //TableColumn<T, S> (ConsumesBean): T Es el tipo de objeto que contiene la tabla S (String): Es el tipo de dato que contiene la columna
   
    @FXML
    private TableColumn<ConsumesBean, String> columnProduct;  //Se puede hacer por string o por Bean, luego habia que cambiar initialize table
    
    @FXML
    private TableColumn<ConsumesBean, Float> columnConsume;
   
    @FXML
    private TableColumn<ConsumesBean, Date> columnDate;
   
    @FXML
    private TextField searchField;
    
    @FXML
    private Button btnSearch;
   
    @FXML
    private DatePicker dpDateFrom;
    
    @FXML
    private DatePicker dpDateTo;
   
    @FXML
    private Button btnAdd;  
    
    @FXML
    private Label lblError;

    @FXML
    private MenuItem itemDelete;
    
   @FXML
    private ComboBox<String> comboSearch;

    private final String PROHIBITED_CHARACTERS = "'\"\\;*%()<>|&,-/!?=";
    
    private Stage stage;
   
    private ConsumesRestClient1 consumesClient;
   
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
            // Establecer el título de la ventana como “Consumes”. 
            stage.setTitle("Consumes");
            //La ventana no debe ser redimensionable. Establecer las dimensiones a 1024 x 720.
            stage.setResizable(false);
            stage.setWidth(1024);
            stage.setHeight(720);
            stage.setResizable(false); 
            //Establecer “Search” como botón por defecto.
            btnSearch.setDefaultButton(true);
           

            // Initialize REST client
            
            consumesClient = new ConsumesRestClient1();

//Cargar la tabla:
//Todas las columnas deben ser editables, establecer setEditable.
//Establecer item Delete.
//Obtener la lista de animales asociados a los grupos del cliente mediante el método de lógica getAllConsumes(clientId).
//Cargar todos los datos obtenidos en tblConsumes, mostrando las columnas: Product,Animal Group, Consume, Date. Descripción de columnas:
//
//Animal Group y Product: combo no editable con el valor del nombre del Animal Group y el valor del nombre del product asociados seleccionados. Edición ComboBoxTableCell.
//
//Date: fecha en formato dd/MM/yyyy, alineación center. Edición DatePicker.
//
//Consume: valor decimal en formato nn.nn, alineación derecha. Edición TextFieldTableCell.

        } catch (Exception e) {
            String errorMsg = "Error initializing window: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
    }

    @Override
public void initialize(URL location, ResourceBundle resources) {
    try {
        //Initialize UI components
        initializeComponents();
        
        // Initialize table
        initializeTable();

        // Set up event handlers
        setupEventHandlers();

        // Load initial data
        loadTableData();
        
        List<ConsumesBean> consumesList = ConsumesManagerFactory.get().getAllConsumes(new GenericType<List<ConsumesBean>>(){});
        System.out.println("TODOxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+ consumesList);
        for(ConsumesBean e: consumesList) {
            System.out.println(e.toString());
        }
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error during initialization: " + e.getMessage(), e);
    }
}

    /**
     * Initializes window components.
     */
    private void initializeComponents() {
        // Initialize ComboBox
        //Cargar la combo no editable comboSearch con los nombres de los campos posibles de filtrado: Product,Date y Animal Group. 
try{       
    if (comboSearch != null) {
        ObservableList<String> options = FXCollections.observableArrayList(
            "Animal Group", "Product", "Date"
        );
        comboSearch.setItems(options);
        comboSearch.setValue(options.get(0)); // Establece un valor por defecto
        comboSearch.setEditable(false);
        
        // Agregar el listener directamente aquí también
        comboSearch.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ComboBox value changed from " + oldValue + " to " + newValue);
            if (newValue != null) {
                updateSearchFieldPrompt(newValue);
            }
        });
    }
        
    }catch(Exception e){ e.printStackTrace(); // Esto imprimirá el stack trace en la consola
        System.out.println("Error: " + e.getMessage());}
  
        //Limpiar los campos de dpSearchFrom y dbSearchTo. Hacer invisibles ambos.
        dpDateFrom.setVisible(false);
        dpDateTo.setVisible(false);
          
        // Initialize search field
        searchField.setPromptText("Enter search text");
        searchField.setText("");
        
       //Habilitar los botones “Search” y “Add”. 
         btnSearch.setDisable(false);
         btnAdd.setDisable(false);
         
       //Search como botón por defecto
         btnSearch.setDefaultButton(true);

    }

    /**
     * Initializes the table and its columns.
     */
    private void initializeTable() {
   
        tableConsumes.setEditable(true);
        // Initialize the Animal Group column
        columnAnimalGroup.setCellValueFactory(new PropertyValueFactory<>("animalGroup"));   
        List<AnimalGroupBean> animalGroupList = new ArrayList<AnimalGroupBean>();
        animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, managerId); 
        ObservableList<AnimalGroupBean> animalGroupData = FXCollections.observableArrayList(animalGroupList);
        columnAnimalGroup.setCellFactory(ComboBoxTableCell.forTableColumn(animalGroupData));
        columnAnimalGroup.setOnEditCommit(event -> handleEditCommit(event, "animalGroup"));
        
        // Initialize Products column
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        List<ProductBean> productList = new ArrayList<ProductBean>();
        productList = ProductFactory.get().getProductByName(new GenericType<List<ProductBean>>() {}, productName); 
        ObservableList<ProductBean> productData = FXCollections.observableArrayList(productList);
        columnProduct.setCellFactory(ComboBoxTableCell.forTableColumn(productData));
        columnProduct.setOnEditCommit(event -> handleEditCommit(event, "product"));
      // Make sure the table is editable


        // Initialize Consumes Amount column
        columnConsume.setCellValueFactory(new PropertyValueFactory<>("consumeAmount"));               
        columnConsume.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Float>() {
  
        public String toString(Float object) {
        return object != null ? object.toString() : "";
         }

   
         public Float fromString(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            // Maneja el error si el usuario ingresa algo que no es un número
            System.err.println("Error al convertir String a Float: " + e.getMessage());
            return 0.0f; // O lanza una excepción personalizada si prefieres
        }
        }
         }));

        columnConsume.setOnEditCommit(event -> handleEditCommit(event, "consumeAmount"));
        columnConsume.setStyle("-fx-alignment: right;");
        // Initialize Date column
       columnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
            columnDate.setCellFactory(new Callback<TableColumn<ConsumesBean, Date>, TableCell<ConsumesBean, Date>>() {
                @Override
                public TableCell<ConsumesBean, Date> call(TableColumn<ConsumesBean, Date> param) {
                    DatePickerTableCell<ConsumesBean> cell = new DatePickerTableCell<>(param);
                    cell.updateDateCallback = (Date updatedDate) -> {
                        try {
                            updateConsumesDate(updatedDate);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(ConsumesController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    };
                    return cell;
                }
            });
            columnConsume.setStyle("-fx-alignment: center;");
    }

    
    
     private <T> void handleEditCommit(CellEditEvent<ConsumesBean, T> event, String fieldName) {
        try {
            TablePosition<ConsumesBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("El valor ingresado no es válido.");
            }

            int row = pos.getRow();
            ConsumesBean consumes = event.getTableView().getItems().get(row);
            ConsumesBean consumesCopy = (ConsumesBean) consumes.clone();

            // actualiza en la capa lógica y también en el objeto original
            switch (fieldName) {
                case "animalGroup":
                     if (newValue instanceof AnimalGroupBean) {
                        consumesCopy.setAnimalGroup((AnimalGroupBean) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumesCopy);
                        consumes.setAnimalGroup((AnimalGroupBean) newValue);
                    }
                    break;
                case "product":
                     if (newValue instanceof String) {
                    consumesCopy.setProduct((ProductBean)newValue);
                        ConsumesManagerFactory.get().updateConsume(consumesCopy);
                        consumes.setProduct((ProductBean) newValue);
                    }
                    break;
                case "consumeAmount":
                    if (newValue instanceof Float) {
                       consumesCopy.setConsumeAmount((Float) newValue);
                        ConsumesManagerFactory.get().updateConsume(consumesCopy);
                        consumes.setConsumeAmount((Float) newValue);
                    }
                    break;
                case "Date":
                    if (newValue instanceof Date) {
                        consumesCopy.setAnimalGroup((AnimalGroupBean) newValue);
                        ConsumesManagerFactory.get().updateconsume(consumesCopy);
                        consumes.setAnimalGroup((AnimalGroupBean) newValue);
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
    
    /**
     * Sets up event handlers for UI components.
     */
    private void setupEventHandlers() {
        btnSearch.setOnAction(this::handleSearchAction);
        btnAdd.setOnAction(this::handleCreateAction);
        itemDelete.setOnAction(this::handleDeleteAction);
//        itemOpen.setOnAction(this::handleOpenAction);
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
                searchField.setVisible(true);
                dpDateFrom.setVisible(false);
                dpDateTo.setVisible(false);
                searchField.setPromptText("Enter product name");
                break;
            case "Animal Group":
                searchField.setVisible(true);
                dpDateFrom.setVisible(false);
                dpDateTo.setVisible(false);
                searchField.setPromptText("Enter animal group name");
                break;
            case "Date":
                    searchField.setVisible(false);
                    dpDateFrom.setVisible(true);
                    dpDateTo.setVisible(true);
                break;
        }
    }

    /**
     * Handles the search action.
     */
    private void handleSearchAction(ActionEvent event) {
        try {
            LOGGER.info("Handling search action.");
                String searchText = searchField.getText();
                String searchType = comboSearch.getValue();
                List<ConsumesBean> ConsumesList = null;
                //Faltaaaaaaaaaaaaaaaaaa
               //Validaciones de formato correcto
               //parsear el tipo de dato string del searchText a el tipo que se pida para enviar
               
            switch (searchType) {
                case "Animal Group":
                    searchField.setVisible(true);
                    dpDateFrom.setVisible(false);
                    dpDateTo.setVisible(false);
                    if(searchField.getText() != null && !searchField.getText().isEmpty()){
                      
                        ConsumesList = ConsumesManagerFactory.get().findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {}, Long.parseLong(searchField.getText()));  
                    }
                    else{
                         consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {});
                    }
                    break;
                case "Product":
                    searchField.setVisible(true);
                    dpDateFrom.setVisible(false);
                    dpDateTo.setVisible(false);
                    if(searchField.getText() != null && !searchField.getText().isEmpty()){
                        ConsumesList = ConsumesManagerFactory.get().findConsumesByProduct(new GenericType<List<ConsumesBean>>() {}, Long.parseLong(searchField.getText()));
                    }
                    else{
                         consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {});
                    }
                    break;
                case "Date":
                    searchField.setVisible(false);
                    dpDateFrom.setVisible(true);
                    dpDateTo.setVisible(true);
                    String from = (!dpDateFrom.getValue().toString().isEmpty()) ? dpDateFrom.getValue().toString() : null;
                    String to = (!dpDateTo.getValue().toString().isEmpty()) ? dpDateTo.getValue().toString() : null;
    
                    if (from != null && to != null){
                        ConsumesList = ConsumesManagerFactory.get().getConsumesByDate(new GenericType<List<ConsumesBean>>() {}, from, to);
                    }
                    else if(from != null){
                        ConsumesList = ConsumesManagerFactory.get().getConsumesByDateFrom(new GenericType<List<ConsumesBean>>() {}, from);
                    }
                    else if(to != null){
                        ConsumesList = ConsumesManagerFactory.get().getConsumesByDateTo(new GenericType<List<ConsumesBean>>() {}, to);
                    }
                    else{
                       ConsumesList= consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>() {});
                    }
                    break;
            
            }

            tableConsumes.setItems(FXCollections.observableArrayList(ConsumesList));
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
    }  //Mirar como hacer bien el create

    /**
     * Handles the delete action.
     */
    private void handleDeleteAction(Event event) {
        try {
            ConsumesBean selectedConsume = tableConsumes.getSelectionModel().getSelectedItem();
//            if (selectedConsume != null) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                    "Are you sure you want to delete this consume?",
//                    ButtonType.OK, ButtonType.CANCEL);
//                
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    consumesClient.deleteConsume(
//                        selectedConsume.getConsumesId().getProductId().toString(),
//                        selectedConsume.getConsumesId().getAnimalGroupId().toString()
//                    );
//                    loadTableData();
//                    LOGGER.info("Consume deleted successfully.");
//                }
//            }
        } catch (WebApplicationException e) {
            String errorMsg = "Error deleting consume: " + e.getMessage();
            showErrorAlert(errorMsg);
            LOGGER.log(Level.SEVERE, errorMsg);
        }
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
            List<ConsumesBean> consumes = consumesClient.getAllConsumes(new GenericType<List<ConsumesBean>>(){});
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
    
        /**
     * Action event handler for print button. It shows a JFrame containing a report.
     * This JFrame allows to print the report.
     * @param event The ActionEvent object for the event.
     */
//    @FXML
//    private void handleImprimirAction(ActionEvent event){
//        try {
//            LOGGER.info("Beginning printing action...");
//            JasperReport report=
//                JasperCompileManager.compileReport(getClass()
//                    .getResourceAsStream("/javafxapplicationud3example/ui/report/newReport1.jrxml"));
//            //Data for the report: a collection of UserBean passed as a JRDataSource 
//            //implementation 
//            JRBeanCollectionDataSource dataItems=
//                    new JRBeanCollectionDataSource((Collection<UserBean>)this.tbUsers.getItems());
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
//            //If there is an error show message and
//            //log it.
//            showErrorAlert("Error al imprimir:\n"+
//                            ex.getMessage());
//            LOGGER.log(Level.SEVERE,
//                        "UI GestionUsuariosController: Error printing report: {0}",
//                        ex.getMessage());
//        }
//    }
//    /**
//     * Action event handler for help button. It shows a Stage containing a scene 
//     * with a web viewer showing a help page for the window.
//     * @param event The ActionEvent object for the event.
//     */
//    @FXML
//    private void handleHelpAction(ActionEvent event){
//        try{
//            LOGGER.info("Loading help view...");
//            //Load node graph from fxml file
//            FXMLLoader loader=
//                new FXMLLoader(getClass().getResource("/javafxapplicationud3example/ui/view/Help.fxml"));
//                Parent root = (Parent)loader.load();
//                HelpController helpController=
//                        ((HelpController)loader.getController());
//                //Initializes and shows help stage
//                helpController.initAndShowStage(root);
//        }catch(Exception ex){
//                //If there is an error show message and
//                //log it.
//                showErrorAlert("Error al mostrar ventana de ayuda:\n"+
//                                ex.getMessage());
//                LOGGER.log(Level.SEVERE,
//                            "UI GestionUsuariosController: Error loading help window: {0}",
//                            ex.getMessage());
//        }
//
//    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
