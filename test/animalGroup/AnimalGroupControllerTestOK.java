/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animalGroup;

import DTO.AnimalGroupBean;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Ander
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnimalGroupControllerTestOK extends ApplicationTest {

    private static final Logger logger = Logger.getLogger(AnimalGroupControllerTestOK.class.getName());
    private static boolean loggedIn = false;

    @Override
    public void start(Stage stage) throws Exception {
        if (!loggedIn) {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/view/SignIn.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Test
    public void testA_OpenAnimalGroupView() {
        logger.info("Iniciando test Open");
        clickOn("#tfUsername").write("ander@paia.com");
        clickOn("#pfPasswd").write("12345678");
        clickOn("#btnSignIn");

        verifyThat("#tbAnimalGroup", isVisible());
        loggedIn = true;
    }

    @Test
    public void testB_GetAnimalGroups() {
        logger.info("Iniciando test Get");
        TableView<AnimalGroupBean> table = lookup("#tbAnimalGroup").query();

        assertNotNull("Animal group table should be not null", table);

        ObservableList<AnimalGroupBean> items = table.getItems();

        assertNotNull("Animal group list should be not null", items);
        assertNotEquals("Animal group table shouldn't be empty", 0, items.size());

        for (Object item : items) {
            assertNotNull("The element in the table should be not null", item);
            assertEquals("The element in the table should be an AnimalGroupBean", AnimalGroupBean.class, item.getClass());
        }
    }

    @Test
    public void testC_DeleteAnimalGroup() {
        logger.info("Iniciando test Delete");
        TableView<AnimalGroupBean> table = lookup("#tbAnimalGroup").query();

        TableRow<AnimalGroupBean> row = lookup(".table-row-cell").nth(0).query();

        AnimalGroupBean selectedAnimalGroup = (AnimalGroupBean) table.getSelectionModel()
                .getSelectedItem();
        rightClickOn(row);
        clickOn("#miDelete");
        clickOn("Sí");

        ObservableList<AnimalGroupBean> items = table.getItems();
        boolean isDeleted = items.stream().noneMatch(item -> item.equals(selectedAnimalGroup));

        assertTrue("The deleted item should not be in the table", isDeleted);
    }

    @Test
    public void testD_AddAnimalGroup() {
        logger.info("Iniciando test Add");

        TableView<AnimalGroupBean> table = lookup("#tbAnimalGroup").query();
        int initialSize = table.getItems().size();

        clickOn("#btnCreate");

        int lastIndex = table.getItems().size() - 1;

        assertEquals("The table should have one row more", initialSize + 1, table.getItems().size());

        TableRow<AnimalGroupBean> newRow = lookup(".table-row-cell").nth(lastIndex).query();
        clickOn(newRow);

        ObservableList<AnimalGroupBean> items = table.getItems();

        AnimalGroupBean newItem = items.stream()
                .filter(item -> "Group of animals".equals(item.getDescription()))
                .findFirst()
                .orElse(null);

        assertNotNull("The new element should have the description: 'Group of animals'", newItem);
        assertEquals("The element in the table should be an AnimalGroupBean", AnimalGroupBean.class, newItem.getClass());
    }

    @Test
    public void testE_UpdateAnimalGroup() {
        logger.info("Iniciando test Update");

        TableView<AnimalGroupBean> table = lookup("#tbAnimalGroup").query();

        int lastIndex = table.getItems().size() - 1;

        TableRow<String> row = lookup(".table-row-cell").nth(lastIndex).query();

        Node cell = from(row).lookup(".table-cell").nth(0).query();

        clickOn(cell);

        write("Test Group");
        push(KeyCode.ENTER);

        AnimalGroupBean updatedAnimalGroup = table.getItems().get(lastIndex);
        assertEquals("The name of the group should be 'Test Group'", "Test Group", updatedAnimalGroup.getName());
    }

}
