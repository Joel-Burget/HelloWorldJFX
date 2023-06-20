package C195;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class mainController {


    public void initialize(){

        for (Object e: Appointment.getAllAppointments()){
            System.out.println(e);
        }
    }
}
