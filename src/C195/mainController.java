package C195;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.util.ArrayList;


public class mainController {
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn<Customers, Integer> custIdCol;
    @FXML
    private TableColumn<Customers, String> customerName;

    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> titles = new ArrayList<String>();

    public void initialize(){
        String getTitle = "SELECT Title from appointments";


        String getId = "SELECT Appointment_ID FROM appointments";
        //building title array
        try{
            JDBC.makePreparedStatement(getTitle, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                titles.add(rs.getString(1));
            }

            JDBC.makePreparedStatement(getId, JDBC.getConnection());
            rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                ids.add(rs.getString(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        for(String title : titles){
            System.out.println(title);
        }

        //building table
        custIdCol.setCellValueFactory(new PropertyValueFactory<Customers, Integer>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<Customers, String>("customerName"));
        customerTable.setItems(Customers.getAllCustomers());
    }
}
