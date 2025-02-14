/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import DTO.ManagerBean;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import DTO.ProductBean;
import DTO.ProviderBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
        
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import businessLogic.product.ProductManagerFactory;
import businessLogic.provider.ProviderManagerFactory;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.ws.rs.ProcessingException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import ui.cellFactories.DatePickerTableCell;
import ui.cellFactories.SpinnerTableCellFactory;

/**
 * FXML Controller class
 *
 * @author InigoFreire
 */
public class ProductController implements Initializable {

    @FXML
    private ComboBox<String> comboSearch;

    @FXML
    private DatePicker dpSearch;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView<ProductBean> tbProduct;

    @FXML
    private TableColumn<ProductBean, String> tcName;

    @FXML
    private TableColumn<ProductBean, Float> tcPrice;

    @FXML
    private TableColumn<ProductBean, String> tcMonthlyConsume;

    @FXML
    private TableColumn<ProductBean, Integer> tcStock;

    @FXML
    private TableColumn<ProductBean, ProviderBean> tcProviders;

    @FXML
    private TableColumn<ProductBean, Date> tcCreatedDate;

    @FXML
    private Label lblInfo;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnPrint;

    @FXML
    private MenuItem miDelete;

    private ObservableList<ProductBean> productData;

    private static ManagerBean manager;

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());
    private static final String PROHIBITED_CHARACTERS = "'\"\\;*%()<>|&,-/!?=";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Cargar los elementos en el ComboBox de búsqueda
        comboSearch.getItems().addAll("Product", "Created date");
        // Establecer por defecto "Product"
        comboSearch.setValue("Product");
        // Añadir listener para el ComboBox
        comboSearch.valueProperty().addListener(this::handleComboBoxChange);
        // Enfocar el campo de búsqueda
        tfSearch.requestFocus();
        // Habilitar el Botón btnSearch.
        btnSearch.setDisable(false);
        // Habilitar el Botón btnAdd.
        btnAdd.setDisable(false);
        // Ocultar el Label lblInfo.
        lblInfo.setVisible(false);
        // Deshabilitar el DatePicker dpSearch
        dpSearch.setDisable(true);
        
        // Cargar la tabla:
        // Establecer como editables las columnas Product, Price, Stock y Providers.
        // Llamar al método de lógica getAllProducts() y obtener una lista con todos los
        // registros almacenados de los productos.
        // Cargar los datos obtenidos en la Table View tableProduct, que tiene las
        // siguientes columnas:
        // Product: String | Editable (como TextFieldtableCell)
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcName.setCellFactory(TextFieldTableCell.<ProductBean>forTableColumn());
        tcName.setOnEditCommit(event -> handleEditCommit(event, "name"));

        // Price: Float | Editable (como SpinnerTableCell)
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        // TODO cambiar a factoria de celda de Spinner
        tcPrice.setCellFactory(param -> new SpinnerTableCellFactory(0.0f, 1000.0f, 0.5f)); // Para valores Float
        tcPrice.setOnEditCommit(event -> handleEditCommit(event, "price"));

        // Mensual consume: Consume() | No editable
        tcMonthlyConsume.setCellValueFactory(new PropertyValueFactory<ProductBean, String>("monthlyConsume"));
        tcMonthlyConsume.setEditable(false);

        // Stock: Integer | Editable
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcStock.setCellFactory(TextFieldTableCell.<ProductBean, Integer>forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }));
        tcStock.setOnEditCommit(event -> handleEditCommit(event, "stock"));

        // Providers: List<Providers> | Editable (como ComboBox)
        List<ProviderBean> providerList = ProviderManagerFactory.get()
                .getAllProviders(new GenericType<List<ProviderBean>>() {
                });
        ObservableList<ProviderBean> providerData = FXCollections.observableArrayList(providerList);

        tcProviders.setCellValueFactory(new PropertyValueFactory<>("provider"));

        tcProviders.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<ProviderBean>() {
            @Override
            public String toString(ProviderBean provider) {
                return provider != null ? provider.getName() : ""; // Muestra el nombre del proveedor
            }

            @Override
            public ProviderBean fromString(String string) {
                // Convierte el nombre del proveedor de nuevo a un objeto ProviderBean
                return providerData.stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        }, providerData)); // Pasa la lista de proveedores como parámetro
        tcProviders.setOnEditCommit(event -> {
            TablePosition<ProductBean, ProviderBean> pos = event.getTablePosition();
            ProviderBean newProvider = event.getNewValue(); // Nuevo proveedor seleccionado
            int row = pos.getRow();
            ProductBean product = event.getTableView().getItems().get(row);

            // Actualiza el proveedor en el producto
            product.setProvider(newProvider);

            // Actualiza la tabla
            event.getTableView().refresh();
        });
        tcProviders.setEditable(true);

        // Created date: Date Picker | No editable
        tcCreatedDate.setCellValueFactory(new PropertyValueFactory<ProductBean, Date>("createDate"));
        tcCreatedDate.setCellFactory(new Callback<TableColumn<ProductBean, Date>, TableCell<ProductBean, Date>>() {
            @Override
            public TableCell<ProductBean, Date> call(TableColumn<ProductBean, Date> param) {
                return new DatePickerTableCell<>(param);
            }
        });
        tcCreatedDate.setStyle("-fx-alignment: center;");
        tcCreatedDate.setEditable(false);

        // Mostrar la ventana.
        // stage.show();
        // En caso de producirse alguna excepción, se mostrará un mensaje al usuario con
        // el texto de la misma en el Label lblInfo.
        showAllProducts();

        // Añadir listener para el Botón btnSearch y btnAdd
        btnSearch.setOnAction(this::onSearchButtonClicked);
        btnAdd.setOnAction(this::onAddButtonClicked);
        miDelete.setOnAction(this::onDeleteMenuItemClicked);
    }

    private <T> void handleEditCommit(CellEditEvent<ProductBean, T> event, String fieldName) {
        try {
            TablePosition<ProductBean, T> pos = event.getTablePosition();
            T newValue = event.getNewValue();

            if (newValue == null || (newValue instanceof String && ((String) newValue).trim().isEmpty())) {
                throw new IllegalArgumentException("El valor ingresado no es válido.");
            }

            int row = pos.getRow();
            ProductBean product = event.getTableView().getItems().get(row);
            ProductBean productCopy = product.clone();

            switch (fieldName) {
                case "name":
                    if (newValue instanceof String) {
                        String newName = (String) newValue;
                        if (newName.matches(".*[" + PROHIBITED_CHARACTERS + "].*")) {
                            throw new IllegalArgumentException("El nombre contiene caracteres no permitidos.");
                        }
                        List<ProductBean> existingProducts = ProductManagerFactory.get().getAllProducts(new GenericType<List<ProductBean>>() {
                        });
                        boolean nameExists = existingProducts.stream().anyMatch(p -> p.getName().equalsIgnoreCase(newName));
                        if (nameExists) {
                            throw new IllegalArgumentException("El nombre del producto ya existe. Introduzca un nombre único.");
                        }
                        productCopy.setName(newName);
                        ProductManagerFactory.get().updateProduct(productCopy);
                        product.setName(newName);
                    }
                    break;
                case "price":
                    if (newValue instanceof Float) {
                        productCopy.setPrice((Float) newValue);
                        ProductManagerFactory.get().updateProduct(productCopy);
                        product.setPrice((Float) newValue);
                    }
                    break;
                case "stock":
                    if (newValue instanceof Integer) {
                        productCopy.setStock((Integer) newValue);
                        ProductManagerFactory.get().updateProduct(productCopy);
                        product.setStock((Integer) newValue);
                    } else {
                        throw new NumberFormatException("Ha introducido un valor no numérico. Introduzca un número entero por favor.");
                    }
                    break;
                case "providers":
                    if (newValue instanceof ProviderBean) {
                        productCopy.setProvider((ProviderBean) newValue);
                        ProductManagerFactory.get().updateProduct(productCopy);
                        product.setProvider((ProviderBean) newValue);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Campo desconocido: " + fieldName);
            }

            lblInfo.setText("Product's information has been successfully updated.");
            event.getTableView().refresh();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            event.consume();
        }
    }

    private void handleComboBoxChange(ObservableValue<? extends String> observable, Object oldValue, Object newValue) {
        if ("Created Date".equalsIgnoreCase(newValue.toString())) {
            // Ocultar el campo de búsqueda de texto
            tfSearch.setVisible(false);
            tfSearch.setDisable(true);
            tfSearch.clear();
            // Mostrar el campo de fecha y habilitarlo
            dpSearch.setVisible(true);
            dpSearch.setDisable(false);
        } else if ("Product".equals(newValue.toString())) {
            // Ocultar los campos de fecha
            dpSearch.setVisible(false);
            dpSearch.setDisable(true);
            // Mostrar el campo de búsqueda de texto
            tfSearch.setVisible(true);
            tfSearch.clear();
            tfSearch.setDisable(false);
        }
    }

    private void onSearchButtonClicked(ActionEvent event) {
        String searchType = comboSearch.getValue().toString();
        try {
            List<ProductBean> productList = null;

            switch (searchType) {
                case "Product":
                    String searchText = tfSearch.getText();
                    if (searchText != null && !searchText.trim().isEmpty()) {
                        productList = ProductManagerFactory.get()
                                .getProductByName(new GenericType<List<ProductBean>>() {
                                }, searchText.trim());
                    } else {
                        showAllProducts();
                    }
                    break;

                case "Created date":
                    if (dpSearch.getValue() != null) {
                        String date = dpSearch.getValue().toString();
                        if (!date.isEmpty()) {
                            productList = ProductManagerFactory.get()
                                    .getProductsByCreatedDate(new GenericType<List<ProductBean>>() {
                                    }, date);
                        } else {
                            showAllProducts();
                        }
                    } else {
                        showAllProducts();
                    }
                    break;

                default:
                    System.out.println("Opción de búsqueda no válida: " + searchType);
                    showAllProducts();
                    break;
            }

            if (productList != null) {
                ObservableList<ProductBean> productData = FXCollections.observableArrayList(productList);
                tbProduct.setItems(productData);
            }
        } catch (WebApplicationException e) {
            System.err.println("Error fetching product: " + e.getMessage());
        }
    }

    private void onAddButtonClicked(ActionEvent event) {
        try {
            ProductBean newProduct = new ProductBean();
            newProduct.setName("New Product");
            newProduct.setPrice(0f);
            newProduct.setStock(0);
            newProduct.setCreateDate(java.sql.Date.valueOf(LocalDate.now()));  // Usar LocalDate.now() para obtener la fecha de hoy

            // Validaciones antes de enviar la solicitud
            if (newProduct.getName() == null || newProduct.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
            }
            if (newProduct.getPrice() < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo.");
            }
            if (newProduct.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo.");
            }

            // Verificar si el producto ya existe
            List<ProductBean> existingProducts = ProductManagerFactory.get().getAllProducts(new GenericType<List<ProductBean>>() {
            });
            boolean nameExists = existingProducts.stream().anyMatch(p -> p.getName().equalsIgnoreCase(newProduct.getName()));
            if (nameExists) {
                throw new IllegalArgumentException("El producto ya existe en la base de datos.");
            }

            // Enviar solicitud de creación con manejo de errores detallado
            try {
                ProductManagerFactory.get().createProduct(newProduct);
                lblInfo.setText("Nuevo producto añadido correctamente.");
                btnSearch.fire();
            } catch (WebApplicationException e) {
                String errorResponse = e.getResponse().readEntity(String.class);
                throw new IllegalArgumentException("Error en la creación del producto: " + errorResponse);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            lblInfo.setText(e.getMessage());
        }
    }

    private void onDeleteMenuItemClicked(ActionEvent event) {
        try {
            ObservableList<ProductBean> selectedProducts = tbProduct.getSelectionModel().getSelectedItems();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the selected products?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                for (ProductBean product : selectedProducts) {
                    ProductManagerFactory.get().deleteProductById(product.getId().toString());
                }
            }
            showAllProducts();
        } catch (WebApplicationException | ProcessingException e) {
            showErrorAlert("SERVER ERROR", "Please contact support", e.getMessage());
        }
    }

    private void handlePrintAction(ActionEvent event) {
        try {
            logger.info("Beginning printing action...");

            // Cargar el archivo del reporte de manera segura
            InputStream reportStream = getClass().getResourceAsStream("/ui.reports/productReport.jrxml");
            if (reportStream == null) {
                throw new JRException("No se pudo encontrar el archivo de reporte.");
            }

            JasperReport report = JasperCompileManager.compileReport(reportStream);

            // Obtener los productos de la tabla y verificar que sean del tipo correcto
            List<ProductBean> productList = new ArrayList<>();
            for (Object item : tbProduct.getItems()) {
                if (item instanceof ProductBean) {
                    productList.add((ProductBean) item);
                } else {
                    throw new JRException("Error al convertir los datos de la tabla.");
                }
            }

            // Crear la fuente de datos para Jasper
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource(productList);

            // Mapa de parámetros (puedes agregar más si es necesario)
            Map<String, Object> parameters = new HashMap<>();

            // Llenar el reporte con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);

            // Crear y mostrar el visor del reporte
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);

        } catch (JRException ex) {
            logger.severe("Error al generar el reporte: " + ex.getMessage());
        }
    }

    private void showAllProducts() {
        try {
            logger.info("Solicitando todos los productos...");
            List<ProductBean> productList = ProductManagerFactory.get()
                    .getAllProducts(new GenericType<List<ProductBean>>() {
                    });

            if (productList != null && !productList.isEmpty()) {
                logger.info("Productos cargados: " + productList.size());
                productData = FXCollections.observableArrayList(productList);
                tbProduct.setItems(productData);
                btnAdd.setDisable(false);
            } else {
                logger.severe("ERROR. No tiene productos asociados");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No tiene productos asociados", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception e) {
            logger.severe("Error inesperado al cargar los productos: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error inesperado al cargar los productos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public static ManagerBean getManager() {
        return manager;
    }

    public static void setManager(ManagerBean manager) {
        ProductController.manager = manager;
    }

    private void showErrorAlert(String head, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(head);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
