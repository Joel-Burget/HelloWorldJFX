package C195;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.ZoneId;

public class LoginController {

    public Button loginButton;
    public Button cancelButton;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label regionLabel;


    public void initialize(){
        ZoneId zoneId = ZoneId.systemDefault();
        regionLabel.setText(zoneId.toString());
    }


    public void loginAction(ActionEvent actionEvent) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            String username = usernameField.getText();
            String password = passwordField.getText();

            String statement = "SELECT User_Name, Password from USERS WHERE User_Name= '" + username + "' AND Password= '" + password + "'";
            String select = "SELECT *  from USERS";


            try{
                JDBC.makePreparedStatement(statement, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery(statement);

                while(rs.next()){

                    Stage mainForm = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                    mainForm.setTitle("Welcome");
                    mainForm.setScene(new Scene(root, 250, 250));
                    mainForm.show();
                    stage.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }

    }

    public void cancelAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
