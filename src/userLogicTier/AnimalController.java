/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userLogicTier;

import DTO.AnimalBean;
import DTO.AnimalGroupBean;
import DTO.SpeciesBean;
import cellFactories.DatePickerTableCell;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Label;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author Aitziber
 */
public class AnimalController implements Initializable {
    
    
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
    private Label lblFrom;
    
    @FXML
    private Label lblTo;
    
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
    private TableColumn<AnimalBean,AnimalGroupBean> tcAnimalGroup;
    @FXML
    private TableColumn<AnimalBean,String> tcSubespecies;
    @FXML
    private TableColumn<AnimalBean, SpeciesBean> tcSpecies;
    @FXML
    private TableColumn tcConsume;
    
    @FXML
    private MenuItem miDelete;


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

            // Cargar los elementos en el combo de búsqueda
            comboSearch.getItems().addAll("Subespecies", "Animal Group", "Birthdate");
    //
    //        // Seleccionar por defecto "Animal Group"
            comboSearch.setValue("Animal Group");
    //        
    //        // Escuchar cambios en el ComboBox
            comboSearch.valueProperty().addListener(this::handleComboBoxChange);
    //
    //        // Limpiar y habilitar el campo tfSearch
    //        tfSearch.clear();
    //        tfSearch.setDisable(false);
    //        
    //        // Enfocar el campo de búsqueda
            tfSearch.requestFocus();  
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
            
            animalGroupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, "1");             
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
           
            ////////////////////////////////////////////////////////////////
            tcConsume.setCellValueFactory(new PropertyValueFactory<>("monthlyConsume"));
            tcConsume.setStyle("-fx-alignment: center-right;");
            ////////////////////////////////////////////////////////////////

            miDelete.setDisable(true);
            tbAnimal.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalBean>) change -> {
                miDelete.setDisable(tbAnimal.getSelectionModel().getSelectedItems().isEmpty());
            });
            miDelete.setOnAction(this::onDeleteMenuItemClicked);
            
          
            //aqui crear una nueva linea para introducir un animal
            btnAdd.setDisable(false);
            btnAdd.setOnAction(this::onAddBUttonClicked);

            tbAnimal.setEditable(true);
            //        stage.show(); 
            
            showAllAnimals();
    }  
    
    private void onAddBUttonClicked(ActionEvent event){
        //aqui crear una nueva linea para introducir un animal
        AnimalBean newAnimal = new AnimalBean();
        
        newAnimal.setName("New Animal");
        newAnimal.setSubespecies("Unknown");
        newAnimal.setBirthdate(new Date());
        newAnimal.setMonthlyConsume(0);
        
        // si tenemos un grupop animal filtrado asignara ese
        // si no, traigo todos los grupos animales y asigno el primero
        
        List<AnimalGroupBean> availableAnimalGroups = new ArrayList<AnimalGroupBean>();
        availableAnimalGroups = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {}, "1");
        
        if (availableAnimalGroups != null && !availableAnimalGroups.isEmpty()) {
            newAnimal.setAnimalGroup(availableAnimalGroups.get(0));
        } else {
            System.out.println("No se encontraron grupos de animales para el managerId: 1");
        }
        
        List<SpeciesBean> availableSpecies = new ArrayList<SpeciesBean>();
        availableSpecies = SpeciesManagerFactory.get().getAllSpecies(new GenericType<List<SpeciesBean>>() {});
       
        if (availableSpecies != null && !availableSpecies.isEmpty()) {
            newAnimal.setSpecies(availableSpecies.get(0));
        } else {
            System.out.println("No se encontraron especies");
        }
       
        AnimalManagerFactory.get().createAnimal(newAnimal);
        
        showAllAnimals();
      
//        tbAnimal.refresh();
        
        //poner en modo edicion la casilla que contenga en la columna name "New Animal"
        final int frow;
        for (int row = 0; row < tbAnimal.getItems().size(); row++) {
            AnimalBean animal = tbAnimal.getItems().get(row);
            if (animal.getName().equals("New Animal")) {                
//                tbAnimal.edit(row, tcName);
               
                frow=row;
                Platform.runLater(() -> tbAnimal.edit(frow, tcName));
                        
                System.out.println(tbAnimal.getEditingCell());
                
              
//                tbAnimal.getEditingCell().startEdit();
            
//                tbAnimal.edit(row, TableColumn<AnimalBean, String> tcName);
                tbAnimal.refresh();
                break;
            }
        }
        
//        tbAnimal.refresh();
        
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
   
    private void handleComboBoxChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if ("Birthdate".equals(newValue)) {
            // Ocultar el campo de búsqueda de texto
            tfSearch.setVisible(false);
            tfSearch.clear();
            
            // Mostrar los campos de fecha
//            lblFrom.setVisible(true);
//            lblTo.setVisible(true);
//            dpSearchFrom.setVisible(true);
//            dpSearchTo.setVisible(true);
        } else if ("Subespecies".equals(newValue)|| "Animal Group".equals(newValue)) {            
            // Ocultar los campos de fecha
//            lblFrom.setVisible(false);
//            lblTo.setVisible(false);
//            dpSearchFrom.setVisible(false);
//            dpSearchTo.setVisible(false);
//            dpSearchFrom.clear();
//            dpSearchTo.clear();
            
            // Mostrar el campo de búsqueda de texto
            tfSearch.setVisible(true);
            tfSearch.clear();
        }
    }
    
    private void onSearchButtonClicked(ActionEvent event) {
        String searchType = comboSearch.getValue();
        try {
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

            if (animalList != null) {
                ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(animalList);
                tbAnimal.setItems(animalData);
            }
        } catch (WebApplicationException e) {
            System.err.println("Error fetching animals: " + e.getMessage());
        }
    }
    
    private void showAllAnimals() {
        try {
            System.out.println("CAMBIAR showAllAnimals, id de manager estatico!!");
            List<AnimalBean> allAnimals = AnimalManagerFactory.get().getAllAnimals(new GenericType<List<AnimalBean>>() {}, "1");
            ObservableList<AnimalBean> animalData = FXCollections.observableArrayList(allAnimals);
            tbAnimal.setItems(animalData);
        } catch (WebApplicationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los animales: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}