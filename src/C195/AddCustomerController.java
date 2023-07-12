package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCustomerController{
    private MainController MC;
    public TextField customerIDField;
    public TextField nameField;
    public TextField addressField;
    public ComboBox countryBox;
    public ComboBox stateBox;
    public Button addButton;
    public Button cancelButton;
    public TextField zipField;
    public TextField phoneField;
    String state;
    ObservableList<String> countries = FXCollections.observableArrayList();
    ObservableList<String> states = FXCollections.observableArrayList();
    ObservableList<String> clear = FXCollections.observableArrayList();
    public String foo;

    public void initialize() {
        //Adding Countries to combo box
        countries.add("Canada");
        countries.add("United Kingdom");
        countries.add("United States");
        countryBox.setItems(countries);

        //setting on-action to generate state combo box based on country selected
        countryBox.setOnAction((e) -> {
            stateBox.getItems().clear();
            if(countryBox.getSelectionModel().getSelectedItem().equals("Canada")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/C195/canadianProvinces.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }

            if(countryBox.getSelectionModel().getSelectedItem().equals("United Kingdom")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/C195/britishIsles.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }

            if(countryBox.getSelectionModel().getSelectedItem().equals("United States")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("src/C195/usStates.txt"));
                    while((state = reader.readLine()) != null){
                        states.add(state);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stateBox.setItems(states);
            }
            });
    }

    public void addButtonAction(){
        int ID = Customers.generateCustomerID();
        String name = nameField.getText();
        String address = addressField.getText();
        String zip = zipField.getText();
        String phone = phoneField.getText();
        int divisionID = 0;

        try {
            String query = "SELECT Division_ID FROM first_level_divisions WHERE Division = '"
                    + stateBox.getSelectionModel().getSelectedItem() + "';";
            JDBC.makePreparedStatement(query, JDBC.getConnection());
            ResultSet rs = JDBC.getPreparedStatement().executeQuery();

            while(rs.next()){
                System.out.println(rs.getInt(1));
                divisionID = rs.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        Customers tempCustomer = new Customers(ID, name, address, zip, phone, divisionID);

        Customers.createCustomer(tempCustomer);
        Customers.getAllCustomers().add(tempCustomer);
        addButton.getScene().getWindow().hide();
        MC.refreshCustomerTable();
    }

    public void setMainController(MainController MC){
        this.MC = MC;
    }

    public void cancelButtonAction(){
        cancelButton.getScene().getWindow().hide();
    }
}
