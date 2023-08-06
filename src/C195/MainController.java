package C195;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

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
    public static Appointment selectedAppointment;
    public Label customerWarningLabel;
    public Label appointmentWarningLabel;

    public void initialize() {
        appointmentWarningLabel.setVisible(false);
        customerWarningLabel.setVisible(false);
        //creating customer table
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerZip.setCellValueFactory(new PropertyValueFactory<>("customerZip"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        divisionId.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        customerTableView.setItems(Customers.getAllCustomers());


        //creating week appointment table
        weekID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        weekTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        weekDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        weekLocations.setCellValueFactory(new PropertyValueFactory<>("location"));
        weekType.setCellValueFactory(new PropertyValueFactory<>("type"));
        weekStart.setCellValueFactory(new PropertyValueFactory<>("startString"));
        weekEnd.setCellValueFactory(new PropertyValueFactory<>("endString"));
        weekCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        weekUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        weekAppointmentTableView.setItems(Appointment.getAllWeekAppointments());

        //creating month appointment table
        monthID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        monthTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        monthDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        monthLocations.setCellValueFactory(new PropertyValueFactory<>("location"));
        monthType.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthStart.setCellValueFactory(new PropertyValueFactory<>("startString"));
        monthEnd.setCellValueFactory(new PropertyValueFactory<>("endString"));
        monthCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        monthUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
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
    public static Appointment getSelectedAppointment() {return selectedAppointment;}

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

    public void addAppointmentAction() throws IOException{
        //Creates a reference to FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("createAppointment.fxml"));
        Parent root = loader.load();
        CreateAppointmentController appointmentController = loader.getController();
        appointmentController.setMainController(this);

        //creating new window
        Stage addCustomer = new Stage();
        addCustomer.setTitle("Create a new Appointment");
        addCustomer.setScene(new Scene(root, 437, 450));
        addCustomer.show();
    }

    public void modifyAppointmentAction() throws IOException{
        selectedAppointment = weekAppointmentTableView.getSelectionModel().getSelectedItem();
        appointmentWarningLabel.setVisible(false);

        if(selectedAppointment != null){
            //Creates a reference to FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyAppointmentForm.fxml"));
            Parent root = loader.load();
            ModifyAppointmentController appointmentController = loader.getController();
            appointmentController.setMainController(this);

            //creating new window
            Stage addCustomer = new Stage();
            addCustomer.setTitle("Modify an Appointment");
            addCustomer.setScene(new Scene(root, 437, 450));
            addCustomer.show();
        }else if(monthAppointmentTableView.getSelectionModel().getSelectedItem() != null){
            selectedAppointment = monthAppointmentTableView.getSelectionModel().getSelectedItem();

            //Creates a reference to FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyAppointment.fxml"));
            Parent root = loader.load();
            CreateAppointmentController appointmentController = loader.getController();
            appointmentController.setMainController(this);

            //creating new window
            Stage addCustomer = new Stage();
            addCustomer.setTitle("Modify an Appointment");
            addCustomer.setScene(new Scene(root, 437, 450));
            addCustomer.show();
        }else{
            appointmentWarningLabel.setText("Please select an appointment.");
            appointmentWarningLabel.setVisible(true);
        }

    }

    public void refreshAppointmentTables(){
        weekAppointmentTableView.getItems().clear();
        weekAppointmentTableView.setItems(Appointment.getAllWeekAppointments());

        monthAppointmentTableView.getItems().clear();
        monthAppointmentTableView.setItems(Appointment.getAllMonthAppointments());
    }

    public void deleteAppointmentAction(){
        Appointment selectedAppointment;
        if(monthAppointmentTableView.getSelectionModel().getSelectedItem() != null){
            selectedAppointment = monthAppointmentTableView.getSelectionModel().getSelectedItem();

            try{
                String query = "DELETE FROM appointments WHERE Appointment_ID= " + selectedAppointment.getAppointmentID() + ";";
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                JDBC.getPreparedStatement().executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(weekAppointmentTableView.getSelectionModel().getSelectedItem() != null){
            selectedAppointment = weekAppointmentTableView.getSelectionModel().getSelectedItem();

            try{
                String query = "DELETE FROM appointments WHERE Appointment_ID= " + selectedAppointment.getAppointmentID() + ";";
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                JDBC.getPreparedStatement().executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        refreshAppointmentTables();
    }

}

