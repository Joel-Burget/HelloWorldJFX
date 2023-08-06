package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ModifyAppointmentController {
    public Label timeWarningLabel;
    public ComboBox customerBox;
    private MainController MC;

    public TextField appointmentIDField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public DatePicker startPicker;
    public DatePicker endPicker;
    public ComboBox<Integer> startHour;
    public ComboBox<Integer> endHour;
    public ComboBox<Integer> startMinute;
    public ComboBox<Integer> endMinute;
    public ComboBox contactBox;
    public Button saveButton;
    public Button cancelButton;

    public void initialize(){
        timeWarningLabel.setVisible(false);
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        hours.addAll(8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        minutes.addAll(00, 30);
        appointmentIDField.setText(Integer.toString(Appointment.generateAppointmentID()));

        startHour.setItems(hours);
        startMinute.setItems(minutes);
        endHour.setItems(hours);
        endMinute.setItems(minutes);

        startPicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        endPicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        String query = "SELECT * FROM contacts";
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try{
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                contactNames.add(rs.getString(2));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        contactBox.setItems(contactNames);

        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (Customers customer: Customers.getAllCustomers()) {
            customerNames.add(customer.getCustomerName());
        }
        customerBox.setItems(customerNames);

        Appointment selectedAppointment = MainController.getSelectedAppointment();

        appointmentIDField.setText(Integer.toString(selectedAppointment.getAppointmentID()));
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        typeField.setText(selectedAppointment.getType());

    }

    public void setMainController(MainController MC) {
        this.MC = MC;
    }

    public void saveButtonAction(){
        if(verifySave()){
            String query = "SELECT * FROM contacts WHERE Contact_Name= '" + contactBox.getSelectionModel().getSelectedItem() + "';";
            int contactID = 0;
            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();
                while(rs.next()){
                    contactID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            int userID = 0;
            query = "SELECT * FROM users WHERE User_Name='" + LoginController.getUsername() + "';";
            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();

                while(rs.next()){
                    userID = rs.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            int id = Integer.parseInt(appointmentIDField.getText());
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            String customerID = Integer.toString(Customers.getAllCustomers().get(customerBox.getSelectionModel().getSelectedIndex()).getCustomerId());
            LocalDateTime startDate = LocalDateTime.of(startPicker.getValue(), LocalTime.of(startHour.getValue(), startMinute.getValue()));
            LocalDateTime endDate = LocalDateTime.of(endPicker.getValue(), LocalTime.of(endHour.getValue(), endMinute.getValue()));

            Appointment appointment = new Appointment(id, title, description, location, type, convertToUtc(startDate), convertToUtc(endDate),
                    LoginController.getUsername(), customerID, userID, contactID);


             query = "UPDATE appointments SET Title = '" + appointment.getTitle() + "', " + "Description = '" + appointment.getDescription()
                     + "', " + "Type = ' " + appointment.getType() + "', " + "Start = '" + appointment.getStart() + "', " +
                     "End ='" + appointment.getEnd() +  "', " + "Last_Update = '" + LocalDateTime.now() + "', "
                     + "Last_Updated_By = '" + LoginController.getUsername() + "', " + "Customer_ID = ' " +
                     appointment.getCustomerID() + "', " + "User_ID = '" + appointment.getUserID() + "', "
                     + "Contact_ID = '" + appointment.getContactID()  + "' " + "WHERE Appointment_ID= '" + appointment.getAppointmentID() + "';";

            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                JDBC.getPreparedStatement().executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }

            MC.refreshAppointmentTables();
            saveButton.getScene().getWindow().hide();
        }
    }

    public void cancelButtonAction(){
        cancelButton.getScene().getWindow().hide();
    }

    public LocalDateTime convertToUtc(LocalDateTime time){
        return time.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public boolean verifySave(){
        if(titleField.getText().isEmpty()){
            titleField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(descriptionField.getText().isEmpty()){
            descriptionField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(locationField.getText().isEmpty()){
            locationField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(typeField.getText().isEmpty()){
            typeField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(contactBox.getSelectionModel().isEmpty()){
            contactBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(startPicker.getValue() == null){
            startPicker.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(endPicker.getValue() == null){
            endPicker.setBorder(new Border(new BorderStroke(javafx.scene.paint.Paint.valueOf("red"), BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
            return false;
        }
        if(Appointment.getAllAppointments().isEmpty()){
            //if returns nothing, skip, no appointment
            return true;
        }else{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            for (Appointment appointment: Appointment.getAllAppointments()) {
                LocalDateTime startDate = LocalDateTime.of(startPicker.getValue(), LocalTime.of(startHour.getValue(), startMinute.getValue()));
                LocalDateTime endDate = LocalDateTime.of(endPicker.getValue(), LocalTime.of(endHour.getValue(), endMinute.getValue()));

                boolean between = isBetween(appointment, startDate, endDate);
                if(between){
                    timeWarningLabel.setText("Cannot schedule an appointment during and appointment");
                    timeWarningLabel.setVisible(true);
                    return false;
                }else{
                    boolean diff = overlaps(appointment, startDate, endDate);
                    if(diff){
                        //true: dates overlap
                        timeWarningLabel.setText("Appointments overlap");
                        timeWarningLabel.setVisible(true);
                        return false;
                    }else{
                        //false: valid time
                        timeWarningLabel.setText("Appointments set");
                        timeWarningLabel.setVisible(true);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public boolean overlaps(Appointment appointment, LocalDateTime inputStartDate, LocalDateTime inputEndDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

        LocalDateTime appointmentStartDate = LocalDateTime.parse(appointment.getStartString(), dtf);
        LocalDateTime appointmentEndDate = LocalDateTime.parse(appointment.getEndString(), dtf);

        return inputEndDate.isAfter(appointmentStartDate) && inputStartDate.isBefore(appointmentEndDate);
    }

    public boolean isBetween(Appointment appointment, LocalDateTime inputStartDate, LocalDateTime inputEndDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

        LocalDateTime appointmentStartDate = LocalDateTime.parse(appointment.getStartString(), dtf);
        LocalDateTime appointmentEndDate = LocalDateTime.parse(appointment.getEndString(), dtf);

        return inputStartDate.isAfter(appointmentStartDate) && inputStartDate.isBefore(appointmentEndDate);
    }
}
