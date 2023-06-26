package C195;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Time;


public class mainController {

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

    public void initialize(){

        id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locations.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, Time>("end"));
        appointmentTableView.setItems(Appointment.getAllAppointments());

    }
}
