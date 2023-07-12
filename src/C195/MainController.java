package C195;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
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
    public Button editCusomter;
    public Button editAppointment;
    public Button deleteAppointment;


    public void initialize(){

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

    public void refreshCustomerTable(){
        customerTableView.getItems().clear();
        customerTableView.setItems(Customers.getAllCustomers());
    }


}
