package product;

import DTO.ProductBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import java.text.SimpleDateFormat;

/**
 * Test class for ProductController.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerTestOK extends ApplicationTest {

    private TableView table;
    private TableColumn tcStock;
    private ProductBean selectedProduct;
    private static boolean loggedIn = false;

    @Override
    public void start(Stage stage) throws Exception {
        if (!loggedIn) {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/view/SignIn.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        table = lookup("#tbProduct").query();
    }

    @Test
    public void testA_OpenProductView() {

        clickOn("#tfUsername").write("ander@paia.com");
        clickOn("#pfPasswd").write("12345678");
        clickOn("#btnSignIn");

        verifyThat("#menuBar", isVisible());
        clickOn("#menuNavigateTo");
        clickOn("#miProduct");
        verifyThat("#tbProduct", isVisible());

        loggedIn = true;

    }

    @Test
    public void testB_GetProducts() {
        table = lookup("#tbProduct").query();

        assertNotNull("Product is null; ", table);

        ObservableList<ProductBean> items = table.getItems();

        assertNotNull("Product list must be null; ", items);
        assertNotEquals("Product table must not be null; ", 0, items.size());

        for (Object item : items) {
            assertNotNull("Table cannot be null; ", item);
            assertEquals("Table elements must be ProductBean type; ", ProductBean.class, item.getClass());
        }
    }

    @Test
    public void testC_DeleteProduct() {
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data; ", rowCount, 0);
        clickOn("#tcStock");
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Selected row is null; ", row);
        clickOn(row);

        ProductBean selectedProduct = (ProductBean) table.getSelectionModel()
                .getSelectedItem();
        rightClickOn(row);
        clickOn("#miDelete");
        clickOn("Sí");
        ObservableList<ProductBean> items = table.getItems();
        boolean isDeleted = items.stream().noneMatch(item -> item.equals(selectedProduct));

        assertTrue("The deleted item should not be in the table", isDeleted);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductControllerTestOK.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testD_CreateProduct() {
        verifyThat("#tbProduct", isVisible());

        ObservableList<ProductBean> items = table.getItems();
        ProductBean newProduct = items.stream()
                .filter(product -> product.getName().equals("New Product"))
                .findFirst()
                .orElse(null);

        clickOn("#btnAdd");
        items = table.getItems();
        newProduct = items.stream()
                .filter(product -> product.getName().equals("New Product"))
                .findFirst()
                .orElse(null);

        assertNotNull("El producto 'New Product' no se ha creado", newProduct);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProductControllerTestOK.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testE_UpdateProduct() throws InterruptedException {
        TableView<ProductBean> table = lookup("#tbProduct").query();

        int lastIndex = table.getItems().size() - 1;

        // Verifica que el producto inicial tiene el nombre 'New Product'
        ProductBean updatedProduct = table.getItems().get(lastIndex);
        assertEquals("The name of the product should be 'New Product'", "New Product", updatedProduct.getName());

        // Actualizar nombre del producto con doble clic
        TableRow<String> row = lookup(".table-row-cell").nth(lastIndex).query();
        Node cell = from(row).lookup(".table-cell").nth(0).query(); // Nombre del producto
        clickOn(cell); // Clic en la celda
        clickOn(cell); // Doble clic para asegurarse de que está en modo edición
        write("Test Product");
        push(KeyCode.ENTER);

        // Añadir un pequeño retraso para asegurarse de que el cambio se refleje
        Thread.sleep(500); // Espera medio segundo para dar tiempo al cambio

        // Verifica que el nombre del producto se actualizó a 'Test Product'
        updatedProduct = table.getItems().get(lastIndex);
        assertEquals("The name of the product should be 'Test Product'", "Test Product", updatedProduct.getName());

        // Actualizar precio
        row = lookup(".table-row-cell").nth(lastIndex).query();
        cell = from(row).lookup(".table-cell").nth(1).query(); // Precio
        clickOn(cell);
        write("20.0");
        push(KeyCode.ENTER);

        updatedProduct = table.getItems().get(lastIndex);
        assertEquals("The price of the product should be '20.0'", 20.0, updatedProduct.getPrice(), 0.0);

        // Actualizar stock
        row = lookup(".table-row-cell").nth(lastIndex).query();
        cell = from(row).lookup(".table-cell").nth(3).query(); // Stock
        clickOn(cell);
        write("50");
        push(KeyCode.ENTER);

        updatedProduct = table.getItems().get(lastIndex);
        assertEquals("The stock of the product should be '50.0'", 50.0, updatedProduct.getStock(), 0.0);

        // Seleccionar un proveedor del ComboBox
        row = lookup(".table-row-cell").nth(lastIndex).query();
        cell = from(row).lookup(".table-cell").nth(4).query(); // Celda del ComboBox de proveedores
        clickOn(cell); // Clic en la celda para abrir el ComboBox

        // Esperamos un poco para asegurarnos de que el ComboBox y la lista de proveedores se carguen
        Thread.sleep(500); // Puede que necesites ajustar este tiempo dependiendo de la carga de datos

        // Asumimos que el proveedor está en la primera posición del ComboBox
        // Si no, puedes ajustar el índice de selección según el orden de los proveedores
        Node providerCell = lookup(".combo-box-popup .list-view .list-cell").nth(0).query(); // Nodo específico para el proveedor
        if (providerCell != null) {
            clickOn(providerCell); // Selección del primer proveedor

            // Verifica que el proveedor se haya seleccionado correctamente
            updatedProduct = table.getItems().get(lastIndex);
            assertNotNull("The product should have a provider selected", updatedProduct.getProvider());
        } 
    }

}
