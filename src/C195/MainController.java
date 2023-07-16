package C195;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import java.util.Date;


public class MainController {
    @FXML
    public TableView<Customers> customerTableView;
    @FXML
    public TableColumn<Customers, Integer> customerId;
    @FXML
    public TableColumn<Customers, String> customerName;
    @FXML
    public TableColumn<Customers, String> customerAddress;
    @FXML
    public TableColumn<Customers, String> customerZip;
    @FXML
    public TableColumn<Customers, String> customerPhone;
    @FXML
    public TableColumn<Customers, Integer> divisionId;

    @FXML
    public TableView<Appointment> weekAppointmentTableView;
    @FXML
    public TableColumn<Appointment, Integer> weekID;
    @FXML
    public TableColumn<Appointment, String> weekTitle;
    @FXML
    public TableColumn<Appointment, String> weekDescription;
    @FXML
    public TableColumn<Appointment, String> weekLocations;
    @FXML
    public TableColumn<Appointment, String> weekType;
    @FXML
    public TableColumn<Appointment, String> weekStart;
    @FXML
    public TableColumn<Appointment, String> weekEnd;
    @FXML
    public TableColumn<Appointment, String> weekCustomerID;
    @FXML
    public TableColumn<Appointment, String> weekUserID;

    @FXML
    public TableView<Appointment> monthAppointmentTableView;
    @FXML
    public TableColumn<Appointment, Integer> monthID;
    @FXML
    public TableColumn<Appointment, String> monthTitle;
    @FXML
    public TableColumn<Appointment, String> monthDescription;
    @FXML
    public TableColumn<Appointment, String> monthLocations;
    @FXML
    public TableColumn<Appointment, String> monthType;
    @FXML
    public TableColumn<Appointment, Time> monthStart;
    @FXML
    public TableColumn<Appointment, Time> monthEnd;
    @FXML
    public TableColumn<Appointment, String> monthCustomerID;
    @FXML
    public TableColumn<Appointment, String> monthUserID;

    public Button newCustomer;
    public Button createAppointment;
    public Button deleteCustomer;
    public Button editCustomer;
    public Button editAppointment;
    public Button deleteAppointment;
    public static Customers selectedCustomer;
    public Label customerWarningLabel;

    public void initialize() {
        /*Appointment appointment = new Appointment(Appointment.generateAppointmentID(), "IT Scrum 2", "Weekly IT Scrum meeting",
                "Conf 3", "Weekly", LocalDateTime.of(2023, 7, 24, 13, 0),
                LocalDateTime.of(2023, 7, 24, 13, 30), LoginController.getUsername(),
                "1", "1", 1);

        Appointment.createAppointment(appointment);*/

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime aptTime = Appointment.getAllWeekAppointments().get(0).getStart();

        customerWarningLabel.setVisible(false);
        //creating customer table
        customerId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<Customers, String>("address"));
        customerZip.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerZip"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerPhone"));
        divisionId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("divisionId"));
        customerTableView.setItems(Customers.getAllCustomers());


        //creating week appointment table
        weekID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentID"));
        weekTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        weekDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        weekLocations.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        weekType.setCellValueFactory(new PropertyValueFactory<>("type"));
        weekStart.setCellValueFactory(new PropertyValueFactory<>("startString"));
        weekEnd.setCellValueFactory(new PropertyValueFactory<>("endString"));
        weekCustomerID.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerID"));
        weekUserID.setCellValueFactory(new PropertyValueFactory<Appointment, String>("userID"));
        weekAppointmentTableView.setItems(Appointment.getAllWeekAppointments());
        System.out.println(Appointment.getAllWeekAppointments().get(0).getStartString());

        //creating month appointment table
        monthID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentID"));
        monthTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        monthDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        monthLocations.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        monthType.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        monthStart.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("start"));
        monthEnd.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("end"));
        monthCustomerID.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerID"));
        monthUserID.setCellValueFactory(new PropertyValueFactory<Appointment, String>("userID"));
        monthAppointmentTableView.setItems(Appointment.getAllMonthAppointments());

    }

    public void newCustomerAction() throws IOException {
       //Creates a reference to FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
        Parent root = loader.load();
        AddCustomerController customerController = loader.getController();
        customerController.setMainController(this);

        //creating new window
        Stage addCustomer = new Stage();
        addCustomer.setTitle("Add a new Customer");
        addCustomer.setScene(new Scene(root, 365, 400));
        addCustomer.show();
    }

    public void refreshCustomerTable() {
        customerTableView.getItems().clear();
        customerTableView.setItems(Customers.getAllCustomers());
    }

    public static Customers getSelectedCustomer() {
        return selectedCustomer;
    }

    public void modifyCustomerAction() throws IOException {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            customerWarningLabel.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modifyCustomer.fxml"));
            Parent root = loader.load();
            ModifyCustomerController modifyController = loader.getController();
            modifyController.setMainController(this);

            Stage modifyCustomer = new Stage();
            modifyCustomer.setTitle("Modify Customer");
            modifyCustomer.setScene(new Scene(root, 365, 400));
            modifyCustomer.show();
        } else {
            customerWarningLabel.setText("Please Select a customer to modify");
            customerWarningLabel.setVisible(true);
        }
    }

    public void deleteCustomerAction() {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
                int count;
                String statement = "SELECT COUNT(Appointment_ID) FROM appointments WHERE Customer_ID='" + selectedCustomer.getCustomerId() + "'";
                try {
                    JDBC.makePreparedStatement(statement, JDBC.getConnection());
                    ResultSet rs = JDBC.getPreparedStatement().executeQuery();
                    System.out.println(statement);
                    while (rs.next()) {
                        count = rs.getInt(1);
                        if (count > 0) {
                            customerWarningLabel.setText(selectedCustomer.getCustomerName() + " could not be deleted: Please delete all " +
                                    "customers appointments first.");
                            customerWarningLabel.setVisible(true);
                        } else {
                            customerWarningLabel.setVisible(false);
                            String query = "DELETE FROM customers WHERE Customer_ID= '" + selectedCustomer.getCustomerId() + "';";
                            try {
                                JDBC.makePreparedStatement(query, JDBC.getConnection());
                                JDBC.getPreparedStatement().executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            customerWarningLabel.setText(selectedCustomer.getCustomerName() + " has been deleted.");
                            customerWarningLabel.setVisible(true);
                            refreshCustomerTable();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }else{
              customerWarningLabel.setText("Please select a customer to delete");
              customerWarningLabel.setVisible(true);
            }
    }
    public static class Item {
        private LocalDateTime date;

        public Item(LocalDateTime date) {
            this.date = date;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }
    }
}

