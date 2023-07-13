package C195;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public TableView<Appointment> appointmentTableView;
    @FXML
    public TableColumn<Appointment, Integer> id;
    @FXML
    public TableColumn<Appointment, String> title;
    @FXML
    public TableColumn<Appointment, String> description;
    @FXML
    public TableColumn<Appointment, String> locations;
    @FXML
    public TableColumn<Appointment, String> type;
    @FXML
    public TableColumn<Appointment, Time> start;
    @FXML
    public TableColumn<Appointment, Time> end;
    public Button newCustomer;
    public Button createAppointment;
    public Button deleteCustomer;
    public Button editCustomer;
    public Button editAppointment;
    public Button deleteAppointment;
    public static Customers selectedCustomer;
    public Label customerWarningLabel;


    public void initialize() {
        customerWarningLabel.setVisible(false);
        //creating customer table
        customerId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<Customers, String>("address"));
        customerZip.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerZip"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerPhone"));
        divisionId.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("divisionId"));
        customerTableView.setItems(Customers.getAllCustomers());


        //creating appointment table
        id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locations.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("end"));
        appointmentTableView.setItems(Appointment.getAllAppointments());

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
}
