package sk.itsovy.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.itsovy.Database.Database;
import sk.itsovy.Employee;

public class Controller {
    public TextField usernameField;
    public PasswordField passwordField;
    public Label errorlabel;
    private String user;

    public String getUser() {
        return user;
    }

    public void verifyUser(ActionEvent actionEvent)
    {
        Database dbase = Database.getInstanceDB();

        user = usernameField.getText();
        if(dbase.matchUser(user, passwordField.getText()))
        {
            System.out.println("User exists");
            errorlabel.setVisible(false);
            usernameField.setText("");
            passwordField.setText("");

            Parent root;
            try
            {
                Employee emp = dbase.getEmployee();
                root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Logged in as: " + emp.getFirstname() + " " + emp.getLastname());
                stage.setScene(new Scene(root, 820, 500));
                stage.setResizable(false);
                stage.show();

                //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                Stage stage2 = (Stage) errorlabel.getScene().getWindow();
                stage2.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
            {
                System.out.println("User does not exist");
                errorlabel.setVisible(true);
            }
    }

}
