package C195;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LoginController {

    public Button loginButton;
    public Button cancelButton;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    public Label regionLabel;
    @FXML
    public Label errorLabel;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;


    public static String username;
    String password;
    String systemLanguage;

    public void initialize() throws IOException {
        Locale currentLocale = Locale.getDefault();
        regionLabel.setText(currentLocale.toString());
        systemLanguage = Locale.getDefault().getLanguage();


        //Setting buttons to French and correcting UI layout
        if(systemLanguage.equals("fr")){
            usernameLabel.setText("Nom d'utilisateur");
            usernameField.setLayoutX(160);
            passwordLabel.setText("Mot de passe");
            passwordField.setLayoutX(160);
            loginButton.setText("Connexion");
            loginButton.setLayoutX(160);
            cancelButton.setText("Annuler");
            cancelButton.setLayoutX(250);
        }
    }


    public void loginAction(ActionEvent actionEvent) throws IOException {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            username = usernameField.getText();
            password = passwordField.getText();
            errorLabel.setVisible(false);
            String statement = "SELECT * from USERS WHERE User_Name= '" + username + "'";
            File file = new File("src/C195/loginActivity.txt");
            FileWriter myWriter = new FileWriter(file, true);
            try{
                JDBC.makePreparedStatement(statement, JDBC.getConnection());
                ResultSet rs = JDBC.getPreparedStatement().executeQuery(statement);

                while(rs.next()){
                    //checking username and password, if true, launches main form
                    if(rs.getString(2).equals(username) && rs.getString(3).equals(password)){
                        myWriter.append("\n" + getUsername() + " has logged in successfully at: " + dtf.format(LocalDateTime.now()));
                        Stage mainForm = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                        mainForm.setTitle("Welcome");
                        mainForm.setScene(new Scene(root, 1120, 725));
                        mainForm.show();
                        Stage login = (Stage) loginButton.getScene().getWindow();
                        login.close();
                    }
                    else{
                        myWriter.append("\n" + getUsername() + " has failed to login at: " + dtf.format(LocalDateTime.now()));

                        //setting error message for incorrect password
                        if(!rs.getString(3).equals(password)){
                            if(systemLanguage.equals("fr")){
                                errorLabel.setText("Le mot de passe est incorrect");
                            }else{
                                errorLabel.setText("Password is Incorrect");
                            }
                            errorLabel.setVisible(true);
                        }
                    }
                    myWriter.close();
                }
                //translating error messages to French and checking username
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

    public static String getUsername(){return username;}

}
