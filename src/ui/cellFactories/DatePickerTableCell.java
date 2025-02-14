package ui.cellFactories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

/**
 * This class represents a custom TableCell for editing Date values using a DatePicker. It allows the user to edit a Date directly within the table. The class inherits from TableCell and overrides methods to handle the display and editing of Date values.
 *
 * @param <T> The type of the object in the table row. This class is generic and can be used with any type of data that includes Date values.
 *
 * @author Aitziber
 */
public class DatePickerTableCell<T> extends TableCell<T, Date> {

    /**
     * Formatter to format dates in "EEE, MMM d, ''yy" format
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, ''yy", Locale.US);

    /**
     * DatePicker component used for selecting a date
     */
    private DatePicker datePicker;

    /**
     * Consumer to notify the controller when the date is updated
     */
    public java.util.function.Consumer<Date> updateDateCallback;

    /**
     * Constructor for DatePickerTableCell.
     *
     * @param column The column associated with this cell.
     */
    public DatePickerTableCell(TableColumn<T, Date> column) {
    }

    /**
     * Starts the editing process, replacing the cell text with a DatePicker.
     *
     * This method is triggered when the user starts editing a cell. If the cell is not empty, it creates a DatePicker and allows the user to select a new date. When the user selects a new date, the commitEdit method will be called to update the cell and notify the controller.
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            datePicker = new DatePicker();

            // Set the current date value in the DatePicker
            datePicker.setValue(convertToLocalDate(getItem()));

            // Define the action when a date is selected in the DatePicker
            datePicker.setOnAction((e) -> {
                // Commit the new value (convert it back to Date and send it to the table)
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            });

            // Set the text to null and display the DatePicker in place of the text
            setText(null);
            setGraphic(datePicker);
        }
    }

    /**
     * Cancels the editing process and restores the original date value.
     *
     * If the user cancels the edit, this method will be triggered. It ensures that the original date value is restored and displayed in the cell without applying any changes.
     */
    @Override
    public void cancelEdit() {
        // Restore the original value as text in the cell and clear the graphic (DatePicker)
        // TODO: en el datepicker si se confirma la edicion y no hay nada, poner la fecha antigua
        setGraphic(null);
        setText(formatter.format(convertToLocalDate(getItem())));
        super.cancelEdit();
    }

    /**
     * Commits the edited date and updates the cell.
     *
     * This method is called when the user selects a date and confirms the edit. It commits the new value, updates the display of the cell, and triggers the updateDateCallback to notify the controller of the updated date.
     *
     * @param newValue The new date selected by the user from the DatePicker.
     */
    @Override
    public void commitEdit(Date newValue) {
        super.commitEdit(newValue);
        // Update the cell's text to show the newly selected date
        setText(formatter.format(convertToLocalDate(newValue)));
        // Clear the DatePicker after editing
        setGraphic(null);

        // If the callback is not null, notify the controller with the new date
        if (updateDateCallback != null) {
            // Sends the new date to the controller
            updateDateCallback.accept(newValue);
        }
    }

    /**
     * Updates the cell content, displaying the current date or a DatePicker if editing.
     *
     * This method updates the visual content of the cell based on whether it is in editing mode or not. If the cell is empty or the item is null, it will display nothing. If the cell is being edited, it will show the DatePicker, otherwise it will display the date in the formatted text.
     *
     * @param item The current date item to display. This is the value that will be shown in the cell.
     * @param empty True if the cell is empty, otherwise false. This helps determine if the cell should be cleared or show the item.
     */
    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        // If the item is empty or null, clear the content of the cell
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // If the cell is in editing mode, show the DatePicker
            if (isEditing()) {
                setText(null);
                setGraphic(datePicker);
            } else {
                // Otherwise, display the formatted date text
                setText(formatter.format(convertToLocalDate(item)));
                setGraphic(null);
            }
        }
    }

    /**
     * Converts a Date object to a LocalDate.
     *
     * This utility method converts a Date object into a LocalDate object. The conversion is necessary because the DatePicker works with LocalDate, but the table cell contains a Date object.
     *
     * @param date The Date object to be converted.
     * @return The corresponding LocalDate, or null if the input date is null.
     */
    private LocalDate convertToLocalDate(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}
