/**
 * A custom TableCell factory for displaying a Spinner control in a TableView cell 
 * that holds a Float value.
 * <p>
 * This factory creates a spinner for editing values within a specific range (min, max, step) 
 * and commits the changes when the value is modified or the Spinner loses focus.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Displays a Spinner for editing Float values.</li>
 *   <li>Automatically commits changes when the value is modified or when the Spinner loses focus.</li>
 *   <li>Reverts to a Label for displaying the value when not editing.</li>
 * </ul>
 * 
 * @param min The minimum value for the Spinner.
 * @param max The maximum value for the Spinner.
 * @param step The step value for the Spinner.
 * @author Ander
 * @author Iñigo
 * @version 1.0
 */
package ui.cellFactories;

import DTO.ProductBean;
import javafx.scene.control.*;

public class SpinnerTableCellFactory extends TableCell<ProductBean, Float> {
    private final Spinner<Double> spinner;
    private final Label label;

    public SpinnerTableCellFactory(float min, float max, float step) {
        // Convert min, max, and step to Double for the Spinner
        spinner = new Spinner<>((double) min, (double) max, (double) min, (double) step);
        spinner.setEditable(true);

        // Commit the edit when the value changes
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && getTableRow() != null && getTableRow().getItem() != null) {
                commitEdit(newValue.floatValue()); // Convert Double to Float before committing
            }
        });

        // Commit the edit when the Spinner loses focus
        spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(spinner.getValue().floatValue()); // Convert Double to Float
            }
        });

        label = new Label();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(spinner);
        spinner.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(label);
    }

    @Override
    public void updateItem(Float value, boolean empty) {
        super.updateItem(value, empty);
        if (empty || value == null) {
            label.setText("");
            setGraphic(label);
        } else {
            spinner.getValueFactory().setValue(value.doubleValue()); // Update the spinner's value
            if (isEditing()) {
                setGraphic(spinner);
            } else {
                label.setText(String.valueOf(value));
                setGraphic(label);
            }
        }
    }
}
