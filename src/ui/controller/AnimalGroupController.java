/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import businessLogic.animalGroup.AnimalGroupFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import DTO.AnimalGroupBean;
import DTO.ConsumesBean;
import DTO.ManagerBean;
import businessLogic.consumes.ConsumesManagerFactory;
import javafx.scene.control.ButtonType;
import javax.ws.rs.ProcessingException;
import ui.cellFactories.DatePickerTableCell;
import ui.utilities.WindowManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Controller for managing the Animal Group view.
 * <p>
 * This controller handles the display and editing of animal groups associated with a given manager. It supports the following functionalities:
 * <ul>
 * <li>Search animal groups by name and manager ID.</li>
 * <li>Create a new animal group.</li>
 * <li>Edit properties of an existing animal group (name, description, area, etc.).</li>
 * <li>Delete one or more selected animal groups.</li>
 * <li>Open related views (e.g. view animals, consumes, products) for a selected animal group.</li>
 * <li>Print a report of animal groups via JasperReports.</li>
 * <li>Log out of the current session.</li>
 * </ul>
 * <p>
 * The controller initializes the TableView, configures its columns and cell factories, sets up the appropriate event handlers for buttons and menu items, and fetches the animal groups based on the currently set manager.
 * </p>
 * <p>
 * <strong>Note:</strong> The manager must be set (using {@link #setManager(ManagerBean)}) before calling methods that depend on it (e.g. {@link #showAnimalGroups()}).
 * </p>
 *
 * @author Ander
 */
public class AnimalGroupController implements Initializable {

    /**
     * Logger to track the class activity and handle debugging information.
     */
    private static final Logger logger = Logger.getLogger(AnimalGroupController.class.getName());

    // FXML controls
    /**
     * Button to create a new animal group.
     */
    @FXML
    private Button btnCreate;

    /**
     * Button to log out of the current session.
     */
    @FXML
    private Button btnLogOut;

    /**
     * Button to trigger the search action.
     */
    @FXML
    private Button btnSearch;

    /**
     * Button to trigger the print action.
     */
    @FXML
    private Button btnPrint;

    /**
     * Text field for entering the name of an animal group to search.
     */
    @FXML
    private TextField searchField;

    /**
     * TableView displaying the list of animal groups.
     */
    @FXML
    private TableView<AnimalGroupBean> tbAnimalGroup;

    /**
     * TableColumn displaying the name of each animal group.
     */
    @FXML
    private TableColumn<AnimalGroupBean, String> tcName;

    /**
     * TableColumn displaying the description of each animal group.
     */
    @FXML
    private TableColumn<AnimalGroupBean, String> tcDescription;

    /**
     * TableColumn displaying the consumption amount associated with each animal group.
     */
    @FXML
    private TableColumn tcConsume;

    /**
     * TableColumn displaying the creation date of each animal group.
     */
    @FXML
    private TableColumn tcDate;

    /**
     * MenuItem for deleting selected animal groups.
     */
    @FXML
    private MenuItem miDelete;

    /**
     * MenuItem for opening the details of a selected animal group.
     */
    @FXML
    private MenuItem miOpen;

    /**
     * TableColumn displaying the area assigned to each animal group.
     */
    @FXML
    private TableColumn<AnimalGroupBean, String> tcArea;

    /**
     * The observable list of animal groups used to populate the TableView.
     */
    private ObservableList<AnimalGroupBean> groupData;

    /**
     * The currently logged-in manager.
     */
    private static ManagerBean manager;

    /**
     * Sets the manager for this controller.
     * <p>
     * This method must be called before loading data (e.g. calling {@link #showAnimalGroups()}).
     * </p>
     *
     * @param manager the manager to be set.
     */
    public static void setManager(ManagerBean manager) {
        AnimalGroupController.manager = manager;
    }

    /**
     * Returns the current manager.
     *
     * @return the current ManagerBean.
     */
    public static ManagerBean getManager() {
        return manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tbAnimalGroup.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            logger.log(Level.INFO, "Initializing Animal Group controller");

            // Set focus on the search field and make the search button the default.
            searchField.requestFocus();
            btnSearch.setDefaultButton(true);

            // Configure buttons.
            btnSearch.setOnAction(this::onSearchButtonClicked);
            btnCreate.setOnAction(this::onCreateButtonClicked);
            btnLogOut.setOnAction(this::onLogOutButtonClicked);
            btnPrint.setOnAction(this::handlePrintAction);

            // Configure menu items.
            miDelete.setDisable(true);
            // Enable the delete menu item only when at least one item is selected.
            tbAnimalGroup.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalGroupBean>) change -> {
                miDelete.setDisable(tbAnimalGroup.getSelectionModel().getSelectedItems().isEmpty());
            });
            miDelete.setOnAction(this::onDeleteMenuItemClicked);
            miOpen.setDisable(true);
            // Enable the open menu item only when exactly one item is selected.
            tbAnimalGroup.getSelectionModel().getSelectedItems().addListener((ListChangeListener<AnimalGroupBean>) change -> {
                if (tbAnimalGroup.getSelectionModel().getSelectedItems().isEmpty()
                        || tbAnimalGroup.getSelectionModel().getSelectedItems().size() > 1) {
                    miOpen.setDisable(true);
                } else {
                    miOpen.setDisable(false);
                }
            });
            miOpen.setOnAction(this::onOpenMenuItemClicked);
            // miPrint.setOnAction(this::handlePrintAction); // Uncomment if needed.

            // Configure table columns.
            // Name column.
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcName.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcName.setOnEditCommit(event -> handleEditCommit(event, "name"));

            // Description column.
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcDescription.setOnEditCommit(event -> handleEditCommit(event, "description"));

            // Area column.
            tcArea.setCellValueFactory(new PropertyValueFactory<>("area"));
            tcArea.setCellFactory(TextFieldTableCell.<AnimalGroupBean>forTableColumn());
            tcArea.setOnEditCommit(event -> handleEditCommit(event, "area"));

            // Consumes column.
            tcConsume.setCellValueFactory(new PropertyValueFactory<>("consume"));
            tcConsume.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            tcConsume.setStyle("-fx-alignment: center;");

            // Creation date column.
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

            // Make the table editable.
            tbAnimalGroup.setEditable(true);

            // Only load animal groups if the manager has been set.
            if (manager != null) {
                showAnimalGroups();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Something went wrong during initialization: ", e);
        }
    }

    /**
     * Handles the commit of an edit in a table cell.
     *
     * @param <T> the type of the cell value.
     * @param event the cell edit event.
     * @param column the name of the column being edited.
     */
    private <T> void handleEditCommit(CellEditEvent<AnimalGroupBean, T> event, String column) {
        try {
            // Retrieve the edited value and the table position.
            TablePosition<AnimalGroupBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            // Validate the new value.
            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("The value is not valid");
            }

            int row = pos.getRow();
            AnimalGroupBean group = event.getTableView().getItems().get(row);
            AnimalGroupBean groupCopy = group.clone();

            switch (column) {
                case "name":
                    if (newValue instanceof String) {
                        // Check for duplicated names.
                        List<AnimalGroupBean> groupList = tbAnimalGroup.getItems();
                        for (AnimalGroupBean ag : groupList) {
                            if (ag.getName().equalsIgnoreCase(newValue.toString())) {
                                throw new Exception("An animal group with the same name already exists, please try another name");
                            } else if (((String) newValue).matches(".*\\d.*")) {
                                throw new Exception("The name can't have any digit");
                            }
                        }
                        groupCopy.setName((String) newValue);
                        AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                        group.setName((String) newValue);
                    }
                    break;
                case "description":
                    if (newValue instanceof String) {
                        if (((String) newValue).matches(".*\\d.*")) {
                            throw new Exception("The description can't have any digit");
                        }
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
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing Animal Group: ", e);
            showErrorAlert("ERROR", "Error editing Animal Group: ", e.getMessage());
        } finally {
            event.getTableView().refresh();
        }
    }

    /**
     * Updates the creation date of the selected animal group.
     *
     * @param updatedDate the new creation date.
     * @throws CloneNotSupportedException if cloning of the animal group fails.
     */
    private void updateAnimalGroupByDate(Date updatedDate) throws CloneNotSupportedException {
        AnimalGroupBean group = tbAnimalGroup.getSelectionModel().getSelectedItem();
        if (group != null && updatedDate != null) {
            try {
                if (updatedDate.after(new Date())) {
                    throw new Exception("The new date cannot be after the actual date. Please insert a valid date");
                }
                AnimalGroupBean groupCopy = group.clone();
                groupCopy.setCreationDate(updatedDate);
                AnimalGroupFactory.get().updateAnimalGroup(groupCopy);
                group.setCreationDate(updatedDate);
                logger.log(Level.INFO, "Animal group updated. New Date: {0}", group.getCreationDate());
            } catch (WebApplicationException e) {
                logger.log(Level.SEVERE, "Error updating animal group", e);
                showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
            } catch (Exception ex) {
                showErrorAlert("ERROR", "Invalid date", ex.getMessage());
            }
        }
    }

    /**
     * Handles the search button click event.
     *
     * @param event the action event triggered by clicking the search button.
     */
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
        } catch (WebApplicationException | ProcessingException | NullPointerException e) {
            logger.log(Level.SEVERE, "Error fetching animal groups: ", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    /**
     * Handles the create button click event.
     * <p>
     * This method creates a new animal group with default values and assigns the current manager. It then refreshes the TableView and enters edit mode for the newly created group.
     * </p>
     *
     * @param event the action event triggered by clicking the create button.
     */
    @FXML
    private void onCreateButtonClicked(ActionEvent event) {
        try {
            AnimalGroupBean group = new AnimalGroupBean();
            String groupName = "New Group";
            group.setName(groupName);
            group.setDescription("Group of animals");
            group.setArea("Undefined zone");
            List<ManagerBean> managers = new ArrayList<>();
            managers.add(manager);
            group.setManagers(managers);
            group.setCreationDate(new Date());

            logger.log(Level.INFO, "Creating animal group: {0}", group.toString());
            AnimalGroupFactory.get().createAnimalGroup(group);

            showAnimalGroups();

            final int NEW_GROUP_ROW;
            for (int row = 0; row < tbAnimalGroup.getItems().size(); row++) {
                group = tbAnimalGroup.getItems().get(row);
                if (group.getName().equals(groupName)) {
                    NEW_GROUP_ROW = row;
                    Platform.runLater(() -> tbAnimalGroup.edit(NEW_GROUP_ROW, tcName));
                    tbAnimalGroup.refresh();
                    break;
                }
            }
        } catch (WebApplicationException | ProcessingException e) {
            logger.log(Level.SEVERE, "Error creating animal group", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    /**
     * Handles the deletion of selected animal groups.
     * <p>
     * This method displays a confirmation dialog before deleting the selected groups.
     * </p>
     *
     * @param event the action event triggered by the delete menu item.
     */
    private void onDeleteMenuItemClicked(ActionEvent event) {
        try {
            ObservableList<AnimalGroupBean> selectedGroups = tbAnimalGroup.getSelectionModel().getSelectedItems();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected Animal Groups?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                for (AnimalGroupBean agb : selectedGroups) {
                    AnimalGroupFactory.get().deleteAnimalGroupById(agb.getId().toString());
                }
            }
            showAnimalGroups();
        } catch (WebApplicationException | ProcessingException e) {
            logger.log(Level.SEVERE, "Error deleting animal group", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        }
    }

    /**
     * Handles the event for opening a new window for the selected animal group.
     * <p>
     * Depending on the view parameter, it opens a different window.
     * </p>
     *
     * @param event the action event triggered by the open menu item.
     */
    private void onOpenMenuItemClicked(ActionEvent event) {
        try {
            AnimalGroupBean group = tbAnimalGroup.getSelectionModel().getSelectedItem();
            logger.log(Level.INFO, "Opening animal group: {0}", group.getName());
            ((Scene) tbAnimalGroup.getScene()).getWindow().hide();
            WindowManager.openAnimalViewWithAnimalGroup("/ui/view/Animal.fxml", "Animal", manager, group);
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "Error opening window: ", e);
        }
    }

    /**
     * Handles the print action by compiling and displaying a JasperReports report.
     *
     * @param event the action event triggered by clicking the print button.
     */
    private void handlePrintAction(ActionEvent event) {
        try {
            logger.log(Level.INFO, "Beginning printing action...");
            JasperReport report
                    = JasperCompileManager.compileReport(getClass()
                            .getResourceAsStream("/ui/reports/AnimalGroupReport.jrxml"));
            //Data for the report: a collection of UserBean passed as a JRDataSource 
            //implementation 
            JRBeanCollectionDataSource dataItems
                    = new JRBeanCollectionDataSource((Collection<AnimalGroupBean>) this.tbAnimalGroup.getItems());
            //Map of parameter to be passed to the report
            Map<String, Object> parameters = new HashMap<>();
            //Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            //Create and show the report window. The second parameter false value makes 
            //report window not to close app.
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            // jasperViewer.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        } catch (JRException ex) {
            //If there is an error show message and
            //log it.
            logger.log(Level.SEVERE, "Error printing report: ", ex);
            showErrorAlert("ERROR", "Error printing report", ex.getMessage());

        }
    }

    /**
     * Handles the log out action by closing the current window and opening the SignIn window.
     *
     * @param event the action event triggered by clicking the log out button.
     */
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
     * Displays a confirmation dialog for log out.
     *
     * @return {@code true} if the user confirms log out; {@code false} otherwise.
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

    /**
     * Loads and displays the list of animal groups associated with the current manager.
     * <p>
     * This method retrieves the animal groups from the factory using the manager's ID and updates the TableView. If the manager is null, it logs a warning.
     * </p>
     */
    private void showAnimalGroups() {
        try {
            logger.log(Level.INFO, "Getting animal groups");
            if (manager != null) {
                List<AnimalGroupBean> groupList = AnimalGroupFactory.get().getAnimalGroupsByManager(new GenericType<List<AnimalGroupBean>>() {
                }, manager.getId().toString());
                List<AnimalGroupBean> groupListAux = new ArrayList<>();
                for (AnimalGroupBean group : groupList) {
                    Double totalConsume = 0.0;
                    List<ConsumesBean> consumes = ConsumesManagerFactory.get().findConsumesByAnimalGroup(new GenericType<List<ConsumesBean>>() {
                    }, group.getName());
                    for (int i = 0; i < consumes.size(); i++) {
                        totalConsume += consumes.get(i).getConsumeAmount();
                    }
                    group.setConsume(totalConsume);
                    groupListAux.add(group);
                }
                logger.log(Level.INFO, "Animal groups gotten");
                groupData = FXCollections.observableArrayList(groupListAux);
                tbAnimalGroup.setItems(groupData);
            } else {
                logger.log(Level.WARNING, "Manager is null");
            }
        } catch (WebApplicationException | ProcessingException e) {
            logger.log(Level.SEVERE, "Error fetching animal groups", e);
            showErrorAlert("SERVER ERROR", "Please contact with support", e.getMessage());
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Something is null: ", e);
        }
    }

    /**
     * Displays an error alert with a specified header, title, and message.
     *
     * @param head the header text for the alert.
     * @param title the title of the alert dialog.
     * @param message the message content of the alert dialog.
     */
    private void showErrorAlert(String head, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(head);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
