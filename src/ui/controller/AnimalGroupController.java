/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import businessLogic.animalGroup.AnimalGroupFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import DTO.AnimalGroupBean;
import DTO.ManagerBean;
import ui.cellFactories.DatePickerTableCell;
import ui.utilities.WindowManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Ander
 */
public class AnimalGroupController implements Initializable {

    /**
     * Logger to track the class activity and handle debugging information.
     */
    private static final Logger logger = Logger.getLogger(AnimalGroupController.class.getName());

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnSearch;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<AnimalGroupBean> tbAnimalGroup;

    @FXML
    private TableColumn<AnimalGroupBean, String> tcName;

    @FXML
    private TableColumn<AnimalGroupBean, String> tcDescription;

//    @FXML
//    private TableColumn tcAnimals;
    @FXML
    private TableColumn tcConsume;

    @FXML
    private TableColumn tcDate;

    @FXML
    private MenuItem miDelete;

    @FXML
    private MenuItem miOpen;

//    @FXML
//    private MenuItem miAnimals;
//
//    @FXML
//    private MenuItem miConsumes;
//
//    @FXML
//    private MenuItem miProducts;
//
//    @FXML
//    private MenuItem miPrint;

    @FXML
    private TableColumn<AnimalGroupBean, String> tcArea;

    private ObservableList<AnimalGroupBean> groupData;

    private static ManagerBean manager;
    
    public static void setManager(ManagerBean manager) {
        AnimalGroupController.manager = manager;
    }

    public static ManagerBean getManager() {
        return manager;
    }
    

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tbAnimalGroup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            logger.log(Level.INFO, "Initilizing Animal Group controller");

            // Focus on searchField
            searchField.requestFocus();
            // Search button set to default button (when pressed intro)
            btnSearch.setDefaultButton(true);

            // BUTTONS
            btnSearch.setOnAction(this::onSearchButtonClicked);
            btnCreate.setOnAction(this::onCreateButtonClicked);
            btnLogOut.setOnAction(this::onLogOutButtonClicked);

            // MENUITEMS
            miDelete.setDisable(true);
            // Disabled until some item is selected
            tbAnimalGroup.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalGroupBean>) change -> {
                miDelete.setDisable(tbAnimalGroup.getSelectionModel().getSelectedItems().isEmpty());
            });
            miDelete.setOnAction(this::onDeleteMenuItemClicked);
            miOpen.setDisable(true);
            // Disabled if there is no selection OR multiple selection
            tbAnimalGroup.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalGroupBean>) change -> {
                if (tbAnimalGroup.getSelectionModel().getSelectedItems().isEmpty() || tbAnimalGroup.getSelectionModel().getSelectedItems().size() > 1) {
                    miOpen.setDisable(true);
                } else {
                    miOpen.setDisable(false);
                }
            });

            miOpen.setOnAction(this::onOpenMenuItemClicked);

//            miPrint.setOnAction(this::handlePrintAction);

            // COLUMNS
            // Name column
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcName.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcName.setOnEditCommit(event -> handleEditCommit(event, "name"));

            // Description column
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcDescription.setOnEditCommit(event -> handleEditCommit(event, "description"));

            // Area column
            tcArea.setCellValueFactory(new PropertyValueFactory<>("area"));
            tcArea.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcArea.setOnEditCommit(event -> handleEditCommit(event, "area"));

            // Animals column
//            tcAnimals.setCellValueFactory(new PropertyValueFactory<>("animals"));
//            tcAnimals.setStyle("-fx-alignment: center;");
            // Consumes column
            tcConsume.setCellValueFactory(new PropertyValueFactory<>("consume"));
            tcConsume.setStyle("-fx-alignment: center;");

            // Creation date column
            tcDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
            tcDate.setStyle("-fx-alignment: center;");
            tcDate.setCellFactory(new Callback<TableColumn<AnimalGroupBean, Date>, TableCell<AnimalGroupBean, Date>>() {
                @Override
                public TableCell<AnimalGroupBean, Date> call(TableColumn<AnimalGroupBean, Date> param) {
                    DatePickerTableCell<AnimalGroupBean> cell = new DatePickerTableCell<>(param);
                    cell.updateDateCallback = (Date updatedDate) -> {
                        try {
                            updateAnimalGroupByDate(updatedDate);
                        } catch (CloneNotSupportedException ex) {
                            logger.log(Level.SEVERE, "Error updating animal group: ", ex);
                        }
                    };
                    return cell;
                }
            });

            // Table is editable
            tbAnimalGroup.setEditable(true);
            
            if (manager != null) {
                showAnimalGroups();
            }
            
        } catch (Exception e) {
            // If something goes wrong
            logger.log(Level.SEVERE, "Something went wrong: ", e);
        }
    }

    private <T> void handleEditCommit(CellEditEvent<AnimalGroupBean, T> event, String column) {
        try {
            // Get the position of the event
            TablePosition<AnimalGroupBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            // Control that the value is a String or is not empty
            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("The value is not valid");
            }

            // Get the position of the row from the position of the event
            int row = pos.getRow();
            // Get the object from the row
            AnimalGroupBean group = event.getTableView().getItems().get(row);
            // Make a copy of the object
            AnimalGroupBean groupCopy = group.clone();

            switch (column) {
                case "name":
                    if (newValue instanceof String) {
                        try {
                            // Checks if the name already exists
                            List<AnimalGroupBean> groupList = tbAnimalGroup.getItems();
                            for (AnimalGroupBean ag : groupList) {
                                if (ag.getName().equalsIgnoreCase(newValue.toString())) {
                                    throw new Exception("An animal group with the same name already exists, please try another name");
                                }
                            }
                            groupCopy.setName((String) newValue);
                            AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                            group.setName((String) newValue);
                        } catch (Exception e) {
                            showErrorAlert("ERROR", "Animal group already exists", e.getMessage());
                        }
                    }
                    break;
                case "description":
                    if (newValue instanceof String) {
                        groupCopy.setDescription((String) newValue);
                        AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                        group.setDescription((String) newValue);
                    }
                    break;
                case "area":
                    if (newValue instanceof String) {
                        groupCopy.setArea((String) newValue);
                        AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                        group.setArea((String) newValue);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("The value is not valid: " + column);
            }

            event.getTableView().refresh();

        } catch (CloneNotSupportedException | IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Error editing Animal Group: ", e);

        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error fetching animal groups: ", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    private void updateAnimalGroupByDate(Date updatedDate) throws CloneNotSupportedException {
        AnimalGroupBean group = (AnimalGroupBean) tbAnimalGroup.getSelectionModel().getSelectedItem();
        if (group != null && updatedDate != null) {
            try {
                // Check that the updated date is before the actual date
                if (updatedDate.after(new Date())) {
                    throw new Exception("The new date can not be after the actual date. Please insert a valid date");
                }
                AnimalGroupBean groupCopy = group.clone();
                groupCopy.setCreationDate(updatedDate);
                AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                group.setCreationDate(updatedDate);
                logger.log(Level.INFO, "Animal group updated. New Date: {0}", group.getCreationDate());
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error updating", e);
                showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
            } catch (Exception ex) {
                showErrorAlert("ERROR", "Invalid date", ex.getMessage());
            }
        }
    }

    private void onSearchButtonClicked(ActionEvent event) {
        try {
            List<AnimalGroupBean> groupList = null;
            if (searchField.getText() != null && !searchField.getText().isEmpty()) {
                logger.log(Level.INFO, "Searching group/s by name and manager id");
                groupList = AnimalGroupFactory.get().getAnimalGroupByName(new GenericType<List<AnimalGroupBean>>() {
                }, searchField.getText(), manager.getId().toString());
                logger.log(Level.INFO, "Group/s gotten");
            } else {
                showAnimalGroups();
            }

            if (groupList != null) {
                ObservableList<AnimalGroupBean> filteredData = FXCollections.observableArrayList(groupList);
                tbAnimalGroup.setItems(filteredData);
            } else {
                logger.log(Level.INFO, "No animal groups found");
            }
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error fetching animal groups: ", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    @FXML
    private void onCreateButtonClicked(ActionEvent event) {
        try {
            // Create new group
            AnimalGroupBean group = new AnimalGroupBean();
            // Set attributes
            String groupName = "Group " + (int) (Math.random() * 1000000);
            group.setName(groupName);
            group.setDescription("Group of animals");
            group.setArea("Undefined zone");
            // Make a list for the managers (just actual manager but has to be a list)
            List<ManagerBean> managers = new ArrayList<>();
            managers.add(manager);
            group.setManagers(managers);
            // Actual date
            group.setCreationDate(new Date());
            // group.setConsumes(0);

            logger.log(Level.INFO, "Creating animal group: {0}", group.toString());
            AnimalGroupFactory.get().createAnimalGroup(group);

            showAnimalGroups();

            // Set in edition mode the new animal group (searched by the new name)
            final int NEW_GROUP_ROW;
            for (int row = 0; row < tbAnimalGroup.getItems().size(); row++) {
                group = (AnimalGroupBean) tbAnimalGroup.getItems().get(row);
                if (group.getName().equals(groupName)) {
                    NEW_GROUP_ROW = row;
                    Platform.runLater(() -> tbAnimalGroup.edit(NEW_GROUP_ROW, tcName));
                    tbAnimalGroup.refresh();
                    break;
                }
            }
        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error creating animal group", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    private void onDeleteMenuItemClicked(ActionEvent event) {
        try {
            ObservableList<AnimalGroupBean> selectedGroups = tbAnimalGroup.getSelectionModel().getSelectedItems();

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected Animal Groups?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    for (AnimalGroupBean agb : selectedGroups) {
                        AnimalGroupFactory.get().deleteAnimalGroupById(agb.getId().toString());
                    }
                } catch (WebApplicationException e) {
                    logger.log(Level.SEVERE, "Error deleting animal/s", e);
                }
            }
            showAnimalGroups();

        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error creating animal group", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    private void onOpenMenuItemClicked (ActionEvent event){
        try {

            AnimalGroupBean group = (AnimalGroupBean) tbAnimalGroup.getSelectionModel().getSelectedItem();
            logger.log(Level.INFO, "Opening animal group: {0}", group.getName());
            ((Scene) tbAnimalGroup.getScene()).getWindow().hide();
            WindowManager.openAnimalViewWithAnimalGroup("/ui/view/Animal.fxml", "Animal", manager, group);

        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error opening window: ", e);
        }
    }

//    private void handlePrintAction(ActionEvent event) {
//        try {
//            logger.info("Beginning printing action...");
//
//            JasperReport report
//                    = JasperCompileManager.compileReport(getClass()
//                            .getResourceAsStream("/reports/AnimalGroupReport.jrxml"));
//            Data for the report: a collection of UserBean passed as a JRDataSource 
//            implementation 
//            JRBeanCollectionDataSource dataItems
//                    = new JRBeanCollectionDataSource((Collection<AnimalGroupBean>) this.tbAnimalGroup.getItems());
//            Map of parameter to be passed to the report
//            Map<String, Object> parameters = new HashMap<>();
//            Fill report with data
//            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
//            Create and show the report window. The second parameter false value makes 
//            report window not to close app.
//            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
//            jasperViewer.setVisible(true);
//             jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//        } catch (JRException ex) {
//            If there is an error show message and
//            log it.
//            showErrorAlert("Error al imprimir:\n"+
//                            ex.getMessage());
//
//        }
//    }

    private void onLogOutButtonClicked(ActionEvent event) {
        logger.log(Level.INFO, "Log out action initiated");

        if (showLogoutConfirmation()) {
            try {
                Stage home = (Stage) btnLogOut.getScene().getWindow();
                home.close();
                WindowManager.openWindow("/ui/view/SignIn.fxml", "SignIn");

                logger.log(Level.INFO, "Successfully navigated to Sign In screen.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error during logout process.", e);
            }
        } else {
            logger.log(Level.INFO, "Log out cancelled by user.");
        }
    }

    /**
     * Displays a confirmation dialog for the logout action.
     *
     * @return {@code true} if the user confirms the logout, {@code false} otherwise
     */
    private boolean showLogoutConfirmation() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText("This window will be closed.");
            alert.setContentText("Are you sure you want to log out?");

            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error showing logout confirmation", e);
            return false;
        }
    }

    private void showAnimalGroups() {
        try {
            // Get animal groups
            logger.log(Level.INFO, "Getting animal groups");
            if (manager != null) {
                List<AnimalGroupBean> groupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {
                }, manager.getId().toString());
                logger.log(Level.INFO, "Animal groups gotten");
                // Show animal groups
                groupData = FXCollections.observableArrayList(groupList);
                tbAnimalGroup.setItems(groupData);
            } else {
                logger.log(Level.WARNING, "Manager is null");
            }

        } catch (WebApplicationException e) {
            logger.log(Level.SEVERE, "Error fetching animal groups", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Something is null: ", e);
        }
    }

    /**
     * Helper method to display an error alert with a custom title and message.
     *
     * @param head the error type.
     * @param title title of the alert dialog.
     * @param message message content of the alert dialog.
     */
    private void showErrorAlert(String head, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(head);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
