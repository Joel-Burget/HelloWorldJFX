package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    public TextArea upComingAppointments;
    public TextArea totalReportField;
    public ChoiceBox contactBox;

    @FXML
    public TableView <Appointment> scheduleReportTable;
    @FXML
    public TableColumn<Appointment, String> appointmentID;
    @FXML
    public TableColumn<Appointment, String> title;
    @FXML
    public TableColumn<Appointment, String> description;
    @FXML
    public TableColumn<Appointment, LocalDateTime> start;
    @FXML
    public TableColumn<Appointment, LocalDateTime> end;
    @FXML
    public TableColumn<Appointment, Integer> customerID;
    public ComboBox totalReportBox;
    public TextArea totalHourText;

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

        upComingAppointments.setText("You have no upcoming appointments.");

        for (Appointment appointment: Appointment.getAllAppointments()) {
            LocalDateTime fifteenMinutes = LocalDateTime.now().plusMinutes(15);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            LocalDateTime appointmentTime = LocalDateTime.parse(appointment.getStartString(), dtf);
            int minutes = (int) ChronoUnit.MINUTES.between(appointmentTime, fifteenMinutes);

            if(minutes <= 15 && minutes >= 0){
                upComingAppointments.setText("You have an upcoming appointment:\nAppointment ID: " + appointment.getAppointmentID() +
                        "\nDate: " + appointment.getStartString());
            }
        }

        typeReport();
        contactReport();
        totalHoursReport();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyAppointmentForm.fxml"));
            Parent root = loader.load();
            ModifyAppointmentController appointmentController = loader.getController();
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

    public void typeReport(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> months = new ArrayList<String>();
        totalReportField.setText("Total Appointments by Type In: " + LocalDateTime.now().getMonth() + "\n");

        int count = 0;
        for(Appointment appointment: Appointment.getAllAppointments()) {
            if(!types.contains(appointment.getType().toLowerCase())){
                types.add(appointment.getType());
            }
        }
        for(Appointment appointment : Appointment.getAllAppointments()){
            LocalDateTime date = LocalDateTime.parse(appointment.getStartString(), dtf);
            String month = date.getMonth().toString();

            if(!months.contains(month)){
                months.add(month);
            }
        }

        for (String type: types) {
            String query = "SELECT COUNT(Appointment_ID) FROM appointments WHERE MONTH(start) = MONTH(CURRENT_DATE) AND type ='" + type + "';";
            try{
                JDBC.makePreparedStatement(query, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery();

                while(rs.next()){
                    totalReportField.appendText(type + ": " + rs.getInt(1) + " appointments\n");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void contactReport(){
        String query;
        ObservableList<String> contactList = FXCollections.observableArrayList();
        ObservableList<Appointment> selectedAppointments = FXCollections.observableArrayList();

        try {
            query = "SELECT * FROM contacts";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            while(rs.next()){
                contactList.add(rs.getString(2));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        contactBox.setItems(contactList);

        contactBox.setOnAction((e) ->{
            selectedAppointments.clear();
            scheduleReportTable.getItems().clear();
            int id = 0;
           String getID = "SELECT * FROM contacts WHERE Contact_Name = '" + contactBox.getSelectionModel().getSelectedItem() + "';";

           try{
               JDBC.makePreparedStatement(getID, JDBC.getConnection());
               ResultSet rs2 = JDBC.getPreparedStatement().executeQuery();
               while(rs2.next()){
                  id = rs2.getInt(1);
               }
           }catch(SQLException exception){
               exception.printStackTrace();
           }

            for (Appointment appointment: Appointment.getAllAppointments()) {
                if(appointment.getContactID() == id){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
                    LocalDateTime startTime = LocalDateTime.parse(appointment.getStartString(), dtf);
                    if(startTime.isAfter(LocalDateTime.now().minus(Period.ofDays(1)))){
                        selectedAppointments.add(appointment);
                    }
                }
            }

            appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            start.setCellValueFactory(new PropertyValueFactory<>("startString"));
            end.setCellValueFactory(new PropertyValueFactory<>("endString"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            scheduleReportTable.setItems(selectedAppointments);
        });
    }

    public void totalHoursReport(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
        String query;

        ObservableList<String> contactList = FXCollections.observableArrayList();

        try {
            query = "SELECT * FROM contacts";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();
            while(rs.next()){
                contactList.add(rs.getString(2));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        totalReportBox.setItems(contactList);

        totalReportBox.setOnAction((e) -> {
            long totalMonthHours = 0;
            long totalMonthMin = 0;
            int id = 0;
            String getID = "SELECT * FROM contacts WHERE Contact_Name = '" + totalReportBox.getSelectionModel().getSelectedItem() + "';";
            try{
                JDBC.makePreparedStatement(getID, JDBC.getConnection());
                ResultSet rs2 = JDBC.getPreparedStatement().executeQuery();
                while(rs2.next()){
                    id = rs2.getInt(1);
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }

            for (Appointment appointment : Appointment.getAllMonthAppointments()) {
                if(appointment.getContactID() == id){
                    LocalDateTime start = LocalDateTime.parse(appointment.getStartString(), dtf);
                    LocalDateTime end = LocalDateTime.parse(appointment.getEndString(), dtf);
                    long diffHour = Duration.between(start, end).toHours();

                    if(diffHour == 0){
                        long diffMin = Duration.between(start, end).toMinutes();
                        totalMonthMin = totalMonthMin + diffMin;

                    }
                    totalMonthHours = totalMonthHours + diffHour;
                }
            }
            while(totalMonthMin >= 60){
                totalMonthMin = totalMonthMin - 60;
                totalMonthHours = totalMonthHours + 1;
            }
            totalHourText.setText(totalReportBox.getSelectionModel().getSelectedItem() + " has " + totalMonthHours + " hours " +
                    "and " + totalMonthMin + " mintues of appointments.");
        });
    }
}

