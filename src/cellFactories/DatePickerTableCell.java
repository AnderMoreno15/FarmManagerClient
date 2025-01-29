/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cellFactories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;


/**
 *
 * @author Aitziber
 */
public class DatePickerTableCell<T> extends TableCell<T, Date> {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DatePicker datePicker;
    
    // Consumer to notify controller when the date is updated
    public java.util.function.Consumer<Date> updateDateCallback;

    public DatePickerTableCell(TableColumn<T, Date> column) {
    }

    @Override
    public void startEdit() {
        
        if (!isEmpty()) {
            super.startEdit();
            datePicker = new DatePicker();

            datePicker.setValue(convertToLocalDate(getItem()));
            datePicker.setOnAction((e) -> {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            });

            setText(null);
            setGraphic(datePicker);
        }
    }
    
    @Override
    public void cancelEdit() {
        System.out.println("en el datepicker si se confirma la edicion y no hay nada, poner la fecha antigua");
        setGraphic(null);
        setText(formatter.format(convertToLocalDate(getItem())));
        super.cancelEdit();
    }
    
    @Override
    public void commitEdit(Date newValue) {
        super.commitEdit(newValue);
        setText(formatter.format(convertToLocalDate(newValue)));
        setGraphic(null);
        
        if (updateDateCallback != null) {
            updateDateCallback.accept(newValue);  // Envía la nueva fecha al controlador
        }
    }
    
    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(formatter.format(convertToLocalDate(item)));
                setGraphic(null);
            }
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}