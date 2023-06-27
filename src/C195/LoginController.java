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
import java.util.Locale;

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
    Stage login = new Stage();
    String systemLanguage;

    public void initialize() throws IOException {
        Locale currentLocale = Locale.getDefault();
        System.out.println(currentLocale);
        regionLabel.setText(currentLocale.toString());
        systemLanguage = Locale.getDefault().getLanguage();
        System.out.println(systemLanguage);
    }


    public void loginAction(ActionEvent actionEvent) {
            username = usernameField.getText();
            password = passwordField.getText();
            errorLabel.setVisible(false);
            String statement = "SELECT * from USERS WHERE User_Name= '" + username + "'";

            try{
                System.out.println("button pressed");
                JDBC.makePreparedStatement(statement, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery(statement);

                while(rs.next()){
                    //checking username and password, if true, launches main form
                    if(rs.getString(2).equals(username) && rs.getString(3).equals(password)){
                        Stage mainForm = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                        mainForm.setTitle("Welcome");
                        mainForm.setScene(new Scene(root, 250, 250));
                        mainForm.show();
                        Stage login = (Stage) loginButton.getScene().getWindow();
                        login.close();
                    }
                    else{
                        //setting error message for incorrect password
                        if(!rs.getString(3).equals(password)){
                            System.out.println("This logic fired");
                            if(systemLanguage.equals("fr")){
                                errorLabel.setText("Le mot de passe est incorrect");
                            }else{
                                errorLabel.setText("Password is Incorrect");
                            }
                            errorLabel.setVisible(true);
                        }
                    }
                }

                if(!rs.next()){
                    if(!errorLabel.isVisible()){
                        if(systemLanguage.equals("fr")){
                            errorLabel.setText("Le nom d'utilisateur est incorrect");
                        }else{
                            errorLabel.setText("Username is incorrect");
                        }
                        errorLabel.setVisible(true);
                    }
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
