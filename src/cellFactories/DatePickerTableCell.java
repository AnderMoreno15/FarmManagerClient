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
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

/**
 *
 * @author Aitziber
 */
public class DatePickerTableCell<T> extends TableCell<T, Date> {
    
//    private final DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/M/yyyy");
//    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    private final DatePicker datePicker;
//
//    public DatePickerTableCell(TableColumn<T, Date> column) {
//        this.datePicker = new DatePicker();
//
//        // Convertidor entre LocalDate y String
//        this.datePicker.setConverter(new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate object) {
//                if (object != null) {
//                    return formatter.format(object);
//                }
//                return null;
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (!Optional.ofNullable(string).orElse("").isEmpty()) {
//                    return LocalDate.parse(string, parser);
//                }
//                return null;
//            }
//        });
//
//        // Manejar pérdida de foco en el editor de texto del DatePicker
//        this.datePicker.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if (!newValue) { // Si pierde el foco
//                    try {
//                        String text = datePicker.getEditor().getText();
//                        LocalDate localDate = LocalDate.parse(text, parser);
//                        commitEdit(convertToDate(localDate));
//                    } catch (Exception e) {
//                        cancelEdit();
//                        datePicker.getEditor().setText(formatter.format(convertToLocalDate(getItem())));
//                    }
//                }
//            }
//        });
//
//        // Sincronizar cambios en el valor del DatePicker
//        this.datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (isEditing() && newValue != null) {
//                commitEdit(convertToDate(newValue));
//            }
//        });
//
//        // Configuración de propiedades de edición y presentación
//        editableProperty().bind(column.editableProperty());
//        contentDisplayProperty().bind(
//                Bindings.when(editingProperty()) // Solo mostrar el DatePicker cuando esté en edición
//                        .then(ContentDisplay.GRAPHIC_ONLY)
//                        .otherwise(ContentDisplay.TEXT_ONLY)
//        );
//    }
//
//    @Override
//    public void startEdit() {
//        if (!isEmpty()) {
//            super.startEdit();
//            datePicker.setValue(convertToLocalDate(getItem()));
//    //        datePicker.setOnAction((e) -> {
//    //                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//    //            });
//            setText(null);
//            setGraphic(datePicker);
//       } 
//    }
//
//    @Override
//    public void cancelEdit() {
//        super.cancelEdit();
//        setText(formatter.format(convertToLocalDate(getItem()))); // Mostrar el texto formateado de la fecha
//        setGraphic(null);
//    }
//
//    @Override
//    public void commitEdit(Date newValue) {
//        super.commitEdit(newValue); // Confirmar la edición con el nuevo valor
//        setText(formatter.format(convertToLocalDate(newValue))); // Mostrar el texto formateado de la fecha
//        setGraphic(null); // Eliminar el DatePicker
//    }
//
//    @Override
//    protected void updateItem(Date item, boolean empty) {
//        super.updateItem(item, empty);
//
//        if (empty || item == null) {
//            setText(null);
//            setGraphic(null);
//        } else {
//            setText(formatter.format(convertToLocalDate(item))); // Mostrar el texto formateado
//            setGraphic(isEditing() ? datePicker : null); // Mostrar el DatePicker solo si la celda está en modo de edición
//        }
//    }
//
//    private LocalDate convertToLocalDate(Date date) {
//        if (date != null) {
//            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        }
//        return null;
//    }
//
//    private Date convertToDate(LocalDate localDate) {
//        if (localDate != null) {
//            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        }
//        return null;
//    }
//}
    
private final DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/M/yyyy");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DatePicker datePicker;
    
    // Consumer to notify controller when the date is updated
    public java.util.function.Consumer<Date> updateDateCallback;

    public DatePickerTableCell(TableColumn<T, Date> column) {
        // Al crear la celda, no inicializamos el DatePicker hasta que se edite.
    }

    @Override
    public void startEdit() {
        
        if (!isEmpty()) {
            super.startEdit();
            datePicker = new DatePicker();

            // Configuramos el DatePicker y lo mostramos solo en modo edición
            datePicker.setValue(convertToLocalDate(getItem()));
            datePicker.setOnAction((e) -> {
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            });

            setText(null);
            setGraphic(datePicker);  // Mostrar el DatePicker solo durante la edición
        }
    }
    
 
    

    @Override
    public void cancelEdit() {
        // Cuando se cancela la edición, eliminamos el DatePicker y mostramos el texto
        setGraphic(null);
        setText(formatter.format(convertToLocalDate(getItem())));
        super.cancelEdit();
    }

//    @Override
//    public void commitEdit(Date newValue) {
//        System.out.println("en el datepicker si se confirma la edicion y no hay nada, poner la fecha antigua");
//        // Cuando la edición se confirma, actualizamos el valor y mostramos el texto formateado
//        super.commitEdit(newValue);
//        setText(formatter.format(convertToLocalDate(newValue)));
//        
//        //aqui peticion de update
//        
//        setGraphic(null);
//    }
    
    @Override
    public void commitEdit(Date newValue) {
        super.commitEdit(newValue);
        setText(formatter.format(convertToLocalDate(newValue)));
        setGraphic(null);
    }
    

    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);  // No mostrar nada si está vacío
        } else {
            if (isEditing()) {
                setText(null);
                setGraphic(datePicker);  // Mostrar el DatePicker solo durante la edición
            } else {
                // Cuando no se está editando, mostramos la fecha formateada
                setText(formatter.format(convertToLocalDate(item)));
                setGraphic(null);  // No mostrar el DatePicker
            }
        }
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    private Date convertToDate(LocalDate localDate) {
        if (localDate != null) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }
}