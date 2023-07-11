package C195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AddCustomerController{
    public TextField customerIDField;
    public TextField nameField;
    public TextField addressField;
    public ComboBox countryBox;
    public ComboBox stateBox;
    public Button addButton;
    public Button cancelButton;
    String state;
    ObservableList<String> countries = FXCollections.observableArrayList();
    ObservableList<String> states = FXCollections.observableArrayList();
    ObservableList<String> clear = FXCollections.observableArrayList();

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
                stateBox.setItems(clear);
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

    }

    public void cancelButtonAction(){
        cancelButton.getScene().getWindow().hide();
    }
}
