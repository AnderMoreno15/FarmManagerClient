/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import businessLogic.animalGroup.AnimalGroupFactory;
import businessLogic.animal.AnimalManagerFactory;
import businessLogic.species.SpeciesManagerFactory;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.view.JasperViewer;
import java.util.Collection;
import java.util.Map;
//import net.sf.jasperreports.engine.JasperPrint;


/**
 * FXML Controller class
 *
 * @author Aitziber
 */
public class AnimalController implements Initializable {
    
    /**
     * Logger to track the class activity and handle debugging information.
     */
    private static final Logger logger = Logger.getLogger(AnimalController.class.getName());
    
    
    private Stage stage;
    
    @FXML
    private ComboBox<String> comboSearch;
    
    @FXML
    private TextField tfSearch;
    
    @FXML
    private Button btnSearch;

    @FXML
    private Button btnAdd;
    
    @FXML
    private Button btnPrint;
    
    @FXML
    private Text txtFrom;
    
    @FXML
    private Text txtTo;
    
    @FXML
    private DatePicker dpSearchFrom;

    @FXML
    private DatePicker dpSearchTo;
    
    @FXML
    private TableView<AnimalBean> tbAnimal;
    
    @FXML
    private TableColumn<AnimalBean,String> tcName;
    @FXML
    private TableColumn<AnimalBean,Date> tcBirthdate;
    @FXML
    private TableColumn<AnimalBean, AnimalGroupBean> tcAnimalGroup;
    @FXML
    private TableColumn<AnimalBean,String> tcSubespecies;
    @FXML
    private TableColumn<AnimalBean, SpeciesBean> tcSpecies;
    
    @FXML
    private MenuItem miDelete;
    
    @FXML
    private StackPane stack;
    
    @FXML
    private HBox hboxDatePicker;
    
    private static ManagerBean manager;
    
    public static void setManager(ManagerBean manager) {
        AnimalController.manager = manager;
    }

    public static ManagerBean getManager() {
        return manager;
    }
    
    private static AnimalGroupBean conditionalAnimalGroup;

    public static AnimalGroupBean getConditionalAnimalGroup() {
        return conditionalAnimalGroup;
    }

    public static void setConditionalAnimalGroup(AnimalGroupBean conditionalAnimalGroup) {
        AnimalController.conditionalAnimalGroup = conditionalAnimalGroup;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
     
        
        tbAnimal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    //        // Establecer el título de la ventana
    //        stage.setTitle("Animals");
    //
    //        // Establecer dimensiones fijas
    //        stage.setWidth(1024);
    //        stage.setHeight(720);

    //        // Deshabilitar la redimensión de la ventana
    //        stage.setResizable(false);

    //        // Limpiar los campos de fechas
    //        dpSearchFrom.setValue(null);
    //        dpSearchTo.setValue(null);
    //
    //        // Hacer los campos de fechas invisibles
    //        dpSearchFrom.setVisible(false);
    //        dpSearchTo.setVisible(false);

    tfSearch.setText("");
            // Cargar los elementos en el combo de búsqueda
            comboSearch.getItems().addAll("Subespecies", "Animal Group", "Birthdate");
    //
    //        // Seleccionar por defecto "Animal Group"
            comboSearch.setValue("Animal Group");
    //        
    //        // Escuchar cambios en el ComboBox
            comboSearch.valueProperty().addListener(this::handleComboBoxChange);
            
            tfSearch.toFront();
            // Enfocar el campo de búsqueda
//            tfSearch.requestFocus();  
            
            showDateFields(false);

    //        
    //         // Habilitar los botones
            btnSearch.setDisable(false);
    //        btnAdd.setDisable(false);
    //
    //        // Establecer "Search" como el botón por defecto
            btnSearch.setDefaultButton(true);
            btnSearch.setOnAction(this::onSearchButtonClicked);  
        
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

    //        Animal Group y Species: combo no editable con el valor del nombre 
    //        del Animal Group y el valor del nombre de la Species asociados 
    //        seleccionados. Edición ComboBoxTableCell

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
            
//            btnAdd.setDisable(false);
            btnAdd.setOnAction(this::onAddButtonClicked);

            tbAnimal.setEditable(true);
            //        stage.show(); 
            
//            showAllAnimals();
            
//            if (this.conditionalAnimalGroup != null) {
//                tfSearch.setText(conditionalAnimalGroup.getName());
//                btnSearch.fire();
//            } else{
//                showAllAnimals();
//            }
//            
//            btnPrint.setOnAction(this::handlePrintAction); 
    }
    
    private void handleComboBoxChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if ("Birthdate".equals(newValue)) {
            // Ocultar el campo de búsqueda de texto
            tfSearch.toBack();
            tfSearch.setVisible(false);
            // Mostrar los campos de fecha
            showDateFields(true);
        } else if ("Subespecies".equals(newValue) || "Animal Group".equals(newValue)) {
            // Ocultar los campos de fecha
            showDateFields(false);
            // Mostrar el campo de búsqueda de texto
            tfSearch.clear();
            tfSearch.toFront();
            tfSearch.setVisible(true);
        }
    }
    
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
    
    private void onSearchButtonClicked(ActionEvent event) {
        try {
            String searchType = comboSearch.getValue();
            List<AnimalBean> animalList = null;

            switch (searchType) {
                case "Animal Group":
                    if(tfSearch.getText() != null && !tfSearch.getText().isEmpty()){
                        animalList = AnimalManagerFactory.get().getAnimalsByAnimalGroup(new GenericType<List<AnimalBean>>() {}, tfSearch.getText());
                    }
                    else{
                         showAllAnimals();
                    }
                    break;
                case "Subespecies":
                    if(tfSearch.getText() != null && !tfSearch.getText().isEmpty()){
                        animalList = AnimalManagerFactory.get().getAnimalsBySubespecies(new GenericType<List<AnimalBean>>() {}, tfSearch.getText());
                    }
                    else{
                         showAllAnimals();
                    }
                    break;
                case "Birthdate":
                    String from = (!dpSearchFrom.getValue().toString().isEmpty()) ? dpSearchFrom.getValue().toString() : null;
                    String to = (!dpSearchTo.getValue().toString().isEmpty()) ? dpSearchTo.getValue().toString() : null;
    
                    if (from != null && to != null){
                        animalList = AnimalManagerFactory.get().getAnimalsByBirthdate(new GenericType<List<AnimalBean>>() {}, from, to);
                    }
                    else if(from != null){
                        animalList = AnimalManagerFactory.get().getAnimalsByBirthdateFrom(new GenericType<List<AnimalBean>>() {}, from);
                    }
                    else if(to != null){
                        animalList = AnimalManagerFactory.get().getAnimalsByBirthdateTo(new GenericType<List<AnimalBean>>() {}, to);
                    }
                    else{
                        showAllAnimals();
                    }
                    break;
            }

            if (animalList != null && !animalList.isEmpty()) {
                ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(animalList);
                tbAnimal.setItems(animalData);
                btnAdd.setDisable(false);
            }
            
        } catch (WebApplicationException e) {
            System.err.println("Error fetching animals: " + e.getMessage());
        }
    }
    
    private <T> void handleEditCommit(CellEditEvent<AnimalBean, T> event, String fieldName) {
        try {
            TablePosition<AnimalBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("El valor ingresado no es válido.");
            }

            int row = pos.getRow();
            AnimalBean animal = event.getTableView().getItems().get(row);
            AnimalBean animalCopy = animal.clone();

            // actualiza en la capa lógica y también en el objeto original
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
                    throw new IllegalArgumentException("Campo desconocido: " + fieldName);
            }

            event.getTableView().refresh();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
        }
    }
    
    private void updateAnimalBirthdate(Date updatedDate) throws CloneNotSupportedException {
        AnimalBean animal = tbAnimal.getSelectionModel().getSelectedItem();
        if (animal != null && updatedDate != null) {
            AnimalBean animalCopy = animal.clone();
            animalCopy.setBirthdate(updatedDate);
            try {
                AnimalManagerFactory.get().updateAnimal(animalCopy);
                animal.setBirthdate(updatedDate);
                
                
            } catch (WebApplicationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    
    private void onDeleteMenuItemClicked(ActionEvent event) {
        ObservableList<AnimalBean> selectedAnimals = tbAnimal.getSelectionModel().getSelectedItems();
        List<AnimalBean> successfullyDeleted = new ArrayList<>();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected animals?", ButtonType.YES, ButtonType.NO);   
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                for (AnimalBean selectedAnimal : selectedAnimals) {
                    try {
                        System.out.println(selectedAnimal.toString());
                        AnimalManagerFactory.get().deleteAnimalById(String.valueOf(selectedAnimal.getId()));
                        successfullyDeleted.add(selectedAnimal);
                
                    } catch (WebApplicationException e) {
                        System.err.println("Error deleting animal: " + selectedAnimal.getName() + " - " + e.getMessage());
                    }
                }

                if (!successfullyDeleted.isEmpty()) {
                    tbAnimal.getItems().removeAll(successfullyDeleted);
                    tbAnimal.getSelectionModel().clearSelection();
                    tbAnimal.refresh();
                }

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, 
                    "Unexpected error during deletion: " + e.getMessage(), 
                    ButtonType.OK);
                errorAlert.showAndWait();
            }
        }     
    }
    
    private void onAddButtonClicked(ActionEvent event){

        AnimalBean newAnimal = new AnimalBean();
        //crear constructor por defecto
        newAnimal.setName("New Animal");
        newAnimal.setSubespecies("Unknown");
        newAnimal.setBirthdate(new Date());
        newAnimal.setMonthlyConsume(0);
        
        String filterType = comboSearch.getValue();
        String filterValue = tfSearch.getText();

        // Lógica para asignar AnimalGroup o Subespecie según el filtro
        if ("Animal Group".equals(filterType)) {
            if (filterValue != null && !filterValue.isEmpty()) {
                AnimalGroupBean choiceAnimalGroup = AnimalGroupFactory.get().getAnimalGroupByName(new GenericType<AnimalGroupBean>() {}, filterValue, String.valueOf(manager.getId()));
                // internal server error si no hay coincidencias, tratarlo y darle un defaultAnimalGroup

                if (choiceAnimalGroup != null) {
                    newAnimal.setAnimalGroup(choiceAnimalGroup);
                } else {
                    setDefaultAnimalGroup(newAnimal);
                }
            }
        } else if ("Subespecies".equals(filterType)) {
            if (filterValue != null && !filterValue.isEmpty()) {
                // Asignar la subespecie basada en el filtro
                newAnimal.setSubespecies(filterValue);
                setDefaultAnimalGroup(newAnimal);
            }
        }

        List<SpeciesBean> availableSpecies = new ArrayList<SpeciesBean>();
        availableSpecies = SpeciesManagerFactory.get().getAllSpecies(new GenericType<List<SpeciesBean>>() {});
       
        if (availableSpecies != null && !availableSpecies.isEmpty()) {
            newAnimal.setSpecies(availableSpecies.get(0));
        } else {
            System.out.println("No se encontraron especies");
        }
       
        AnimalManagerFactory.get().createAnimal(newAnimal);
        
        //lanzar accion btnSearch
        btnSearch.fire();

        //poner en modo edicion la casilla que contenga en la columna name "New Animal"
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
    
    private void setDefaultAnimalGroup(AnimalBean newAnimal){
        List<AnimalGroupBean> availableAnimalGroups = new ArrayList<AnimalGroupBean>();
        availableAnimalGroups = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, String.valueOf(manager.getId()));
        
        if (availableAnimalGroups != null && !availableAnimalGroups.isEmpty()) {
            newAnimal.setAnimalGroup(availableAnimalGroups.get(0));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "First you need to create an animal group", ButtonType.OK);
            alert.showAndWait();
        }
    }
    
    private void showAllAnimals() {
        try {
            List<AnimalBean> allAnimals = AnimalManagerFactory.get().getAllAnimals(new GenericType<List<AnimalBean>>() {}, String.valueOf(manager.getId()));
                    
            if (allAnimals != null && !allAnimals.isEmpty()) {
                ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(allAnimals);
                tbAnimal.setItems(animalData);
                btnAdd.setDisable(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No tiene animales asociados", ButtonType.OK);
                alert.showAndWait();
            }
                    
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los animales: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    
//    private void handlePrintAction(ActionEvent event){
//        try {
//            logger.info("Beginning printing action...");
//           
//            JasperReport report=
//                JasperCompileManager.compileReport(getClass()
//                    .getResourceAsStream("/ui/reports/animalReport.jrxml"));
//            //Data for the report: a collection of UserBean passed as a JRDataSource 
//            //implementation 
//            JRBeanCollectionDataSource dataItems=
//                    new JRBeanCollectionDataSource((Collection<AnimalBean>)this.tbAnimal.getItems());
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
            //If there is an error show message and
            //log it.
//            showErrorAlert("Error al imprimir:\n"+
//                            ex.getMessage());
            
//        }
//    }
}