package C195;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.ZoneId;

public class LoginController {

    public Button loginButton;
    public Button cancelButton;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label regionLabel;
    @FXML
    public Label errorLabel;

    String username;
    String password;
    Stage stage;

    public void initialize() throws IOException {
        ZoneId zoneId = ZoneId.systemDefault();
        regionLabel.setText(zoneId.toString());
        stage = new Stage();
    }


    public void loginAction(ActionEvent actionEvent) {
            username = usernameField.getText();
            password = passwordField.getText();
            String statement = "SELECT * from USERS WHERE User_Name= '" + username + "'";

            try{
                System.out.println("button pressed");
                JDBC.makePreparedStatement(statement, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery(statement);

                if(rs.next()){
                    stage.close();
                    Stage mainForm = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                    mainForm.setTitle("Welcome");
                    mainForm.setScene(new Scene(root, 250, 250));
                    mainForm.show();
                }
                else{
                    errorLabel.setText("Incorrect Username");
                    errorLabel.setVisible(true);
                }
            }catch(SQLException sqe) {
                System.out.println("Error Code: " + sqe.getErrorCode());
                sqe.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void cancelAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
